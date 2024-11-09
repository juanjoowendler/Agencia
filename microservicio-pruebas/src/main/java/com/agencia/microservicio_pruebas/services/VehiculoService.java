package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.dtos.KmRegistradosPorVehiculoDTO;
import com.agencia.microservicio_pruebas.entities.Vehiculo;
import com.agencia.microservicio_pruebas.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehiculoService {
    @Autowired
    VehiculoRepository vehiculoRepository;

    public Vehiculo findById(Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    // Km recorridos por un vehiculo en un periodo determinado
    public List<KmRegistradosPorVehiculoDTO> findKmRegistradosByVehicle(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Long idVehiculo) {
        return vehiculoRepository.findKmRegistradosByVehicle(fechaHoraInicio, fechaHoraFin, idVehiculo);
    }
}
