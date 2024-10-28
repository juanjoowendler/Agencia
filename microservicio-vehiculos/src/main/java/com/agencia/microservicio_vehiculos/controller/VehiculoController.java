package com.agencia.microservicio_vehiculos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    // Estado
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Microservicio vehiculos ejecutandose.");
    }
}
