package cl.sanosysalvos.reporte.controller; // Ajusta a 'reporte' si tu carpeta es singular

import cl.sanosysalvos.reporte.model.ReporteModel; // Importa tu entidad
import cl.sanosysalvos.reporte.service.ReporteService; // Importa tu interfaz de servicio
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping
    public ResponseEntity<ReporteModel> crear(@RequestBody ReporteModel reporte) {
        return ResponseEntity.ok(reporteService.guardarReporte(reporte));
    }

    @GetMapping
    public ResponseEntity<List<ReporteModel>> listarTodos() {
        return ResponseEntity.ok(reporteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteModel> actualizar(@PathVariable Long id, @RequestBody ReporteModel reporteActualizado) {
        return ResponseEntity.ok(reporteService.actualizarReporte(id, reporteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}