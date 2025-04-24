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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductoController {

    @Autowired
    private ProductoService productoService;

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

    @GetMapping("vendedor/producto/getAll")
    public ResponseEntity<?> getProductos() {
        try {
            return ResponseEntity.ok(productoService.getProductos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error retrieving productos: " + e.getMessage());
        }
    }

    @GetMapping("vendedor/producto/getById/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(productoService.getProductoById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error retrieving producto by ID: " + e.getMessage());
        }
    }

}
