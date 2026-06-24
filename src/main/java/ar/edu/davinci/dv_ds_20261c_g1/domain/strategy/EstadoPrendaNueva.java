package ar.edu.davinci.dv_ds_20261c_g1.domain.strategy;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;

/**
 * Estado Nueva: el precio de venta es igual al precio base.
 */
public class EstadoPrendaNueva implements EstadoPrendaStrategy {

    @Override
    public BigDecimal precioVenta(Prenda prenda) {
        return prenda.getPrecioBase() != null ? prenda.getPrecioBase() : BigDecimal.ZERO;
    }
}
