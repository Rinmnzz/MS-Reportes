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

    @Override
    public List<ReporteResponseDTO> obtenerTodos() {
        // Busca todas las entidades, las convierte en un Stream y mapea cada una a DTO
        return reporteRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReporteResponseDTO obtenerPorId(Long id) {
        // Busca por ID. Si no lo encuentra, lanza una excepción que tu GlobalExceptionHandler puede atrapar
        ReporteModel reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
        
        return mapToResponseDTO(reporte);
    }

    @Override
    @Transactional
    public ReporteResponseDTO actualizarReporte(Long id, ReporteRequestDTO dto) {
        // 1. Buscar el reporte existente
        ReporteModel reporteExistente = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. Reporte no encontrado con ID: " + id));

        // 2. Actualizar los campos con los datos del DTO
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
        reporteExistente.setCoordenadas(dto.getCoordenadas());

        // 3. Guardar los cambios (al estar en @Transactional a veces no es necesario el .save(), pero es buena práctica explícita)
        ReporteModel reporteActualizado = reporteRepository.save(reporteExistente);

        // 4. Retornar el DTO actualizado
        return mapToResponseDTO(reporteActualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(Long id) {
        // Validar primero si existe para no lanzar errores extraños de JPA
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Reporte no encontrado con ID: " + id);
        }
        reporteRepository.deleteById(id);
    }

    // Método auxiliar unificado para mapear de Model a DTO usando el patrón Builder
    private ReporteResponseDTO mapToResponseDTO(ReporteModel model) {
        return ReporteResponseDTO.builder()
                .id(model.getIdReporte()) // <-- AHORA ES ASÍ
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