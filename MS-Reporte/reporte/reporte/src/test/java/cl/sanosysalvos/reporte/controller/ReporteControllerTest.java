package cl.sanosysalvos.reporte.controller;

import cl.sanosysalvos.reporte.dto.ReporteRequestDTO;
import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.model.TipoReporte;
import cl.sanosysalvos.reporte.service.ReporteService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReporteControllerTest {

    private final ReporteService reporteService = mock(ReporteService.class);
    private final ReporteController controller = new ReporteController(reporteService);

    @Test
    void crear_devuelveCreatedConBody() {
        ReporteResponseDTO response = responseDto(1L);
        when(reporteService.guardarReporte(org.mockito.ArgumentMatchers.any(ReporteRequestDTO.class))).thenReturn(response);

        var result = controller.crear(new ReporteRequestDTO());

        assertEquals(201, result.getStatusCode().value());
        assertEquals(1L, result.getBody().getId());
        verify(reporteService).guardarReporte(org.mockito.ArgumentMatchers.any(ReporteRequestDTO.class));
    }

    @Test
    void listar_devuelveOkConLista() {
        when(reporteService.obtenerTodos()).thenReturn(List.of(responseDto(2L)));

        var result = controller.listar();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void obtenerPorId_devuelveOkConReporte() {
        when(reporteService.obtenerPorId(3L)).thenReturn(responseDto(3L));

        var result = controller.obtenerPorId(3L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(3L, result.getBody().getId());
    }

    @Test
    void actualizar_devuelveOkConReporteActualizado() {
        when(reporteService.actualizarReporte(org.mockito.ArgumentMatchers.eq(4L), org.mockito.ArgumentMatchers.any(ReporteRequestDTO.class)))
            .thenReturn(responseDto(4L));

        var result = controller.actualizar(4L, new ReporteRequestDTO());

        assertEquals(200, result.getStatusCode().value());
        assertEquals(4L, result.getBody().getId());
    }

    @Test
    void eliminar_devuelveNoContent() {
        var result = controller.eliminar(5L);

        assertEquals(204, result.getStatusCode().value());
        assertNull(result.getBody());
        verify(reporteService).eliminarReporte(5L);
    }

    private ReporteResponseDTO responseDto(Long id) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setId(id);
        dto.setTipoReporte(TipoReporte.PERDIDO);
        dto.setRaza("Labrador");
        return dto;
    }
}