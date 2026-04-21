package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;

    // Inyección de dependencias mediante constructor
    public ReporteServiceImpl(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    @Transactional
    public ReporteModel guardarReporte(ReporteModel reporte) {
        
        ReporteModel guardado = reporteRepository.save(reporte);

      

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