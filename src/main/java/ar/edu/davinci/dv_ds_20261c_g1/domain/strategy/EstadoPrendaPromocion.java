package ar.edu.davinci.dv_ds_20261c_g1.domain.strategy;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;

/**
 * Estado Promocion: el precio de venta es el precio base menos un valor fijo
 * (valorPromocion de la prenda).
 */
public class EstadoPrendaPromocion implements EstadoPrendaStrategy {

    @Override
    public BigDecimal precioVenta(Prenda prenda) {
        BigDecimal base = prenda.getPrecioBase() != null ? prenda.getPrecioBase() : BigDecimal.ZERO;
        BigDecimal valorFijo = prenda.getValorPromocion() != null ? prenda.getValorPromocion() : BigDecimal.ZERO;
        BigDecimal resultado = base.subtract(valorFijo);
        return resultado.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : resultado;
    }
}
