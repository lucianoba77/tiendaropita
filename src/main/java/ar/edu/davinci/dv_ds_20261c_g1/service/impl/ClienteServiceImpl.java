package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.ClienteRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> list() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente get(Long id) throws BusinessException {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No existe el cliente con id " + id));
    }

    @Override
    public Cliente save(Cliente cliente) throws BusinessException {
        validar(cliente);
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente update(Long id, Cliente cliente) throws BusinessException {
        Cliente existente = get(id);
        validar(cliente);
        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        return clienteRepository.save(existente);
    }

    @Override
    public void delete(Long id) throws BusinessException {
        Cliente existente = get(id);
        clienteRepository.delete(existente);
    }

    private void validar(Cliente cliente) throws BusinessException {
        if (cliente == null) {
            throw new BusinessException("El cliente es obligatorio");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new BusinessException("El nombre del cliente es obligatorio");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            throw new BusinessException("El apellido del cliente es obligatorio");
        }
    }
}
