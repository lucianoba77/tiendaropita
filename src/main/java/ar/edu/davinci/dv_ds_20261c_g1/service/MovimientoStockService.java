package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.util.List;

import ar.edu.davinci.dv_ds_20261c_g1.domain.MovimientoStock;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoMovimientoStock;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

public interface MovimientoStockService {

    void registrar(Long prendaId, Integer cantidad, TipoMovimientoStock tipo,
            Long referenciaVentaId, String observacion) throws BusinessException;

    List<MovimientoStock> listarPorPrenda(Long prendaId) throws BusinessException;
}
