package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.entities.Posicion;
import com.agencia.microservicio_pruebas.repositories.PosicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PosicionService {
    @Autowired
    PosicionRepository posicionRepository;

    public Posicion savePosicion(Posicion posicion) {
        return posicionRepository.save(posicion);
    }
}
