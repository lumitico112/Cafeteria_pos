package com

.cafeteria.service;

import com.cafeteria.dto.ReportFilterDTO;

public interface ReportService {
    
    byte[] generateBoletaPDF(Integer pedidoId);
    
    String generateTicketASCII(Integer pedidoId);
    
    byte[] generatePedidosReport(ReportFilterDTO filter);
    
    byte[] generateProductosReport(ReportFilterDTO filter);
    
    byte[] generateUsuariosReport(ReportFilterDTO filter);
    
    byte[] generateInventarioReport(ReportFilterDTO filter);
}
