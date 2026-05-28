package cl.sanosysalvos.reporte.model;

import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReporteModelAndDtoTest {

    @Test
    void reporteModel_tieneEstadoPorDefectoActivo() {
        ReporteModel reporte = new ReporteModel();

        assertEquals("ACTIVO", reporte.getEstado());
    }

    @Test
    void reporteResponseDto_constructorParcialAsignaCampos() {
        ReporteResponseDTO dto = new ReporteResponseDTO(8L, "Labrador", TipoReporte.PERDIDO);

        assertEquals(8L, dto.getId());
        assertEquals("Labrador", dto.getRaza());
        assertEquals(TipoReporte.PERDIDO, dto.getTipoReporte());
    }
}