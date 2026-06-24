package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.PrendaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrendaServiceImpl implements PrendaService {

    private final PrendaRepository prendaRepository;

    private final StockService stockService;

    @Override
    public List<Prenda> list() {
        return prendaRepository.findAll();
    }

    @Override
    public Page<Prenda> list(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("El pageable es obligatorio");
        }
        return prendaRepository.findAll(pageable);
    }

    @Override
    public Prenda get(Long id) throws BusinessException {
        if (id == null) {
            throw new BusinessException("El id de la prenda es obligatorio");
        }
        return prendaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No existe la prenda con id " + id));
    }

    @Override
    public Prenda save(Prenda prenda) throws BusinessException {
        validar(prenda);
        prenda.setId(null);
        return prendaRepository.save(prenda);
    }

    @Override
    public Prenda update(Long id, Prenda prenda) throws BusinessException {
        Prenda existente = get(id);
        validar(prenda);
        existente.setDescripcion(prenda.getDescripcion());
        existente.setPrecioBase(prenda.getPrecioBase());
        existente.setTipoPrenda(prenda.getTipoPrenda());
        existente.setEstadoPrenda(prenda.getEstadoPrenda());
        existente.setValorPromocion(prenda.getValorPromocion());
        return prendaRepository.save(existente);
    }

    @Override
    public void delete(Long id) throws BusinessException {
        Prenda existente = get(id);
        if (existente.getId() != null) {
            stockService.eliminarPorPrenda(id);
            prendaRepository.delete(existente);
        }
    }

    private void validar(Prenda prenda) throws BusinessException {
        if (prenda == null) {
            throw new BusinessException("La prenda es obligatoria");
        }
        if (prenda.getDescripcion() == null || prenda.getDescripcion().isBlank()) {
            throw new BusinessException("La descripcion de la prenda es obligatoria");
        }
        if (prenda.getPrecioBase() == null || prenda.getPrecioBase().signum() < 0) {
            throw new BusinessException("El precio base debe ser mayor o igual a cero");
        }
    }
}
