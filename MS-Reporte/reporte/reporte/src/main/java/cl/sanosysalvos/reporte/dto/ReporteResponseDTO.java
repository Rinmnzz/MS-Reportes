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
public class ReporteResponseDTO {
    
    private Long id; 
    private Integer idUsuario;
    
    private TipoReporte tipoReporte; 
    
    private String direccion;
    private String coordenadas;
    
    private String nombreMascota;
    private String tipoMascota;
    private String raza;
    private TamanoMascota tamano;
    private String color;
    private String sexo;
    
    private String descripcion;
    private String fotoMascota;
    
    private String estado;

    public ReporteResponseDTO(Long id, String raza, TipoReporte tipoReporte) {
        this.id = id;
        this.raza = raza;
        this.tipoReporte = tipoReporte;
    }
}