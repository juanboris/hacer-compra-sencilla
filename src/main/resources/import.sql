/*CREATE EXTENSION IF NOT EXISTS unaccent;

/*Datos de prueba*/
INSERT INTO usuarios (username, password, enabled) VALUES ('boris', '$2a$10$pB/.tyUwY835OvZQoYCAA.OATkf9d4GzJvKilwJAIevIOYZpwG4Jm', true);
INSERT INTO usuarios (username, password, enabled) VALUES ('admin', '$2a$10$L6Be9tZubezdNwI7aNIE1u4j1XQxRk8kBxt6QjRe9yJiZ7V9BegGO', true);

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, roles_id) VALUES(1,1);
INSERT INTO usuarios_roles (usuario_id, roles_id) VALUES(2,2);
INSERT INTO usuarios_roles (usuario_id, roles_id) VALUES(2,1);

INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Gel', 4.2, 'Lactovit', 'Baño', '2021-02-25', '2021-02-25', 4.2, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Pepinos', 2.5, 'Verdura', '2021-02-25', '2021-02-25', 2.5, 1);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Cacao en polvo', 3.4, 'Cola Cao', 'Procesado', '2021-02-25', '2021-02-25', 3.4, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Kiwi', 4.2, 'Fruta', '2021-02-25', '2021-02-25', 4.2, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Tomates', 1.2, 'Verdura', '2021-02-25', '2021-02-25', 1.2, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Leche de coco', 4.2, 'Lácteo', '2021-02-25', '2021-02-25',4.2, 1);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Pasta', 1.2, 'Rummo', 'Pasta', '2021-02-25', '2021-02-25', 1.2, 1);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Champú', 4.2, 'Lactovit', 'Baño', '2021-02-25', '2021-02-25', 4.2, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Cola', 2.5, 'Verdura', '2021-02-25', '2021-02-25', 2.5, 1);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Horchata', 3.4, 'Cola Cao', 'Procesado', '2021-02-25', '2021-02-25', 3.4, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Ternera', 4.2, 'Fruta', '2021-02-25', '2021-02-25', 4.2, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Pollo', 1.2, 'Verdura', '2021-02-25', '2021-02-25', 1.2, 1);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Curry', 4.2, 'Lácteo', '2021-02-25', '2021-02-25', 4.2, 1);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Guano', 1.2, 'Rummo', 'Pasta', '2021-02-25', '2021-02-25', 1.2, 1);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Rata', 4.2, 'Lactovit', 'Baño', '2021-02-25', '2021-02-25', 4.2, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Queso', 2.5, 'Verduras', '2021-02-25', '2021-02-25', 2.5, 2);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Berenjenas', 3.4, 'Cola Cao', 'Procesados', '2021-02-25', '2021-02-25', 3.4, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Plátanos', 4.2, 'Fruta', '2021-02-25', '2021-02-25', 4.2, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Pimentón', 1.2, 'Verduras', '2021-02-25', '2021-02-25', 1.2, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Lechuga', 4.2, 'Lácteo', '2021-02-25', '2021-02-25', 4.2, 2);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Chocolate', 1.2, 'Rummo', 'Pasta', '2021-02-25', '2021-02-25', 1.2, 2);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Ñora', 4.2, 'Lactovit', 'Baño', '2021-02-25', '2021-02-25', 4.2, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Nata', 2.5, 'Verduras', '2021-02-25', '2021-02-25', 2.5, 2);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Salchichón', 3.4, 'Cola Cao', 'Procesados', '2021-02-25', '2021-02-25', 3.4, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Chorizo', 4.2, 'Fruta', '2021-02-25', '2021-02-25', 4.2, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Sobrasada', 1.2, 'Verduras', '2021-02-25', '2021-02-25', 1.2, 2);
INSERT INTO productos (nombre, precio, tipo, created, modified, media_precio, usuario_id) VALUES ('Pera', 4.2, 'Lácteo', '2021-02-25', '2021-02-25', 4.2, 2);
INSERT INTO productos (nombre, precio, marca, tipo, created, modified, media_precio, usuario_id) VALUES ('Patata', 1.2, 'Rummo', 'Pasta', '2021-02-25', '2021-02-25', 1.2, 2);

