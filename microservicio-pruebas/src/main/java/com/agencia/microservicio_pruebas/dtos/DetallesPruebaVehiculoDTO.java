package com.agencia.microservicio_pruebas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallesPruebaVehiculoDTO {
    private Long vehiculoId;
    private String patente;
    private String descripcionModelo;
    private String nombreMarca;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String tipoDocumentoInteresado;
    private String documentoInteresado;
    private String apellidoInteresado;
    private String nombreInteresado;
    private long nroLicenciaInteresado;
    private LocalDateTime fechaVencimientoLicencia;
    private Long legajoEmpleado;
    private String apellidoEmpleado;
    private String nombreEmpleado;
    private String comentarios;
}