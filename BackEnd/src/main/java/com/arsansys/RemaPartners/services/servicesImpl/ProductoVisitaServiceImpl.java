package com.arsansys.RemaPartners.services.servicesImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;
import com.arsansys.RemaPartners.repositories.ProductoVisitaRepository;
import com.arsansys.RemaPartners.services.ProductoVisitaService;

/**
 * Implementación del servicio para la gestión de visitas a productos.
 */
@Service
public class ProductoVisitaServiceImpl implements ProductoVisitaService {

    @Autowired
    private ProductoVisitaRepository productoVisitaRepository;

    /**
     * Registra una visita a un producto para un vendedor en el mes y año actual.
     * 
     * @param productoId Identificador del producto.
     * @param vendedorId Identificador del vendedor.
     * @return Entidad de visita actualizada o creada.
     */
    @Override
    public ProductoVisitaEntity registrarVisita(String productoId, String vendedorId) {
        LocalDateTime ahora = LocalDateTime.now();
        int año = ahora.getYear();
        int mes = ahora.getMonthValue();

        Optional<ProductoVisitaEntity> visitaExistente = productoVisitaRepository
                .findByProductoIdAndAñoAndMes(productoId, año, mes);

        if (visitaExistente.isPresent()) {
            ProductoVisitaEntity visita = visitaExistente.get();
            visita.setCantidadVisitas(visita.getCantidadVisitas() + 1);
            visita.setUltimaActualizacion(ahora);
            return productoVisitaRepository.save(visita);
        } else {
            ProductoVisitaEntity nuevaVisita = ProductoVisitaEntity.builder()
                    .productoId(productoId)
                    .vendedorId(vendedorId)
                    .año(año)
                    .mes(mes)
                    .cantidadVisitas(1)
                    .ultimaActualizacion(ahora)
                    .build();
            return productoVisitaRepository.save(nuevaVisita);
        }
    }

    /**
     * Obtiene todas las visitas de un producto.
     * 
     * @param productoId Identificador del producto.
     * @return Lista de entidades de visitas del producto.
     */
    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorProducto(String productoId) {
        return productoVisitaRepository.findByProductoId(productoId);
    }

    /**
     * Obtiene todas las visitas de un vendedor.
     * 
     * @param vendedorId Identificador del vendedor.
     * @return Lista de entidades de visitas del vendedor.
     */
    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorVendedor(String vendedorId) {
        return productoVisitaRepository.findByVendedorId(vendedorId);
    }

    /**
     * Obtiene todas las visitas de un mes y año específicos.
     * 
     * @param año Año de las visitas.
     * @param mes Mes de las visitas.
     * @return Lista de entidades de visitas del mes y año.
     */
    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorMes(int año, int mes) {
        return productoVisitaRepository.findByAñoAndMes(año, mes);
    }

    /**
     * Obtiene todas las visitas de un vendedor en un mes y año específicos.
     * 
     * @param vendedorId Identificador del vendedor.
     * @param año        Año de las visitas.
     * @param mes        Mes de las visitas.
     * @return Lista de entidades de visitas del vendedor en el mes y año.
     */
    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorVendedorYMes(String vendedorId, int año, int mes) {
        return productoVisitaRepository.findByVendedorIdAndAñoAndMes(vendedorId, año, mes);
    }

    /**
     * Obtiene estadísticas de visitas por mes para un vendedor y año específicos.
     * 
     * @param vendedorId Identificador del vendedor.
     * @param año        Año de las estadísticas.
     * @return Mapa con el número de visitas por mes.
     */
    @Override
    public Map<String, Long> obtenerEstadisticasVisitasPorMes(String vendedorId, int año) {
        List<ProductoVisitaEntity> visitas = productoVisitaRepository.findByVendedorId(vendedorId);

        Map<String, Long> visitasPorMes = new HashMap<>();
        // Inicializar todos los meses con 0 visitas
        for (int i = 1; i <= 12; i++) {
            visitasPorMes.put(String.valueOf(i), 0L);
        }

        // Sumar las visitas por mes
        for (ProductoVisitaEntity visita : visitas) {
            if (visita.getAño() == año) {
                String mes = String.valueOf(visita.getMes());
                visitasPorMes.put(mes, visitasPorMes.getOrDefault(mes, 0L) + visita.getCantidadVisitas());
            }
        }

        return visitasPorMes;
    }

    /**
     * Obtiene el total de visitas para un vendedor.
     * 
     * @param vendedorId Identificador del vendedor.
     * @return Total de visitas.
     */
    @Override
    public long getTotalVisitasPorVendedor(String vendedorId) {
        List<ProductoVisitaEntity> visitas = productoVisitaRepository.findByVendedorId(vendedorId);
        return visitas.stream().mapToLong(ProductoVisitaEntity::getCantidadVisitas).sum();
    }
}
