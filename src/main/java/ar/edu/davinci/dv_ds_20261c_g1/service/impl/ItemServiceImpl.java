package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.ItemRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.ItemService;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final PrendaService prendaService;

    @Override
    public List<Item> list() {
        return itemRepository.findAll();
    }

    @Override
    public Item get(Long id) throws BusinessException {
        return itemRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No existe el item con id " + id));
    }

    @Override
    public Item buildItem(Long prendaId, Integer cantidad) throws BusinessException {
        if (cantidad == null || cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }
        Prenda prenda = prendaService.get(prendaId);
        return Item.builder()
                .prenda(prenda)
                .cantidad(cantidad)
                .build();
    }
}
