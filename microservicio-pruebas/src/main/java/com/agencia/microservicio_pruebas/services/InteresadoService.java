package com.agencia.microservicio_pruebas.services;


import com.agencia.microservicio_pruebas.entities.Interesado;
import com.agencia.microservicio_pruebas.repositories.InteresadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InteresadoService {
    @Autowired
    InteresadoRepository interesadoRepository;

    public Interesado findById(Long id) {
        return interesadoRepository.findById(id).orElse(null);
    }
}
