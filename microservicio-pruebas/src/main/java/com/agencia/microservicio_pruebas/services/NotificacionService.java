package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.entities.Notificacion;
import com.agencia.microservicio_pruebas.entities.NotificacionTelefono;
import com.agencia.microservicio_pruebas.repositories.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    NotificacionRepository notificacionRepository;

    public void saveNotificacion(NotificacionTelefono notificacionTelefono) {
        List<Long> telefonos = notificacionTelefono.getTelefonos();
        Notificacion notificacion = notificacionTelefono.getNotificacion();

        for (long telefono : telefonos) {
            Notificacion nuevaNotificacion = new Notificacion();
            nuevaNotificacion.setDescripcion(notificacion.getDescripcion());
            nuevaNotificacion.setLegajo(notificacion.getLegajo());
            nuevaNotificacion.setTelefonoContactoInteresado(telefono);
            notificacionRepository.save(nuevaNotificacion);
        }
    }
}
