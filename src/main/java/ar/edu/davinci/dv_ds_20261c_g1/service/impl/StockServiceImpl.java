package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.StockRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional(readOnly = true)
    public Stock obtenerPorPrenda(Long prendaId) throws BusinessException {
        return stockRepository.findByPrendaId(prendaId)
                .orElseThrow(() -> new BusinessException("No hay stock registrado para la prenda " + prendaId));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer cantidadDisponible(Long prendaId) {
        return stockRepository.findByPrendaId(prendaId)
                .map(Stock::getCantidad)
                .orElse(0);
    }

    @Override
    @Transactional
    public Stock establecer(Prenda prenda, Integer cantidad) throws BusinessException {
        if (prenda == null || prenda.getId() == null) {
            throw new BusinessException("La prenda es obligatoria para registrar stock");
        }
        int valor = (cantidad != null) ? cantidad : 0;
        if (valor < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
        Stock stock = stockRepository.findByPrendaId(prenda.getId())
                .orElseGet(() -> Stock.builder().prenda(prenda).cantidad(0).build());
        stock.setCantidad(valor);
        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void descontar(Long prendaId, Integer cantidad) throws BusinessException {
        if (cantidad == null || cantidad <= 0) {
            throw new BusinessException("La cantidad a descontar debe ser mayor a cero");
        }
        Stock stock = obtenerPorPrenda(prendaId);
        if (!stock.tieneStockSuficiente(cantidad)) {
            throw new BusinessException("Stock insuficiente para la prenda " + prendaId
                    + ": disponible " + stock.getCantidad() + ", solicitado " + cantidad);
        }
        stock.descontar(cantidad);
        stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void reponer(Long prendaId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            return;
        }
        stockRepository.findByPrendaId(prendaId).ifPresent(stock -> {
            stock.reponer(cantidad);
            stockRepository.save(stock);
        });
    }

    @Override
    @Transactional
    public void eliminarPorPrenda(Long prendaId) {
        stockRepository.deleteByPrendaId(prendaId);
    }
}
