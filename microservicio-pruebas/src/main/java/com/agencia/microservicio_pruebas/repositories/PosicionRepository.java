package com.agencia.microservicio_pruebas.repositories;

import com.agencia.microservicio_pruebas.entities.Posicion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosicionRepository extends CrudRepository<Posicion, Long> {
}
