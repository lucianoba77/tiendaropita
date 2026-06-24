-- Datos de ejemplo para entorno de desarrollo (MySQL).
-- Se ejecuta luego de que Hibernate crea las tablas (defer-datasource-initialization=true).
-- Usa INSERT IGNORE con ids explicitos para ser idempotente entre reinicios.

INSERT IGNORE INTO prendas (prd_id, prd_descripcion, prd_precio_base, prd_tipo_prenda, prd_estado_prenda, prd_valor_promocion) VALUES
(1, 'Camisa Celeste', 10.24, 'CAMISA', 'NUEVA', NULL),
(2, 'Camisa Blanca', 100.50, 'CAMISA', 'NUEVA', NULL),
(3, 'Saco Vestir', 102.40, 'SACO', 'PROMOCION', 20.00),
(4, 'Pantalon Gabardina Beige', 1004.00, 'PANTALON', 'NUEVA', NULL),
(5, 'Tapado Negro', 3234.22, 'TAPADO', 'LIQUIDACION', NULL),
(6, 'Campera de Cuero', 5000.00, 'CAMPERA', 'NUEVA', NULL),
(7, 'Bufanda de Lana', 850.75, 'BUFANDA', 'PROMOCION', 100.00),
(8, 'Media Deportiva', 300.00, 'MEDIA', 'NUEVA', NULL);

INSERT IGNORE INTO clientes (cli_id, cli_nombre, cli_apellido) VALUES
(1, 'Juan', 'Perez'),
(2, 'Maria', 'Gomez'),
(3, 'Carlos', 'Lopez');

INSERT IGNORE INTO negocios (neg_id, neg_nombre) VALUES
(1, 'Tienda Ropita Central');
