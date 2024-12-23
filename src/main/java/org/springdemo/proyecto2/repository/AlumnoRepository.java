package org.springdemo.proyecto2.repository;

import org.springdemo.proyecto2.model.Alumno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @Query(value = "SELECT * FROM alumnos", nativeQuery = true)
    List<Alumno> findAllAlumnosQuery();

    @Query(value = "SELECT * FROM alumnos WHERE id = :id", nativeQuery = true)
    Alumno findAlumnoByIdQuery(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO alumnos (nombre, apellido, edad, curso) VALUES (:nombre, :apellido, :edad, :curso)", nativeQuery = true)
    int saveAlumnoQuery(@Param("nombre") String nombre, @Param("apellido") String apellido,
                        @Param("edad") int edad, @Param("curso") String curso);

    @Modifying
    @Transactional
    @Query(value = "UPDATE alumnos SET nombre = :nombre, apellido = :apellido, edad = :edad, curso = :curso WHERE id = :id", nativeQuery = true)
    void updateAlumnoQuery(@Param("id") Long id, @Param("nombre") String nombre,
                           @Param("apellido") String apellido, @Param("edad") int edad,
                           @Param("curso") String curso);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM alumnos WHERE id = :id", nativeQuery = true)
    void deleteAlumnoByIdQuery(@Param("id") Long id);

    @Query(value = "SELECT * FROM alumnos WHERE curso = :curso", nativeQuery = true)
    List<Alumno> findAlumnosByCursoQuery(@Param("curso") String curso);

    @Query(value = "SELECT * FROM alumnos WHERE edad BETWEEN :edadMinima AND :edadMaxima", nativeQuery = true)
    List<Alumno> findAlumnosByRangoEdad(@Param("edadMinima") int edadMinima,
                                        @Param("edadMaxima") int edadMaxima);

}
