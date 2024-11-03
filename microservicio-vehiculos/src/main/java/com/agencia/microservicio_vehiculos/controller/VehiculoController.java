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
            // Posicion del vehiculo
            double latitud = posicion.getLatitud();
            double longitud = posicion.getLongitud();

            // Obtener la configuración de la API
            ConfiguracionResponse configuracionAPI = configuracionService.obtenerConfiguracion();
            double latAgencia = configuracionAPI.getCoordenadasAgencia().getLat();
            double lonAgencia = configuracionAPI.getCoordenadasAgencia().getLon();
            double radioAdmitidoKm = configuracionAPI.getRadioAdmitidoKm();

            // Calcular el radio en grados para la latitud y la longitud
            double radioLatitudGrados = radioAdmitidoKm / 111.0;
            double radioLongitudGrados = radioAdmitidoKm / (111.0 * Math.cos(Math.toRadians(latAgencia)));

            // Comprobar si la posición está fuera del radio
            if (latitud < (latAgencia - radioLatitudGrados) || latitud > (latAgencia + radioLatitudGrados) ||
                    longitud < (lonAgencia - radioLongitudGrados) || longitud > (lonAgencia + radioLongitudGrados)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El vehiculo se encuentra fuera del radio permitido de 5 km alrededor de la Agencia.");
            }

            // Comprobar las zonas restringidas particulares
            List<ZonaRestringida> zonasRestringidas = configuracionAPI.getZonasRestringidas();
            System.out.println(zonasRestringidas);
            for (ZonaRestringida zona : zonasRestringidas) {
                double latNoroeste = zona.getNoroeste().getLat();
                double lonNoroeste = zona.getNoroeste().getLon();
                double latSureste = zona.getSureste().getLat();
                double lonSureste = zona.getSureste().getLon();

                // Comprobar si la posición está dentro de la zona restringida
                if (latitud <= latNoroeste && latitud >= latSureste && longitud >= lonNoroeste && longitud <= lonSureste) {
                    // ACA DEBERIA DE ENVIAR UN MENSAJE...
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("El vehiculo se encuentra en una prueba y su posición está dentro de una zona restringida.");
                }
            }

            // Si pasa todas las verificaciones
            return ResponseEntity.status(HttpStatus.OK).body("El vehiculo se encuentra en una prueba y su posición no está dentro de una zona restringida ni fuera del radio permitido.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("El vehiculo no se encuentra en una prueba.");
    }
}


