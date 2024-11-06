package com.agencia.microservicio_pruebas.services;

import com.agencia.microservicio_pruebas.entities.Prueba;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteService {

    public byte[] generarReporteIncidentes(List<Prueba> incidentes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Incidentes");

        // Crear columnas excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Patente Vehículo");
        headerRow.createCell(1).setCellValue("Tipo Documento");
        headerRow.createCell(2).setCellValue("Documento Interesado");
        headerRow.createCell(3).setCellValue("Nombre Interesado");
        headerRow.createCell(4).setCellValue("Apellido Interesado");
        headerRow.createCell(5).setCellValue("Restringido");
        headerRow.createCell(6).setCellValue("Legajo Empleado");
        headerRow.createCell(7).setCellValue("Nombre Empleado");
        headerRow.createCell(8).setCellValue("Apellido Empleado");
        headerRow.createCell(9).setCellValue("Fecha Hora Inicio");
        headerRow.createCell(10).setCellValue("Fecha Hora Fin");
        headerRow.createCell(11).setCellValue("Activa");
        headerRow.createCell(12).setCellValue("Comentarios");

        // Poblar los datos de incidentes
        int rowNum = 1;
        for (Prueba prueba : incidentes) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(prueba.getVehiculo().getPatente());
            row.createCell(1).setCellValue(prueba.getInteresado().getTipoDocumento());
            row.createCell(2).setCellValue(prueba.getInteresado().getDocumento());
            row.createCell(3).setCellValue(prueba.getInteresado().getNombre());
            row.createCell(4).setCellValue(prueba.getInteresado().getApellido());
            row.createCell(5).setCellValue(prueba.getInteresado().isRestringido() ? "Sí" : "No");
            row.createCell(6).setCellValue(prueba.getEmpleado().getLegajo());
            row.createCell(7).setCellValue(prueba.getEmpleado().getNombre());
            row.createCell(8).setCellValue(prueba.getEmpleado().getApellido());
            row.createCell(9).setCellValue(prueba.getFechaHoraInicio().toString());
            row.createCell(10).setCellValue(prueba.getFechaHoraFin().toString());
            row.createCell(11).setCellValue(prueba.isActiva() ? "Sí" : "No");
            row.createCell(12).setCellValue(prueba.getComentarios());
        }

        // Acomodar filas del excel
        for (int i = 0; i <= 12; i++) {
            sheet.autoSizeColumn(i);
        }

        // Escribir el archivo en Bytes para poder convertirlo a un Excel
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        } finally {
            workbook.close();
        }
    }
}
