package cl.sanosysalvos.reporte.service;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoServiceTest {

    @Test
    void obtenerCoordenadas_devuelveLatLonCuandoHayResultados() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        GeoService geoService = new GeoService(restTemplate);
        setApiKey(geoService, "test-key");

        List<Map<String, Object>> body = List.of(Map.of("lat", "-33.45", "lon", "-70.66"));
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        )).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        String coordenadas = geoService.obtenerCoordenadas("Calle Falsa 123");

        assertEquals("-33.45,-70.66", coordenadas);
    }

    @Test
    void obtenerCoordenadas_devuelveCeroCuandoNoHayResultados() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        GeoService geoService = new GeoService(restTemplate);
        setApiKey(geoService, "test-key");

        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        )).thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        String coordenadas = geoService.obtenerCoordenadas("Calle Sin Resultado");

        assertEquals("0,0", coordenadas);
    }

    @SuppressWarnings("java:S3011")
    private static void setApiKey(GeoService geoService, String apiKey) {
        try {
            var field = GeoService.class.getDeclaredField("apiKey");
            field.setAccessible(true);
            field.set(geoService, apiKey);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
