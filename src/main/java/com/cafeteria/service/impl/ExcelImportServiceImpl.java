package com.cafeteria.service.impl;

import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.Usuario;
import com.cafeteria.service.ExcelImportService;
import com.cafeteria.service.ProductoService;
import com.cafeteria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    @Override
    public Map<String, Object> importUsuarios(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    UsuarioDTO usuario = new UsuarioDTO();
                    usuario.setNombre(getCellValue(row.getCell(0)));
                    usuario.setApellido(getCellValue(row.getCell(1)));
                    usuario.setCorreo(getCellValue(row.getCell(2)));
                    usuario.setContrasena(getCellValue(row.getCell(3)));
                    
                    String rolStr = getCellValue(row.getCell(4));
                    // Map role name to ID (assuming: ADMIN=1, EMPLEADO=2, CLIENTE=3)
                    if ("ADMIN".equalsIgnoreCase(rolStr)) {
                        usuario.setIdRol(1);
                    } else if ("EMPLEADO".equalsIgnoreCase(rolStr)) {
                        usuario.setIdRol(2);
                    } else {
                        usuario.setIdRol(3); // CLIENTE by default
                    }
                    
                    usuario.setTelefono(getCellValue(row.getCell(5)));
                    usuario.setDireccion(getCellValue(row.getCell(6)));
                    
                    usuarioService.registrar(usuario);
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    errors.add("Fila " + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errors.add("Error al procesar archivo: " + e.getMessage());
            errorCount++;
        }

        result.put("success", successCount);
        result.put("errors", errorCount);
        result.put("errorDetails", errors);
        result.put("message", String.format("Importados: %d, Errores: %d", successCount, errorCount));
        
        return result;
    }

    @Override
    public Map<String, Object> importProductos(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    ProductoDTO producto = new ProductoDTO();
                    producto.setNombre(getCellValue(row.getCell(0)));
                    producto.setDescripcion(getCellValue(row.getCell(1)));
                    
                    String precioStr = getCellValue(row.getCell(2));
                    producto.setPrecio(new java.math.BigDecimal(precioStr));
                    
                    String categoriaIdStr = getCellValue(row.getCell(3));
                    if (categoriaIdStr != null && !categoriaIdStr.isEmpty()) {
                        producto.setIdCategoria(Integer.parseInt(categoriaIdStr));
                    }
                    
                    String stockStr = getCellValue(row.getCell(4));
                    if (stockStr != null && !stockStr.isEmpty()) {
                        producto.setCantidadActual(Integer.parseInt(stockStr));
                    }
                    
                    String stockMinStr = getCellValue(row.getCell(5));
                    if (stockMinStr != null && !stockMinStr.isEmpty()) {
                        producto.setStockMinimo(Integer.parseInt(stockMinStr));
                    }
                    
                    producto.setUnidadMedida(getCellValue(row.getCell(6)));
                    
                    productoService.crear(producto);
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    errors.add("Fila " + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errors.add("Error al procesar archivo: " + e.getMessage());
            errorCount++;
        }

        result.put("success", successCount);
        result.put("errors", errorCount);
        result.put("errorDetails", errors);
        result.put("message", String.format("Importados: %d, Errores: %d", successCount, errorCount));
        
        return result;
    }

    @Override
    public byte[] generateUsuariosTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            // Header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Nombre*", "Apellido*", "Correo*", "Contraseña*", "Rol* (ADMIN/EMPLEADO/CLIENTE)", "Teléfono", "Dirección"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Example data
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("Juan");
            exampleRow.createCell(1).setCellValue("Pérez");
            exampleRow.createCell(2).setCellValue("juan.perez@example.com");
            exampleRow.createCell(3).setCellValue("password123");
            exampleRow.createCell(4).setCellValue("CLIENTE");
            exampleRow.createCell(5).setCellValue("987654321");
            exampleRow.createCell(6).setCellValue("Av. Principal 123");

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar plantilla de usuarios: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] generateProductosTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            CellStyle headerStyle = createHeaderStyle(workbook);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Nombre*", "Descripción", "Precio*", "ID Categoría*", "Stock Inicial", "Stock Mínimo", "Unidad Medida"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Example data
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("Café Americano");
            exampleRow.createCell(1).setCellValue("Café americano de 8 oz");
            exampleRow.createCell(2).setCellValue(5.50);
            exampleRow.createCell(3).setCellValue(1);
            exampleRow.createCell(4).setCellValue(100);
            exampleRow.createCell(5).setCellValue(10);
            exampleRow.createCell(6).setCellValue("unidad");

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar plantilla de productos: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
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
}
