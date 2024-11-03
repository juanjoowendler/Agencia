package com.agencia.microservicio_vehiculos.services;

import com.agencia.microservicio_vehiculos.entities.Notificacion;
import com.agencia.microservicio_vehiculos.repositories.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;

    public Notificacion saveNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }
}
