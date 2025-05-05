DROP DATABASE IF EXISTS Nimesuki;
CREATE DATABASE IF NOT EXISTS Nimesuki;
USE Nimesuki;

CREATE TABLE Anime (
  id_anime INT PRIMARY KEY auto_increment, 
  nombre VARCHAR(50) NOT NULL,
  categorias VARCHAR(150),
  anho_salida INT,
  descripcion VARCHAR(1000) NOT NULL,
  imagen VARCHAR(100),
  cap_totales INT NOT NULL
);

CREATE TABLE Usuario (
  id_usuario INT PRIMARY KEY auto_increment, 
  nombre VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  contrasenha VARCHAR(60) NOT NULL,
  tipo ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
);

CREATE TABLE Favoritos (
	id_anime_FK INT,
  id_usuario_FK INT,
  valoracion INT,       
  cap_actual INT,        
  PRIMARY KEY (id_usuario_FK, id_anime_FK),
	FOREIGN KEY (id_anime_FK) REFERENCES anime(id_anime),
  FOREIGN KEY (id_usuario_FK) REFERENCES usuario(id_usuario)
);