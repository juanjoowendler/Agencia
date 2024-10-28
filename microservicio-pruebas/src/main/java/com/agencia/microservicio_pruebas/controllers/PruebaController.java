package com.agencia.microservicio_pruebas.controllers;

import com.agencia.microservicio_pruebas.entities.Empleado;
import com.agencia.microservicio_pruebas.entities.Interesado;
import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.entities.Vehiculo;
import com.agencia.microservicio_pruebas.services.EmpleadoService;
import com.agencia.microservicio_pruebas.services.InteresadoService;
import com.agencia.microservicio_pruebas.services.PruebaService;
import com.agencia.microservicio_pruebas.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pruebas")
public class PruebaController {

    @Autowired
    private PruebaService pruebaService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private InteresadoService interesadoService;

    // PUNTO A
    @PostMapping()
    public ResponseEntity<String> savePrueba(@RequestBody Prueba prueba) {

        Empleado empleado = empleadoService.findByLegajo(prueba.getEmpleado().getLegajo());
        Interesado interesado = interesadoService.findById(prueba.getInteresado().getId());
        Vehiculo vehiculo = vehiculoService.findById(prueba.getVehiculo().getId());

        // Verificar que cada entidad haya sido encontrada
        if (empleado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado.");
        }
        if (interesado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Interesado no encontrado.");
        }
        if (vehiculo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehículo no encontrado.");
        }

        // Asignar las entidades encontradas a la prueba
        prueba.setEmpleado(empleado);
        prueba.setInteresado(interesado);
        prueba.setVehiculo(vehiculo);

        // Validar si el cliente está restringido
        if (interesado.isRestringido()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El cliente está restringido para realizar pruebas.");
        }

        // Validar si la licencia está vencida
        LocalDateTime fechaVencimiento = interesado.getFechaVencimientoLicencia();
        if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El cliente tiene la licencia vencida.");
        }

        // Validar la disponibilidad del vehículo
        LocalDateTime fechaHoraInicio = prueba.getFechaHoraInicio();
        LocalDateTime fechaHoraFin = prueba.getFechaHoraFin();

        if (!pruebaService.isVehiculoDisponible(vehiculo, fechaHoraInicio, fechaHoraFin)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El vehículo no está disponible en el rango de tiempo solicitado.");
        }

        if (!empleadoService.isEmpleadoDisponible(empleado, fechaHoraInicio, fechaHoraFin)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El empleado no está disponible en el rango de tiempo solicitado.");
        }

        // Si pasa todas las validaciones, guardar la prueba
        pruebaService.savePrueba(prueba);
        return ResponseEntity.ok("Prueba creada exitosamente");
    }

    // CONTROL PUNTO B (traer todas las pruebas)
    @GetMapping()
    public List<Prueba> findAllPruebas() {
        return pruebaService.findAll();
    }

    // PUNTO B
    @GetMapping("/en-curso")
    public List<Prueba> getPruebasEnCurso() {
        LocalDateTime fechaActual = LocalDateTime.now();
        return pruebaService.findAllEnCurso(fechaActual);
    }

    // PUNTO C
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<String> finalizarPrueba(@PathVariable("id") Long id, @RequestBody String comentario) {
        Prueba prueba = pruebaService.findById(id);
        System.out.println(prueba);
        if (prueba == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prueba no encontrada.");
        }

        // Verificar si la prueba está en curso
        LocalDateTime fechaActual = LocalDateTime.now();
        if (fechaActual.isBefore(prueba.getFechaHoraInicio()) || fechaActual.isAfter(prueba.getFechaHoraFin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La prueba no está en curso.");
        }

        prueba.setFechaHoraFin(LocalDateTime.now());
        prueba.setComentarios(comentario);
        pruebaService.savePrueba(prueba);

        return ResponseEntity.ok("Prueba finalizada exitosamente.");
    }
}

