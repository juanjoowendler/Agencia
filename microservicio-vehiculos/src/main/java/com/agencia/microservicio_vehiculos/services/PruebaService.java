package com.agencia.microservicio_vehiculos.services;

import com.agencia.microservicio_vehiculos.entities.Prueba;
import com.agencia.microservicio_vehiculos.entities.Vehiculo;
import com.agencia.microservicio_vehiculos.repositories.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PruebaService {
    @Autowired
    PruebaRepository pruebaRepository;

    public List<Prueba> isVehiculoInPruebasAndCumpleLimites(Vehiculo vehiculo, LocalDateTime fechaHora) {
        return findByVehiculoAndFecha(vehiculo, fechaHora);
    }

    public List<Prueba> findByVehiculoAndFecha(Vehiculo vehiculo, LocalDateTime fechaHora) {
        return pruebaRepository.findByVehiculoAndFecha(vehiculo, fechaHora);
    }
}
