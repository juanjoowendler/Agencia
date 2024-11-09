package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.dtos.DetallesPruebaVehiculoDTO;
import com.agencia.microservicio_pruebas.dtos.IncidentesDTO;
import com.agencia.microservicio_pruebas.dtos.IncidentesEmpleadoDTO;
import com.agencia.microservicio_pruebas.dtos.KmRegistradosPorVehiculoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteService {

    // Incidentes
    public byte[] generarReporteIncidentes(List<IncidentesDTO> incidentes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Incidentes");

        // Crear columnas excel en el orden correcto según la consulta SQL
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Incidente");
        headerRow.createCell(1).setCellValue("Fecha Hora Inicio");
        headerRow.createCell(2).setCellValue("Fecha Hora Fin");
        headerRow.createCell(3).setCellValue("Legajo Empleado");
        headerRow.createCell(4).setCellValue("Apellido Empleado");
        headerRow.createCell(5).setCellValue("Nombre Empleado");
        headerRow.createCell(6).setCellValue("Apellido Interesado");
        headerRow.createCell(7).setCellValue("Nombre Interesado");
        headerRow.createCell(8).setCellValue("Descripción Notificación");
        headerRow.createCell(9).setCellValue("Patente Vehículo");

        // Poblar los datos de incidentes en el orden correcto
        int rowNum = 1;
        for (IncidentesDTO incidente : incidentes) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(incidente.getId()); // ID Incidente
            row.createCell(1).setCellValue(incidente.getFechaHoraInicio().toString()); // Fecha Hora Inicio
            row.createCell(2).setCellValue(incidente.getFechaHoraFin().toString()); // Fecha Hora Fin
            row.createCell(3).setCellValue(incidente.getLegajoEmpleado()); // Legajo Empleado
            row.createCell(4).setCellValue(incidente.getApellidoEmpleado()); // Apellido Empleado
            row.createCell(5).setCellValue(incidente.getNombreEmpleado()); // Nombre Empleado
            row.createCell(6).setCellValue(incidente.getApellidoInteresado()); // Apellido Interesado
            row.createCell(7).setCellValue(incidente.getNombreInteresado()); // Nombre Interesado
            row.createCell(8).setCellValue(incidente.getDescripcionNotificacion()); // Descripción Notificación
            row.createCell(9).setCellValue(incidente.getPatenteVehiculo()); // Patente Vehículo
        }

        // Escribir el archivo en Bytes para poder convertirlo a un Excel
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        } finally {
            workbook.close();
        }
    }

    //  Incidentes para un Empleado
    public byte[] generarReporteIncidentesParaUnEmpleado(List<IncidentesEmpleadoDTO> incidentesEmpleado) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("IncidentesEmpleado");

        // Crear la fila de encabezado con los nombres de las columnas
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Prueba");
        headerRow.createCell(1).setCellValue("Legajo");
        headerRow.createCell(2).setCellValue("Apellido Empleado");
        headerRow.createCell(3).setCellValue("Nombre Empleado");
        headerRow.createCell(4).setCellValue("Teléfono Contacto");
        headerRow.createCell(5).setCellValue("Patente Vehículo");
        headerRow.createCell(6).setCellValue("Incidente");
        headerRow.createCell(7).setCellValue("Cantidad de Incidentes");

        // Poblar los datos de los incidentes del empleado en el reporte
        int rowNum = 1;
        for (IncidentesEmpleadoDTO incidente : incidentesEmpleado) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(incidente.getIdPrueba()); // ID Prueba
            row.createCell(1).setCellValue(incidente.getLegajo()); // Legajo
            row.createCell(2).setCellValue(incidente.getEmpleadoApellido()); // Apellido Empleado
            row.createCell(3).setCellValue(incidente.getEmpleadoNombre()); // Nombre Empleado
            row.createCell(4).setCellValue(incidente.getTelefonoContacto()); // Teléfono Contacto
            row.createCell(5).setCellValue(incidente.getPatente()); // Patente Vehículo
            row.createCell(6).setCellValue(incidente.getIncidente()); // Incidente
            row.createCell(7).setCellValue(incidente.getCantidadIncidentes()); // Cantidad de Incidentes
        }

        // Escribir el archivo en Bytes para poder convertirlo a un Excel
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        } finally {
            workbook.close();
        }
    }

    // Km recorridos por un vehiculo en un periodo determinado
    public byte[] generarReporteKmPorVehiculo(List<KmRegistradosPorVehiculoDTO> kmRegistrados) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Kilómetros por Vehículo");

        // Encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Fecha Hora Inicio");
        headerRow.createCell(2).setCellValue("Fecha Hora Fin");
        headerRow.createCell(3).setCellValue("Patente");
        headerRow.createCell(4).setCellValue("Kilómetros Registrados");

        // Llenar con los datos
        int rowNum = 1;
        for (KmRegistradosPorVehiculoDTO dto : kmRegistrados) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getId());
            row.createCell(1).setCellValue(dto.getFechaHoraInicio().toString());
            row.createCell(2).setCellValue(dto.getFechaHoraFin().toString());
            row.createCell(3).setCellValue(dto.getPatente());
            row.createCell(4).setCellValue(dto.getKmRegistrados());
        }

        // Ajustar el tamaño de las columnas
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // Convertir a byte[] para escribir en la respuesta HTTP
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // Detalle de pruebas para un vehiculo
    public byte[] generarReportePruebasVehiculo(List<DetallesPruebaVehiculoDTO> pruebas) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pruebas Vehículo");

        // Encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Vehículo");
        headerRow.createCell(1).setCellValue("Patente");
        headerRow.createCell(2).setCellValue("Modelo");
        headerRow.createCell(3).setCellValue("Marca");
        headerRow.createCell(4).setCellValue("Fecha Hora Inicio");
        headerRow.createCell(5).setCellValue("Fecha Hora Fin");
        headerRow.createCell(6).setCellValue("Tipo Documento");
        headerRow.createCell(7).setCellValue("Documento");
        headerRow.createCell(8).setCellValue("Apellido Interesado");
        headerRow.createCell(9).setCellValue("Nombre Interesado");
        headerRow.createCell(10).setCellValue("Nro Licencia");
        headerRow.createCell(11).setCellValue("Vencimiento Licencia");
        headerRow.createCell(12).setCellValue("Legajo Empleado");
        headerRow.createCell(13).setCellValue("Apellido Empleado");
        headerRow.createCell(14).setCellValue("Nombre Empleado");
        headerRow.createCell(15).setCellValue("Comentarios");

        // Llenar datos de las pruebas
        int rowNum = 1;
        for (DetallesPruebaVehiculoDTO prueba : pruebas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(prueba.getVehiculoId());
            row.createCell(1).setCellValue(prueba.getPatente());
            row.createCell(2).setCellValue(prueba.getDescripcionModelo());
            row.createCell(3).setCellValue(prueba.getNombreMarca());
            row.createCell(4).setCellValue(prueba.getFechaHoraInicio().toString());
            row.createCell(5).setCellValue(prueba.getFechaHoraFin().toString());
            row.createCell(6).setCellValue(prueba.getTipoDocumentoInteresado());
            row.createCell(7).setCellValue(prueba.getDocumentoInteresado());
            row.createCell(8).setCellValue(prueba.getApellidoInteresado());
            row.createCell(9).setCellValue(prueba.getNombreInteresado());
            row.createCell(10).setCellValue(prueba.getNroLicenciaInteresado());
            row.createCell(11).setCellValue(prueba.getFechaVencimientoLicencia().toString());
            row.createCell(12).setCellValue(prueba.getLegajoEmpleado());
            row.createCell(13).setCellValue(prueba.getApellidoEmpleado());
            row.createCell(14).setCellValue(prueba.getNombreEmpleado());
            row.createCell(15).setCellValue(prueba.getComentarios());
        }

        // Convertir a byte[] para escribir en la respuesta HTTP
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

}
