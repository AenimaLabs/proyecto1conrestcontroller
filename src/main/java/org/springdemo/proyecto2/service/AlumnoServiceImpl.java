package org.springdemo.proyecto2.service;

import org.springdemo.proyecto2.model.Alumno;
import org.springdemo.proyecto2.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Alumno obtenerAlumnoPorId(Long id) {
        Optional<Alumno> alumnoOptional = alumnoRepository.findById(id);
        if (alumnoOptional.isPresent()) {
            return alumnoOptional.get();
        } else {
            throw new RuntimeException("Alumno no encontrado con ID: " + id);
        }
    }

    @Override
    @Transactional
    public Alumno guardarAlumno(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    @Override
    @Transactional
    public Alumno actualizarAlumno(Long id, Alumno alumno) {
        if (alumnoRepository.existsById(id)) {
            alumno.setId(id);
            return alumnoRepository.save(alumno);
        } else {
            throw new RuntimeException("No se puede actualizar. Alumno no encontrado con ID: " + id);
        }
    }

    @Override
    @Transactional
    public void eliminarAlumno(Long id) {
        if (alumnoRepository.existsById(id)){
            alumnoRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pude eliminar. Alumno no encontrado por ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> obtenerAlumnosPorCurso(String curso) {
        return alumnoRepository.findAlumnosByCursoQuery(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> obtenerAlumnosPorRangoEdad(int edadMinima, int edadMaxima) {
        return alumnoRepository.findAlumnosByRangoEdad(edadMinima,edadMaxima);
    }
}
