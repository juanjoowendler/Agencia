package com.agencia.microservicio_pruebas.repositories;

import com.agencia.microservicio_pruebas.entities.Interesado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteresadoRepository extends CrudRepository<Interesado, Long> {
}
