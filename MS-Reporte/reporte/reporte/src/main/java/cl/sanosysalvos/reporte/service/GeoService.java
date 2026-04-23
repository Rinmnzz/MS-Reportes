package cl.sanosysalvos.reporte.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {

    // Spring leerá el valor desde application.properties automáticamente
    @Value("${locationiq.api-key}")
    private String apiKey;

    public String obtenerCoordenadas(String direccion) {
        // La URL ahora usa la variable apiKey que inyectamos arriba
        String url = "https://us1.locationiq.com/v1/search?key=" + this.apiKey + 
                     "&q=" + direccion + "&format=json";
        
        RestTemplate restTemplate = new RestTemplate();
        
        // Aquí realizarías la petición. Por ahora, dejamos la estructura lista.
        try {
            // Ejemplo de llamada: String respuesta = restTemplate.getForObject(url, String.class);
            return "Coordenadas obtenidas para: " + direccion;
        } catch (Exception e) {
            return "Error al conectar con LocationIQ: " + e.getMessage();
        }
    }
}