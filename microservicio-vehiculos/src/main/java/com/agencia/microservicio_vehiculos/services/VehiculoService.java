package com.agencia.microservicio_vehiculos.services;

import com.agencia.microservicio_vehiculos.entities.ConfiguracionResponse;
import com.agencia.microservicio_vehiculos.entities.Posicion;
import com.agencia.microservicio_vehiculos.entities.Vehiculo;
import com.agencia.microservicio_vehiculos.entities.ZonaRestringida;
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

    public String evaluarPosicion(Posicion posicion) {
        Vehiculo vehiculo = posicion.getVehiculo();
        LocalDateTime fechaHora = LocalDateTime.parse(posicion.getFechaHora());

        if (!pruebaService.isVehiculoInPruebasAndCumpleLimites(vehiculo, fechaHora)) {
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
                    return "El vehiculo se encuentra en una prueba y su posición está dentro de una zona restringida.";
                }
            }

            // Si pasa todas las verificaciones
            return "El vehiculo se encuentra en una prueba y su posición no está dentro de una zona restringida ni fuera del radio permitido.";
        }
        return "El vehiculo no se encuentra en una prueba.";
    }
}
