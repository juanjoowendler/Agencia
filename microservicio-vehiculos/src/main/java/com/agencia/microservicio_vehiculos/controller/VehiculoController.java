package com.agencia.microservicio_vehiculos.controller;

import com.agencia.microservicio_vehiculos.entities.ConfiguracionResponse;
import com.agencia.microservicio_vehiculos.entities.Posicion;
import com.agencia.microservicio_vehiculos.services.ConfiguracionService;
import com.agencia.microservicio_vehiculos.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ConfiguracionService configuracionService;

    // Estado
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Microservicio vehiculos ejecutándose.");
    }

    // API
    @GetMapping("/configuracion")
    public ResponseEntity<ConfiguracionResponse> getConfiguracion() {
        return ResponseEntity.ok(configuracionService.obtenerConfiguracion());
    }

    // PUNTO D
    @PostMapping("/evaluar")
    public ResponseEntity<String> evaluarPosicion(@RequestBody Posicion posicion) {
        String resultado = vehiculoService.evaluarPosicion(posicion);

        return ResponseEntity.ok(resultado);
    }
}
