package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.entities.Vehiculo;
import com.agencia.microservicio_pruebas.repositories.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PruebaService {
    @Autowired
    PruebaRepository pruebaRepository;

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

}
