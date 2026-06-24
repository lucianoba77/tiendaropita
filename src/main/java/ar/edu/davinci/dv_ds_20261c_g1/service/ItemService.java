package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.util.List;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

public interface ItemService {

    List<Item> list();

    Item get(Long id) throws BusinessException;

    Item buildItem(Long prendaId, Integer cantidad) throws BusinessException;
}
