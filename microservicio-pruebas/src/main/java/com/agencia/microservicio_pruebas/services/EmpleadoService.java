package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.entities.Empleado;
import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    EmpleadoRepository empleadoRepository;

    public Empleado findByLegajo(Long legajo) {
        return empleadoRepository.findById(legajo).orElse(null);
    }

    public boolean isEmpleadoDisponible(Empleado empleado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        List<Prueba> empleadosOcupados = findByEmpleadoAndFecha(empleado, fechaHoraInicio, fechaHoraFin);
        return empleadosOcupados.isEmpty();
    }

    public List<Prueba> findByEmpleadoAndFecha(Empleado empleado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        return empleadoRepository.findByEmpleadoAndFecha(empleado, fechaHoraInicio, fechaHoraFin);
    }
}
