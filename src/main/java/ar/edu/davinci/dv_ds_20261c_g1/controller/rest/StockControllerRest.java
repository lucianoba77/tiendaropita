package ar.edu.davinci.dv_ds_20261c_g1.controller.rest;



import java.util.List;



import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;



import ar.edu.davinci.dv_ds_20261c_g1.controller.response.MovimientoStockResponse;

import ar.edu.davinci.dv_ds_20261c_g1.controller.response.StockResponse;

import ar.edu.davinci.dv_ds_20261c_g1.domain.MovimientoStock;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;

import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;

import ar.edu.davinci.dv_ds_20261c_g1.service.MovimientoStockService;

import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;

import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;

import lombok.RequiredArgsConstructor;



@RestController

@RequestMapping("/api/prendas/{prendaId}/stock")

@RequiredArgsConstructor

public class StockControllerRest {



    private final PrendaService prendaService;



    private final StockService stockService;



    private final MovimientoStockService movimientoStockService;



    @GetMapping

    public ResponseEntity<StockResponse> obtener(@PathVariable Long prendaId) throws BusinessException {

        Prenda prenda = prendaService.get(prendaId);

        return ResponseEntity.ok(buildResponse(prenda));

    }



    @GetMapping("/movimientos")

    public List<MovimientoStockResponse> movimientos(@PathVariable Long prendaId) throws BusinessException {

        return movimientoStockService.listarPorPrenda(prendaId).stream()

                .map(this::toMovimientoResponse)

                .toList();

    }



    @PutMapping

    public ResponseEntity<StockResponse> establecer(@PathVariable Long prendaId,

            @RequestParam Integer cantidad) throws BusinessException {

        Prenda prenda = prendaService.get(prendaId);

        stockService.establecer(prenda, cantidad);

        return ResponseEntity.ok(buildResponse(prenda));

    }



    @PostMapping("/reponer")

    public ResponseEntity<StockResponse> reponer(@PathVariable Long prendaId,

            @RequestParam Integer cantidad) throws BusinessException {

        Prenda prenda = prendaService.get(prendaId);

        stockService.reponer(prendaId, cantidad);

        return ResponseEntity.ok(buildResponse(prenda));

    }



    private StockResponse buildResponse(Prenda prenda) throws BusinessException {

        Stock stock = stockService.obtenerPorPrenda(prenda.getId());

        return StockResponse.builder()

                .prendaId(prenda.getId())

                .prendaDescripcion(prenda.getDescripcion())

                .cantidad(stock.getCantidad())

                .stockMinimo(stock.getStockMinimo())

                .bajoMinimo(stock.estaBajoMinimo())

                .build();

    }



    private MovimientoStockResponse toMovimientoResponse(MovimientoStock movimiento) {

        return MovimientoStockResponse.builder()

                .id(movimiento.getId())

                .prendaId(movimiento.getPrenda().getId())

                .cantidad(movimiento.getCantidad())

                .tipo(movimiento.getTipo())

                .fecha(movimiento.getFecha())

                .referenciaVentaId(movimiento.getReferenciaVentaId())

                .observacion(movimiento.getObservacion())

                .build();

    }

}


