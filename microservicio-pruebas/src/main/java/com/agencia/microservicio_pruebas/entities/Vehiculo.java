package com.agencia.microservicio_pruebas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PATENTE", unique = true)
    private String patente;

    @Column(name = "ANIO")
    private int anio;

    @ManyToOne
    @JoinColumn(name = "ID_MODELO", nullable = false)
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "ID_POSICION", nullable = false)
    private Posicion posicion;
}