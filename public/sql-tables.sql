BEGIN TRANSACTION;
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
	"FECHA_HORA"	TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	"LATITUD"	REAL NOT NULL,
	"LONGITUD"	REAL NOT NULL,
	PRIMARY KEY("ID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Pruebas" (
	"ID"	INTEGER NOT NULL,
	"ID_VEHICULO"	INTEGER NOT NULL,
	"ID_INTERESADO"	INTEGER NOT NULL,
	"ID_EMPLEADO"	INTEGER NOT NULL,
	"FECHA_HORA_INICIO"	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"FECHA_HORA_FIN"	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"COMENTARIOS"	TEXT DEFAULT 'Sin Comentarios',
	"ACTIVA"	BOOLEAN NOT NULL DEFAULT FALSE,
	"KM_REGISTRADOS"	TEXT DEFAULT '0',
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
	"ID_POSICION"	INTEGER NOT NULL DEFAULT (1),
	PRIMARY KEY("ID" AUTOINCREMENT),
	CONSTRAINT "Vehiculos_Modelos_FK" FOREIGN KEY("ID_MODELO") REFERENCES "Modelos"("ID"),
	CONSTRAINT "Vehiculos_Posiciones_FK" FOREIGN KEY("ID_POSICION") REFERENCES "Posiciones"("ID")
);
COMMIT;


// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table Empleados {
  LEGAJO integer [primary key, note: 'Auto-increment']
  NOMBRE varchar(30) [not null]
  APELLIDO varchar(50) [not null]
  TELEFONO_CONTACTO integer [not null]
}

Table Interesados {
  ID integer [primary key, note: 'Auto-increment']
  TIPO_DOCUMENTO varchar(3) [default: 'DNI', not null]
  DOCUMENTO varchar(10) [not null]
  NOMBRE varchar(50) [not null]
  APELLIDO varchar(50) [not null]
  RESTRINGIDO boolean [default: false, not null]
  NRO_LICENCIA integer [unique, not null]
  FECHA_VENCIMIENTO_LICENCIA timestamp [not null]
}

Table Marcas {
  ID integer [primary key, note: 'Auto-increment']
  NOMBRE varchar(30) [not null]
}

Table Modelos {
  ID integer [primary key, note: 'Auto-increment']
  ID_MARCA integer [not null]
  DESCRIPCION text [not null]
}

Table Notificaciones {
  ID integer [primary key, note: 'Auto-increment']
  LEGAJO integer [not null]
  DESCRIPCION text
}

Table Posiciones {
  ID integer [primary key, note: 'Auto-increment']
  FECHA_HORA timestamp [default: 'CURRENT_TIMESTAMP', not null]
  LATITUD real [not null]
  LONGITUD real [not null]
}

Table Pruebas {
  ID integer [primary key, note: 'Auto-increment']
  ID_VEHICULO integer [not null]
  ID_INTERESADO integer [not null]
  ID_EMPLEADO integer [not null]
  FECHA_HORA_INICIO timestamp [default: 'CURRENT_TIMESTAMP', not null]
  FECHA_HORA_FIN timestamp [default: 'CURRENT_TIMESTAMP', not null]
  COMENTARIOS text [default: 'Sin Comentarios']
  ACTIVA boolean [default: false, not null]
  KM_REGISTRADOS varchar(10) [default: '0']
}

Table Vehiculos {
  ID integer [primary key, note: 'Auto-increment']
  PATENTE varchar [unique, not null]
  ID_MODELO integer [not null]
  ANIO integer [default: 2019, not null]
  ID_POSICION integer [default: 1, not null]
}

// Define the relationships between tables
Ref: Notificaciones.LEGAJO > Empleados.LEGAJO // many-to-one
Ref: Modelos.ID_MARCA > Marcas.ID // many-to-one
Ref: Pruebas.ID_EMPLEADO > Empleados.LEGAJO // many-to-one
Ref: Pruebas.ID_INTERESADO > Interesados.ID // many-to-one
Ref: Pruebas.ID_VEHICULO > Vehiculos.ID // many-to-one
Ref: Vehiculos.ID_MODELO > Modelos.ID // many-to-one
Ref: Vehiculos.ID_POSICION > Posiciones.ID // many-to-one

