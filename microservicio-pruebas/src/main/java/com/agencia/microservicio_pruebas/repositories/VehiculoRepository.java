package com.agencia.microservicio_pruebas.repositories;


import com.agencia.microservicio_pruebas.dtos.DetallesPruebaVehiculoDTO;
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

    // Detalle de pruebas para un vehiculo
    @Query("SELECT new com.agencia.microservicio_pruebas.dtos.DetallesPruebaVehiculoDTO(" +
            "v.id, v.patente, m.descripcion, k.nombre, " +
            "p.fechaHoraInicio, p.fechaHoraFin, " +
            "i.tipoDocumento, i.documento, i.apellido, i.nombre, " +
            "i.numeroLicencia, i.fechaVencimientoLicencia, " +
            "e.legajo, e.apellido, e.nombre, p.comentarios) " +
            "FROM Prueba p " +
            "JOIN Vehiculo v ON p.vehiculo.id = v.id " +
            "JOIN Modelo m ON v.modelo.id = m.id " +
            "JOIN Marca k ON m.marca.id = k.id " +
            "JOIN Empleado e ON p.empleado.id = e.legajo " +
            "JOIN Interesado i ON p.interesado.id = i.id " +
            "WHERE v.id = :idVehiculo")
    List<DetallesPruebaVehiculoDTO> findPruebasByVehiculoId(@Param("idVehiculo") Long idVehiculo);
}
