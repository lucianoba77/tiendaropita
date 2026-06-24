package ar.edu.davinci.dv_ds_20261c_g1.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class VentaTest {

    private Prenda prenda(String precioBase) {
        return Prenda.builder()
                .descripcion("Prenda Test")
                .precioBase(new BigDecimal(precioBase))
                .tipoPrenda(TipoPrenda.CAMISA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build();
    }

    private List<Item> items(Prenda prenda, int cantidad) {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().prenda(prenda).cantidad(cantidad).build());
        return items;
    }

    @Test
    void ventaEfectivoAplicaDescuento10CuandoNoSuperaElLimite() {
        VentaEfectivo venta = VentaEfectivo.builder()
                .items(items(prenda("500.00"), 1))
                .build();

        // bruto 500 -> descuento 10% (50) -> total 450
        assertEquals(0, new BigDecimal("450.00").compareTo(venta.calcularTotal()));
    }

    @Test
    void ventaEfectivoAplicaDescuento15CuandoSuperaElLimite() {
        VentaEfectivo venta = VentaEfectivo.builder()
                .items(items(prenda("500.00"), 3))
                .build();

        // bruto 1500 (>1000) -> descuento 15% (225) -> total 1275
        assertEquals(0, new BigDecimal("1275.00").compareTo(venta.calcularTotal()));
    }

    @Test
    void ventaTarjetaAplicaRecargoSegunCuotasYCoeficiente() {
        VentaTarjeta venta = VentaTarjeta.builder()
                .cantidadCuotas(3)
                .coeficiente(new BigDecimal("0.01"))
                .items(items(prenda("500.00"), 2))
                .build();

        // bruto 1000 -> recargo = 0.01 * 3 * 1000 = 30 -> total 1030
        assertEquals(0, new BigDecimal("1030.00").compareTo(venta.calcularTotal()));
    }

    @Test
    void importeBrutoEsLaSumaDeLosItems() {
        VentaEfectivo venta = VentaEfectivo.builder().build();
        venta.addItem(Item.builder().prenda(prenda("100.00")).cantidad(2).build());
        venta.addItem(Item.builder().prenda(prenda("50.00")).cantidad(1).build());

        assertEquals(0, new BigDecimal("250.00").compareTo(venta.importeBruto()));
    }
}
