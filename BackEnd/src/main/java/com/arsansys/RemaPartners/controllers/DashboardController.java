package com.arsansys.RemaPartners.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;
import com.arsansys.RemaPartners.services.ProductoService;
import com.arsansys.RemaPartners.services.ProductoVisitaService;
import com.arsansys.RemaPartners.services.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProductoVisitaService productoVisitaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Endpoint para obtener las estadísticas generales del dashboard
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getDashboardStats(@RequestParam(required = false) Integer year) {
        try {
            // Si no se proporciona un año, usar el año actual
            if (year == null) {
                year = LocalDateTime.now().getYear();
            }

            System.out.println("Año solicitado: " + year); // Depuración

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userService.getUserByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }

            // Obtener estadísticas según el rol
            Map<String, Object> stats = new HashMap<>();

            // Obtener productos del vendedor o todos los productos si es admin
            List<ProductoEntity> productos;
            if (user.getRol().getName().name().equals("ADMIN") || user.getRol().getName().name().equals("TRABAJADOR")) {
                productos = productoService.getProductos();
            } else {
                productos = new ArrayList<>();
                // Aquí implementar la lógica para obtener solo los productos del vendedor
                for (ProductoEntity producto : productoService.getProductos()) {
                    if (producto.getIdUsuario().equals(user.getId())) {
                        productos.add(producto);
                    }
                }
            }

            // Estadísticas generales
            stats.put("totalProductos", productos.size());

            // Total de visitas
            long totalVisitas = 0;
            for (ProductoEntity producto : productos) {
                totalVisitas += producto.getVisitas() != null ? producto.getVisitas() : 0;
            }
            stats.put("totalVisitas", totalVisitas);

            // Estadísticas de visitas por mes para el año proporcionado
            Map<String, Long> visitasPorMes = productoVisitaService.obtenerEstadisticasVisitasPorMes(user.getId(),
                    year);
            stats.put("visitasPorMes", visitasPorMes);

            // Productos más visitados (top 5)
            List<Map<String, Object>> productosTopVisitas = new ArrayList<>();
            productos.sort((p1, p2) -> {
                Long v1 = p1.getVisitas() != null ? p1.getVisitas() : 0L;
                Long v2 = p2.getVisitas() != null ? p2.getVisitas() : 0L;
                return Long.compare(v2, v1); // Orden descendente
            });

            int count = 0;
            for (ProductoEntity producto : productos) {
                if (count >= 5)
                    break;

                Map<String, Object> productoInfo = new HashMap<>();
                productoInfo.put("id", producto.getId());
                productoInfo.put("titulo", producto.getTitulo());
                productoInfo.put("visitas", producto.getVisitas() != null ? producto.getVisitas() : 0);

                productosTopVisitas.add(productoInfo);
                count++;
            }
            stats.put("productosTopVisitas", productosTopVisitas);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las estadísticas de visitas por producto
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/producto/{id}/visitas")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getProductoVisitas(@PathVariable String id) {
        try {
            // Verificar si el producto existe
            ProductoEntity producto = productoService.getProductoById(id);
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
            }

            // Obtener el usuario actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userService.getUserByUsername(username);

            // Verificar permisos (solo el vendedor del producto o un admin/trabajador puede
            // ver esto)
            if (!user.getRol().getName().name().equals("ADMIN") &&
                    !user.getRol().getName().name().equals("TRABAJADOR") &&
                    !producto.getIdUsuario().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permisos para ver estas estadísticas");
            }

            // Obtener estadísticas de visitas para este producto
            List<ProductoVisitaEntity> visitasProducto = productoVisitaService.obtenerVisitasPorProducto(id);

            Map<String, Object> stats = new HashMap<>();
            stats.put("producto", producto);
            stats.put("totalVisitas", producto.getVisitas());
            stats.put("visitasPorMes", visitasProducto);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas de visitas: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las estadísticas de visitas por mes
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/visitas/mes/{año}/{mes}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getVisitasPorMes(@PathVariable int año, @PathVariable int mes) {
        try {
            // Obtener el usuario actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userService.getUserByUsername(username);

            // Obtener visitas del mes para el vendedor o todas si es admin/trabajador
            List<ProductoVisitaEntity> visitasMes;
            if (user.getRol().getName().name().equals("ADMIN") || user.getRol().getName().name().equals("TRABAJADOR")) {
                visitasMes = productoVisitaService.obtenerVisitasPorMes(año, mes);
            } else {
                visitasMes = productoVisitaService.obtenerVisitasPorVendedorYMes(user.getId(), año, mes);
            }

            // Calcular total de visitas
            long totalVisitasMes = visitasMes.stream().mapToLong(ProductoVisitaEntity::getCantidadVisitas).sum();

            Map<String, Object> resultados = new HashMap<>();
            resultados.put("año", año);
            resultados.put("mes", mes);
            resultados.put("totalVisitas", totalVisitasMes);
            resultados.put("detalleVisitas", visitasMes);

            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas por mes: " + e.getMessage());
        }
    }
}
