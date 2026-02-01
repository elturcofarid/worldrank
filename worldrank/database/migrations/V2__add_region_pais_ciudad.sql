-- ========================================================
-- Script de migración:添加 país y ciudad con sistema de regiones
-- Fecha: 2026-02-01
-- ========================================================

-- 1. Añadir columnas pais y ciudad a la tabla lugar
-- ========================================================
ALTER TABLE lugar ADD COLUMN IF NOT EXISTS pais VARCHAR(255);
ALTER TABLE lugar ADD COLUMN IF NOT EXISTS ciudad VARCHAR(255);

-- Crear índices para búsquedas rápidas
CREATE INDEX IF NOT EXISTS idx_lugar_pais ON lugar(pais);
CREATE INDEX IF NOT EXISTS idx_lugar_ciudad ON lugar(ciudad);

-- 2. Crear tabla de regiones (países y ciudades)
-- ========================================================
CREATE TABLE IF NOT EXISTS region (
    id_region UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('PAIS', 'CIUDAD')),
    codigo_iso VARCHAR(10),
    id_pais UUID REFERENCES region(id_region),
    centro geography(Point, 4326),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para regiones
CREATE INDEX IF NOT EXISTS idx_region_nombre ON region(nombre);
CREATE INDEX IF NOT EXISTS idx_region_tipo ON region(tipo);
CREATE INDEX IF NOT EXISTS idx_region_codigo_iso ON region(codigo_iso);
CREATE INDEX IF NOT EXISTS idx_region_id_pais ON region(id_pais);

-- 3. Crear tabla de tracking de regiones visitadas por usuario
-- ========================================================
CREATE TABLE IF NOT EXISTS usuario_region (
    id_usuario_region UUID PRIMARY KEY(),
    id_us DEFAULT gen_random_uuiduario UUID NOT NULL REFERENCES usuario(id_usuario),
    id_region UUID NOT NULL REFERENCES region(id_region),
    primera_visita TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    puntos_obtenidos INTEGER NOT NULL DEFAULT 0,
    -- Unique constraint para evitar registros duplicados
    UNIQUE(id_usuario, id_region)
);

-- Índices para usuario_region
CREATE INDEX IF NOT EXISTS idx_usuario_region_usuario ON usuario_region(id_usuario);
CREATE INDEX IF NOT EXISTS idx_usuario_region_region ON usuario_region(id_region);
CREATE INDEX IF NOT EXISTS idx_usuario_region_tipo ON usuario_region(id_region, region.tipo);

-- 4. Funciones útiles (opcional)
-- ========================================================

-- Función para contar países visitados por usuario
CREATE OR REPLACE FUNCTION fn_paises_visitados(uid UUID)
RETURNS INTEGER AS $$
BEGIN
    RETURN (
        SELECT COUNT(DISTINCT r.id_region)
        FROM usuario_region ur
        JOIN region r ON ur.id_region = r.id_region
        WHERE ur.id_usuario = uid AND r.tipo = 'PAIS'
    );
END;
$$ LANGUAGE plpgsql;

-- Función para contar ciudades visitadas por usuario
CREATE OR REPLACE FUNCTION fn_ciudades_visitadas(uid UUID)
RETURNS INTEGER AS $$
BEGIN
    RETURN (
        SELECT COUNT(DISTINCT r.id_region)
        FROM usuario_region ur
        JOIN region r ON ur.id_region = r.id_region
        WHERE ur.id_usuario = uid AND r.tipo = 'CIUDAD'
    );
END;
$$ LANGUAGE plpgsql;

-- 5. Datos de ejemplo (opcional - para testing)
-- ========================================================
-- INSERT INTO region (nombre, tipo, codigo_iso) VALUES 
--     ('España', 'PAIS', 'es'),
--     ('México', 'PAIS', 'mx'),
--     ('Argentina', 'PAIS', 'ar');

-- NOTA: Estas tablas se crearán automáticamente si JPA/Hibernate está configurado con ddl-auto=update
-- Este script es útil para entornos de producción o si se prefiere control manual de la BD
