package ar.edu.davinci.dv_ds_20261c_g1.service;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

public interface StockService {

    Stock obtenerPorPrenda(Long prendaId) throws BusinessException;

    Integer cantidadDisponible(Long prendaId);

    /**
     * Crea o actualiza el stock de una prenda con la cantidad indicada (valor absoluto).
     */
    Stock establecer(Prenda prenda, Integer cantidad) throws BusinessException;

    /**
     * Descuenta la cantidad vendida; lanza BusinessException si no hay stock suficiente.
     */
    void descontar(Long prendaId, Integer cantidad) throws BusinessException;

    /**
     * Repone unidades al stock de la prenda (por ejemplo al quitar items de una venta).
     */
    void reponer(Long prendaId, Integer cantidad);

    void eliminarPorPrenda(Long prendaId);
}
