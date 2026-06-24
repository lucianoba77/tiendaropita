-- =====================================================================
-- Tienda Ropita - Script de creacion de tablas (MySQL 8+)
-- Grupo: dv-ds-20261c-g1
-- Ejecutar sobre la base de datos: dv_ds_20261c_g1
--   CREATE DATABASE IF NOT EXISTS dv_ds_20261c_g1;
--   USE dv_ds_20261c_g1;
-- =====================================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS ventas_tarjeta;
DROP TABLE IF EXISTS ventas_efectivo;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS prendas;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS negocios;
SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------- Prendas -----------------------------------
CREATE TABLE prendas (
    prd_id              BIGINT       NOT NULL AUTO_INCREMENT,
    prd_descripcion     VARCHAR(255) NOT NULL,
    prd_precio_base     DECIMAL(19,2) DEFAULT NULL,
    prd_tipo_prenda     VARCHAR(255) DEFAULT NULL,
    prd_estado_prenda   VARCHAR(255) DEFAULT NULL,
    prd_valor_promocion DECIMAL(19,2) DEFAULT NULL,
    PRIMARY KEY (prd_id)
);

-- ------------------------- Clientes ----------------------------------
CREATE TABLE clientes (
    cli_id       BIGINT       NOT NULL AUTO_INCREMENT,
    cli_nombre   VARCHAR(255) DEFAULT NULL,
    cli_apellido VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (cli_id)
);

-- ------------------------- Negocios ----------------------------------
CREATE TABLE negocios (
    neg_id     BIGINT       NOT NULL AUTO_INCREMENT,
    neg_nombre VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (neg_id)
);

-- ------------------------- Ventas (base, JOINED) ---------------------
CREATE TABLE ventas (
    vta_id     BIGINT      NOT NULL AUTO_INCREMENT,
    tipo_venta VARCHAR(31) DEFAULT NULL,
    vta_fecha  DATE        DEFAULT NULL,
    vta_cli_id BIGINT      NOT NULL,
    vta_neg_id BIGINT      DEFAULT NULL,
    PRIMARY KEY (vta_id),
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (vta_cli_id) REFERENCES clientes (cli_id),
    CONSTRAINT fk_ventas_negocio FOREIGN KEY (vta_neg_id) REFERENCES negocios (neg_id)
);

-- ------------------------- Venta Efectivo ----------------------------
CREATE TABLE ventas_efectivo (
    vta_id BIGINT NOT NULL,
    PRIMARY KEY (vta_id),
    CONSTRAINT fk_efectivo_venta FOREIGN KEY (vta_id) REFERENCES ventas (vta_id)
);

-- ------------------------- Venta Tarjeta -----------------------------
CREATE TABLE ventas_tarjeta (
    vta_id               BIGINT        NOT NULL,
    vtt_cantidad_cuotas  INT           DEFAULT NULL,
    vtt_coeficiente      DECIMAL(19,4) DEFAULT NULL,
    PRIMARY KEY (vta_id),
    CONSTRAINT fk_tarjeta_venta FOREIGN KEY (vta_id) REFERENCES ventas (vta_id)
);

-- ------------------------- Items -------------------------------------
CREATE TABLE items (
    itm_id       BIGINT NOT NULL AUTO_INCREMENT,
    itm_cantidad INT    DEFAULT NULL,
    itm_prd_id   BIGINT DEFAULT NULL,
    itm_vta_id   BIGINT DEFAULT NULL,
    PRIMARY KEY (itm_id),
    CONSTRAINT fk_items_prenda FOREIGN KEY (itm_prd_id) REFERENCES prendas (prd_id),
    CONSTRAINT fk_items_venta  FOREIGN KEY (itm_vta_id) REFERENCES ventas (vta_id)
);
