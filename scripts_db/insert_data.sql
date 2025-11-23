INSERT INTO paises (pais_nombre) VALUES
('Colombia'),
('México'),
('Perú'),
('Chile'),
('Argentina');

INSERT INTO ciudades (ciud_nombre, ciud_pais_ID) VALUES
('Bogotá', 1),
('Medellín', 1),
('Cali', 1),
('Ciudad de México', 2),
('Guadalajara', 2);

INSERT INTO localizaciones (localiz_direccion, localiz_ciudad_ID) VALUES
('Calle 45 # 99-88', 1),
('Carrera 34 # 31-08', 2),
('Avenida Loquesea # 44-34', 3),
('Av. México 150', 4),
('Av. Dorada 3000', 5);

INSERT INTO departamentos (dpto_nombre, dpto_localiz_ID) VALUES
('Recursos Humanos', 1),
('Finanzas', 2),
('Tecnología', 3),
('Comercial', 4),
('Logística', 5);

INSERT INTO cargos (cargo_nombre, cargo_sueldo_minimo, cargo_sueldo_maximo) VALUES
('Gerente',     5000.00,  9000.00),
('Coordinador', 3500.00,  6000.00),
('Analista',    2500.00,  4000.00),
('Auxiliar',    1500.00,  2500.00),
('Director',    7000.00, 12000.00);

INSERT INTO empleados (
    empl_nombre, empl_apellido, empl_email,
    empl_fecha_nac, empl_sueldo, empl_comision,
    empl_cargo_ID, empl_gerente_ID, empl_dpto_ID
) VALUES
('Shakira', 'Mebarak', 'shakira.mebarak@empresa.com', '1980-02-07', 8500000, 500000, 5, NULL, 1),
('Carlos', 'Vives', 'carlos.vives@empresa.com', '1980-02-06', 5500000, 300000, 1, 1, 2),
('James', 'Rodríguez', 'james.rodriguez@empresa.com', '1980-02-03', 3200000, 150000, 3, 2, 3),
('Karol', 'G', 'karol.g@empresa.com', '1980-02-01', 1800000, 80000, 4, 2, 4),
('Maluma', 'Londoño', 'maluma.londono@empresa.com', '1980-02-04', 4200000, 120000, 2, 1, 5);

INSERT INTO historico (emphist_fecha_retiro, emphist_cargo_ID, emphist_dpto_ID) VALUES
('2022-01-15', 4, 1),
('2021-12-30', 3, 2),
('2020-06-20', 2, 3),
('2023-03-10', 1, 4),
('2023-07-01', 5, 5);