package com.agencia.microservicio_pruebas.repositories;

import com.agencia.microservicio_pruebas.dtos.IncidentesDTO;
import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.entities.Vehiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PruebaRepository extends CrudRepository<Prueba, Long> {
    @Query("SELECT p FROM Prueba p WHERE p.vehiculo = :vehiculo AND ((p.fechaHoraInicio <= :fechaHoraFin AND p.fechaHoraFin >= :fechaHoraInicio))")
    List<Prueba> findByVehiculoAndFecha(@Param("vehiculo") Vehiculo vehiculo,
                                        @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
                                        @Param("fechaHoraFin") LocalDateTime fechaHoraFin);

    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraInicio <= :fechaActual AND p.fechaHoraFin >= :fechaActual")
    List<Prueba> findAllEnCurso(@Param("fechaActual") LocalDateTime fechaActual);

    @Query("SELECT new com.agencia.microservicio_pruebas.dtos.IncidentesDTO(" +
            "p.id, p.fechaHoraInicio, p.fechaHoraFin, " +
            "e.legajo, e.apellido, e.nombre, " +
            "i.apellido, i.nombre, n.descripcion, v.patente) " +
            "FROM Prueba p " +
            "JOIN Empleado e ON p.empleado.legajo = e.legajo " +
            "JOIN Notificacion n ON n.legajo = e.legajo " +
            "JOIN Interesado i ON p.interesado.id = i.id " +
            "JOIN Vehiculo v ON p.vehiculo.id = v.id " +
            "WHERE n.descripcion IS NOT NULL")
    List<IncidentesDTO> findIncidentes();


}
