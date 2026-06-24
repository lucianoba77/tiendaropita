package ar.edu.davinci.dv_ds_20261c_g1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;

@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private StockRepository stockRepository;

    @Test
    void guardaYRecuperaStockPorPrenda() {
        Prenda prenda = prendaRepository.save(Prenda.builder()
                .descripcion("Campera de Cuero")
                .precioBase(new BigDecimal("5000.00"))
                .tipoPrenda(TipoPrenda.CAMPERA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build());

        stockRepository.save(Stock.builder().prenda(prenda).cantidad(12).build());

        Optional<Stock> recuperado = stockRepository.findByPrendaId(prenda.getId());
        assertTrue(recuperado.isPresent());
        assertEquals(12, recuperado.get().getCantidad());
    }
}
