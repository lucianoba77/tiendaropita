# Diagrama de secuencia — Agregar item a una venta

Flujo cuando el cliente agrega un item vía REST (`POST /api/ventas/{id}/items`).

```mermaid
sequenceDiagram
    participant Client as ClienteHTTP
    participant VCR as VentaControllerRest
    participant VS as VentaServiceImpl
    participant PS as PrendaService
    participant SS as StockServiceImpl
    participant MS as MovimientoStockService
    participant SR as StockRepository
    participant VR as VentaRepository

    Client->>VCR: POST /api/ventas/{ventaId}/items
    VCR->>VS: addItem(ventaId, prendaId, cantidad)
    VS->>VR: findById(ventaId)
    VR-->>VS: Venta
    VS->>PS: get(prendaId)
    PS-->>VS: Prenda
    VS->>SS: descontar(prendaId, cantidad, ventaId)
    SS->>SR: findByPrendaId(prendaId)
    SR-->>SS: Stock

    alt stock insuficiente
        SS-->>VS: BusinessException
        VS-->>VCR: BusinessException
        VCR-->>Client: 400 Bad Request
    else stock suficiente
        SS->>SS: stock.descontar(cantidad)
        SS->>SR: save(stock)
        Note over SS,SR: @Version detecta conflictos concurrentes
        SS->>MS: registrar(prendaId, VENTA, ventaId)
        MS-->>SS: ok
        SS-->>VS: ok
        VS->>VS: venta.addItem(item)
        VS->>VR: save(venta)
        VR-->>VS: Venta actualizada
        VS-->>VCR: Venta
        VCR-->>Client: 200 OK + VentaResponse
    end
```

## Escenario de concurrencia

Si dos ventas intentan descontar el último item al mismo tiempo:

1. Ambas leen `Stock` con la misma `@Version`.
2. La primera hace `save` y la versión incrementa.
3. La segunda recibe `OptimisticLockingFailureException`.
4. `GlobalRestExceptionHandler` responde **409 Conflict** con mensaje para reintentar.
