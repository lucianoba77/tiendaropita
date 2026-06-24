package ar.edu.davinci.dv_ds_20261c_g1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20261c_g1.repository.ClienteRepository;
import ar.edu.davinci.dv_ds_20261c_g1.repository.PrendaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.repository.VentaRepository;

@SpringBootTest
@Transactional
class NegocioServiceImplTest {

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Test
    void calcularResumenDelDiaIncluyeDesgloseYPrendaMasVendida() {
        LocalDate hoy = LocalDate.now();

        Cliente cliente = Objects.requireNonNull(clienteRepository.save(
                Cliente.builder().nombre("Juan").apellido("Perez").build()));
        Prenda remera = Objects.requireNonNull(prendaRepository.save(Prenda.builder()
                .descripcion("Remera")
                .precioBase(new BigDecimal("100.00"))
                .tipoPrenda(TipoPrenda.CAMISA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build()));
        Prenda pantalon = Objects.requireNonNull(prendaRepository.save(Prenda.builder()
                .descripcion("Pantalon")
                .precioBase(new BigDecimal("200.00"))
                .tipoPrenda(TipoPrenda.PANTALON)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build()));

        List<Item> itemsEfectivo = new ArrayList<>();
        itemsEfectivo.add(Item.builder().prenda(remera).cantidad(3).build());

        VentaEfectivo ventaEfectivo = VentaEfectivo.builder()
                .cliente(cliente)
                .fecha(hoy)
                .items(itemsEfectivo)
                .build();
        itemsEfectivo.forEach(i -> i.setVenta(ventaEfectivo));
        ventaRepository.save(ventaEfectivo);

        List<Item> itemsTarjeta = new ArrayList<>();
        itemsTarjeta.add(Item.builder().prenda(pantalon).cantidad(1).build());

        VentaTarjeta ventaTarjeta = VentaTarjeta.builder()
                .cliente(cliente)
                .fecha(hoy)
                .cantidadCuotas(1)
                .coeficiente(new BigDecimal("0.05"))
                .items(itemsTarjeta)
                .build();
        itemsTarjeta.forEach(i -> i.setVenta(ventaTarjeta));
        ventaRepository.save(ventaTarjeta);

        var resumen = negocioService.calcularResumenDelDia(hoy);

        assertEquals(2, resumen.getCantidadVentas());
        assertEquals(0, resumen.getTotalEfectivo().compareTo(ventaEfectivo.calcularTotal()));
        assertEquals(0, resumen.getTotalTarjeta().compareTo(ventaTarjeta.calcularTotal()));
        assertEquals(0, resumen.getTotal().compareTo(
                resumen.getTotalEfectivo().add(resumen.getTotalTarjeta())));
        assertEquals("Remera", resumen.getPrendaMasVendidaDescripcion());
        assertEquals(3, resumen.getPrendaMasVendidaUnidades());
        assertNotNull(negocioService.calcularGananciasDelDia(hoy));
    }
}
