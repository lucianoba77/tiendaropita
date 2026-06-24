package ar.edu.davinci.dv_ds_20261c_g1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.StockRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.impl.StockServiceImpl;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    @Test
    void descontarReduceElStockCuandoHaySuficiente() throws BusinessException {
        Stock stock = Stock.builder().id(1L).cantidad(10).build();
        when(stockRepository.findByPrendaId(1L)).thenReturn(Optional.of(stock));

        stockService.descontar(1L, 4);

        assertEquals(6, stock.getCantidad());
        verify(stockRepository).save(stock);
    }

    @Test
    void descontarLanzaExcepcionCuandoNoHayStockSuficiente() {
        Stock stock = Stock.builder().id(1L).cantidad(2).build();
        when(stockRepository.findByPrendaId(1L)).thenReturn(Optional.of(stock));

        assertThrows(BusinessException.class, () -> stockService.descontar(1L, 5));
        verify(stockRepository, never()).save(any());
    }

    @Test
    void descontarLanzaExcepcionCuandoNoExisteStockParaLaPrenda() {
        when(stockRepository.findByPrendaId(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> stockService.descontar(99L, 1));
    }

    @Test
    void reponerAumentaElStockExistente() {
        Stock stock = Stock.builder().id(1L).cantidad(3).build();
        when(stockRepository.findByPrendaId(1L)).thenReturn(Optional.of(stock));

        stockService.reponer(1L, 5);

        assertEquals(8, stock.getCantidad());
        verify(stockRepository).save(stock);
    }

    @Test
    void cantidadDisponibleDevuelveCeroCuandoNoHayStock() {
        when(stockRepository.findByPrendaId(7L)).thenReturn(Optional.empty());

        assertEquals(0, stockService.cantidadDisponible(7L));
    }
}
