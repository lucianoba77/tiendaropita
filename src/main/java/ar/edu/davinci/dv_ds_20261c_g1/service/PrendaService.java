package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

public interface PrendaService {

    List<Prenda> list();

    Page<Prenda> list(Pageable pageable);

    Prenda get(Long id) throws BusinessException;

    Prenda save(Prenda prenda) throws BusinessException;

    Prenda update(Long id, Prenda prenda) throws BusinessException;

    void delete(Long id) throws BusinessException;
}
