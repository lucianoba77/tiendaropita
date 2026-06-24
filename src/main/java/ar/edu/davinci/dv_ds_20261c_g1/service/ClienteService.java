package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.util.List;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

public interface ClienteService {

    List<Cliente> list();

    Cliente get(Long id) throws BusinessException;

    Cliente save(Cliente cliente) throws BusinessException;

    Cliente update(Long id, Cliente cliente) throws BusinessException;

    void delete(Long id) throws BusinessException;
}
