package com.arsansys.RemaPartners.services.servicesImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;
import com.arsansys.RemaPartners.repositories.ProductoVisitaRepository;
import com.arsansys.RemaPartners.services.ProductoService;
import com.arsansys.RemaPartners.services.ProductoVisitaService;

@Service
public class ProductoVisitaServiceImpl implements ProductoVisitaService {

    @Autowired
    private ProductoVisitaRepository productoVisitaRepository;

    @Autowired
    private ProductoService productoService;

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

    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorProducto(String productoId) {
        return productoVisitaRepository.findByProductoId(productoId);
    }

    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorVendedor(String vendedorId) {
        return productoVisitaRepository.findByVendedorId(vendedorId);
    }

    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorMes(int año, int mes) {
        return productoVisitaRepository.findByAñoAndMes(año, mes);
    }

    @Override
    public List<ProductoVisitaEntity> obtenerVisitasPorVendedorYMes(String vendedorId, int año, int mes) {
        return productoVisitaRepository.findByVendedorIdAndAñoAndMes(vendedorId, año, mes);
    }

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

    @Override
    public long getTotalVisitasPorVendedor(String vendedorId) {
        List<ProductoVisitaEntity> visitas = productoVisitaRepository.findByVendedorId(vendedorId);
        return visitas.stream().mapToLong(ProductoVisitaEntity::getCantidadVisitas).sum();
    }
}
