package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.model.ReporteModel;
import java.util.List;

public interface ReporteService {
    ReporteModel guardarReporte(ReporteModel reporte);
    List<ReporteModel> obtenerTodos();
    ReporteModel obtenerPorId(Long id);
    ReporteModel actualizarReporte(Long id, ReporteModel reporte);
    void eliminarReporte(Long id);
}