package com.agencia.microservicio_pruebas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentesEmpleadoDTO {
    private Long idPrueba;
    private Long legajo;
    private String empleadoApellido;
    private String empleadoNombre;
    private long telefonoContacto;
    private String patente;
    private String incidente;
    private Long cantidadIncidentes;
}
