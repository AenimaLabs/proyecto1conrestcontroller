package org.springdemo.proyecto2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdemo.proyecto2.model.Alumno;
import org.springdemo.proyecto2.repository.AlumnoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceImplTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    private Alumno alumnoTest;
    private List<Alumno> alumnosList;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        alumnoTest = new Alumno();
        alumnoTest.setId(1L);
        alumnoTest.setNombre("Juan");
        alumnoTest.setApellido("Pérez");
        alumnoTest.setEdad(20);
        alumnoTest.setCurso("Matemáticas");

        alumnosList = Arrays.asList(
                alumnoTest,
                new Alumno(2L, "María", "García", 22, "Física"),
                new Alumno(3L, "Pedro", "López", 19, "Matemáticas")
        );
    }

    @Test
    void obtenerTodosLosAlumnos_DebeRetornarListaDeAlumnos() {
        // Given
        when(alumnoRepository.findAll()).thenReturn(alumnosList);

        // When
        List<Alumno> resultado = alumnoService.obtenerTodosLosAlumnos();

        // Then
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(alumnoRepository).findAll();
    }

    @Test
    void obtenerAlumnoPorId_CuandoExisteAlumno_DebeRetornarAlumno() {
        // Given
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumnoTest));

        // When
        Alumno resultado = alumnoService.obtenerAlumnoPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        verify(alumnoRepository).findById(1L);
    }

    @Test
    void obtenerAlumnoPorId_CuandoNoExisteAlumno_DebeLanzarExcepcion() {
        // Given
        when(alumnoRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alumnoService.obtenerAlumnoPorId(99L);
        });

        assertEquals("Alumno no encontrado con ID: 99", exception.getMessage());
        verify(alumnoRepository).findById(99L);
    }

    @Test
    void guardarAlumno_DebeRetornarAlumnoGuardado() {
        // Given
        Alumno nuevoAlumno = new Alumno(null, "Nuevo", "Alumno", 21, "Historia");
        Alumno alumnoGuardado = new Alumno(4L, "Nuevo", "Alumno", 21, "Historia");
        when(alumnoRepository.save(nuevoAlumno)).thenReturn(alumnoGuardado);

        // When
        Alumno resultado = alumnoService.guardarAlumno(nuevoAlumno);

        // Then
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Nuevo", resultado.getNombre());
        verify(alumnoRepository).save(nuevoAlumno);
    }

    @Test
    void actualizarAlumno_CuandoExisteAlumno_DebeActualizarYRetornarAlumno() {
        // Given
        Long id = 1L;
        Alumno alumnoActualizado = new Alumno(id, "Juan Actualizado", "Pérez", 21, "Matemáticas");
        when(alumnoRepository.existsById(id)).thenReturn(true);
        when(alumnoRepository.save(alumnoActualizado)).thenReturn(alumnoActualizado);

        // When
        Alumno resultado = alumnoService.actualizarAlumno(id, alumnoActualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        verify(alumnoRepository).existsById(id);
        verify(alumnoRepository).save(alumnoActualizado);
    }

    @Test
    void actualizarAlumno_CuandoNoExisteAlumno_DebeLanzarExcepcion() {
        // Given
        Long id = 99L;
        Alumno alumnoInexistente = new Alumno(id, "Test", "Test", 20, "Test");
        when(alumnoRepository.existsById(id)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alumnoService.actualizarAlumno(id, alumnoInexistente);
        });

        assertEquals("No se puede actualizar. Alumno no encontrado con ID: 99", exception.getMessage());
        verify(alumnoRepository).existsById(id);

        ArgumentCaptor<Alumno> alumnoCaptor = ArgumentCaptor.forClass(Alumno.class);
        verify(alumnoRepository, never()).save(alumnoCaptor.capture());
    }

    @Test
    void eliminarAlumno_CuandoExisteAlumno_DebeEliminarAlumno() {
        // Given
        Long id = 1L;
        when(alumnoRepository.existsById(id)).thenReturn(true);
        doNothing().when(alumnoRepository).deleteById(id);

        // When
        alumnoService.eliminarAlumno(id);

        // Then
        verify(alumnoRepository).existsById(id);
        verify(alumnoRepository).deleteById(id);
    }

    @Test
    void eliminarAlumno_CuandoNoExisteAlumno_DebeLanzarExcepcion() {
        // Given
        Long id = 99L;
        when(alumnoRepository.existsById(id)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alumnoService.eliminarAlumno(id);
        });

        assertEquals("No se pude eliminar. Alumno no encontrado por ID: 99", exception.getMessage());
        verify(alumnoRepository).existsById(id);
        verify(alumnoRepository, never()).deleteById(anyLong());
    }

    @Test
    void obtenerAlumnosPorCurso_DebeRetornarListaDeAlumnosDelCurso() {
        // Given
        String curso = "Matemáticas";
        List<Alumno> alumnosMatematicas = alumnosList.stream()
                                                     .filter(a -> a.getCurso().equals(curso))
                                                     .collect(Collectors.toList());
        when(alumnoRepository.findAlumnosByCursoQuery(curso)).thenReturn(alumnosMatematicas);

        // When
        List<Alumno> resultado = alumnoService.obtenerAlumnosPorCurso(curso);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(a -> a.getCurso().equals(curso)));
        verify(alumnoRepository).findAlumnosByCursoQuery(curso);
    }

    @Test
    void obtenerAlumnosPorRangoEdad_DebeRetornarListaDeAlumnosEnRango() {
        // Given
        int edadMinima = 19;
        int edadMaxima = 21;
        List<Alumno> alumnosEnRango = alumnosList.stream()
                                                 .filter(a -> a.getEdad() >= edadMinima && a.getEdad() <= edadMaxima)
                                                 .collect(Collectors.toList());
        when(alumnoRepository.findAlumnosByRangoEdad(edadMinima, edadMaxima)).thenReturn(alumnosEnRango);

        // When
        List<Alumno> resultado = alumnoService.obtenerAlumnosPorRangoEdad(edadMinima, edadMaxima);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(a -> a.getEdad() >= edadMinima && a.getEdad() <= edadMaxima));
        verify(alumnoRepository).findAlumnosByRangoEdad(edadMinima, edadMaxima);
    }

}