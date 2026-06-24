# Tienda Ropita

Sistema de gestion para la materia **Diseno de Sistemas** (Escuela Da Vinci).
Aplicacion Java / Spring Boot con catalogo de prendas, clientes, ventas (efectivo y
tarjeta) con sus items, y calculo de ganancias del dia.

- **Grupo:** dv-ds-20261c-g1
- **Paquete base:** `ar.edu.davinci.dv_ds_20261c_g1`
- **Puerto:** `8090` -> http://localhost:8090

## Stack tecnologico

- Java 17+ (probado con Java 21)
- Spring Boot 3.3.4 (Web, Data JPA, Thymeleaf, Validation, Actuator, REST Docs)
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
- `/tienda/prendas/list` - ABM de prendas
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
de ventas (`VentaTest`), del calculo de ganancias (`NegocioTest`) y de persistencia
JPA con H2 (`PrendaRepositoryTest`).
