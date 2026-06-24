package ar.edu.davinci.dv_ds_20261c_g1.controller.rest;

import java.util.List;

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
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/prendas")
@RequiredArgsConstructor
public class PrendaControllerRest {

    private final PrendaService prendaService;

    private final StockService stockService;

    private final PrendaMapper prendaMapper;

    @GetMapping("/all")
    public List<PrendaResponse> getAll() {
        return prendaService.list().stream()
                .map(this::toResponseConStock)
                .toList();
    }

    @GetMapping
    public Page<PrendaResponse> getPaged(Pageable pageable) {
        return prendaService.list(pageable).map(this::toResponseConStock);
    }

    @GetMapping("/stock-bajo")
    public List<PrendaResponse> stockBajo() {
        return stockService.listarStockBajo().stream()
                .map(stock -> toResponseConStock(stock.getPrenda()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrendaResponse> getById(@PathVariable Long id) throws BusinessException {
        Prenda prenda = prendaService.get(id);
        return ResponseEntity.ok(toResponseConStock(prenda));
    }

    @PostMapping
    public ResponseEntity<PrendaResponse> create(@Valid @RequestBody PrendaInsertRequest request)
            throws BusinessException {
        Prenda prenda = prendaService.save(prendaMapper.toEntity(request));
        stockService.establecer(prenda, request.getStockInicial(), request.getStockMinimo());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseConStock(prenda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrendaResponse> update(@PathVariable Long id,
            @Valid @RequestBody PrendaUpdateRequest request) throws BusinessException {
        Prenda prenda = prendaService.update(id, prendaMapper.toEntity(request));
        if (request.getStock() != null || request.getStockMinimo() != null) {
            Integer cantidad = request.getStock() != null
                    ? request.getStock()
                    : stockService.cantidadDisponible(id);
            stockService.establecer(prenda, cantidad, request.getStockMinimo());
        }
        return ResponseEntity.ok(toResponseConStock(prenda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws BusinessException {
        prendaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PrendaResponse toResponseConStock(Prenda prenda) {
        PrendaResponse response = prendaMapper.toResponse(prenda);
        response.setStockDisponible(stockService.cantidadDisponible(prenda.getId()));
        return response;
    }
}
