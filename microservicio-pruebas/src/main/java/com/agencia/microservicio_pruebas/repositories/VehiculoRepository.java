package com.agencia.microservicio_pruebas.repositories;


import com.agencia.microservicio_pruebas.dtos.KmRegistradosPorVehiculoDTO;
import com.agencia.microservicio_pruebas.entities.Vehiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {
    // REPORTE

    // Km recorridos por un vehiculo en un periodo determinado
    @Query("SELECT new com.agencia.microservicio_pruebas.dtos.KmRegistradosPorVehiculoDTO(" +
            "p.id, " +
            "p.fechaHoraInicio, " +
            "p.fechaHoraFin, " +
            "v.patente, " +
            "SUM(CAST(p.kmRegistrados AS double))) " +
            "FROM Prueba p " +
            "JOIN Vehiculo v ON p.vehiculo.id = v.id " +
            "WHERE p.fechaHoraInicio >= :fechaHoraInicio " +
            "AND p.fechaHoraFin <= :fechaHoraFin " +
            "AND v.id = :idVehiculo " +
            "GROUP BY v.patente")
    List<KmRegistradosPorVehiculoDTO> findKmRegistradosByVehicle(
            @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
            @Param("fechaHoraFin") LocalDateTime fechaHoraFin,
            @Param("idVehiculo") Long idVehiculo
    );

}
