package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoMovimientoStock;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.StockRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.MovimientoStockService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final MovimientoStockService movimientoStockService;

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
    @Transactional(readOnly = true)
    public Integer stockMinimo(Long prendaId) {
        return stockRepository.findByPrendaId(prendaId)
                .map(Stock::getStockMinimo)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaBajoMinimo(Long prendaId) {
        return stockRepository.findByPrendaId(prendaId)
                .map(Stock::estaBajoMinimo)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stock> listarStockBajo() {
        return stockRepository.findStockBajo();
    }

    @Override
    @Transactional
    public Stock establecer(Prenda prenda, Integer cantidad) throws BusinessException {
        return establecer(prenda, cantidad, null);
    }

    @Override
    @Transactional
    public Stock establecer(Prenda prenda, Integer cantidad, Integer stockMinimo) throws BusinessException {
        if (prenda == null || prenda.getId() == null) {
            throw new BusinessException("La prenda es obligatoria para registrar stock");
        }
        int valor = (cantidad != null) ? cantidad : 0;
        if (valor < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
        if (stockMinimo != null && stockMinimo < 0) {
            throw new BusinessException("El stock minimo no puede ser negativo");
        }
        Stock stock = stockRepository.findByPrendaId(prenda.getId())
                .orElseGet(() -> Stock.builder().prenda(prenda).cantidad(0).stockMinimo(0).build());
        int anterior = (stock.getCantidad() != null) ? stock.getCantidad() : 0;
        stock.setCantidad(valor);
        if (stockMinimo != null) {
            stock.setStockMinimo(stockMinimo);
        } else if (stock.getStockMinimo() == null) {
            stock.setStockMinimo(0);
        }
        Stock guardado = stockRepository.save(stock);
        int delta = Math.abs(valor - anterior);
        if (delta > 0) {
            movimientoStockService.registrar(prenda.getId(), delta, TipoMovimientoStock.AJUSTE, null,
                    "Ajuste de stock: " + anterior + " -> " + valor);
        }
        return guardado;
    }

    @Override
    @Transactional
    public void descontar(Long prendaId, Integer cantidad) throws BusinessException {
        descontar(prendaId, cantidad, null);
    }

    @Override
    @Transactional
    public void descontar(Long prendaId, Integer cantidad, Long ventaId) throws BusinessException {
        if (cantidad == null || cantidad <= 0) {
            throw new BusinessException("La cantidad a descontar debe ser mayor a cero");
        }
        Stock stock = stockRepository.findByPrendaId(prendaId)
                .orElseThrow(() -> new BusinessException("No hay stock registrado para la prenda " + prendaId));
        if (!stock.tieneStockSuficiente(cantidad)) {
            throw new BusinessException("Stock insuficiente para la prenda " + prendaId
                    + ": disponible " + stock.getCantidad() + ", solicitado " + cantidad);
        }
        stock.descontar(cantidad);
        stockRepository.save(stock);
        movimientoStockService.registrar(prendaId, cantidad, TipoMovimientoStock.VENTA, ventaId, null);
    }

    @Override
    @Transactional
    public void reponer(Long prendaId, Integer cantidad) {
        reponer(prendaId, cantidad, null);
    }

    @Override
    @Transactional
    public void reponer(Long prendaId, Integer cantidad, Long ventaId) {
        if (cantidad == null || cantidad <= 0) {
            return;
        }
        stockRepository.findByPrendaId(prendaId).ifPresent(stock -> {
            stock.reponer(cantidad);
            stockRepository.save(stock);
            try {
                movimientoStockService.registrar(prendaId, cantidad, TipoMovimientoStock.REPOSICION, ventaId, null);
            } catch (BusinessException ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        });
    }

    @Override
    @Transactional
    public void eliminarPorPrenda(Long prendaId) {
        stockRepository.deleteByPrendaId(prendaId);
    }
}
