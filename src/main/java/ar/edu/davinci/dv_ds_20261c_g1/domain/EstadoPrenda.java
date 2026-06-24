package ar.edu.davinci.dv_ds_20261c_g1.domain;

import ar.edu.davinci.dv_ds_20261c_g1.domain.strategy.EstadoPrendaLiquidacion;
import ar.edu.davinci.dv_ds_20261c_g1.domain.strategy.EstadoPrendaNueva;
import ar.edu.davinci.dv_ds_20261c_g1.domain.strategy.EstadoPrendaPromocion;
import ar.edu.davinci.dv_ds_20261c_g1.domain.strategy.EstadoPrendaStrategy;

/**
 * Enumerado del estado de una prenda. Cada estado conoce su estrategia de
 * calculo de precio (patron Strategy).
 */
public enum EstadoPrenda {

    NUEVA(new EstadoPrendaNueva()),
    PROMOCION(new EstadoPrendaPromocion()),
    LIQUIDACION(new EstadoPrendaLiquidacion());

    private final EstadoPrendaStrategy strategy;

    EstadoPrenda(EstadoPrendaStrategy strategy) {
        this.strategy = strategy;
    }

    public EstadoPrendaStrategy getStrategy() {
        return strategy;
    }
}
