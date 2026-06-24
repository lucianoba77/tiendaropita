# Tienda Ropita

Sistema de gestion para la materia **Diseno de Sistemas** (Escuela Da Vinci).
Aplicacion Java / Spring Boot con catalogo de prendas, clientes, ventas (efectivo y
tarjeta) con sus items, y calculo de ganancias del dia.

- **Grupo:** dv-ds-20261c-g1
- **Paquete base:** `ar.edu.davinci.dv_ds_20261c_g1`
- **Puerto:** `8090` -> http://localhost:8090

## Cumplimiento de los criterios de entrega (TP Final)

Mapeo de los puntos pedidos por la catedra (Diseno de Sistemas - ACN5AV):

| Punto | Requisito | Estado |
|-------|-----------|--------|
| 1 | Proyecto Java exportable + scripts SQL de MySQL; corre en `localhost:8090` | Completo |
| 2 | Vista de Venta: cargar venta Efectivo y Tarjeta; agregar, modificar y quitar items | Completo |
| 3 | Entidad `Negocio`/Tienda con lista de ventas y "calcular ganancias de un dia" | Completo |
| 4 | `Prenda` con `EstadoPrenda` (Nueva, Promocion, Liquidacion) resuelto con Strategy | Completo |
| 5 | Entidad para administrar el **stock** de cada producto; al vender se descuenta el stock | Completo |

## Stack tecnologico

- Java 17+ (probado con Java 21)
- Spring Boot 3.5.15 (Web, Data JPA, Thymeleaf, Validation, Actuator, REST Docs)
- MySQL 8 (desarrollo) / H2 (tests)
- Lombok + MapStruct
- Maven (incluye Maven Wrapper `mvnw`)

## Patrones de diseno aplicados

- **MVC**: controladores web (Thymeleaf) y REST separados de servicios y dominio.
- **Strategy** (`EstadoPrenda`): el precio de venta se calcula segun el estado de la prenda:
  - `NUEVA`: precio de venta = precio base.
  - `PROMOCION`: precio de venta = precio base - valor de promocion.
  - `LIQUIDACION`: precio de venta = 50% del precio base.
- **Template Method** (`Venta`): `calcularTotal() = importeBruto() + recargo() - descuento()`.
  - `VentaEfectivo`: sin recargo; descuento del 15% si el bruto supera $1000, sino 10%.
  - `VentaTarjeta`: recargo = `cantidadCuotas * coeficiente * importeBruto`; sin descuento.

## Documentacion de diseno

Diagramas UML en la carpeta [`docs/`](docs/):

| Diagrama | Archivo | Uso en la defensa |
|----------|---------|-------------------|
| Clases | [diagrama-clases.md](docs/diagrama-clases.md) | Strategy en `EstadoPrenda`, Template Method en `Venta`, auditoria de stock |
| Secuencia | [diagrama-secuencia-add-item.md](docs/diagrama-secuencia-add-item.md) | Flujo agregar item con validacion de stock y bloqueo optimista |
| Capas | [diagrama-capas.md](docs/diagrama-capas.md) | Separacion web/REST/servicio/repositorio/dominio |

**Frase guia para la presentacion:** *"El patron Strategy vive en el enum `EstadoPrenda`; el Template Method, en la jerarquia de `Venta`; y la auditoria de stock registra cada movimiento desde un unico punto de escritura en `StockServiceImpl`."*

## Gestion de stock (Punto 5)

Administra la cantidad disponible de cada prenda y la mantiene consistente con las ventas:

- **Entidad dedicada** `Stock` con relacion 1 a 1 a `Prenda` (tabla `stocks`, columnas `stk_*`).
- **`StockService`** centraliza la logica: `descontar` valida que haya stock suficiente y lanza
  `BusinessException` si no alcanza; `reponer` devuelve unidades; `establecer` fija el stock; y
  `cantidadDisponible` consulta el disponible.
