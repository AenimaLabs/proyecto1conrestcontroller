package org.springdemo.proyecto2.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor //constructor vacío sin conflicto
@AllArgsConstructor
@ToString
@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrementar
    private Long id;
    @Column(name = "nombre", length = 50)
    private String nombre;
    @Column(name = "apellido", length = 50)
    private String apellido;
    @Column(name = "edad")
    private int edad;
    @Column(name = "curso", length = 15)
    private String curso;
}
