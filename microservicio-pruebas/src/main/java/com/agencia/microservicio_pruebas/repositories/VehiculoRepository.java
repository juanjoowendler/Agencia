package com.agencia.microservicio_pruebas.repositories;


import com.agencia.microservicio_pruebas.entities.Vehiculo;
import org.springframework.data.repository.CrudRepository;

public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {
}
