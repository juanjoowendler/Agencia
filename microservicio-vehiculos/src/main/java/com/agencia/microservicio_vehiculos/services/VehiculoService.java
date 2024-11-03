package com.agencia.microservicio_vehiculos.services;

import com.agencia.microservicio_vehiculos.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehiculoService {

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private PruebaService pruebaService;

    @Autowired
    private NotificacionService notificacionService;

    public String evaluarPosicion(Posicion posicion) {
        Vehiculo vehiculo = posicion.getVehiculo();
        LocalDateTime fechaHora = LocalDateTime.parse(posicion.getFechaHora());
        List<Prueba> pruebas = pruebaService.isVehiculoInPruebasAndCumpleLimites(vehiculo, fechaHora);
        if (!pruebas.isEmpty()) {
            Prueba prueba = pruebas.get(0);
            Empleado empleado = prueba.getEmpleado();
            double latitud = posicion.getLatitud();
            double longitud = posicion.getLongitud();

            ConfiguracionResponse configuracionAPI = configuracionService.obtenerConfiguracion();
            double latAgencia = configuracionAPI.getCoordenadasAgencia().getLat();
            double lonAgencia = configuracionAPI.getCoordenadasAgencia().getLon();
            double radioAdmitidoKm = configuracionAPI.getRadioAdmitidoKm();

            double radioLatitudGrados = radioAdmitidoKm / 111.0;
            double radioLongitudGrados = radioAdmitidoKm / (111.0 * Math.cos(Math.toRadians(latAgencia)));

            // Comprobar si la posición está fuera del radio
            if (latitud < (latAgencia - radioLatitudGrados) || latitud > (latAgencia + radioLatitudGrados) ||
                    longitud < (lonAgencia - radioLongitudGrados) || longitud > (lonAgencia + radioLongitudGrados)) {

                Notificacion notificacion = new Notificacion();
                notificacion.setLegajo(empleado.getLegajo());
                notificacion.setDescripcion("El vehiculo se encuentra fuera del radio permitido. Debe regresar.");
                notificacionService.saveNotificacion(notificacion);

                return "El vehiculo se encuentra fuera del radio permitido de 5 km alrededor de la Agencia.";
            }

            // Comprobar las zonas restringidas particulares
            List<ZonaRestringida> zonasRestringidas = configuracionAPI.getZonasRestringidas();
            for (ZonaRestringida zona : zonasRestringidas) {
                double latNoroeste = zona.getNoroeste().getLat();
                double lonNoroeste = zona.getNoroeste().getLon();
                double latSureste = zona.getSureste().getLat();
                double lonSureste = zona.getSureste().getLon();

                // Comprobar si la posición está dentro de la zona restringida
                if (latitud <= latNoroeste && latitud >= latSureste && longitud <= lonSureste && longitud >= lonNoroeste) {
                    prueba.getInteresado().setRestringido(true);
                    Notificacion notificacion = new Notificacion();
                    notificacion.setLegajo(empleado.getLegajo());
                    notificacion.setDescripcion("El vehiculo se encuentra en una zona restringida. Debe regresar.");
                    notificacionService.saveNotificacion(notificacion);
                    return "El vehiculo se encuentra en una prueba y su posición está dentro de una zona restringida.";
                }
            }

            // Si pasa todas las verificaciones
            return "El vehiculo se encuentra en una prueba y su posición no está dentro de una zona restringida ni fuera del radio permitido.";
        }
        return "El vehiculo no se encuentra en una prueba.";
    }
}
