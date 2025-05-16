package com.arsansys.RemaPartners.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;
import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.services.FacturaService;
import com.arsansys.RemaPartners.services.ProductoService;

/**
 * Controlador para gestionar facturas
 */
@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ProductoService productoService;

    /**
     * Crea una nueva factura
     * 
     * @param facturaEntity Datos de la factura a crear
     * @return La factura creada
     */
    @PostMapping("/create")
    public ResponseEntity<FacturaEntity> createFactura(@RequestBody FacturaEntity facturaEntity) {
        try {
            // Calculamos el importe total si no viene informado
            if (facturaEntity.getImporteTotalCentimos() == null &&
                    facturaEntity.getPrecioCentimos() != null &&
                    facturaEntity.getCantidad() != null) {

                facturaEntity.setImporteTotalCentimos(
                        facturaEntity.getPrecioCentimos() * facturaEntity.getCantidad());
            }

            FacturaEntity nuevaFactura = facturaService.createFactura(facturaEntity);
            return new ResponseEntity<>(nuevaFactura, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene una factura por su ID
     * 
     * @param id ID de la factura
     * @return La factura encontrada o error si no existe
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<FacturaEntity> getFacturaById(@PathVariable("id") String id) {
        try {
            FacturaEntity factura = facturaService.getFacturaById(id);
            return new ResponseEntity<>(factura, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene facturas donde el usuario es el comprador
     * 
     * @param idComprador ID del comprador
     * @return Lista de facturas del comprador
     */
    @GetMapping("/getByBuyerId/{idComprador}")
    public ResponseEntity<List<FacturaEntity>> getFacturasByIdComprador(
            @PathVariable("idComprador") String idComprador) {
        try {
            List<FacturaEntity> facturas = facturaService.getFacturasByIdComprador(idComprador);
            return new ResponseEntity<>(facturas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene facturas donde el usuario es el vendedor
     * 
     * @param idVendedor ID del vendedor
     * @return Lista de facturas del vendedor
     */
    @GetMapping("/getBySellerId/{idVendedor}")
    public ResponseEntity<List<FacturaEntity>> getFacturasByIdVendedor(@PathVariable("idVendedor") String idVendedor) {
        try {
            List<FacturaEntity> facturas = facturaService.getFacturasByIdVendedor(idVendedor);
            return new ResponseEntity<>(facturas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene facturas asociadas a un producto
     * 
     * @param idProducto ID del producto
     * @return Lista de facturas del producto
     */
    @GetMapping("/getByProductId/{idProducto}")
    public ResponseEntity<List<FacturaEntity>> getFacturasByIdProducto(@PathVariable("idProducto") String idProducto) {
        try {
            List<FacturaEntity> facturas = facturaService.getFacturasByIdProducto(idProducto);
            return new ResponseEntity<>(facturas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene facturas asociadas a un chat
     * 
     * @param idChat ID del chat
     * @return Lista de facturas del chat
     */
    @GetMapping("/getByChatId/{idChat}")
    public ResponseEntity<List<FacturaEntity>> getFacturasByIdChat(@PathVariable("idChat") String idChat) {
        try {
            List<FacturaEntity> facturas = facturaService.getFacturasByIdChat(idChat);
            return new ResponseEntity<>(facturas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crea una factura a partir de la venta de un producto
     * 
     * @param idProducto  ID del producto vendido
     * @param idComprador ID del comprador
     * @param idVendedor  ID del vendedor
     * @param cantidad    Cantidad vendida
     * @param idChat      ID del chat donde se realizó la venta
     * @return La factura creada
     */
    @PostMapping("/createFromSale/{idProducto}/{idComprador}/{idVendedor}/{cantidad}/{idChat}")
    public ResponseEntity<FacturaEntity> createFacturaFromSale(
            @PathVariable("idProducto") String idProducto,
            @PathVariable("idComprador") String idComprador,
            @PathVariable("idVendedor") String idVendedor,
            @PathVariable("cantidad") Integer cantidad,
            @PathVariable("idChat") String idChat) {

        try {
            // Obtener información del producto
            ProductoEntity producto = productoService.getProductoById(idProducto);

            // Crear la factura
            FacturaEntity factura = FacturaEntity.builder()
                    .idProducto(idProducto)
                    .idComprador(idComprador)
                    .idVendedor(idVendedor)
                    .cantidad(cantidad)
                    .precioCentimos(producto.getPrecioCentimos())
                    .importeTotalCentimos(producto.getPrecioCentimos() * cantidad)
                    .moneda(producto.getMoneda().toString())
                    .tituloProducto(producto.getTitulo())
                    .idChat(idChat)
                    .build();

            FacturaEntity nuevaFactura = facturaService.createFactura(factura);
            return new ResponseEntity<>(nuevaFactura, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
