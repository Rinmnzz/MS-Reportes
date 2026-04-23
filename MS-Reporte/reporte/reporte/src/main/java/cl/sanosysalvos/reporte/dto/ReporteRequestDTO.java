package cl.sanosysalvos.reporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import cl.sanosysalvos.reporte.model.TipoReporte;
import cl.sanosysalvos.reporte.model.TamanoMascota;

@Data
public class ReporteRequestDTO {

    @NotNull(message = "El idUsuario es obligatorio")
    private Integer idUsuario;

    @NotBlank(message = "Las coordenadas son obligatorias")
    private String coordenadas;

    @NotNull(message = "El tipo de reporte es obligatorio")
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