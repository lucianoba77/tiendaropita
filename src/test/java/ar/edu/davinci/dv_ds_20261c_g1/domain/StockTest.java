package ar.edu.davinci.dv_ds_20261c_g1.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void tieneStockSuficienteCuandoLaCantidadAlcanza() {
        Stock stock = Stock.builder().cantidad(10).build();

        assertTrue(stock.tieneStockSuficiente(10));
        assertTrue(stock.tieneStockSuficiente(5));
        assertFalse(stock.tieneStockSuficiente(11));
    }

    @Test
    void descontarReduceLaCantidadDisponible() {
        Stock stock = Stock.builder().cantidad(10).build();

        stock.descontar(3);

        assertEquals(7, stock.getCantidad());
    }

    @Test
    void reponerAumentaLaCantidadDisponible() {
        Stock stock = Stock.builder().cantidad(10).build();

        stock.reponer(5);

        assertEquals(15, stock.getCantidad());
    }
}
