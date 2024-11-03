package com.agencia.microservicio_vehiculos.controller;

import com.agencia.microservicio_vehiculos.entities.ConfiguracionResponse;
import com.agencia.microservicio_vehiculos.entities.Posicion;
import com.agencia.microservicio_vehiculos.entities.Vehiculo;
import com.agencia.microservicio_vehiculos.entities.ZonaRestringida;
import com.agencia.microservicio_vehiculos.services.ConfiguracionService;
import com.agencia.microservicio_vehiculos.services.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private PruebaService pruebaService;

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

    // PUNTO D
    // Evaluates vehicle position
    @PostMapping("/evaluar")
    public ResponseEntity<String> evaluarPosicion(@RequestBody Posicion posicion) {
        Vehiculo vehiculo = posicion.getVehiculo();
        LocalDateTime fechaHora = LocalDateTime.parse(posicion.getFechaHora());

        if (!pruebaService.isVehiculoInPruebasAndCumpleLimites(vehiculo, fechaHora)) {
            double latitud = posicion.getLatitud();
            double longitud = posicion.getLongitud();

            ConfiguracionResponse configuracionAPI = configuracionService.obtenerConfiguracion();
            List<ZonaRestringida> zonasRestringidas = configuracionAPI.getZonasRestringidas();
            System.out.println(zonasRestringidas);
            // Iterar sobre las zonas restringidas
            for (ZonaRestringida zona : zonasRestringidas) {
                double latNoroeste = zona.getNoroeste().getLat();
                double lonNoroeste = zona.getNoroeste().getLon();
                double latSureste = zona.getSureste().getLat();
                double lonSureste = zona.getSureste().getLon();

                // Comprobar si la posición está dentro de la zona restringida
                if (latitud <= latNoroeste && latitud >= latSureste && longitud >= lonNoroeste && longitud <= lonSureste) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("El vehiculo se encuentra en una prueba y su posicion esta dentro de una zona restringida.");
                    // enviar mensaje...
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("El vehiculo se encuentra en una prueba y su posicion no esta dentro de una zona restringida.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("El vehiculo no se encuentra en una prueba.");
    }

}
