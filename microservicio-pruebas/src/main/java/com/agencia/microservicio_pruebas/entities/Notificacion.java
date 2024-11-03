package com.agencia.microservicio_pruebas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @ToString.Exclude
    @OneToMany
    private List<Empleado> empleados;
}
