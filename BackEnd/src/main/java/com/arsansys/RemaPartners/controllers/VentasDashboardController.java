package com.arsansys.RemaPartners.controllers;

import java.time.LocalDateTime;
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
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.services.ProductoService;
import com.arsansys.RemaPartners.services.UserService;
import com.arsansys.RemaPartners.services.VentasDashboardService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/dashboard")
public class VentasDashboardController {

    @Autowired
    private VentasDashboardService ventasDashboardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductoService productoService;

    /**
     * Endpoint para obtener las estadísticas generales del dashboard de ventas
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/ventas/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getVentasStats(@RequestParam(required = false) Integer year) {
        try {
            // Si no se proporciona un año, usar el año actual
            if (year == null) {
                year = LocalDateTime.now().getYear();
            }

            Map<String, Object> stats = ventasDashboardService.getEstadisticasGenerales(year);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas de ventas: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las estadísticas de ventas para un producto específico
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/ventas/producto/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getProductoVentas(@PathVariable String id) {
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

            Map<String, Object> stats = ventasDashboardService.getEstadisticasProducto(id);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas de ventas del producto: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las estadísticas de ventas mensuales detalladas
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/ventas/mes/{año}/{mes}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getVentasPorMes(@PathVariable int año, @PathVariable int mes) {
        try {
            // Validar que el mes esté entre 1 y 12
            if (mes < 1 || mes > 12) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El mes debe estar entre 1 y 12");
            }

            Map<String, Object> stats = ventasDashboardService.getEstadisticasMes(año, mes);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas por mes: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las estadísticas de ventas de un usuario (como
     * vendedor)
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/ventas/usuario/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getVentasUsuario(@PathVariable String id, @RequestParam(required = false) Integer year) {
        try {
            // Si no se proporciona un año, usar el año actual
            if (year == null) {
                year = LocalDateTime.now().getYear();
            }

            // Obtener el usuario actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity currentUser = userService.getUserByUsername(username);

            // Verificar que el usuario existe
            UserEntity targetUser = userService.getUserById(id);
            if (targetUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Verificar permisos (solo el propio usuario o un admin/trabajador puede ver
            // esto)
            if (!currentUser.getRol().getName().name().equals("ADMIN") &&
                    !currentUser.getRol().getName().name().equals("TRABAJADOR") &&
                    !currentUser.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permisos para ver estas estadísticas");
            }

            Map<String, Object> stats = ventasDashboardService.getEstadisticasVentasUsuario(id, year);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas de ventas del usuario: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener las estadísticas de compras de un usuario (como
     * comprador)
     * Solo accesible para roles ADMIN, VENDEDOR y TRABAJADOR
     */
    @GetMapping("/compras/usuario/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'TRABAJADOR')")
    public ResponseEntity<?> getComprasUsuario(@PathVariable String id, @RequestParam(required = false) Integer year) {
        try {
            // Si no se proporciona un año, usar el año actual
            if (year == null) {
                year = LocalDateTime.now().getYear();
            }

            // Obtener el usuario actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity currentUser = userService.getUserByUsername(username);

            // Verificar que el usuario existe
            UserEntity targetUser = userService.getUserById(id);
            if (targetUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Verificar permisos (solo el propio usuario o un admin/trabajador puede ver
            // esto)
            if (!currentUser.getRol().getName().name().equals("ADMIN") &&
                    !currentUser.getRol().getName().name().equals("TRABAJADOR") &&
                    !currentUser.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permisos para ver estas estadísticas");
            }

            Map<String, Object> stats = ventasDashboardService.getEstadisticasComprasUsuario(id, year);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo estadísticas de compras del usuario: " + e.getMessage());
        }
    }
}
