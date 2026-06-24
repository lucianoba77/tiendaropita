package ar.edu.davinci.dv_ds_20261c_g1.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.davinci.dv_ds_20261c_g1.controller.response.StockResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/prendas/{prendaId}/stock")
@RequiredArgsConstructor
public class StockControllerRest {

    private final PrendaService prendaService;

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<StockResponse> obtener(@PathVariable Long prendaId) throws BusinessException {
        Prenda prenda = prendaService.get(prendaId);
        return ResponseEntity.ok(buildResponse(prenda, stockService.cantidadDisponible(prendaId)));
    }

    @PutMapping
    public ResponseEntity<StockResponse> establecer(@PathVariable Long prendaId,
            @RequestParam Integer cantidad) throws BusinessException {
        Prenda prenda = prendaService.get(prendaId);
        stockService.establecer(prenda, cantidad);
        return ResponseEntity.ok(buildResponse(prenda, stockService.cantidadDisponible(prendaId)));
    }

    @PostMapping("/reponer")
    public ResponseEntity<StockResponse> reponer(@PathVariable Long prendaId,
            @RequestParam Integer cantidad) throws BusinessException {
        Prenda prenda = prendaService.get(prendaId);
        stockService.reponer(prendaId, cantidad);
        return ResponseEntity.ok(buildResponse(prenda, stockService.cantidadDisponible(prendaId)));
    }

    private StockResponse buildResponse(Prenda prenda, Integer cantidad) {
        return StockResponse.builder()
                .prendaId(prenda.getId())
                .prendaDescripcion(prenda.getDescripcion())
                .cantidad(cantidad)
                .build();
    }
}
