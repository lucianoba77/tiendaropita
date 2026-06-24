package ar.edu.davinci.dv_ds_20261c_g1.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class NegocioTest {

    private Prenda prenda(String precioBase) {
        return Prenda.builder()
                .precioBase(new BigDecimal(precioBase))
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build();
    }

    private List<Item> items(Prenda prenda, int cantidad) {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().prenda(prenda).cantidad(cantidad).build());
        return items;
    }

    @Test
    void calcularGananciasDelDiaSumaLosTotalesDeLasVentasDeEseDia() {
        LocalDate hoy = LocalDate.now();

        VentaEfectivo v1 = VentaEfectivo.builder()
                .fecha(hoy)
                .items(items(prenda("500.00"), 1)) // total 450
                .build();

        VentaTarjeta v2 = VentaTarjeta.builder()
                .fecha(hoy)
                .cantidadCuotas(3)
                .coeficiente(new BigDecimal("0.01"))
                .items(items(prenda("500.00"), 2)) // total 1030
                .build();

        Negocio negocio = Negocio.builder()
                .nombre("Tienda Ropita")
                .ventas(new ArrayList<>())
                .build();
        negocio.addVenta(v1);
        negocio.addVenta(v2);

        // 450 + 1030 = 1480
        assertEquals(0, new BigDecimal("1480.00").compareTo(negocio.calcularGananciasDelDia(hoy)));
    }

    @Test
    void ventasDeOtroDiaNoSeContabilizan() {
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);

        VentaEfectivo ventaAyer = VentaEfectivo.builder()
                .fecha(ayer)
                .items(items(prenda("500.00"), 1))
                .build();

        Negocio negocio = Negocio.builder().ventas(new ArrayList<>()).build();
        negocio.addVenta(ventaAyer);

        assertEquals(0, BigDecimal.ZERO.compareTo(negocio.calcularGananciasDelDia(hoy)));
    }
}
