package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.entities.Vehiculo;
import com.agencia.microservicio_pruebas.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService {
    @Autowired
    VehiculoRepository vehiculoRepository;

    public Vehiculo findById(Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }
}
