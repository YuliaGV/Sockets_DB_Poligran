CREATE TABLE PAISES (
    pais_ID INT AUTO_INCREMENT PRIMARY KEY,
    pais_nombre VARCHAR(50)
);

CREATE TABLE CIUDADES (
    ciud_ID INT AUTO_INCREMENT PRIMARY KEY,
    ciud_nombre VARCHAR(50),
    ciud_pais_ID INT,
    FOREIGN KEY (ciud_pais_ID) REFERENCES PAISES(pais_ID)
);

CREATE TABLE LOCALIZACIONES (
    localiz_ID INT AUTO_INCREMENT PRIMARY KEY,
    localiz_direccion VARCHAR(100),
    localiz_ciudad_ID INT,
    FOREIGN KEY (localiz_ciudad_ID) REFERENCES CIUDADES(ciud_ID)
);

CREATE TABLE DEPARTAMENTOS (
    dpto_ID INT AUTO_INCREMENT PRIMARY KEY,
    dpto_nombre VARCHAR(50),
    dpto_localiz_ID INT,
    FOREIGN KEY (dpto_localiz_ID) REFERENCES LOCALIZACIONES(localiz_ID)
);

CREATE TABLE CARGOS (
    cargo_ID INT AUTO_INCREMENT PRIMARY KEY,
    cargo_nombre VARCHAR(50),
    cargo_sueldo_minimo DECIMAL(10,2),
    cargo_sueldo_maximo DECIMAL(10,2)
);

CREATE TABLE EMPLEADOS (
    empl_ID INT AUTO_INCREMENT PRIMARY KEY,
    empl_nombre VARCHAR(50),
    empl_apellido VARCHAR(50),
    empl_email VARCHAR(50),
    empl_fecha_nac DATE,
    empl_sueldo DECIMAL(10,2),
    empl_comision DECIMAL(10,2),
    empl_cargo_ID INT,
    empl_gerente_ID INT NULL,
    empl_dpto_ID INT,
    FOREIGN KEY (empl_cargo_ID) REFERENCES CARGOS(cargo_ID),
    FOREIGN KEY (empl_dpto_ID) REFERENCES DEPARTAMENTOS(dpto_ID)
);

CREATE TABLE HISTORICO (
    emphist_ID INT AUTO_INCREMENT PRIMARY KEY,
    emphist_fecha_retiro DATE,
    emphist_cargo_ID INT,
    emphist_dpto_ID INT,
    FOREIGN KEY (emphist_cargo_ID) REFERENCES CARGOS(cargo_ID),
    FOREIGN KEY (emphist_dpto_ID) REFERENCES DEPARTAMENTOS(dpto_ID)
);

CREATE INDEX idx_ciudades_pais
ON CIUDADES (ciud_pais_ID);

CREATE INDEX idx_localiz_ciudad
ON LOCALIZACIONES (localiz_ciudad_ID);

CREATE INDEX idx_dptos_localiz
ON DEPARTAMENTOS (dpto_localiz_ID);

CREATE INDEX idx_empleados_cargo
ON EMPLEADOS (empl_cargo_ID);

CREATE INDEX idx_empleados_dpto
ON EMPLEADOS (empl_dpto_ID);

CREATE INDEX idx_historico_cargo
ON HISTORICO (emphist_cargo_ID);

CREATE INDEX idx_historico_dpto
ON HISTORICO (emphist_dpto_ID);
