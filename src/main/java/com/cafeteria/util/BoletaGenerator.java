package com.cafeteria.util;

import com.cafeteria.dto.PedidoDTO;
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
import java.time.format.DateTimeFormatter;

@Component
public class BoletaGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public byte[] generateBoleta(PedidoDTO pedido, String companyName, String companyAddress, String companyPhone) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header
            Paragraph header = new Paragraph(companyName)
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
            document.add(header);

            Paragraph address = new Paragraph(companyAddress)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(address);

            Paragraph phone = new Paragraph("Tel: " + companyPhone)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(phone);

            // Spacer
            document.add(new Paragraph("\n"));

            // Boleta title
            Paragraph boletaTitle = new Paragraph("BOLETA DE VENTA")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
            document.add(boletaTitle);

            Paragraph boletaNumber = new Paragraph("N° " + String.format("%08d", pedido.getIdPedido()))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(boletaNumber);

            document.add(new Paragraph("\n"));

            // Client and date info
            document.add(new Paragraph("Cliente: " + pedido.getNombreCliente()).setFontSize(10));
            document.add(new Paragraph("Fecha: " + pedido.getFecha().format(DATE_FORMATTER)).setFontSize(10));
            document.add(new Paragraph("Tipo de Entrega: " + pedido.getTipoEntrega()).setFontSize(10));
            if (pedido.getNombreEmpleado() != null) {
                document.add(new Paragraph("Atendido por: " + pedido.getNombreEmpleado()).setFontSize(10));
            }

            document.add(new Paragraph("\n"));

            // Items table
            float[] columnWidths = {3, 1, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Headers
            table.addHeaderCell("Producto");
            table.addHeaderCell("Cant.");
            table.addHeaderCell("P. Unit.");
            table.addHeaderCell("Subtotal");

            // Items
            for (var detalle : pedido.getDetalles()) {
                table.addCell(detalle.getNombreProducto());
                table.addCell(String.valueOf(detalle.getCantidad()));
                table.addCell("S/. " + detalle.getPrecioUnitario());
                table.addCell("S/. " + detalle.getSubtotal());
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            // Total
            Paragraph total = new Paragraph("TOTAL: S/. " + pedido.getTotal())
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            // Footer
            document.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph("¡Gracias por su compra!")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(footer);

            document.close();
            
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar boleta PDF: " + e.getMessage(), e);
        }
    }

    public String generateTicketASCII(PedidoDTO pedido, String companyName, String companyAddress, String companyPhone) {
        StringBuilder ticket = new StringBuilder();
        int width = 40;

        // Header
        ticket.append(centerText(companyName, width)).append("\n");
        ticket.append(centerText(companyAddress, width)).append("\n");
        ticket.append(centerText("Tel: " + companyPhone, width)).append("\n");
        ticket.append(repeatChar('=', width)).append("\n");

        // Ticket title
        ticket.append(centerText("TICKET DE VENTA", width)).append("\n");
        ticket.append(centerText("N° " + String.format("%08d", pedido.getIdPedido()), width)).append("\n");
        ticket.append(repeatChar('=', width)).append("\n\n");

        // Client and date info
        ticket.append("Cliente: ").append(pedido.getNombreCliente()).append("\n");
        ticket.append("Fecha: ").append(pedido.getFecha().format(DATE_FORMATTER)).append("\n");
        ticket.append("Tipo: ").append(pedido.getTipoEntrega()).append("\n");
        if (pedido.getNombreEmpleado() != null) {
            ticket.append("Atendió: ").append(pedido.getNombreEmpleado()).append("\n");
        }
        ticket.append(repeatChar('-', width)).append("\n\n");

        // Items
        ticket.append("Descripción           Cant.  Importe\n");
        ticket.append(repeatChar('-', width)).append("\n");

        for (var detalle : pedido.getDetalles()) {
            String productName = detalle.getNombreProducto();
            if (productName.length() > 20) {
                productName = productName.substring(0, 17) + "...";
            }

            ticket.append(String.format("%-20s %3d   %7.2f\n",
                productName,
                detalle.getCantidad(),
                detalle.getSubtotal()));
        }

        ticket.append(repeatChar('-', width)).append("\n");

        // Total
        ticket.append(String.format("TOTAL:                    S/. %7.2f\n", pedido.getTotal()));
        ticket.append(repeatChar('=', width)).append("\n\n");

        // Footer
        ticket.append(centerText("¡Gracias por su compra!", width)).append("\n");
        ticket.append(centerText("Vuelva pronto", width)).append("\n");

        return ticket.toString();
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return repeatChar(' ', padding) + text;
    }

    private String repeatChar(char c, int times) {
        return String.valueOf(c).repeat(times);
    }
}
