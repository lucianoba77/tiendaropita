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
        if (fecha == null) {
            return BigDecimal.ZERO;
        }
        return ventaRepository.findByFecha(fecha).stream()
                .map(Venta::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenGananciasDia calcularResumenDelDia(LocalDate fecha) {
        if (fecha == null) {
            return resumenVacio();
        }

        List<Venta> ventas = ventaRepository.findByFecha(fecha);
        TotalesAcumulados totales = new TotalesAcumulados();
        Map<Long, Integer> unidadesPorPrenda = new HashMap<>();
        Map<Long, String> descripcionPorPrenda = new HashMap<>();

        for (Venta venta : ventas) {
            acumularTotales(venta, totales);
            acumularUnidadesPorPrenda(venta, unidadesPorPrenda, descripcionPorPrenda);
        }

        PrendaTop prendaTop = encontrarPrendaMasVendida(unidadesPorPrenda, descripcionPorPrenda);

        return ResumenGananciasDia.builder()
                .total(totales.total().setScale(2, RoundingMode.HALF_UP))
                .cantidadVentas(ventas.size())
                .totalEfectivo(totales.totalEfectivo().setScale(2, RoundingMode.HALF_UP))
                .totalTarjeta(totales.totalTarjeta().setScale(2, RoundingMode.HALF_UP))
                .prendaMasVendidaDescripcion(prendaTop.descripcion())
                .prendaMasVendidaUnidades(prendaTop.unidades())
                .build();
    }

    private static ResumenGananciasDia resumenVacio() {
        return ResumenGananciasDia.builder()
                .total(BigDecimal.ZERO)
                .cantidadVentas(0)
                .totalEfectivo(BigDecimal.ZERO)
                .totalTarjeta(BigDecimal.ZERO)
                .build();
    }

    private static void acumularTotales(Venta venta, TotalesAcumulados totales) {
        BigDecimal totalVenta = venta.calcularTotal();
        if (venta instanceof VentaEfectivo) {
            totales.sumarEfectivo(totalVenta);
        } else if (venta instanceof VentaTarjeta) {
            totales.sumarTarjeta(totalVenta);
        }
    }

    private static void acumularUnidadesPorPrenda(Venta venta,
            Map<Long, Integer> unidadesPorPrenda,
            Map<Long, String> descripcionPorPrenda) {
        if (venta.getItems() == null) {
            return;
        }
        for (Item item : venta.getItems()) {
            if (item.getPrenda() == null || item.getPrenda().getId() == null) {
                continue;
            }
            Prenda prenda = item.getPrenda();
            Long prendaId = prenda.getId();
            int cantidad = (item.getCantidad() != null) ? item.getCantidad() : 0;
            unidadesPorPrenda.merge(prendaId, cantidad, (actual, delta) -> actual + delta);
            descripcionPorPrenda.putIfAbsent(prendaId, prenda.getDescripcion());
        }
    }

    private static PrendaTop encontrarPrendaMasVendida(Map<Long, Integer> unidadesPorPrenda,
            Map<Long, String> descripcionPorPrenda) {
        Long prendaTopId = null;
        int maxUnidades = 0;
        for (Map.Entry<Long, Integer> entry : unidadesPorPrenda.entrySet()) {
            if (entry.getValue() > maxUnidades) {
                maxUnidades = entry.getValue();
                prendaTopId = entry.getKey();
            }
        }
        if (prendaTopId == null) {
            return new PrendaTop(null, null);
        }
        return new PrendaTop(descripcionPorPrenda.get(prendaTopId), maxUnidades);
    }

    private static final class TotalesAcumulados {
        private BigDecimal totalEfectivo = BigDecimal.ZERO;
        private BigDecimal totalTarjeta = BigDecimal.ZERO;

        void sumarEfectivo(BigDecimal monto) {
            totalEfectivo = totalEfectivo.add(monto);
        }

        void sumarTarjeta(BigDecimal monto) {
            totalTarjeta = totalTarjeta.add(monto);
        }

        BigDecimal totalEfectivo() {
            return totalEfectivo;
        }

        BigDecimal totalTarjeta() {
            return totalTarjeta;
        }

        BigDecimal total() {
            return totalEfectivo.add(totalTarjeta);
        }
    }

    private record PrendaTop(String descripcion, Integer unidades) {
    }
}