- **Descuento automatico al vender** (integrado en `VentaService`):
  - Alta de venta con items (REST) y `addItem`: descuentan stock.
  - `updateItem`: ajusta el stock por la diferencia (descuenta o repone segun aumente/baje la cantidad).
  - `removeItem` y baja de venta: reponen el stock de los items.
- **UI**: el stock se carga al crear/editar una prenda, se muestra en el listado de prendas y junto a
  cada prenda al agregar items en una venta.
- **API REST**: `GET/PUT /api/prendas/{id}/stock` y `POST /api/prendas/{id}/stock/reponer`; ademas
  `PrendaResponse` incluye `stockDisponible`.
- **Tests**: `StockTest` (dominio), `StockRepositoryTest` (persistencia con H2), `StockServiceImplTest`
  (validacion de stock insuficiente y descuento/reposicion con Mockito), `VentaServiceStockIntegrationTest`
  (consistencia transaccional real) y `ApiDocumentationTest` (REST Docs).

### Mejoras destacadas (mas alla del TP)

| Mejora | Descripcion |
|--------|-------------|
| Diagramas UML | [`docs/`](docs/) — clases, secuencia add-item, capas |
| Spring REST Docs | `ApiDocumentationTest` + HTML en `target/generated-docs/` |
| Integracion Venta+Stock | Test que valida fallo transaccional sin modificar stock |
| Historial de movimientos | Entidad `MovimientoStock` (VENTA, REPOSICION, AJUSTE) |
| Alertas stock bajo | Campo `stockMinimo`, badge en listado, `GET /api/prendas/stock-bajo` |
| Bloqueo optimista | `@Version` en `Stock`; HTTP 409 ante conflictos concurrentes |
| Ganancias enriquecidas | Desglose efectivo/tarjeta, cantidad de ventas, prenda mas vendida |

