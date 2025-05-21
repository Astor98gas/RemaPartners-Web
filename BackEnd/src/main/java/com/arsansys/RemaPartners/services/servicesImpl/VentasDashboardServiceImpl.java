package com.arsansys.RemaPartners.services.servicesImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;
import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.repositories.FacturaRepository;
import com.arsansys.RemaPartners.services.FacturaService;
import com.arsansys.RemaPartners.services.ProductoService;
import com.arsansys.RemaPartners.services.UserService;
import com.arsansys.RemaPartners.services.VentasDashboardService;

@Service
public class VentasDashboardServiceImpl implements VentasDashboardService {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UserService userService;

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public Map<String, Object> getEstadisticasGenerales(int year) {
        Map<String, Object> stats = new HashMap<>();

        // Obtener todas las facturas del sistema
        List<FacturaEntity> todasFacturas = facturaRepository.findAll();

        // Filtrar por año
        List<FacturaEntity> facturasAño = todasFacturas.stream()
                .filter(f -> f.getFechaEmision().getYear() == year)
                .collect(Collectors.toList());

        // Estadísticas generales
        int totalVentas = facturasAño.size();
        int importeTotal = facturasAño.stream()
                .mapToInt(FacturaEntity::getImporteTotalCentimos)
                .sum();

        // Ventas por mes
        Map<String, Map<String, Integer>> ventasPorMes = new HashMap<>();
        for (int mes = 1; mes <= 12; mes++) {
            final int mesFinal = mes;
            List<FacturaEntity> facturasMes = facturasAño.stream()
                    .filter(f -> f.getFechaEmision().getMonthValue() == mesFinal)
                    .collect(Collectors.toList());

            Map<String, Integer> datosMes = new HashMap<>();
            datosMes.put("cantidad", facturasMes.size());
            datosMes.put("importe", facturasMes.stream()
                    .mapToInt(FacturaEntity::getImporteTotalCentimos)
                    .sum());

            ventasPorMes.put(String.valueOf(mes), datosMes);
        }

        // Productos más vendidos
        Map<String, Map<String, Object>> productosCantidad = new HashMap<>();
        for (FacturaEntity factura : facturasAño) {
            String idProducto = factura.getIdProducto();
            int cantidad = factura.getCantidad();
            int importe = factura.getImporteTotalCentimos();

            productosCantidad.putIfAbsent(idProducto, new HashMap<>());
            Map<String, Object> datosProducto = productosCantidad.get(idProducto);

            datosProducto.put("id", idProducto);
            datosProducto.put("titulo", factura.getTituloProducto());
            datosProducto.putIfAbsent("cantidadVentas", 0);
            datosProducto.putIfAbsent("importeTotal", 0);

            int cantidadActual = (Integer) datosProducto.get("cantidadVentas");
            int importeActual = (Integer) datosProducto.get("importeTotal");

            datosProducto.put("cantidadVentas", cantidadActual + cantidad);
            datosProducto.put("importeTotal", importeActual + importe);
        }

        // Obtener los 5 productos más vendidos
        List<Map<String, Object>> productosTopVentas = productosCantidad.values().stream()
                .sorted((p1, p2) -> {
                    Integer c1 = (Integer) p1.get("cantidadVentas");
                    Integer c2 = (Integer) p2.get("cantidadVentas");
                    return c2.compareTo(c1);
                })
                .limit(5)
                .collect(Collectors.toList());

        // Construir el objeto de respuesta
        stats.put("totalVentas", totalVentas);
        stats.put("importeTotalVentas", importeTotal);
        stats.put("ventasPorMes", ventasPorMes);
        stats.put("productosTopVentas", productosTopVentas);

        return stats;
    }