INSERT INTO recetas(id, created, descripcion, modified, nombre, tiempo, videourl, usuario_id) VALUES (1, '2021-02-25', 'Haces las lentejas y le echas arroz', '2021-02-25', 'Arroz con lentejas', 45, 'https://www.youtube.com/watch?v=m80Svtg84Do', 1);
INSERT INTO recetas(id, created, descripcion, modified, nombre, tiempo, videourl, usuario_id) VALUES (2, '2021-02-25', 'Haces las habichuelas y le echas chorizo', '2021-02-25', 'Habichuelas con chorizo', 35, 'https://www.youtube.com/watch?v=OtgexJy8AP0', 2);
INSERT INTO recetas(id, created, descripcion, modified, nombre, tiempo, videourl, usuario_id) VALUES (3, '2021-02-25', 'Haces el curry y le echas pollo', '2021-02-25', 'Pollo al curry', 62, 'https://www.youtube.com/watch?v=qaoKiH_2Fzo', 2);
INSERT INTO recetas(id, created, descripcion, modified, nombre, tiempo, videourl, usuario_id) VALUES (4, '2021-02-25', 'Salmón a la plancha y listo', '2021-02-25', 'Salmón', 15, 'https://www.youtube.com/watch?v=m80Svtg84Do', 1);
INSERT INTO recetas(id, created, descripcion, modified, nombre, tiempo, videourl, usuario_id) VALUES (5, '2021-02-25', 'Bates los huevos', '2021-02-25', 'Tortilla de patatas', 55, 'https://www.youtube.com/watch?v=OtgexJy8AP0', 1);
INSERT INTO recetas(id, created, descripcion, modified, nombre, tiempo, videourl, usuario_id) VALUES (6, '2021-02-25', 'Bates los garbanzos y le echas tahir', '2021-02-25', 'Humus', 20, 'https://www.youtube.com/watch?v=qaoKiH_2Fzo', 2);

INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (1, 1, 1);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (1, 2, 2);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (1, 3, 3);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (2, 4, 5);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (2, 5, 6);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (2, 6, 7);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (7, 3, 8);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (8, 2, 3);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (9, 4,4);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (4, 4,15);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (4, 5,2);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (7, 5, 20);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (14, 6, 5);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (17, 5,8);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (16, 1,2);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (45, 2,1);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (23, 3,4);
INSERT INTO producto_receta (cantidad, receta_id, producto_id) VALUES (21, 1,5);

INSERT INTO lista_com (id, created, modified, precio, usuario_id) VALUES (1, '2021-02-25', '2021-02-26', 20.1, 1);
INSERT INTO lista_com (id, created, modified, precio, usuario_id) VALUES (2, '2021-03-01', '2021-03-03', 11.1, 1);
INSERT INTO lista_com (id, created, modified, precio, usuario_id) VALUES (3, '2021-04-12', '2021-04-15', 16.2, 1);
INSERT INTO lista_com (id, created, modified, precio, usuario_id) VALUES (4, '2021-02-12', '2021-02-14', 42.9, 2);
INSERT INTO lista_com (id, created, modified, precio, usuario_id) VALUES (5, '2019-06-30', '2019-07-02', 37.5, 2);
INSERT INTO lista_com (id, created, modified, precio, usuario_id) VALUES (6, '2002-02-25', '2002-03-26', 19.8, 2);

INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (1,1,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (1,2,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (2,5,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (2,2,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (3,7,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (3,4,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (4,3,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (4,1,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (4,8,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (4,9,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (5,12,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (5,3,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (5,15,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (5,5,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (5,2,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (6,8,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (6,12,3);
INSERT INTO lista_com_producto (lista_comp_id, producto_id, cantidad) VALUES (6,7,3);
*/