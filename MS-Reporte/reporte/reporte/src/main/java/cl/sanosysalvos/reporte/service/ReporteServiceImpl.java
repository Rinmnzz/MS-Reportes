package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.repository.ReporteRepository;
import cl.sanosysalvos.reporte.service.ReporteService;
import cl.sanosysalvos.reporte.service.GeoService; // Asegúrate de importar tu servicio de geo
import cl.sanosysalvos.reporte.messaging.ReportePublisher; // Asegúrate de importar tu publisher
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final GeoService geoService;
    private final ReportePublisher reportePublisher;

    // Inyección de dependencias actualizada para incluir los nuevos servicios
    public ReporteServiceImpl(ReporteRepository reporteRepository, 
                              GeoService geoService, 
                              ReportePublisher reportePublisher) {
        this.reporteRepository = reporteRepository;
        this.geoService = geoService;
        this.reportePublisher = reportePublisher;
    }

    @Override
    @Transactional
    public ReporteModel guardarReporte(ReporteModel reporte) {
        
        // 1. Lógica de Geolocalización:
        // Convertimos la dirección en coordenadas antes de guardar
        String coordenadas = geoService.obtenerCoordenadas(reporte.getDireccion());
        reporte.setCoordenadas(coordenadas); // Asumiendo que tu modelo tiene este setter

        // 2. Persistencia:
        // Guardamos en la base de datos
        ReporteModel guardado = reporteRepository.save(reporte);

        // 3. Notificación (RabbitMQ):
        // Enviamos el evento al bus de mensajes
        //reportePublisher.publicarNuevoReporte(guardado);

        return guardado;
    }

    @Override
    public List<ReporteModel> obtenerTodos() {
        return reporteRepository.findAll();
    }

    @Override
    public ReporteModel obtenerPorId(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public ReporteModel actualizarReporte(Long id, ReporteModel reporteActualizado) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("No se puede actualizar, el reporte no existe.");
        }
        reporteActualizado.setIdReporte(id);
        return reporteRepository.save(reporteActualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
}