    @Override
    public Map<String, Object> getEstadisticasProducto(String productoId) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Obtener producto
            ProductoEntity producto = productoService.getProductoById(productoId);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado");
            }

            // Obtener todas las facturas del producto
            List<FacturaEntity> facturasProducto = facturaService.getFacturasByIdProducto(productoId);

            // Calcular totales
            int totalVentas = facturasProducto.stream()
                    .mapToInt(FacturaEntity::getCantidad)
                    .sum();
            int importeTotal = facturasProducto.stream()
                    .mapToInt(FacturaEntity::getImporteTotalCentimos)
                    .sum();

            // Agrupar ventas por mes y año
            List<Map<String, Object>> ventasPorMes = new ArrayList<>();

            // Usar un mapa auxiliar para agrupar por año/mes
            Map<String, Map<String, Object>> ventasPorMesYAño = new HashMap<>();

            for (FacturaEntity factura : facturasProducto) {
                int año = factura.getFechaEmision().getYear();
                int mes = factura.getFechaEmision().getMonthValue();
                String key = año + "-" + mes;

                ventasPorMesYAño.putIfAbsent(key, new HashMap<>());
                Map<String, Object> datosMes = ventasPorMesYAño.get(key);

                datosMes.put("mes", mes);
                datosMes.put("año", año);
                datosMes.putIfAbsent("cantidad", 0);
                datosMes.putIfAbsent("importe", 0);

                int cantidadActual = (Integer) datosMes.get("cantidad");
                int importeActual = (Integer) datosMes.get("importe");

                datosMes.put("cantidad", cantidadActual + factura.getCantidad());
                datosMes.put("importe", importeActual + factura.getImporteTotalCentimos());
                datosMes.put("ultimaActualizacion", factura.getFechaEmision().toString());
            }

            // Convertir el mapa a una lista para la respuesta
            ventasPorMes = new ArrayList<>(ventasPorMesYAño.values());

            // Construir el objeto de respuesta
            stats.put("producto", producto);
            stats.put("totalVentas", totalVentas);
            stats.put("importeTotal", importeTotal);
            stats.put("ventasPorMes", ventasPorMes);

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas del producto: " + e.getMessage());
        }

        return stats;
    }

    @Override
    public Map<String, Object> getEstadisticasMes(int año, int mes) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Obtener todas las facturas
            List<FacturaEntity> todasFacturas = facturaRepository.findAll();

            // Filtrar por año y mes
            List<FacturaEntity> facturasMes = todasFacturas.stream()
                    .filter(f -> f.getFechaEmision().getYear() == año &&
                            f.getFechaEmision().getMonthValue() == mes)
                    .collect(Collectors.toList());

            // Calcular totales
            int totalVentas = facturasMes.size();
            int importeTotal = facturasMes.stream()
                    .mapToInt(FacturaEntity::getImporteTotalCentimos)
                    .sum();

            // Agrupar por día del mes
            Map<Integer, Map<String, Integer>> ventasPorDia = new HashMap<>();
            for (FacturaEntity factura : facturasMes) {
                int dia = factura.getFechaEmision().getDayOfMonth();

                ventasPorDia.putIfAbsent(dia, new HashMap<>());
                Map<String, Integer> datosDia = ventasPorDia.get(dia);

                datosDia.putIfAbsent("cantidad", 0);
                datosDia.putIfAbsent("importe", 0);

                int cantidadActual = datosDia.get("cantidad");
                int importeActual = datosDia.get("importe");

                datosDia.put("cantidad", cantidadActual + factura.getCantidad());
                datosDia.put("importe", importeActual + factura.getImporteTotalCentimos());
            }

            // Construir el objeto de respuesta
            stats.put("año", año);
            stats.put("mes", mes);
            stats.put("totalVentas", totalVentas);
            stats.put("importeTotal", importeTotal);
            stats.put("ventasPorDia", ventasPorDia);
            stats.put("facturas", facturasMes);

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas del mes: " + e.getMessage());
        }

        return stats;
    }

    @Override
    public Map<String, Object> getEstadisticasVentasUsuario(String userId, int year) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Obtener facturas del usuario como vendedor
            List<FacturaEntity> facturasVendedor = facturaService.getFacturasByIdVendedor(userId);

            // Filtrar por año
            List<FacturaEntity> facturasAño = facturasVendedor.stream()
                    .filter(f -> f.getFechaEmision().getYear() == year)
                    .collect(Collectors.toList());

            // Calcular totales
            int totalVentas = facturasAño.stream()
                    .mapToInt(FacturaEntity::getCantidad)
                    .sum();
            int importeTotalVentas = facturasAño.stream()
                    .mapToInt(FacturaEntity::getImporteTotalCentimos)
                    .sum();

            // Ventas por mes
            Map<String, Map<String, Integer>> ventasPorMes = new HashMap<>();
            for (int mes = 1; mes <= 12; mes++) {
                final int mesFinal = mes;
                List<FacturaEntity> facturasMes = facturasAño.stream()
                        .filter(f -> f.getFechaEmision().getMonthValue() == mesFinal)
                        .collect(Collectors.toList());

                Map<String, Integer> datosMes = new HashMap<>();
                datosMes.put("cantidad", facturasMes.stream().mapToInt(FacturaEntity::getCantidad).sum());
                datosMes.put("importe", facturasMes.stream()
                        .mapToInt(FacturaEntity::getImporteTotalCentimos)
                        .sum());

                ventasPorMes.put(String.valueOf(mes), datosMes);
            }

            // Productos más vendidos
            Map<String, Map<String, Object>> productosCantidad = new HashMap<>();
            for (FacturaEntity factura : facturasAño) {
                String idProducto = factura.getIdProducto();
                int cantidad = factura.getCantidad();
                int importe = factura.getImporteTotalCentimos();

                productosCantidad.putIfAbsent(idProducto, new HashMap<>());
                Map<String, Object> datosProducto = productosCantidad.get(idProducto);

                datosProducto.put("id", idProducto);
                datosProducto.put("titulo", factura.getTituloProducto());
                datosProducto.putIfAbsent("cantidadVentas", 0);
                datosProducto.putIfAbsent("importeTotal", 0);

                int cantidadActual = (Integer) datosProducto.get("cantidadVentas");
                int importeActual = (Integer) datosProducto.get("importeTotal");

                datosProducto.put("cantidadVentas", cantidadActual + cantidad);
                datosProducto.put("importeTotal", importeActual + importe);
            }

            // Obtener los 5 productos más vendidos
            List<Map<String, Object>> productosTopVentas = productosCantidad.values().stream()
                    .sorted((p1, p2) -> {
                        Integer c1 = (Integer) p1.get("cantidadVentas");
                        Integer c2 = (Integer) p2.get("cantidadVentas");
                        return c2.compareTo(c1);
                    })
                    .limit(5)
                    .collect(Collectors.toList());

            // Construir el objeto de respuesta
            stats.put("totalVentas", totalVentas);
            stats.put("importeTotalVentas", importeTotalVentas);
            stats.put("ventasPorMes", ventasPorMes);
            stats.put("productosTopVentas", productosTopVentas);

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas de ventas del usuario: " + e.getMessage());
        }

        return stats;
    }

    @Override
    public Map<String, Object> getEstadisticasComprasUsuario(String userId, int year) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Obtener facturas del usuario como comprador
            List<FacturaEntity> facturasComprador = facturaService.getFacturasByIdComprador(userId);

            // Filtrar por año
            List<FacturaEntity> facturasAño = facturasComprador.stream()
                    .filter(f -> f.getFechaEmision().getYear() == year)
                    .collect(Collectors.toList());

            // Calcular totales
            int totalCompras = facturasAño.stream()
                    .mapToInt(FacturaEntity::getCantidad)
                    .sum();
            int importeTotalCompras = facturasAño.stream()
                    .mapToInt(FacturaEntity::getImporteTotalCentimos)
                    .sum();

            // Compras por mes
            Map<String, Map<String, Integer>> comprasPorMes = new HashMap<>();
            for (int mes = 1; mes <= 12; mes++) {
                final int mesFinal = mes;
                List<FacturaEntity> facturasMes = facturasAño.stream()
                        .filter(f -> f.getFechaEmision().getMonthValue() == mesFinal)
                        .collect(Collectors.toList());

                Map<String, Integer> datosMes = new HashMap<>();
                datosMes.put("cantidad", facturasMes.stream().mapToInt(FacturaEntity::getCantidad).sum());
                datosMes.put("importe", facturasMes.stream()
                        .mapToInt(FacturaEntity::getImporteTotalCentimos)
                        .sum());

                comprasPorMes.put(String.valueOf(mes), datosMes);
            }

            // Construir el objeto de respuesta
            stats.put("totalCompras", totalCompras);
            stats.put("importeTotalCompras", importeTotalCompras);
            stats.put("comprasPorMes", comprasPorMes);

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas de compras del usuario: " + e.getMessage());
        }

        return stats;
    }
}
