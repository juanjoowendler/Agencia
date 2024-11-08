package com.agencia.microservicio_pruebas.controllers;

import com.agencia.microservicio_pruebas.dtos.IncidentesDTO;
import com.agencia.microservicio_pruebas.dtos.IncidentesEmpleadoDTO;
import com.agencia.microservicio_pruebas.entities.Prueba;
import com.agencia.microservicio_pruebas.services.PruebaService;
import com.agencia.microservicio_pruebas.services.ReporteService;
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

}
