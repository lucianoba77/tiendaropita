package ar.edu.davinci.dv_ds_20261c_g1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;

@DataJpaTest
class PrendaRepositoryTest {

    @Autowired
    private PrendaRepository prendaRepository;

    @Test
    void guardaYRecuperaUnaPrenda() {
        Prenda prenda = Prenda.builder()
                .descripcion("Saco Vestir")
                .precioBase(new BigDecimal("102.40"))
                .tipoPrenda(TipoPrenda.SACO)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build();

        Prenda guardada = prendaRepository.save(Objects.requireNonNull(prenda));

        Long id = guardada.getId();
        assertNotNull(id);

        Optional<Prenda> recuperada = prendaRepository.findById(id);
        assertTrue(recuperada.isPresent());
        assertEquals("Saco Vestir", recuperada.get().getDescripcion());
        assertEquals(TipoPrenda.SACO, recuperada.get().getTipoPrenda());
    }

    @Test
    void listaTodasLasPrendas() {
        Prenda prenda = Prenda.builder()
                .descripcion("Camisa Blanca")
                .precioBase(new BigDecimal("100.50"))
                .tipoPrenda(TipoPrenda.CAMISA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build();
        prendaRepository.save(Objects.requireNonNull(prenda));

        assertTrue(prendaRepository.findAll().size() >= 1);
    }
}
