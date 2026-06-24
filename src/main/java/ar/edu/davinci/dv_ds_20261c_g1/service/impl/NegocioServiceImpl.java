package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;
import ar.edu.davinci.dv_ds_20261c_g1.repository.VentaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.NegocioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NegocioServiceImpl implements NegocioService {

    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> ventasPorFecha(Date fecha) {
        return ventaRepository.findByFecha(fecha);
    }

    /**
     * Ganancias de un dia = suma de los totales de todas las ventas de esa fecha.
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularGananciasDelDia(Date fecha) {
        if (fecha == null) {
            return BigDecimal.ZERO;
        }
        return ventaRepository.findByFecha(fecha).stream()
                .map(Venta::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
