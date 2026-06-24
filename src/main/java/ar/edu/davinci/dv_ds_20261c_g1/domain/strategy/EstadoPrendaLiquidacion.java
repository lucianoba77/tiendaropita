package ar.edu.davinci.dv_ds_20261c_g1.domain.strategy;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;

/**
 * Estado Liquidacion: el precio de venta es el 50% del precio base.
 */
public class EstadoPrendaLiquidacion implements EstadoPrendaStrategy {

    private static final BigDecimal PORCENTAJE_LIQUIDACION = new BigDecimal("0.50");

    @Override
    public BigDecimal precioVenta(Prenda prenda) {
        BigDecimal base = prenda.getPrecioBase() != null ? prenda.getPrecioBase() : BigDecimal.ZERO;
        return base.multiply(PORCENTAJE_LIQUIDACION);
    }
}
