package com.cafeteria.util;

import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generatePedidosReport(List<PedidoDTO> pedidos, String companyName, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header
            addReportHeader(document, companyName, "REPORTE DE PEDIDOS");
            
            if (fechaInicio != null && fechaFin != null) {
                document.add(new Paragraph("Período: " + fechaInicio.format(DATE_FORMATTER) + 
                    " - " + fechaFin.format(DATE_FORMATTER))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));
            }
            
            document.add(new Paragraph("\n"));

            // Table
            float[] columnWidths = {1, 3, 2, 2, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Headers
            table.addHeaderCell("ID");
            table.addHeaderCell("Cliente");
            table.addHeaderCell("Fecha");
            table.addHeaderCell("Estado");
            table.addHeaderCell("Tipo");
            table.addHeaderCell("Total");

            // Data
            BigDecimal totalGeneral = BigDecimal.ZERO;
            for (PedidoDTO pedido : pedidos) {
                table.addCell(String.valueOf(pedido.getIdPedido()));
                table.addCell(pedido.getNombreCliente());
                table.addCell(pedido.getFecha().format(DATE_FORMATTER));
                table.addCell(pedido.getEstado().toString());
                table.addCell(pedido.getTipoEntrega().toString());
                table.addCell("S/. " + pedido.getTotal());
                totalGeneral = totalGeneral.add(pedido.getTotal());
            }

            document.add(table);
            
            // Summary
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de pedidos: " + pedidos.size()).setFontSize(12).setBold());
            document.add(new Paragraph("Total general: S/. " + totalGeneral).setFontSize(12).setBold());

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte PDF de pedidos: " + e.getMessage(), e);
        }
    }

    public byte[] generateProductosReport(List<ProductoDTO> productos, String companyName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            addReportHeader(document, companyName, "REPORTE DE PRODUCTOS");
            document.add(new Paragraph("\n"));

            // Table
            float[] columnWidths = {1, 4, 2, 2, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Headers
            table.addHeaderCell("ID");
            table.addHeaderCell("Nombre");
            table.addHeaderCell("Categoría");
            table.addHeaderCell("Precio");
            table.addHeaderCell("Stock");
            table.addHeaderCell("Estado");

            // Data
            for (ProductoDTO producto : productos) {
                table.addCell(String.valueOf(producto.getIdProducto()));
                table.addCell(producto.getNombre());
                table.addCell(producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "N/A");
                table.addCell("S/. " + producto.getPrecio());
                table.addCell(String.valueOf(producto.getCantidadActual() != null ? producto.getCantidadActual() : 0));
                table.addCell(producto.getEstado().toString());
            }

            document.add(table);
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de productos: " + productos.size()).setFontSize(12).setBold());

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte PDF de productos: " + e.getMessage(), e);
        }
    }

    public byte[] generateUsuariosReport(List<UsuarioDTO> usuarios, String companyName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            addReportHeader(document, companyName, "REPORTE DE USUARIOS");
            document.add(new Paragraph("\n"));

            // Table
            float[] columnWidths = {1, 3, 3, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Headers
            table.addHeaderCell("ID");
            table.addHeaderCell("Nombre");
            table.addHeaderCell("Correo");
            table.addHeaderCell("Rol");
            table.addHeaderCell("Estado");

            // Data
            for (UsuarioDTO usuario : usuarios) {
                table.addCell(String.valueOf(usuario.getIdUsuario()));
                table.addCell(usuario.getNombre() + " " + usuario.getApellido());
                table.addCell(usuario.getCorreo());
                table.addCell(usuario.getNombreRol());
                table.addCell(usuario.getEstado().toString());
            }

            document.add(table);
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de usuarios: " + usuarios.size()).setFontSize(12).setBold());

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte PDF de usuarios: " + e.getMessage(), e);
        }
    }

    public byte[] generateInventarioReport(List<com.cafeteria.dto.InventarioDTO> inventarios, String companyName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            addReportHeader(document, companyName, "REPORTE DE INVENTARIO");
            document.add(new Paragraph("\n"));

            // Table
            float[] columnWidths = {1, 4, 2, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Headers
            table.addHeaderCell("ID");
            table.addHeaderCell("Producto");
            table.addHeaderCell("Stock Actual");
            table.addHeaderCell("Stock Mínimo");
            table.addHeaderCell("Estado");

            // Data
            for (com.cafeteria.dto.InventarioDTO inv : inventarios) {
                table.addCell(String.valueOf(inv.getIdInventario()));
                table.addCell(inv.getNombreProducto());
                table.addCell(String.valueOf(inv.getCantidadActual()));
                table.addCell(String.valueOf(inv.getStockMinimo()));
                
                String estado = inv.getCantidadActual() <= inv.getStockMinimo() ? "BAJO" : "OK";
                table.addCell(estado);
            }

            document.add(table);
            
            long bajoStock = inventarios.stream()
                .filter(inv -> inv.getCantidadActual() <= inv.getStockMinimo())
                .count();
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de productos: " + inventarios.size()).setFontSize(12).setBold());
            document.add(new Paragraph("Productos con stock bajo: " + bajoStock).setFontSize(12).setBold());

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte PDF de inventario: " + e.getMessage(), e);
        }
    }

    private void addReportHeader(Document document, String companyName, String reportTitle) {
        Paragraph header = new Paragraph(companyName)
            .setFontSize(18)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER);
        document.add(header);

        Paragraph title = new Paragraph(reportTitle)
            .setFontSize(14)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        Paragraph date = new Paragraph("Generado: " + LocalDateTime.now().format(DATE_FORMATTER))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(date);
    }
}
