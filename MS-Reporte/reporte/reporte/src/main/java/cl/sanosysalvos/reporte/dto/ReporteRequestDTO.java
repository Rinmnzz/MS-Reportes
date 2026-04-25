package cl.sanosysalvos.reporte.dto;

import cl.sanosysalvos.reporte.model.TipoReporte;
import cl.sanosysalvos.reporte.model.TamanoMascota;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {
    
    private Integer idUsuario;
    private TipoReporte tipoReporte; 
    private String tipoMascota; // Ajustado
    private String nombreMascota;
    private String color;
    private TamanoMascota tamano;
    private String raza;
    private String fotoMascota; // Ajustado
    private String descripcion;
    private String direccion;   // Ajustado
    private String coordenadas; // Ajustado (Asumo que es un String, ej: "-33.45,-70.66")
}