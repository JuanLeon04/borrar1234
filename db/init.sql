CREATE DATABASE IF NOT EXISTS cafeteria;
USE cafeteria;

DROP TABLE IF EXISTS menu_semanal;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE menu_semanal (
  id INT AUTO_INCREMENT PRIMARY KEY,
  dia ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES') NOT NULL,
  nombre_plato VARCHAR(150) NOT NULL DEFAULT '',
  descripcion_plato TEXT NOT NULL,
  imagen_plato VARCHAR(255) DEFAULT '',
  disponibilidad_plato INT NOT NULL DEFAULT 0,
  precio DOUBLE NOT NULL DEFAULT 0.0,
  hora_limite_cancelacion VARCHAR(10) NOT NULL DEFAULT ''
);

INSERT INTO menu_semanal (dia, nombre_plato, descripcion_plato, imagen_plato, disponibilidad_plato, precio, hora_limite_cancelacion)
VALUES
  ('LUNES', '', '', '', 0, 0.0, ''),
  ('MARTES', '', '', '', 0, 0.0, ''),
  ('MIERCOLES', '', '', '', 0, 0.0, ''),
  ('JUEVES', '', '', '', 0, 0.0, ''),
  ('VIERNES', '', '', '', 0, 0.0, '');

CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  codigo_estudiante VARCHAR(50) DEFAULT '',
  correo VARCHAR(100) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL,
  rol ENUM('ADMIN', 'ESTUDIANTE') NOT NULL
);

INSERT INTO usuarios (nombre, apellidos, codigo_estudiante, correo, contrasena, rol)
VALUES
  ('Admin', 'User', 'N/A', 'a@admin.com', 'admin', 'ADMIN'),
  ('Estudiante', 'User', 'EST123', 'e@estu.com', 'estudiante', 'ESTUDIANTE');