Endpoints REST adicionales:

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/prendas/stock-bajo` | Prendas con stock por debajo del minimo |
| GET | `/api/prendas/{id}/stock/movimientos` | Historial de movimientos de stock |
| GET | `/api/prendas/{id}/stock` | Stock con `stockMinimo` y flag `bajoMinimo` |

Ruta web adicional: `/tienda/prendas/{id}/stock/historial`.

## Compatibilidad y funcionamiento

Los flujos del TP **siguen operando igual**. Las mejoras son aditivas; no reemplazan
funcionalidad existente.

**Lo que no cambio:**

- ABM de prendas, clientes y ventas (efectivo/tarjeta)
- Agregar, modificar y quitar items en una venta
- Descuento automatico de stock al vender
- Total de ganancias del dia (mismo calculo; solo se agrego desglose en pantalla)
- Patrones Strategy y Template Method
- Endpoints REST originales (`/api/prendas`, `/api/ventas`, `/api/clientes`, etc.)

**Lo que se agrego:**

- Historial auditable de cada cambio de stock (`MovimientoStock`)
- Stock minimo configurable con alerta visual en el listado
- Reporte de ganancias con mas metricas (efectivo vs tarjeta, prenda top)
- Documentacion de diseno (diagramas) y de API (REST Docs)
- Proteccion ante condiciones de carrera en stock (`@Version` + HTTP 409)

### Detalles a tener en cuenta

1. **Base de datos:** con `ddl-auto=update` (default), Hibernate crea solas las columnas/tablas
   nuevas (`stk_stock_minimo`, `stk_version`, `movimientos_stock`). No hace falta migrar a mano
   salvo que uses `ddl-auto=none` y ejecutes `01-tablas.sql` manualmente.
2. **Stock minimo = 0:** no muestra alerta (valor por defecto). Solo alerta si el minimo es > 0
   y el stock actual cae por debajo.
3. **Historial de stock:** cada venta, reposicion o ajuste manual genera un movimiento. Es
   funcionalidad nueva, no altera comportamiento previo.
4. **Concurrencia:** si dos operaciones modifican el mismo stock simultaneamente, la API responde
   **409 Conflict** con mensaje para reintentar.
5. **Tests:** `mvnw.cmd test` ejecuta 28 tests (unitarios, integracion y REST Docs).

### Verificacion rapida manual

Con la app corriendo en http://localhost:8090:

1. **Stock bajo:** crear/editar una prenda con stock 5 y minimo 10 → badge rojo en el listado.
2. **Historial:** realizar una venta → en "Historial" de esa prenda debe aparecer movimiento VENTA.
3. **Ganancias:** `/tienda/negocio/ganancias` → cards con efectivo/tarjeta y prenda mas vendida.
4. **Stock insuficiente:** intentar vender mas unidades de las disponibles → error sin cambiar stock.
5. **API documentada:** `mvnw.cmd package` → abrir `target/generated-docs/index.html`.

## Decisiones de diseno y buenas practicas

Estas decisiones van mas alla del minimo pedido y refuerzan la calidad del diseno del sistema:

- **Arquitectura en capas con dependencias unidireccionales**: `controller -> service -> repository -> domain`. La capa web (Thymeleaf) y la capa REST estan separadas (`controller.web` vs `controller.rest`), de modo que un cambio de UI no impacta en la API.
- **Programacion contra interfaces (DIP de SOLID)**: cada servicio expone una interfaz (`PrendaService`, `VentaService`, etc.) con su implementacion en `service.impl`. Facilita testear con dobles y sustituir implementaciones.
- **Inyeccion de dependencias por constructor** (con `@RequiredArgsConstructor` de Lombok y campos `final`): las dependencias quedan inmutables y explicitas, evita el acoplamiento al contenedor de `@Autowired` por campo y permite instanciar las clases en tests sin Spring.
- **DTOs + MapStruct**: las entidades JPA nunca se exponen directamente en la API; se mapean a Request/Response con MapStruct, evitando fugas del modelo de persistencia y problemas de serializacion (lazy loading, ciclos).
- **Validacion declarativa**: los Request usan Jakarta Bean Validation (`@NotNull`, `@Positive`, etc.) y se validan con `@Valid` en los controladores, separando la validacion de formato de la validacion de negocio.
- **Manejo centralizado de errores**: `GlobalRestExceptionHandler` (`@RestControllerAdvice`) traduce `BusinessException` y errores de validacion a respuestas HTTP coherentes (400 + cuerpo JSON), evitando `try/catch` repetidos.
- **Reglas de negocio en el dominio**: los calculos viven en las entidades (`Prenda.precioVenta()`, `Venta.calcularTotal()`, `Item.importe()`, `Negocio.calcularGananciasDelDia()`), siguiendo un modelo de dominio rico en vez de un modelo anemico.
- **Transaccionalidad explicita**: los servicios marcan `@Transactional` (y `@Transactional(readOnly = true)` en consultas) para garantizar consistencia y optimizar lecturas.
- **Precision monetaria correcta**: todos los importes usan `BigDecimal` con `RoundingMode.HALF_UP` y escala 2; nunca `double`/`float` (evita errores de redondeo en dinero).
- **Herencia JPA `JOINED`** para `Venta` / `VentaEfectivo` / `VentaTarjeta`: normaliza el esquema y modela correctamente la jerarquia del Template Method.
- **Manejo de relaciones bidireccionales en JSON**: uso de `@JsonManagedReference` / `@JsonBackReference` para evitar recursion infinita al serializar `Venta <-> Item` y `Negocio <-> Venta`.
- **Testing por capas**: pruebas unitarias de los patrones (`PrendaTest` para Strategy, `VentaTest` para Template Method, `NegocioTest` para ganancias) y de persistencia con H2 (`PrendaRepositoryTest`), sin depender de MySQL.
- **Convenciones de esquema consistentes**: nombres de columnas con prefijo por entidad (`prd_`, `cli_`, `vta_`, `itm_`, `neg_`) para legibilidad del modelo relacional.

## Mejoras propuestas (roadmap)

Mejoras identificadas para una proxima iteracion, ordenadas por impacto en el diseno:

1. **Migrar `java.util.Date` a `java.time.LocalDate`/`LocalDateTime`** en `Venta` y filtros de fecha (API moderna, inmutable y sin zona horaria ambigua).
2. **Versionado de esquema con Flyway o Liquibase** en lugar de `ddl-auto=update`, para migraciones reproducibles y controladas.
3. **Documentacion de la API con OpenAPI/Swagger** (`springdoc-openapi`) para explorar y probar los endpoints desde el navegador.
4. **Paginacion y ordenamiento** en los listados (`Pageable`) para escalar cuando crezcan los datos.
5. **Tests de integracion con MockMvc / Spring REST Docs** sobre los controladores REST, complementando las pruebas unitarias actuales. *(Implementado: ver seccion Documentacion API REST.)*
6. **Seguridad con Spring Security** (autenticacion y roles) para proteger las operaciones de escritura.
7. **Internacionalizacion (i18n)** de los mensajes de validacion y de la UI.
8. **Logging con SLF4J** y niveles por entorno, reemplazando `spring.jpa.show-sql=true` en produccion.
9. **Actualizar a una rama de Spring Boot con soporte** (3.5.x o 4.x): la 3.3.x esta en End-of-Life desde junio 2025.
10. **Restringir los endpoints de Actuator** (`management.endpoints.web.exposure.include=*` expone todo; conviene limitar a `health,info`).

## Estructura de paquetes

```
ar.edu.davinci.dv_ds_20261c_g1
├── domain            Entidades JPA, enums y estrategias (Strategy)
├── repository        Interfaces Spring Data JPA
├── service (+impl)   Logica de negocio
├── exceptions        BusinessException
├── controller
│   ├── rest          @RestController (API JSON)
│   ├── web           @Controller (vistas Thymeleaf)
│   ├── request       DTOs de entrada
│   └── response      DTOs de salida
└── mapper            Interfaces @Mapper de MapStruct
```

## Requisitos previos

1. **JDK 17 o superior** instalado (`java -version`).
2. **MySQL 8** corriendo en `localhost:3306` con usuario `root` y password `rootroot`.
   - Podes cambiar estos valores en `src/main/resources/application.properties`.
   - La base `dv_ds_20261c_g1` se crea automaticamente (`createDatabaseIfNotExist=true`)
     y Hibernate crea las tablas (`ddl-auto=update`).

## Como ejecutar en Eclipse (paso a paso)

1. **Tener MySQL corriendo** (ver requisitos). No hace falta crear la base a mano.
2. Abrir Eclipse y elegir `File > Import...`.
3. Seleccionar `Maven > Existing Maven Projects` y `Next`.
4. En `Root Directory` elegir la carpeta **`tienda-ropita`** (la que contiene `pom.xml`) y `Finish`.
   - Eclipse descargara las dependencias con su Maven embebido (m2e). Esperar a que termine.
5. **Habilitar el procesamiento de anotaciones** (necesario para Lombok y MapStruct):
   - Click derecho en el proyecto > `Properties > Java Compiler > Annotation Processing`.
   - Tildar `Enable project specific settings` y `Enable annotation processing`.
   - Recomendado: instalar Lombok en Eclipse una sola vez ejecutando
     `java -jar <repo-m2>/org/projectlombok/lombok/1.18.34/lombok-1.18.34.jar`
     y apuntando al ejecutable de Eclipse. Luego reiniciar Eclipse.
6. Ejecutar la aplicacion:
   - Abrir `src/main/java/ar/edu/davinci/dv_ds_20261c_g1/TiendaRopitaApplication.java`.
   - Click derecho > `Run As > Java Application` (o `Spring Boot App` si tenes Spring Tools).
7. Abrir el navegador en **http://localhost:8090**.

> Si Eclipse marca errores de compilacion en los DTOs/mappers antes de ejecutar,
> hace `Project > Clean...` o `Maven > Update Project (Alt+F5)`; al activar el
> annotation processing las clases generadas por MapStruct se resuelven solas.

## Como ejecutar por consola (sin instalar Maven)

Desde la carpeta `tienda-ropita`:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

Para compilar y correr los tests (usan H2, no requieren MySQL):

```bash
mvnw.cmd test
```

## Scripts SQL (entrega)

En `src/main/resources/sql/`:

- `01-tablas.sql`: creacion de todas las tablas en MySQL.
- `02-data.sql`: datos de ejemplo (prendas, clientes, negocio y ventas).

Si preferis administrar el esquema manualmente:

1. En `application.properties` cambiar `spring.jpa.hibernate.ddl-auto=update` por `none`.
2. Crear la base y ejecutar los scripts:
   ```sql
   CREATE DATABASE IF NOT EXISTS dv_ds_20261c_g1;
   USE dv_ds_20261c_g1;
   SOURCE 01-tablas.sql;
   SOURCE 02-data.sql;
   ```

> Por defecto (con `ddl-auto=update`) el archivo `src/main/resources/db/data-seed.sql`
> carga datos de ejemplo automaticamente al iniciar.

## Navegacion web

- `/` - Menu principal
- `/tienda/prendas/list` - ABM de prendas (con alerta stock bajo e historial)
- `/tienda/prendas/{id}/stock/historial` - Historial de movimientos de stock
- `/tienda/clientes/list` - ABM de clientes
- `/tienda/ventas/list` - Ventas (alta efectivo/tarjeta, detalle con agregar/modificar/quitar items)
- `/tienda/negocio/ganancias` - Calcular ganancias de un dia

## API REST (resumen)

| Metodo | Endpoint                               | Descripcion                       |
|--------|----------------------------------------|-----------------------------------|
| GET    | `/api/prendas/all`                     | Lista de prendas                  |
| POST   | `/api/prendas`                         | Crear prenda                      |
| PUT    | `/api/prendas/{id}`                    | Actualizar prenda                 |
| DELETE | `/api/prendas/{id}`                    | Eliminar prenda                   |
| GET    | `/api/clientes/all`                    | Lista de clientes                 |
| POST   | `/api/clientes`                        | Crear cliente                     |
| GET    | `/api/ventas/all`                      | Lista de ventas                   |
| POST   | `/api/ventas/efectivo`                 | Crear venta en efectivo           |
| POST   | `/api/ventas/tarjeta`                  | Crear venta con tarjeta           |
| POST   | `/api/ventas/{id}/items`               | Agregar item a una venta          |
| PUT    | `/api/ventas/{id}/items/{itemId}`      | Modificar cantidad de un item     |
| DELETE | `/api/ventas/{id}/items/{itemId}`      | Quitar un item                    |
| GET    | `/api/prendas/stock-bajo`              | Prendas con stock bajo minimo     |
| GET    | `/api/prendas/{id}/stock`              | Consultar stock de una prenda     |
| GET    | `/api/prendas/{id}/stock/movimientos`  | Historial de movimientos          |

## Exportar el proyecto a ZIP (entrega)

Antes de comprimir conviene limpiar los binarios:

```bash
mvnw.cmd clean
```

Luego comprimir la carpeta `tienda-ropita` completa (incluyendo `pom.xml`, `src/`,
`mvnw`, `mvnw.cmd` y `.mvn/`). En Windows: click derecho sobre la carpeta >
`Enviar a > Carpeta comprimida (en zip)`.

## Tests

```bash
mvnw.cmd test
```

Incluye pruebas unitarias del patron Strategy (`PrendaTest`), del Template Method
de ventas (`VentaTest`), del calculo de ganancias (`NegocioTest`), de persistencia
JPA con H2 (`PrendaRepositoryTest`), integracion Venta+Stock (`VentaServiceStockIntegrationTest`)
y documentacion REST (`ApiDocumentationTest`).

## Documentacion API REST (Spring REST Docs)

Tras ejecutar los tests, generar el HTML con:

```bash
mvnw.cmd package
```

Abrir `target/generated-docs/index.html` en el navegador. La documentacion se genera
automaticamente desde los tests MockMvc en `ApiDocumentationTest`.
