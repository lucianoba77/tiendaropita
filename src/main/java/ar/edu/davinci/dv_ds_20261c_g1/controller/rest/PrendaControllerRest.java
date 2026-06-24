package ar.edu.davinci.dv_ds_20261c_g1.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import ar.edu.davinci.dv_ds_20261c_g1.controller.request.PrendaInsertRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.request.PrendaUpdateRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.response.PrendaResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.mapper.PrendaMapper;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prendas")
public class PrendaControllerRest {

    @Autowired
    private PrendaService prendaService;

    @Autowired
    private PrendaMapper prendaMapper;

    @GetMapping("/all")
    public List<PrendaResponse> getAll() {
        return prendaMapper.toResponseList(prendaService.list());
    }

    @GetMapping
    public Page<PrendaResponse> getPaged(Pageable pageable) {
        return prendaService.list(pageable).map(prendaMapper::toResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrendaResponse> getById(@PathVariable Long id) throws BusinessException {
        Prenda prenda = prendaService.get(id);
        return ResponseEntity.ok(prendaMapper.toResponse(prenda));
    }

    @PostMapping
    public ResponseEntity<PrendaResponse> create(@Valid @RequestBody PrendaInsertRequest request)
            throws BusinessException {
        Prenda prenda = prendaService.save(prendaMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(prendaMapper.toResponse(prenda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrendaResponse> update(@PathVariable Long id,
            @Valid @RequestBody PrendaUpdateRequest request) throws BusinessException {
        Prenda prenda = prendaService.update(id, prendaMapper.toEntity(request));
        return ResponseEntity.ok(prendaMapper.toResponse(prenda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws BusinessException {
        prendaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
