package com.cafeteria.service.impl;

import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.dto.ReportFilterDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.Pedido;
import com.cafeteria.repository.InventarioRepository;
import com.cafeteria.service.InventarioService;
import com.cafeteria.service.PedidoService;
import com.cafeteria.service.ProductoService;
import com.cafeteria.service.ReportService;
import com.cafeteria.service.UsuarioService;
import com.cafeteria.util.BoletaGenerator;
import com.cafeteria.util.ExcelReportGenerator;
import com.cafeteria.util.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final InventarioService inventarioService;
    private final BoletaGenerator boletaGenerator;
    private final PdfReportGenerator pdfReportGenerator;
    private final ExcelReportGenerator excelReportGenerator;

    @Value("${reports.company-name}")
    private String companyName;

    @Value("${reports.company-address}")
    private String companyAddress;

    @Value("${reports.company-phone}")
    private String companyPhone;

    @Override
    public byte[] generateBoletaPDF(Integer pedidoId) {
        PedidoDTO pedido = pedidoService.buscarPorId(pedidoId);
        return boletaGenerator.generateBoleta(pedido, companyName, companyAddress, companyPhone);
    }

    @Override
    public String generateTicketASCII(Integer pedidoId) {
        PedidoDTO pedido = pedidoService.buscarPorId(pedidoId);
        return boletaGenerator.generateTicketASCII(pedido, companyName, companyAddress, companyPhone);
    }

    @Override
    public byte[] generatePedidosReport(ReportFilterDTO filter) {
        List<PedidoDTO> pedidos = getPedidosFiltered(filter);

        if ("EXCEL".equalsIgnoreCase(filter.getFormato())) {
            return excelReportGenerator.generatePedidosExcel(pedidos);
        } else {
            return pdfReportGenerator.generatePedidosReport(pedidos, companyName, 
                filter.getFechaInicio(), filter.getFechaFin());
        }
    }

    @Override
    public byte[] generateProductosReport(ReportFilterDTO filter) {
        List<ProductoDTO> productos = getProductosFiltered(filter);

        if ("EXCEL".equalsIgnoreCase(filter.getFormato())) {
            return excelReportGenerator.generateProductosExcel(productos);
        } else {
            return pdfReportGenerator.generateProductosReport(productos, companyName);
        }
    }

    @Override
    public byte[] generateUsuariosReport(ReportFilterDTO filter) {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();

        if ("EXCEL".equalsIgnoreCase(filter.getFormato())) {
            return excelReportGenerator.generateUsuariosExcel(usuarios);
        } else {
            return pdfReportGenerator.generateUsuariosReport(usuarios, companyName);
        }
    }

    @Override
    public byte[] generateInventarioReport(ReportFilterDTO filter) {
        List<com.cafeteria.dto.InventarioDTO> inventarios = inventarioService.listarTodos();

        if ("EXCEL".equalsIgnoreCase(filter.getFormato())) {
            return excelReportGenerator.generateInventarioExcel(inventarios);
        } else {
            return pdfReportGenerator.generateInventarioReport(inventarios, companyName);
        }
    }

    private List<PedidoDTO> getPedidosFiltered(ReportFilterDTO filter) {
        List<PedidoDTO> pedidos;

        // Apply filters
        if (filter.getFechaInicio() != null && filter.getFechaFin() != null) {
            pedidos = pedidoService.listarPorFecha(filter.getFechaInicio(), filter.getFechaFin());
        } else if (filter.getEstado() != null) {
            pedidos = pedidoService.listarPorEstado(Pedido.EstadoPedido.valueOf(filter.getEstado()));
        } else if (filter.getIdUsuario() != null) {
            pedidos = pedidoService.listarPorUsuario(filter.getIdUsuario());
        } else {
            pedidos = pedidoService.listarTodos();
        }

        return pedidos;
    }

    private List<ProductoDTO> getProductosFiltered(ReportFilterDTO filter) {
        List<ProductoDTO> productos;

        if (filter.getIdCategoria() != null) {
            productos = productoService.listarPorCategoria(filter.getIdCategoria());
        } else {
            productos = productoService.listarTodos();
        }

        return productos;
    }
}
