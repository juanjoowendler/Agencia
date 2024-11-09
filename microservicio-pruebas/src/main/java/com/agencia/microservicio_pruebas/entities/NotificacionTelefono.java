package com.agencia.microservicio_pruebas.entities;

import com.agencia.microservicio_pruebas.services.NotificacionService;
import lombok.Data;

import javax.swing.text.html.parser.TagElement;
import java.util.List;

@Data
public class NotificacionTelefono {
    private Notificacion notificacion;
    private List<Long> telefonos;
}
