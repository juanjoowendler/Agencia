package com.agencia.microservicio_vehiculos.entities;

import lombok.Data;

@Data
public class VehiculoPosicion {
    private Posicion posicion;
    private Vehiculo vehiculo;
}
