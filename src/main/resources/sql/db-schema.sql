-- ==========================
-- Tablas básicas
-- ==========================

DROP TABLE IF EXISTS GENERO;
DROP TABLE IF EXISTS DIRECTOR;
DROP TABLE IF EXISTS PELICULA;

CREATE TABLE GENERO (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE DIRECTOR (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE PELICULA (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          titulo VARCHAR(200) NOT NULL,
                          fecha_estreno DATE NOT NULL,
                          precio_base_alquiler DOUBLE NOT NULL,
                          clasificacion VARCHAR(20) NOT NULL,  -- Enum mapeado como STRING
                          genero_id BIGINT NOT NULL,
                          director_id BIGINT NOT NULL,
                          CONSTRAINT ck_pelicula_precio_gt0 CHECK (precio_base_alquiler > 0),
                          CONSTRAINT fk_pelicula_genero FOREIGN KEY (genero_id) REFERENCES genero(id),
                          CONSTRAINT fk_pelicula_director FOREIGN KEY (director_id) REFERENCES director(id)
);

CREATE INDEX idx_pelicula_genero ON pelicula (genero_id);
CREATE INDEX idx_pelicula_director ON pelicula (director_id);
CREATE INDEX idx_pelicula_titulo ON pelicula (titulo);
