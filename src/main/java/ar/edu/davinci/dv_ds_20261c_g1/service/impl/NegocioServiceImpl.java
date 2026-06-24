package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.controller.response.ResumenGananciasDia;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20261c_g1.repository.VentaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.NegocioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NegocioServiceImpl implements NegocioService {

    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> ventasPorFecha(LocalDate fecha) {
        return ventaRepository.findByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularGananciasDelDia(LocalDate fecha) {
        return calcularResumenDelDia(fecha).getTotal();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenGananciasDia calcularResumenDelDia(LocalDate fecha) {
        if (fecha == null) {
            return ResumenGananciasDia.builder()
                    .total(BigDecimal.ZERO)
                    .cantidadVentas(0)
                    .totalEfectivo(BigDecimal.ZERO)
                    .totalTarjeta(BigDecimal.ZERO)
                    .build();
        }

        List<Venta> ventas = ventaRepository.findByFecha(fecha);
        BigDecimal totalEfectivo = BigDecimal.ZERO;
        BigDecimal totalTarjeta = BigDecimal.ZERO;
        Map<Long, Integer> unidadesPorPrenda = new HashMap<>();
        Map<Long, String> descripcionPorPrenda = new HashMap<>();

        for (Venta venta : ventas) {
            BigDecimal totalVenta = venta.calcularTotal();
            if (venta instanceof VentaEfectivo) {
                totalEfectivo = totalEfectivo.add(totalVenta);
            } else if (venta instanceof VentaTarjeta) {
                totalTarjeta = totalTarjeta.add(totalVenta);
            }
            if (venta.getItems() != null) {
                for (Item item : venta.getItems()) {
                    if (item.getPrenda() == null || item.getPrenda().getId() == null) {
                        continue;
                    }
                    Prenda prenda = item.getPrenda();
                    Long prendaId = prenda.getId();
                    int cantidad = (item.getCantidad() != null) ? item.getCantidad() : 0;
                    unidadesPorPrenda.merge(prendaId, cantidad, Integer::sum);
                    descripcionPorPrenda.putIfAbsent(prendaId, prenda.getDescripcion());
                }
            }
        }

        BigDecimal total = totalEfectivo.add(totalTarjeta).setScale(2, RoundingMode.HALF_UP);
        totalEfectivo = totalEfectivo.setScale(2, RoundingMode.HALF_UP);
        totalTarjeta = totalTarjeta.setScale(2, RoundingMode.HALF_UP);

        Long prendaTopId = null;
        int maxUnidades = 0;
        for (Map.Entry<Long, Integer> entry : unidadesPorPrenda.entrySet()) {
            if (entry.getValue() > maxUnidades) {
                maxUnidades = entry.getValue();
                prendaTopId = entry.getKey();
            }
        }

        return ResumenGananciasDia.builder()
                .total(total)
                .cantidadVentas(ventas.size())
                .totalEfectivo(totalEfectivo)
                .totalTarjeta(totalTarjeta)
                .prendaMasVendidaDescripcion(prendaTopId != null ? descripcionPorPrenda.get(prendaTopId) : null)
                .prendaMasVendidaUnidades(prendaTopId != null ? maxUnidades : null)
                .build();
    }
}
