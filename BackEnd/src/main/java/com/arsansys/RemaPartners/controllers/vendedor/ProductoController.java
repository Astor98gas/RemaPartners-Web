package com.arsansys.RemaPartners.controllers.vendedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.services.ProductoService;
import com.arsansys.RemaPartners.services.ProductoVisitaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador para la gestión de productos de los vendedores.
 * Permite crear, actualizar, eliminar, obtener y cambiar el estado de
 * productos.
 */
@RestController
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoVisitaService productoVisitaService;

    /**
     * Crea un nuevo producto.
     *
     * @param productoEntity Entidad del producto a crear.
     * @return Mensaje indicando el resultado de la operación.
     */
    @PostMapping("vendedor/producto/create")
    public ResponseEntity<?> createProducto(@RequestBody ProductoEntity productoEntity) {
        try {
            ProductoEntity entity = productoService.createProducto(productoEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Producto added successfully with ID: " + entity.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error adding producto: " + e.getMessage());
        }
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id             ID del producto a actualizar.
     * @param productoEntity Entidad del producto con los nuevos datos.
     * @return Mensaje indicando el resultado de la operación.
     */
    @PostMapping("vendedor/producto/update/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable String id, @RequestBody ProductoEntity productoEntity) {
        try {
            productoEntity.setId(id);
            ProductoEntity entity = productoService.updateProducto(productoEntity);
            return ResponseEntity.ok("Producto updated successfully with ID: " + entity.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     * @return Mensaje indicando el resultado de la operación.
     */
    @DeleteMapping("vendedor/producto/delete/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable String id) {
        try {
            productoService.deleteProductoById(id);
            return ResponseEntity.ok("Producto deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deleting producto: " + e.getMessage());
        }
    }

    /**
     * Obtiene la lista de todos los productos.
     *
     * @return Lista de productos.
     */
    @GetMapping("vendedor/producto/getAll")
    public ResponseEntity<?> getProductos() {
        try {
            return ResponseEntity.ok(productoService.getProductos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error retrieving productos: " + e.getMessage());
        }
    }

    /**
     * Obtiene un producto por su ID e incrementa su contador de visitas.
     *
     * @param id ID del producto.
     * @return Producto correspondiente.
     */
    @GetMapping("vendedor/producto/getById/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable String id) {
        try {
            ProductoEntity producto = productoService.getProductoById(id);
            Long visitas = producto.getVisitas();
            if (visitas == null) {
                visitas = 0L;
            }
            producto.setVisitas(visitas + 1);
            productoService.updateProducto(producto);

            // Registrar la visita en las estadísticas mensuales
            productoVisitaService.registrarVisita(id, producto.getIdUsuario());

            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error retrieving producto by ID: " + e.getMessage());
        }
    }

    /**
     * Obtiene los productos por el ID de la categoría.
     *
     * @param id ID de la categoría.
     * @return Lista de productos de la categoría.
     */
    @GetMapping("vendedor/producto/getProductosByIdCategoria/{id}")
    public ResponseEntity<?> getProductosByIdCategoria(@PathVariable String id) {
        try {
            return ResponseEntity.ok(productoService.getProductosByIdCategoria(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error retrieving productos by categoria: " + e.getMessage());
        }
    }

    /**
     * Cambia el estado (activo/inactivo) de un producto.
     *
     * @param id ID del producto.
     * @return Mensaje indicando el resultado de la operación.
     */
    @PostMapping("vendedor/producto/toggleStatus/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable String id) {
        try {
            productoService.toggleStatus(id);
            return ResponseEntity.ok("Producto status toggled successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error toggling producto status: " + e.getMessage());
        }
    }

}
