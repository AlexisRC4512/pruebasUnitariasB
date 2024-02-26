package com.pruebasUnitariasB.ApiRest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="empleados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre", nullable = false)
    private String nombre;
    @Column(name="apellido", nullable = false)
    private String apellido;
    @Column(name="email", nullable = false)
    private String email;
}
