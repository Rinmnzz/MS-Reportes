package cl.sanosysalvos.reporte.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class GeoService {

    @Value("${locationiq.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String obtenerCoordenadas(String direccion) {
        String direccionFormateada = direccion.trim().replace(" ", "+");
        
        
        String url = "https://us1.locationiq.com/v1/search?key=" + apiKey + 
                     "&q=" + direccionFormateada + "&format=json";

        try {
            System.out.println(">>> Consultando LocationIQ: " + url);
            ResponseEntity<List<Map<String, Object>>> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> response = resp.getBody();

            if (response != null && !response.isEmpty()) {
                Map<String, Object> primerResultado = response.get(0);
                String lat = String.valueOf(primerResultado.get("lat"));
                String lon = String.valueOf(primerResultado.get("lon"));

                return lat + "," + lon;
            }
            return "0,0";
        } catch (Exception e) {
            System.err.println(">>> Error en GeoService: " + e.getMessage());
            return "Error: Servicio de mapas no disponible";
        }
    }
}