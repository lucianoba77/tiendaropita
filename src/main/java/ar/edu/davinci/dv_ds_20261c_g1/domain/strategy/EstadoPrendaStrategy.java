package ar.edu.davinci.dv_ds_20261c_g1.domain.strategy;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;

/**
 * Patron Strategy: define como calcular el precio de venta de una prenda
 * segun su estado (Nueva, Promocion, Liquidacion).
 */
public interface EstadoPrendaStrategy {

    BigDecimal precioVenta(Prenda prenda);
}
