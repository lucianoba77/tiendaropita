package ar.edu.davinci.dv_ds_20261c_g1.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PrendaTest {

    @Test
    void builderDeLombokConstruyeLaPrenda() {
        Prenda prenda = Prenda.builder()
                .descripcion("Camisa Celeste")
                .precioBase(new BigDecimal("100.00"))
                .tipoPrenda(TipoPrenda.CAMISA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build();

        assertNotNull(prenda);
        assertEquals("Camisa Celeste", prenda.getDescripcion());
        assertEquals(TipoPrenda.CAMISA, prenda.getTipoPrenda());
    }

    @Test
    void precioVentaNuevaEsIgualAlPrecioBase() {
        Prenda prenda = Prenda.builder()
                .precioBase(new BigDecimal("100.00"))
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build();

        assertEquals(0, new BigDecimal("100.00").compareTo(prenda.precioVenta()));
    }

    @Test
    void precioVentaPromocionRestaElValorFijo() {
        Prenda prenda = Prenda.builder()
                .precioBase(new BigDecimal("100.00"))
                .estadoPrenda(EstadoPrenda.PROMOCION)
                .valorPromocion(new BigDecimal("30.00"))
                .build();

        assertEquals(0, new BigDecimal("70.00").compareTo(prenda.precioVenta()));
    }

    @Test
    void precioVentaLiquidacionEsLaMitadDelPrecioBase() {
        Prenda prenda = Prenda.builder()
                .precioBase(new BigDecimal("100.00"))
                .estadoPrenda(EstadoPrenda.LIQUIDACION)
                .build();

        assertEquals(0, new BigDecimal("50.00").compareTo(prenda.precioVenta()));
    }

    @Test
    void precioVentaSinEstadoSeComportaComoNueva() {
        Prenda prenda = Prenda.builder()
                .precioBase(new BigDecimal("250.00"))
                .build();

        assertEquals(0, new BigDecimal("250.00").compareTo(prenda.precioVenta()));
    }
}
