package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.dto.ReporteRequestDTO;
import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.repository.ReporteRepository;
import cl.sanosysalvos.reporte.messaging.ReportePublisher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final ReportePublisher reportePublisher;
    private final GeoService geoService; 

   
    public ReporteServiceImpl(
        ReporteRepository reporteRepository, 
        ReportePublisher reportePublisher, 
        GeoService geoService) {
        this.reporteRepository = reporteRepository;
        this.reportePublisher = reportePublisher;
        this.geoService = geoService;
    }

    @Override
    @Transactional
    public ReporteResponseDTO guardarReporte(ReporteRequestDTO dto) {
        ReporteModel reporte = mapToEntity(dto);

        ReporteModel guardado = reporteRepository.save(reporte);

        ReporteResponseDTO responseDTO = mapToResponseDTO(guardado);

        reportePublisher.publicarNuevoReporte(responseDTO);

        return responseDTO;
    }

    @Override
    public List<ReporteResponseDTO> obtenerTodos() {
        return reporteRepository.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ReporteResponseDTO obtenerPorId(Long id) {
        ReporteModel reporte = reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
        return mapToResponseDTO(reporte);
    }

    @Override
    @Transactional
    public ReporteResponseDTO actualizarReporte(Long id, ReporteRequestDTO dto) {
        ReporteModel reporteExistente = reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede actualizar. Reporte no encontrado con ID: " + id));

        aplicarCamposEditables(reporteExistente, dto);
        actualizarCoordenadas(reporteExistente, dto.getDireccion());

        ReporteModel reporteActualizado = reporteRepository.save(reporteExistente);
        return mapToResponseDTO(reporteActualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Reporte no encontrado con ID: " + id);
        }
        reporteRepository.deleteById(id);
    }

    private ReporteModel mapToEntity(ReporteRequestDTO dto) {
        ReporteModel reporte = new ReporteModel();
        aplicarCamposEditables(reporte, dto);
        actualizarCoordenadas(reporte, dto.getDireccion());
        return reporte;
    }

    private void aplicarCamposEditables(ReporteModel reporte, ReporteRequestDTO dto) {
        reporte.setIdUsuario(dto.getIdUsuario());
        reporte.setTipoReporte(dto.getTipoReporte());
        reporte.setTipoMascota(dto.getTipoMascota());
        reporte.setNombreMascota(dto.getNombreMascota());
        reporte.setColor(dto.getColor());
        reporte.setTamano(dto.getTamano());
        reporte.setRaza(dto.getRaza());
        reporte.setFotoMascota(dto.getFotoMascota());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setDireccion(dto.getDireccion());
        reporte.setSexo(dto.getSexo());
    }

    private void actualizarCoordenadas(ReporteModel reporte, String direccion) {
        if (direccion != null && !direccion.isBlank()) {
            reporte.setCoordenadas(geoService.obtenerCoordenadas(direccion));
        }
    }

    private ReporteResponseDTO mapToResponseDTO(ReporteModel model) {
        return ReporteResponseDTO.builder()
            .id(model.getIdReporte())
            .idUsuario(model.getIdUsuario())
            .tipoReporte(model.getTipoReporte())
            .tipoMascota(model.getTipoMascota())
            .nombreMascota(model.getNombreMascota())
            .color(model.getColor())
            .tamano(model.getTamano())
            .raza(model.getRaza())
            .fotoMascota(model.getFotoMascota())
            .descripcion(model.getDescripcion())
            .direccion(model.getDireccion())
            .coordenadas(model.getCoordenadas())
            .sexo(model.getSexo())
            .estado(model.getEstado())
            .build();
    }
}