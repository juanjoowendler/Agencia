-- TABLE GENERATIONS (SQLITE)

CREATE TABLE IF NOT EXISTS "Empleados" (
	"LEGAJO"	INTEGER NOT NULL,
	"NOMBRE"	TEXT(30) NOT NULL,
	"APELLIDO"	TEXT(50) NOT NULL,
	"TELEFONO_CONTACTO"	INTEGER NOT NULL,
	PRIMARY KEY("LEGAJO" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Interesados" (
	"ID"	INTEGER NOT NULL,
	"TIPO_DOCUMENTO"	TEXT(3) NOT NULL DEFAULT ('"DNI"'),
	"DOCUMENTO"	TEXT(10) NOT NULL,
	"NOMBRE"	TEXT(50) NOT NULL,
	"APELLIDO"	TEXT(50) NOT NULL,
	"RESTRINGIDO"	BOOLEAN NOT NULL DEFAULT (FALSE),
	"NRO_LICENCIA"	INTEGER NOT NULL UNIQUE,
	"FECHA_VENCIMIENTO_LICENCIA"	TIMESTAMP NOT NULL,
	PRIMARY KEY("ID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Marcas" (
	"ID"	INTEGER NOT NULL,
	"NOMBRE"	TEXT(30) NOT NULL,
	PRIMARY KEY("ID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Modelos" (
	"ID"	INTEGER NOT NULL,
	"ID_MARCA"	INTEGER NOT NULL,
	"DESCRIPCION"	TEXT NOT NULL,
	PRIMARY KEY("ID" AUTOINCREMENT),
	CONSTRAINT "Modelo_Marca_FK" FOREIGN KEY("ID_MARCA") REFERENCES "Marcas"("ID")
);
CREATE TABLE IF NOT EXISTS "Notificaciones" (
	"ID"	INTEGER NOT NULL,
	"LEGAJO"	INTEGER NOT NULL,
	"DESCRIPCION"	TEXT,
	PRIMARY KEY("ID" AUTOINCREMENT),
	CONSTRAINT "Notificaciones_Empleados_FK" FOREIGN KEY("LEGAJO") REFERENCES "Empleados"("LEGAJO")
);
CREATE TABLE IF NOT EXISTS "Posiciones" (
	"ID"	INTEGER NOT NULL,
	"ID_VEHICULO"	INTEGER NOT NULL,
	"FECHA_HORA"	TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	"LATITUD"	REAL NOT NULL,
	"LONGITUD"	REAL NOT NULL,
	PRIMARY KEY("ID" AUTOINCREMENT),
	CONSTRAINT "Posiciones_Vehiculos_FK" FOREIGN KEY("ID_VEHICULO") REFERENCES "Vehiculos"("ID")
);
CREATE TABLE IF NOT EXISTS "Pruebas" (
	"ID"	INTEGER NOT NULL,
	"ID_VEHICULO"	INTEGER NOT NULL,
	"ID_INTERESADO"	INTEGER NOT NULL,
	"ID_EMPLEADO"	INTEGER NOT NULL,
	"FECHA_HORA_INICIO"	TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	"FECHA_HORA_FIN"	TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	"COMENTARIOS"	TEXT(500),
	PRIMARY KEY("ID" AUTOINCREMENT),
	CONSTRAINT "Pruebas_Empleados_FK" FOREIGN KEY("ID_EMPLEADO") REFERENCES "Empleados"("LEGAJO"),
	CONSTRAINT "Pruebas_Interesados_FK" FOREIGN KEY("ID_INTERESADO") REFERENCES "Interesados"("ID"),
	CONSTRAINT "Pruebas_Vehiculos_FK" FOREIGN KEY("ID_VEHICULO") REFERENCES "Vehiculos"("ID")
);
CREATE TABLE IF NOT EXISTS "Vehiculos" (
	"ID"	INTEGER NOT NULL,
	"PATENTE"	TEXT NOT NULL UNIQUE,
	"ID_MODELO"	INTEGER NOT NULL,
	"ANIO"	INTEGER NOT NULL DEFAULT (2019),
	PRIMARY KEY("ID" AUTOINCREMENT),
	CONSTRAINT "Vehiculos_Modelos_FK" FOREIGN KEY("ID_MODELO") REFERENCES "Modelos"("ID")
);

-- DIAGRAM GENERATOR (https://dbdiagram.io/d/agencia-diagram-671fd31097a66db9a38ce7b6)

// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table Empleados {
  LEGAJO integer [primary key]
  NOMBRE varchar(30)
  APELLIDO varchar(50)
  TELEFONO_CONTACTO integer
}

Table Interesados {
  ID integer [primary key]
  TIPO_DOCUMENTO varchar(3) [default: 'DNI']
  DOCUMENTO varchar(10)
  NOMBRE varchar(50)
  APELLIDO varchar(50)
  RESTRINGIDO boolean [default: false]
  NRO_LICENCIA integer [unique]
  FECHA_VENCIMIENTO_LICENCIA timestamp
}

Table Marcas {
  ID integer [primary key]
  NOMBRE varchar(30)
}

Table Modelos {
  ID integer [primary key]
  ID_MARCA integer
  DESCRIPCION text
}

Table Notificaciones {
  ID integer [primary key]
  LEGAJO integer
  DESCRIPCION text
}

Table Posiciones {
  ID integer [primary key]
  ID_VEHICULO integer
  FECHA_HORA timestamp [default: 'CURRENT_TIMESTAMP']
  LATITUD real
  LONGITUD real
}

Table Pruebas {
  ID integer [primary key]
  ID_VEHICULO integer
  ID_INTERESADO integer
  ID_EMPLEADO integer
  FECHA_HORA_INICIO timestamp [default: 'CURRENT_TIMESTAMP']
  FECHA_HORA_FIN timestamp [default: 'CURRENT_TIMESTAMP']
  COMENTARIOS text(500)
}

Table Vehiculos {
  ID integer [primary key]
  PATENTE varchar
  ID_MODELO integer
  ANIO integer [default: 2019]
}

// Define the relationships between tables
Ref: Notificaciones.LEGAJO > Empleados.LEGAJO // many-to-one
Ref: Modelos.ID_MARCA > Marcas.ID // many-to-one
Ref: Posiciones.ID_VEHICULO > Vehiculos.ID // many-to-one
Ref: Pruebas.ID_EMPLEADO > Empleados.LEGAJO // many-to-one
Ref: Pruebas.ID_INTERESADO > Interesados.ID // many-to-one
Ref: Pruebas.ID_VEHICULO > Vehiculos.ID // many-to-one
Ref: Vehiculos.ID_MODELO > Modelos.ID // many-to-one
