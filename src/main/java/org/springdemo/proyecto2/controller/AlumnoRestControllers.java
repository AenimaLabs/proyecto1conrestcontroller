package org.springdemo.proyecto2.controller;

import org.springdemo.proyecto2.model.Alumno;
import org.springdemo.proyecto2.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoRestControllers {

    //Inyección del servicio de alumnos
    @Autowired
    private AlumnoService alumnoService;

    // Manejador global de excepciones para este controlador
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleNotFoundException(RuntimeException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return error;
    }

    //Obtiene todos los alumnos
    @GetMapping
    public ResponseEntity<List<Alumno>> obtenerTodosLosAlumnos() {
        try {
            List<Alumno> alumnos = alumnoService.obtenerTodosLosAlumnos();
            if (alumnos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Obtiene un alumno por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAlumnoPorId(@PathVariable Long id) {
        try {
            Alumno alumno = alumnoService.obtenerAlumnoPorId(id);
            return ResponseEntity.ok(alumno);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "No se encontró el alumno con ID: " + id);
            response.put("detalles", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //Crea un nuevo alumno
    @PostMapping
    public ResponseEntity<Object> crearAlumno(@RequestBody Alumno alumno) {
        try {
            Alumno nuevoAlumno = alumnoService.guardarAlumno(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al crear el alumno");
            response.put("detalles", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //Actualizar un alumno existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarAlumno(@PathVariable Long id, @RequestBody Alumno alumno) {
        try {
            Alumno alumnoActualizado = alumnoService.actualizarAlumno(id, alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al actualizar el alumno");
            response.put("detalles", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //Elimina un alumno
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAlumno(@PathVariable Long id) {
        try {
            alumnoService.eliminarAlumno(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al eliminar el alumno");
            response.put("detalles", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    //Busca un alumno por curso
    @GetMapping("/curso/{curso}")
    public ResponseEntity<Object> obtenerAlumnosPorCurso(@PathVariable String curso) {
        try {
            List<Alumno> alumnos = alumnoService.obtenerAlumnosPorCurso(curso);
            if (alumnos.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron alumnos en el curso: " + curso);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Buscar alumnos por rango de edad
    @GetMapping("/edad/{edadMinima}-{edadMaxima}")
    public ResponseEntity<Object> obtenerAlumnosPorRangoEdad(
            @PathVariable int edadMinima,
            @PathVariable int edadMaxima) {
        try {
            List<Alumno> alumnos = alumnoService.obtenerAlumnosPorRangoEdad(edadMinima, edadMaxima);
            if (alumnos.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron alumnos en el rango de edad: " + edadMinima + " - " + edadMaxima);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
