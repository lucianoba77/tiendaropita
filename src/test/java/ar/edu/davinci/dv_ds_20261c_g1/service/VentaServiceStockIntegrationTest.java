package ar.edu.davinci.dv_ds_20261c_g1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.ClienteRepository;
import ar.edu.davinci.dv_ds_20261c_g1.repository.PrendaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.repository.VentaRepository;

@SpringBootTest
@Transactional
class VentaServiceStockIntegrationTest {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    private Long clienteId;
    private Long prendaId;
    private Long ventaId;

    @BeforeEach
    void setUp() throws BusinessException {
        Cliente cliente = clienteRepository.save(Cliente.builder()
                .nombre("Ana")
                .apellido("Test")
                .build());

        Prenda prenda = prendaRepository.save(Prenda.builder()
                .descripcion("Remera Test")
                .precioBase(new BigDecimal("100.00"))
                .tipoPrenda(TipoPrenda.CAMISA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build());

        stockService.establecer(prenda, 2, 1);

        VentaEfectivo venta = ventaService.saveEfectivo(VentaEfectivo.builder()
                .cliente(cliente)
                .items(Collections.emptyList())
                .build());

        clienteId = cliente.getId();
        prendaId = prenda.getId();
        ventaId = venta.getId();
    }

    @Test
    void addItemConStockInsuficienteNoModificaElStock() {
        assertThrows(BusinessException.class, () -> ventaService.addItem(ventaId, prendaId, 5));

        assertEquals(2, stockService.cantidadDisponible(prendaId));

        var venta = ventaRepository.findById(ventaId).orElseThrow();
        assertTrue(venta.getItems() == null || venta.getItems().isEmpty());
    }

    @Test
    void addItemExitosoDescuentaElStock() throws BusinessException {
        ventaService.addItem(ventaId, prendaId, 2);

        assertEquals(0, stockService.cantidadDisponible(prendaId));

        var venta = ventaRepository.findById(ventaId).orElseThrow();
        assertEquals(1, venta.getItems().size());
        assertEquals(2, venta.getItems().get(0).getCantidad());
    }
}
