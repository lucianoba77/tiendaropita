package ar.edu.davinci.dv_ds_20261c_g1.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.davinci.dv_ds_20261c_g1.controller.request.ClienteRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.response.ClienteResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.mapper.ClienteMapper;
import ar.edu.davinci.dv_ds_20261c_g1.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteControllerRest {

    private final ClienteService clienteService;

    private final ClienteMapper clienteMapper;

    @GetMapping("/all")
    public List<ClienteResponse> getAll() {
        return clienteMapper.toResponseList(clienteService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(clienteMapper.toResponse(clienteService.get(id)));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest request)
            throws BusinessException {
        Cliente cliente = clienteService.save(clienteMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> update(@PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) throws BusinessException {
        Cliente cliente = clienteService.update(id, clienteMapper.toEntity(request));
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws BusinessException {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
