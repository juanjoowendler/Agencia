package com.agencia.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig {

    @Value("${microservicio-pruebas:http://localhost:8081}")
    private String uriPruebas;

    @Value("${microservicio-vehiculos:http://localhost:8082}")
    private String uriVehiculos;

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("pruebas-route", r -> r.path("/pruebas/**").uri(uriPruebas))
                .route("vehiculos-route", r -> r.path("/vehiculos/**").uri(uriVehiculos))
                .build();
    }
}
