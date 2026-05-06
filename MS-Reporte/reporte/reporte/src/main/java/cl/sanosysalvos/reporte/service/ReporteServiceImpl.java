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
    private final GeoService geoService; // <-- 1. Declaramos el GeoService

    // 2. Inyectamos los 3 servicios en el constructor
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
        // Mapear DTO a Entity
        ReporteModel reporte = new ReporteModel();
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

        // 3. LA MAGIA DEL GEOSERVICE OCURRE AQUÍ
        // Validamos: Si no hay coordenadas, pero sí escribieron una dirección...
        if ((dto.getCoordenadas() == null || dto.getCoordenadas().isBlank()) 
                && dto.getDireccion() != null && !dto.getDireccion().isBlank()) {
            
            // ...vamos al GeoService, le pasamos la dirección y nos trae las coordenadas
            String coordenadasCalculadas = geoService.obtenerCoordenadas(dto.getDireccion());
            reporte.setCoordenadas(coordenadasCalculadas);
        } else {
            // Si el frontend ya mandó las coordenadas precisas, simplemente las usamos
            reporte.setCoordenadas(dto.getCoordenadas());
        }

        // Persistir en base de datos
        ReporteModel guardado = reporteRepository.save(reporte);

        // Mapear a DTO para responder
        ReporteResponseDTO responseDTO = mapToResponseDTO(guardado);

        // Publicar en RabbitMQ para que otros microservicios (ej. Coincidencias) se enteren
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

        reporteExistente.setIdUsuario(dto.getIdUsuario());
        reporteExistente.setTipoReporte(dto.getTipoReporte());
        reporteExistente.setTipoMascota(dto.getTipoMascota());
        reporteExistente.setNombreMascota(dto.getNombreMascota());
        reporteExistente.setColor(dto.getColor());
        reporteExistente.setTamano(dto.getTamano());
        reporteExistente.setRaza(dto.getRaza());
        reporteExistente.setFotoMascota(dto.getFotoMascota());
        reporteExistente.setDescripcion(dto.getDescripcion());
        reporteExistente.setDireccion(dto.getDireccion());

        // También aplicamos la magia aquí por si actualizaron la dirección
        if ((dto.getCoordenadas() == null || dto.getCoordenadas().isBlank()) 
                && dto.getDireccion() != null && !dto.getDireccion().isBlank()) {
            String coordenadasCalculadas = geoService.obtenerCoordenadas(dto.getDireccion());
            reporteExistente.setCoordenadas(coordenadasCalculadas);
        } else {
            reporteExistente.setCoordenadas(dto.getCoordenadas());
        }

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
            .build();
    }
}