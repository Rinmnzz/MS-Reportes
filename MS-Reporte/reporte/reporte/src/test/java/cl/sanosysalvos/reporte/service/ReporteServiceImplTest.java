package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.dto.ReporteRequestDTO;
import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.messaging.ReportePublisher;
import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.model.TipoReporte;
import cl.sanosysalvos.reporte.model.TamanoMascota;
import cl.sanosysalvos.reporte.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ReporteServiceImplTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReportePublisher reportePublisher;

    @Mock
    private GeoService geoService;

    private AutoCloseable mocks;
    private ReporteServiceImpl reporteService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        reporteService = new ReporteServiceImpl(reporteRepository, reportePublisher, geoService);
    }

    @Test
    void guardarReporte_calculaCoordenadasYPublicaEvento() {
        ReporteRequestDTO dto = baseDto();
        dto.setDireccion("Calle Falsa 123");
        when(geoService.obtenerCoordenadas("Calle Falsa 123")).thenReturn("10,20");
        when(reporteRepository.save(any(ReporteModel.class))).thenAnswer(invocation -> {
            ReporteModel reporte = invocation.getArgument(0);
            reporte.setIdReporte(1L);
            return reporte;
        });

        ReporteResponseDTO response = reporteService.guardarReporte(dto);

        ArgumentCaptor<ReporteResponseDTO> captor = ArgumentCaptor.forClass(ReporteResponseDTO.class);
        verify(reportePublisher).publicarNuevoReporte(captor.capture());
        assertEquals(1L, response.getId());
        assertEquals("10,20", response.getCoordenadas());
        assertEquals("10,20", captor.getValue().getCoordenadas());
        assertEquals(TipoReporte.PERDIDO, captor.getValue().getTipoReporte());
        verify(geoService).obtenerCoordenadas("Calle Falsa 123");
    }

    @Test
    void guardarReporte_sinDireccionNoConsultaGeoService() {
        ReporteRequestDTO dto = baseDto();
        dto.setDireccion("   ");
        when(reporteRepository.save(any(ReporteModel.class))).thenAnswer(invocation -> {
            ReporteModel reporte = invocation.getArgument(0);
            reporte.setIdReporte(2L);
            return reporte;
        });

        ReporteResponseDTO response = reporteService.guardarReporte(dto);

        assertEquals(2L, response.getId());
        assertNull(response.getCoordenadas());
        verifyNoInteractions(geoService);
    }

    @Test
    void obtenerTodos_mapeaListaDeResultados() {
        ReporteModel reporte = reporteBase();
        reporte.setIdReporte(3L);
        when(reporteRepository.findAll()).thenReturn(List.of(reporte));

        List<ReporteResponseDTO> resultado = reporteService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getId());
        assertEquals("Labrador", resultado.get(0).getRaza());
    }

    @Test
    void obtenerPorId_devuelveReporteExistente() {
        ReporteModel reporte = reporteBase();
        reporte.setIdReporte(4L);
        when(reporteRepository.findById(4L)).thenReturn(Optional.of(reporte));

        ReporteResponseDTO response = reporteService.obtenerPorId(4L);

        assertEquals(4L, response.getId());
        assertEquals("Labrador", response.getRaza());
    }

    @Test
    void actualizarReporte_actualizaCamposYCoordenadas() {
        ReporteModel existente = reporteBase();
        existente.setIdReporte(5L);
        when(reporteRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(geoService.obtenerCoordenadas("Nueva direccion 456")).thenReturn("11,22");
        when(reporteRepository.save(any(ReporteModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReporteRequestDTO dto = baseDto();
        dto.setTipoMascota("gato");
        dto.setDireccion("Nueva direccion 456");
        dto.setSexo("Hembra");

        ReporteResponseDTO response = reporteService.actualizarReporte(5L, dto);

        assertEquals(5L, response.getId());
        assertEquals("gato", response.getTipoMascota());
        assertEquals("11,22", response.getCoordenadas());
        verify(geoService).obtenerCoordenadas("Nueva direccion 456");
    }

    @Test
    void eliminarReporte_eliminaCuandoExiste() {
        when(reporteRepository.existsById(9L)).thenReturn(true);

        reporteService.eliminarReporte(9L);

        verify(reporteRepository).deleteById(9L);
    }

    @Test
    void eliminarReporte_lanzaErrorCuandoNoExiste() {
        when(reporteRepository.existsById(9L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reporteService.eliminarReporte(9L));

        assertEquals("No se puede eliminar. Reporte no encontrado con ID: 9", exception.getMessage());
        verify(reporteRepository, never()).deleteById(eq(9L));
    }

    private ReporteRequestDTO baseDto() {
        ReporteRequestDTO dto = new ReporteRequestDTO();
        dto.setIdUsuario(7);
        dto.setTipoReporte(TipoReporte.PERDIDO);
        dto.setTipoMascota("perro");
        dto.setNombreMascota("Firulais");
        dto.setColor("marron");
        dto.setTamano(TamanoMascota.MEDIANO);
        dto.setRaza("Labrador");
        dto.setFotoMascota("foto.jpg");
        dto.setDescripcion("Descripcion");
        dto.setDireccion("Direccion base");
        dto.setSexo("Macho");
        return dto;
    }

    private ReporteModel reporteBase() {
        ReporteModel reporte = new ReporteModel();
        reporte.setIdUsuario(7);
        reporte.setTipoReporte(TipoReporte.PERDIDO);
        reporte.setTipoMascota("perro");
        reporte.setNombreMascota("Firulais");
        reporte.setColor("marron");
        reporte.setTamano(TamanoMascota.MEDIANO);
        reporte.setRaza("Labrador");
        reporte.setFotoMascota("foto.jpg");
        reporte.setDescripcion("Descripcion");
        reporte.setDireccion("Direccion base");
        reporte.setSexo("Macho");
        return reporte;
    }
}