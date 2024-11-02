package com.agencia.microservicio_vehiculos.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfiguracionResponse {
    @JsonProperty("coordenadasAgencia")
    private CoordenadasAgencia coordenadasAgencia;
    @JsonProperty("radioAdmitidoKm")
    private double radioAdmitidoKm;
    @JsonProperty("zonasRestringidas")
    private List<ZonaRestringida> zonasRestringidas;

}

