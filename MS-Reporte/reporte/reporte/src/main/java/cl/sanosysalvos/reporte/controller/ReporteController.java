package cl.sanosysalvos.reporte.controller;

import cl.sanosysalvos.reporte.dto.ReporteRequestDTO;
import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ReporteResponseDTO> crear(@Valid @RequestBody ReporteRequestDTO dto) {
        ReporteResponseDTO nuevoReporte = reporteService.guardarReporte(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoReporte);
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listar() {
        List<ReporteResponseDTO> lista = reporteService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtenerPorId(@PathVariable Long id) {
        ReporteResponseDTO reporte = reporteService.obtenerPorId(id);
        return ResponseEntity.ok(reporte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReporteRequestDTO dto) {
        ReporteResponseDTO actualizado = reporteService.actualizarReporte(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}