package com.agencia.microservicio_vehiculos.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordenadasAgencia {
    @JsonProperty("lat")
    private double lat;

    @JsonProperty("lon")
    private double lon;
}
