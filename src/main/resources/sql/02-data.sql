-- =====================================================================
-- Tienda Ropita - Datos de ejemplo (MySQL 8+)
-- Ejecutar luego de 01-tablas.sql sobre la base dv_ds_20261c_g1
-- =====================================================================

-- ------------------------- Prendas -----------------------------------
INSERT INTO prendas (prd_descripcion, prd_precio_base, prd_tipo_prenda, prd_estado_prenda, prd_valor_promocion) VALUES
('Camisa Celeste',           10.24,   'CAMISA',   'NUEVA',       NULL),
('Camisa Blanca',            100.50,  'CAMISA',   'NUEVA',       NULL),
('Saco Vestir',              102.40,  'SACO',     'PROMOCION',   20.00),
('Pantalon Gabardina Beige', 1004.00, 'PANTALON', 'NUEVA',       NULL),
('Tapado Negro',             3234.22, 'TAPADO',   'LIQUIDACION', NULL),
('Campera de Cuero',         5000.00, 'CAMPERA',  'NUEVA',       NULL),
('Bufanda de Lana',          850.75,  'BUFANDA',  'PROMOCION',   100.00),
('Media Deportiva',          300.00,  'MEDIA',    'NUEVA',       NULL);

-- ------------------------- Clientes ----------------------------------
INSERT INTO clientes (cli_nombre, cli_apellido) VALUES
('Juan',   'Perez'),
('Maria',  'Gomez'),
('Carlos', 'Lopez');

-- ------------------------- Negocio -----------------------------------
INSERT INTO negocios (neg_nombre) VALUES
('Tienda Ropita Central');

-- ------------------------- Ventas de ejemplo -------------------------
-- Venta 1: Efectivo, cliente Juan Perez
INSERT INTO ventas (tipo_venta, vta_fecha, vta_cli_id, vta_neg_id) VALUES
('EFECTIVO', CURDATE(), 1, 1);
INSERT INTO ventas_efectivo (vta_id) VALUES (1);
INSERT INTO items (itm_cantidad, itm_prd_id, itm_vta_id) VALUES
(2, 2, 1),  -- 2 x Camisa Blanca
(1, 3, 1);  -- 1 x Saco Vestir (promocion)

-- Venta 2: Tarjeta, cliente Maria Gomez, 3 cuotas, coeficiente 0.01
INSERT INTO ventas (tipo_venta, vta_fecha, vta_cli_id, vta_neg_id) VALUES
('TARJETA', CURDATE(), 2, 1);
INSERT INTO ventas_tarjeta (vta_id, vtt_cantidad_cuotas, vtt_coeficiente) VALUES
(2, 3, 0.0100);
INSERT INTO items (itm_cantidad, itm_prd_id, itm_vta_id) VALUES
(1, 4, 2),  -- 1 x Pantalon
(3, 1, 2);  -- 3 x Camisa Celeste
