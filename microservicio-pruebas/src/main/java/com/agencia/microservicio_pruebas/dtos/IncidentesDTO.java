package com.agencia.microservicio_pruebas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentesDTO {
    private Long id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Long legajoEmpleado;  // Cambiar de String a Long
    private String apellidoEmpleado;
    private String nombreEmpleado;
    private String apellidoInteresado;
    private String nombreInteresado;
    private String descripcionNotificacion;
    private String patenteVehiculo;
}
