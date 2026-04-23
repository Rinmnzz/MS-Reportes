package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.dto.ReporteRequestDTO;
import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.repository.ReporteRepository;
import cl.sanosysalvos.reporte.service.ReporteService;
import cl.sanosysalvos.reporte.messaging.ReportePublisher;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final ReportePublisher reportePublisher;

    public ReporteServiceImpl(ReporteRepository reporteRepository, ReportePublisher reportePublisher) {
        this.reporteRepository = reporteRepository;
        this.reportePublisher = reportePublisher;
    }

    @Override
    @Transactional
    public ReporteResponseDTO guardarReporte(ReporteRequestDTO dto) {
        // 1. Mapear DTO a Entity (Model)
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
        reporte.setCoordenadas(dto.getCoordenadas());

        // 2. Persistir en base de datos
        ReporteModel guardado = reporteRepository.save(reporte);

        // 3. Mapear Entity a ResponseDTO (para retornar y publicar)
        ReporteResponseDTO responseDTO = mapToResponseDTO(guardado);

        // 4. Publicar en RabbitMQ
        reportePublisher.publicarNuevoReporte(responseDTO);

        return responseDTO;
    }

    // Método auxiliar para mapear de Model a DTO
    private ReporteResponseDTO mapToResponseDTO(ReporteModel model) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setIdReporte(model.getIdReporte());
        dto.setIdUsuario(model.getIdUsuario());
        dto.setTipoReporte(model.getTipoReporte());
        dto.setTipoMascota(model.getTipoMascota());
        dto.setNombreMascota(model.getNombreMascota());
        dto.setColor(model.getColor());
        dto.setTamano(model.getTamano());
        dto.setRaza(model.getRaza());
        dto.setFotoMascota(model.getFotoMascota());
        dto.setDescripcion(model.getDescripcion());
        dto.setDireccion(model.getDireccion());
        dto.setCoordenadas(model.getCoordenadas());
        return dto;
    }

    @Override
    public List<ReporteResponseDTO> obtenerTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodos'");
    }

    @Override
    public ReporteResponseDTO obtenerPorId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPorId'");
    }

    @Override
    public ReporteResponseDTO actualizarReporte(Long id, ReporteRequestDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarReporte'");
    }

    @Override
    public void eliminarReporte(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarReporte'");
    }

    // ... el resto de tus métodos (listar, obtener, etc.)
}