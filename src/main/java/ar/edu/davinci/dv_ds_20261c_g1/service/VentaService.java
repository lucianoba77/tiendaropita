package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.util.List;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

public interface VentaService {

    List<Venta> list();

    Venta get(Long id) throws BusinessException;

    VentaEfectivo saveEfectivo(VentaEfectivo venta) throws BusinessException;

    VentaTarjeta saveTarjeta(VentaTarjeta venta) throws BusinessException;

    void delete(Long id) throws BusinessException;

    Venta addItem(Long ventaId, Long prendaId, Integer cantidad) throws BusinessException;

    Venta updateItem(Long ventaId, Long itemId, Integer cantidad) throws BusinessException;

    Venta removeItem(Long ventaId, Long itemId) throws BusinessException;
}
