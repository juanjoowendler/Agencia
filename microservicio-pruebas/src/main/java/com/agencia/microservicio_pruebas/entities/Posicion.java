package com.agencia.microservicio_pruebas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Posiciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Posicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FECHA_HORA")
    private String fechaHora;

    @Column(name = "LATITUD")
    private double latitud;

    @Column(name = "LONGITUD")
    private double longitud;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Vehiculo vehiculo;
}