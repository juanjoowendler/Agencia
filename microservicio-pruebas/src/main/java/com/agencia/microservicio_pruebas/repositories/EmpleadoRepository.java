package com.agencia.microservicio_pruebas.repositories;

import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.entities.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {
    @Query("SELECT p FROM Prueba p WHERE p.empleado = :empleado AND ((p.fechaHoraInicio <= :fechaHoraFin AND p.fechaHoraFin >= :fechaHoraInicio))")
    List<Prueba> findByEmpleadoAndFecha(@Param("empleado") Empleado empleado,
                                        @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
                                        @Param("fechaHoraFin") LocalDateTime fechaHoraFin);
}

