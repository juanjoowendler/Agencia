package com.agencia.microservicio_vehiculos.controller;

import com.agencia.microservicio_vehiculos.entities.ConfiguracionResponse;
import com.agencia.microservicio_vehiculos.services.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private ConfiguracionService configuracionService;

    // Estado
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Microservicio vehiculos ejecutándose.");
    }

    // API
    @GetMapping("/configuracion")
    public ConfiguracionResponse getConfiguracion() {
        return configuracionService.obtenerConfiguracion();
    }

}
