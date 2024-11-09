package com.agencia.microservicio_pruebas.controllers;

import com.agencia.microservicio_pruebas.dtos.DetallesPruebaVehiculoDTO;
import com.agencia.microservicio_pruebas.dtos.IncidentesDTO;
import com.agencia.microservicio_pruebas.dtos.IncidentesEmpleadoDTO;
import com.agencia.microservicio_pruebas.dtos.KmRegistradosPorVehiculoDTO;
import com.agencia.microservicio_pruebas.entities.Notificacion;
import com.agencia.microservicio_pruebas.entities.NotificacionTelefono;
import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.services.NotificacionService;
import com.agencia.microservicio_pruebas.services.PruebaService;
import com.agencia.microservicio_pruebas.services.ReporteService;
import com.agencia.microservicio_pruebas.services.VehiculoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pruebas")
public class PruebaController {

    @Autowired
    private PruebaService pruebaService;

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Microservicio pruebas ejecutándose.");
    }

    @PostMapping()
    public ResponseEntity<String> savePrueba(@RequestBody Prueba prueba) {
        // Llamar al metodo de validacion y preparación
        String error = pruebaService.validarYPrepararPrueba(prueba);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Guardar la prueba si no hay errores
        pruebaService.savePrueba(prueba);
        return ResponseEntity.ok("Prueba creada exitosamente");
    }

    @GetMapping("/listado")
    public List<Prueba> findAllPruebas() {
        return pruebaService.findAll();
    }

    @GetMapping("/en-curso")
    public List<Prueba> getPruebasEnCurso() {
        LocalDateTime fechaActual = LocalDateTime.now();
        return pruebaService.findAllEnCurso(fechaActual);
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<String> finalizarPrueba(@PathVariable("id") Long id, @RequestBody String comentario) {
        Prueba prueba = pruebaService.findById(id);
        if (prueba == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prueba no encontrada.");
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        if (fechaActual.isBefore(prueba.getFechaHoraInicio()) || fechaActual.isAfter(prueba.getFechaHoraFin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La prueba no está en curso.");
        }

        prueba.setFechaHoraFin(LocalDateTime.now());
        prueba.setComentarios(comentario);
        prueba.setActiva(false);
        pruebaService.savePrueba(prueba);

        return ResponseEntity.ok("Prueba finalizada exitosamente.");
    }

    // REPORTE

    // Incidentes

    @GetMapping("/reporte/incidentes")
    public void generarReporteIncidentes(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Incidentes.xlsx");

        // Obtener los incidentes
        List<IncidentesDTO> incidentes = pruebaService.findIncidentes();

        // Generar el reporte y escribirlo en la respuesta HTTP
        byte[] excelData = reporteService.generarReporteIncidentes(incidentes);
        response.getOutputStream().write(excelData);
    }

    // Incidentes para un Empleado
    @GetMapping("/reporte/incidentes/{id}")
    public void generarReporteIncidentesParaUnEmpleado(@PathVariable("id") Long legajo, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Incidentes_Empleado_" + legajo + ".xlsx");

        // Obtener los incidentes específicos del empleado
        List<IncidentesEmpleadoDTO> incidentesEmpleado = pruebaService.findIncidentesParaUnEmpleado(legajo);

        // Generar el reporte y escribirlo en la respuesta HTTP
        byte[] excelData = reporteService.generarReporteIncidentesParaUnEmpleado(incidentesEmpleado);
        response.getOutputStream().write(excelData);
    }

    // Km recorridos por un vehiculo en un periodo determinado
    @GetMapping("/reporte/km-vehiculo/{id}")
    public void generarReporteKmPorVehiculo(
            @PathVariable("id") Long idVehiculo,
            @RequestParam("fechaHoraInicio") String fechaHoraInicio,
            @RequestParam("fechaHoraFin") String fechaHoraFin,
            HttpServletResponse response) throws IOException {

        LocalDateTime inicio = LocalDateTime.parse(fechaHoraInicio);
        LocalDateTime fin = LocalDateTime.parse(fechaHoraFin);

        // Obtener los kilómetros recorridos por el vehículo en el rango de fechas
        List<KmRegistradosPorVehiculoDTO> kmRegistrados = vehiculoService.findKmRegistradosByVehicle(inicio, fin, idVehiculo);

        // Configurar la respuesta HTTP para generar el archivo Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Km_Vehiculo_" + idVehiculo + ".xlsx");

        // Generar el reporte Excel y escribirlo en la respuesta HTTP
        byte[] excelData = reporteService.generarReporteKmPorVehiculo(kmRegistrados);
        response.getOutputStream().write(excelData);
    }

    // Detalle de pruebas para un vehiculo
    @GetMapping("/reporte/detalle-vehiculo/{id}")
    public void generarReportePruebasVehiculo(
            @PathVariable("id") Long idVehiculo,
            HttpServletResponse response) throws IOException {

        // Ejecuta la consulta para obtener las pruebas del vehículo
        List<DetallesPruebaVehiculoDTO> pruebas = vehiculoService.findPruebasByVehiculoId(idVehiculo);

        // Configura la respuesta HTTP para el archivo Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Pruebas_Vehiculo_" + idVehiculo + ".xlsx");

        // Genera el archivo Excel y escribe en la respuesta HTTP
        byte[] excelData = reporteService.generarReportePruebasVehiculo(pruebas);
        response.getOutputStream().write(excelData);
    }

    // PUNTO E
    @PostMapping("/notificar")
    public ResponseEntity<String> savePrueba(@RequestBody NotificacionTelefono notificacionTelefono) {
        notificacionService.saveNotificacion(notificacionTelefono);
        return ResponseEntity.ok("Usuarios notificados.");
    }


}
