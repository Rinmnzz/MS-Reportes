package cl.sanosysalvos.reporte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import cl.sanosysalvos.reporte.model.TipoReporte;
import cl.sanosysalvos.reporte.model.TamanoMascota;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReporteResponseDTO {
    private Long idReporte;
    private Integer idUsuario;
    private String coordenadas;
    private TipoReporte tipoReporte;
    private String tipoMascota;
    private String nombreMascota;
    private String color;
    private TamanoMascota tamano;
    private String raza;
    private String fotoMascota;
    private String descripcion;
    private String direccion;
}