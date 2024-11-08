package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.dtos.IncidentesDTO;
import com.agencia.microservicio_pruebas.dtos.IncidentesEmpleadoDTO;
import com.agencia.microservicio_pruebas.entities.*;
import com.agencia.microservicio_pruebas.repositories.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PruebaService {
    @Autowired
    PruebaRepository pruebaRepository;

    @Autowired
    VehiculoService vehiculoService;

    @Autowired
    EmpleadoService empleadoService;

    @Autowired
    InteresadoService interesadoService;

    @Autowired
    PosicionService posicionService;


    public Prueba findById(Long id) {
        return pruebaRepository.findById(id).orElse(null);
    }

    public List<Prueba> findAll() {
        return (List<Prueba>) pruebaRepository.findAll();
    }

    public List<Prueba> findAllEnCurso(LocalDateTime fechaActual) {
        return pruebaRepository.findAllEnCurso(fechaActual);
    }

    public Prueba savePrueba(Prueba prueba) {
        return pruebaRepository.save(prueba);
    }

    public boolean isVehiculoDisponible(Vehiculo vehiculo, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        List<Prueba> pruebasEnCurso = findByVehiculoAndFecha(vehiculo, fechaHoraInicio, fechaHoraFin);
        return pruebasEnCurso.isEmpty();
    }

    public List<Prueba> findByVehiculoAndFecha(Vehiculo vehiculo, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        return pruebaRepository.findByVehiculoAndFecha(vehiculo, fechaHoraInicio, fechaHoraFin);
    }

    // Metodo para manejar validaciones y preparación de la entidad Prueba
    public String validarYPrepararPrueba(Prueba prueba) {
        Empleado empleado = empleadoService.findByLegajo(prueba.getEmpleado().getLegajo());
        if (empleado == null) {
            return "Empleado no encontrado.";
        }

        Interesado interesado = interesadoService.findById(prueba.getInteresado().getId());
        if (interesado == null) {
            return "Interesado no encontrado.";
        }

        Vehiculo vehiculo = vehiculoService.findById(prueba.getVehiculo().getId());
        if (vehiculo == null) {
            return "Vehículo no encontrado.";
        }

        // Asignar entidades a la prueba
        prueba.setEmpleado(empleado);
        prueba.setInteresado(interesado);
        prueba.setVehiculo(vehiculo);
        prueba.setActiva(true);

        // Validaciones adicionales
        if (interesado.isRestringido()) {
            return "El cliente está restringido para realizar pruebas.";
        }

        LocalDateTime fechaVencimiento = interesado.getFechaVencimientoLicencia();
        if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDateTime.now())) {
            return "El cliente tiene la licencia vencida.";
        }

        LocalDateTime fechaHoraInicio = prueba.getFechaHoraInicio();
        LocalDateTime fechaHoraFin = prueba.getFechaHoraFin();

        if (!isVehiculoDisponible(vehiculo, fechaHoraInicio, fechaHoraFin)) {
            return "El vehículo no está disponible en el rango de tiempo solicitado.";
        }

        if (!empleadoService.isEmpleadoDisponible(empleado, fechaHoraInicio, fechaHoraFin)) {
            return "El empleado no está disponible en el rango de tiempo solicitado.";
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        if (prueba.getFechaHoraFin().isBefore(fechaActual)) {
            prueba.setActiva(false);
        }

        return null;
    }

    // Reporte Incidentes
    public List<IncidentesDTO> findIncidentes() {
        return pruebaRepository.findIncidentes();
    }

    // Reporte Incidentes para un Empleado
    public List<IncidentesEmpleadoDTO> findIncidentesParaUnEmpleado(Long legajo) {
        return pruebaRepository.findIncidentesParaUnEmpleado(legajo);
    }
}
