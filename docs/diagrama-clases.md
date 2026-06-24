# Diagrama de clases — Tienda Ropita

## Catálogo y Strategy (EstadoPrenda)

```mermaid
classDiagram
    class Prenda {
        +Long id
        +String descripcion
        +BigDecimal precioBase
        +TipoPrenda tipoPrenda
        +EstadoPrenda estadoPrenda
        +BigDecimal valorPromocion
        +precioVenta() BigDecimal
    }

    class EstadoPrenda {
        <<enumeration>>
        NUEVA
        PROMOCION
        LIQUIDACION
        +getStrategy() EstadoPrendaStrategy
    }

    class EstadoPrendaStrategy {
        <<interface>>
        +precioVenta(Prenda) BigDecimal
    }

    class EstadoPrendaNueva
    class EstadoPrendaPromocion
    class EstadoPrendaLiquidacion

    EstadoPrenda --> EstadoPrendaStrategy : delega
    EstadoPrendaNueva ..|> EstadoPrendaStrategy
    EstadoPrendaPromocion ..|> EstadoPrendaStrategy
    EstadoPrendaLiquidacion ..|> EstadoPrendaStrategy
    Prenda --> EstadoPrenda
```

**Patrón Strategy:** cada valor de `EstadoPrenda` encapsula su algoritmo de precio en una implementación de `EstadoPrendaStrategy`.

## Ventas y Template Method

```mermaid
classDiagram
    class Venta {
        <<abstract>>
        +Long id
        +LocalDate fecha
        +Cliente cliente
        +List~Item~ items
        +Negocio negocio
        +importeBruto() BigDecimal
        +calcularTotal() BigDecimal
        +recargo(BigDecimal)* BigDecimal
        +descuento(BigDecimal)* BigDecimal
    }

    class VentaEfectivo {
        +recargo() BigDecimal
        +descuento() BigDecimal
    }

    class VentaTarjeta {
        +Integer cantidadCuotas
        +BigDecimal coeficiente
        +recargo() BigDecimal
        +descuento() BigDecimal
    }

    class Item {
        +Long id
        +Integer cantidad
        +Prenda prenda
        +importe() BigDecimal
    }

    class Cliente {
        +Long id
        +String nombre
        +String apellido
    }

    VentaEfectivo --|> Venta
    VentaTarjeta --|> Venta
    Venta "1" --> "*" Item
    Venta --> Cliente
    Item --> Prenda
```

**Patrón Template Method:** `Venta.calcularTotal()` define el esqueleto (`importeBruto + recargo - descuento`); las subclases implementan los pasos variables.

## Stock y auditoría

```mermaid
classDiagram
    class Stock {
        +Long id
        +Integer cantidad
        +Integer stockMinimo
        +Long version
        +tieneStockSuficiente(int) boolean
        +descontar(int)
        +reponer(int)
        +estaBajoMinimo() boolean
    }

    class MovimientoStock {
        +Long id
        +Integer cantidad
        +TipoMovimientoStock tipo
        +LocalDateTime fecha
        +Long referenciaVentaId
        +String observacion
    }

    class TipoMovimientoStock {
        <<enumeration>>
        VENTA
        REPOSICION
        AJUSTE
    }

    class Negocio {
        +Long id
        +String nombre
        +calcularGananciasDelDia(LocalDate) BigDecimal
    }

    Stock "1" --> "1" Prenda
    MovimientoStock "*" --> "1" Prenda
    MovimientoStock --> TipoMovimientoStock
    Negocio "1" --> "*" Venta
```

**Auditoría de dominio:** cada cambio de stock genera un `MovimientoStock`. `StockServiceImpl` notifica al servicio de movimientos tras cada operación (Observer simplificado).

**Concurrencia:** `@Version` en `Stock` habilita bloqueo optimista ante ventas simultáneas.
