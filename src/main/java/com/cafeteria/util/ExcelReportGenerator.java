package com.cafeteria.util;

import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.dto.UsuarioDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExcelReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generatePedidosExcel(List<PedidoDTO> pedidos) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pedidos");

            // Header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Cliente", "Fecha", "Estado", "Tipo Entrega", "Total", "Atendido Por"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;
            BigDecimal totalGeneral = BigDecimal.ZERO;
            
            for (PedidoDTO pedido : pedidos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pedido.getIdPedido());
                row.createCell(1).setCellValue(pedido.getNombreCliente());
                
                Cell dateCell = row.createCell(2);
                dateCell.setCellValue(pedido.getFecha().format(DATE_FORMATTER));
                
                row.createCell(3).setCellValue(pedido.getEstado().toString());
                row.createCell(4).setCellValue(pedido.getTipoEntrega().toString());
                
                Cell totalCell = row.createCell(5);
                totalCell.setCellValue(pedido.getTotal().doubleValue());
                totalCell.setCellStyle(currencyStyle);
                
                row.createCell(6).setCellValue(pedido.getNombreEmpleado() != null ? pedido.getNombreEmpleado() : "");
                
                totalGeneral = totalGeneral.add(pedido.getTotal());
            }

            // Total row
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(4);
            totalLabelCell.setCellValue("TOTAL GENERAL:");
            totalLabelCell.setCellStyle(headerStyle);
            
            Cell totalValueCell = totalRow.createCell(5);
            totalValueCell.setCellValue(totalGeneral.doubleValue());
            totalValueCell.setCellStyle(currencyStyle);

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de pedidos: " + e.getMessage(), e);
        }
    }

    public byte[] generateProductosExcel(List<ProductoDTO> productos) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Descripción", "Categoría", "Precio", "Stock", "Stock Mínimo", "Estado"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;
            for (ProductoDTO producto : productos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(producto.getIdProducto());
                row.createCell(1).setCellValue(producto.getNombre());
                row.createCell(2).setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
                row.createCell(3).setCellValue(producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "");
                
                Cell priceCell = row.createCell(4);
                priceCell.setCellValue(producto.getPrecio().doubleValue());
                priceCell.setCellStyle(currencyStyle);
                
                row.createCell(5).setCellValue(producto.getCantidadActual() != null ? producto.getCantidadActual() : 0);
                row.createCell(6).setCellValue(producto.getStockMinimo() != null ? producto.getStockMinimo() : 0);
                row.createCell(7).setCellValue(producto.getEstado().toString());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de productos: " + e.getMessage(), e);
        }
    }

    public byte[] generateUsuariosExcel(List<UsuarioDTO> usuarios) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            CellStyle headerStyle = createHeaderStyle(workbook);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Apellido", "Correo", "Rol", "Estado", "Teléfono", "Dirección"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;
            for (UsuarioDTO usuario : usuarios) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(usuario.getIdUsuario());
                row.createCell(1).setCellValue(usuario.getNombre());
                row.createCell(2).setCellValue(usuario.getApellido());
                row.createCell(3).setCellValue(usuario.getCorreo());
                row.createCell(4).setCellValue(usuario.getNombreRol());
                row.createCell(5).setCellValue(usuario.getEstado().toString());
                row.createCell(6).setCellValue(usuario.getTelefono() != null ? usuario.getTelefono() : "");
                row.createCell(7).setCellValue(usuario.getDireccion() != null ? usuario.getDireccion() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de usuarios: " + e.getMessage(), e);
        }
    }

    public byte[] generateInventarioExcel(List<com.cafeteria.dto.InventarioDTO> inventarios) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");

            CellStyle headerStyle = createHeaderStyle(workbook);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Producto", "Stock Actual", "Stock Mínimo", "Unidad Medida", "Estado"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;
            for (com.cafeteria.dto.InventarioDTO inv : inventarios) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(inv.getIdInventario());
                row.createCell(1).setCellValue(inv.getNombreProducto());
                row.createCell(2).setCellValue(inv.getCantidadActual());
                row.createCell(3).setCellValue(inv.getStockMinimo());
                row.createCell(4).setCellValue(inv.getUnidadMedida());
                
                String estado = inv.getCantidadActual() <= inv.getStockMinimo() ? "BAJO STOCK" : "OK";
                row.createCell(5).setCellValue(estado);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de inventario: " + e.getMessage(), e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("S/. #,##0.00"));
        return style;
    }
}
