package ar.edu.davinci.dv_ds_20261c_g1.controller.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.davinci.dv_ds_20261c_g1.controller.request.ItemRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.request.VentaEfectivoRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.request.VentaTarjetaRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.response.VentaResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.mapper.VentaMapper;
import ar.edu.davinci.dv_ds_20261c_g1.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaControllerRest {

    private final VentaService ventaService;

    private final VentaMapper ventaMapper;

    @GetMapping("/all")
    public List<VentaResponse> getAll() {
        return ventaMapper.toResponseList(ventaService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> getById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(ventaMapper.toResponse(ventaService.get(id)));
    }

    @PostMapping("/efectivo")
    public ResponseEntity<VentaResponse> createEfectivo(@Valid @RequestBody VentaEfectivoRequest request)
            throws BusinessException {
        VentaEfectivo venta = VentaEfectivo.builder()
                .cliente(Cliente.builder().id(request.getClienteId()).build())
                .items(toItems(request.getItems()))
                .build();
        VentaEfectivo guardada = ventaService.saveEfectivo(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaMapper.toResponse(guardada));
    }

    @PostMapping("/tarjeta")
    public ResponseEntity<VentaResponse> createTarjeta(@Valid @RequestBody VentaTarjetaRequest request)
            throws BusinessException {
        VentaTarjeta venta = VentaTarjeta.builder()
                .cliente(Cliente.builder().id(request.getClienteId()).build())
                .cantidadCuotas(request.getCantidadCuotas())
                .coeficiente(request.getCoeficiente())
                .items(toItems(request.getItems()))
                .build();
        VentaTarjeta guardada = ventaService.saveTarjeta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaMapper.toResponse(guardada));
    }

    @PostMapping("/{ventaId}/items")
    public ResponseEntity<VentaResponse> addItem(@PathVariable Long ventaId,
            @Valid @RequestBody ItemRequest request) throws BusinessException {
        Venta venta = ventaService.addItem(ventaId, request.getPrendaId(), request.getCantidad());
        return ResponseEntity.ok(ventaMapper.toResponse(venta));
    }

    @PutMapping("/{ventaId}/items/{itemId}")
    public ResponseEntity<VentaResponse> updateItem(@PathVariable Long ventaId, @PathVariable Long itemId,
            @RequestParam Integer cantidad) throws BusinessException {
        Venta venta = ventaService.updateItem(ventaId, itemId, cantidad);
        return ResponseEntity.ok(ventaMapper.toResponse(venta));
    }

    @DeleteMapping("/{ventaId}/items/{itemId}")
    public ResponseEntity<VentaResponse> removeItem(@PathVariable Long ventaId, @PathVariable Long itemId)
            throws BusinessException {
        Venta venta = ventaService.removeItem(ventaId, itemId);
        return ResponseEntity.ok(ventaMapper.toResponse(venta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws BusinessException {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<Item> toItems(List<ItemRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(r -> Item.builder()
                        .prenda(Prenda.builder().id(r.getPrendaId()).build())
                        .cantidad(r.getCantidad())
                        .build())
                .collect(Collectors.toList());
    }
}
