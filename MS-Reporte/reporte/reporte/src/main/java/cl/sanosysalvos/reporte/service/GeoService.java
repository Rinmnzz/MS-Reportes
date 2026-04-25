package cl.sanosysalvos.reporte.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {

    
    @Value("${locationiq.api-key}")
    private String apiKey;

    public String obtenerCoordenadas(String direccion) {
        
        String url = "https://us1.locationiq.com/v1/search?key=" + this.apiKey + "&q=" + direccion + "&format=json";
        
        RestTemplate restTemplate = new RestTemplate();
        
        
        try {
            
            return "Coordenadas obtenidas para: " + direccion;
        } catch (Exception e) {
            return "Error al conectar con LocationIQ: " + e.getMessage();
        }
    }
}