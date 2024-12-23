package org.springdemo.proyecto2.service;

import org.springdemo.proyecto2.model.Alumno;

import java.util.List;

public interface AlumnoService {

    // Obtener todos los alumnos
    List<Alumno> obtenerTodosLosAlumnos();

    // Obtener un alumno por su ID
    Alumno obtenerAlumnoPorId(Long id);

    // Guardar un nuevo alumno
    Alumno guardarAlumno(Alumno alumno);

    // Actualizar un alumno existente
    Alumno actualizarAlumno(Long id, Alumno alumno);

    // Eliminar un alumno por su ID
    void eliminarAlumno(Long id);

    // Obtener alumnos por curso
    List<Alumno> obtenerAlumnosPorCurso(String curso);

    // Obtener alumnos por rango de edades
    List<Alumno> obtenerAlumnosPorRangoEdad(int edadMinima, int edadMaxima);
}
