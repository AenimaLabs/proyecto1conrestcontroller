package org.springdemo.proyecto2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdemo.proyecto2.model.Alumno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Usa el perfil de configuración para MySQL
class AlumnoRepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    private Alumno alumno1;
    private Alumno alumno2;

    @BeforeEach
    void setUp() {
        alumnoRepository.deleteAll();

        // Crear y guardar el primer alumno
        alumno1 = new Alumno();
        alumno1.setNombre("john");
        alumno1.setApellido("Doe");
        alumno1.setEdad(20);
        alumno1.setCurso("Matematicas");
        alumno1 = alumnoRepository.save(alumno1);  // Guardar y actualizar referencia
        System.out.println(alumno1);

        // Crear y guardar el segundo alumno
        alumno2 = new Alumno();
        alumno2.setNombre("Jane");
        alumno2.setApellido("Smith");
        alumno2.setEdad(22);
        alumno2.setCurso("Ciencias");
        alumno2 = alumnoRepository.save(alumno2);  // Guardar y actualizar referencia
        System.out.println(alumno2);
    }

    @Test
    void testGuardarAlumno() {
        // Crear nuevo alumno
        Alumno alumno = new Alumno();
        alumno.setNombre("Alice");
        alumno.setApellido("Brown");
        alumno.setEdad(19);
        alumno.setCurso("Historia");
        System.out.println(alumno);


        // FUNCION: S save(S entity)
        // S: Es el retorno de la funcion
        // save: es el nombre de la funcion
        // S entity: es el parametro que recibe la funcion para guardar
        Alumno alumnoGuardado = alumnoRepository.save(alumno);

        // Busca que el campo que se le proporcione no sea NULL
        // En otras palabras el atributo id de la entidad alumnoGuardado
        assertNotNull(alumnoGuardado.getId());

        // Busca que el campo de la derecha sea igual al campo de la izquierda.
        // El valor de la izquierda es lo que uno "Espera obtener"
        // El valor de la derecha lo que realmente "Obtenemos"
        assertEquals("Alice", alumnoGuardado.getNombre());

        // Imprimimos todos los datos del objeto alumnoGuardado
        System.out.println(alumnoGuardado);
    }

    @Test
    void testBuscarPorId() {
        // Se utiliza el repositorio para buscar un registro específico en la base de datos por su ID.
        //Devuelve un objeto Optional que puede contener el Alumno encontrado o estar vacío si no existe.
        Optional<Alumno> alumnoEncontrado = alumnoRepository.findById(alumno1.getId());

        // Se asegura de que el Optional contiene un valor (es decir, el Alumno fue encontrado).
        assertTrue(alumnoEncontrado.isPresent());

        // Si el Alumno existe, se extrae con .get() y se compara su nombre con el valor esperado, en este caso, "john".
        assertEquals("john", alumnoEncontrado.get().getNombre());
    }

    @Test
    void testBuscarTodos() {
        // La funcion findAll() retorna todos los registros que apunten a la entidad de su repository

        // findAll() devuelve todos los registros de la tabla alumnos en forma de lista.
        // Esta lista contiene objetos de tipo Alumno que representan cada registro en la base de datos.
        List<Alumno> alumnos = alumnoRepository.findAll();

        // AsserEquals espera que el contenido a la izquierda de la coma sea igual al que esta a la derecha de la coma
        // El valor a la izquierda de la coma o primer parametro es el valor esperado.
        // El valor a la derecha de la coma o segundo parametro es el valor realmente obtenido
        assertEquals(2, alumnos.size());

        // Metodo ForEach utilizado para recorrer listas de datos sin preocuparnos por los indices
        // Comienza en el primer valor o elemento y termina en el ultimo
        for (Alumno alumno : alumnos) {
            System.out.println(alumno);
        }
    }

    @Test
    void testActualizarAlumno() {
        // Buscar alumno existente
        // Buscar alumno existente en la base de datos utilizando su ID.
        // Si existe, el resultado será un Optional que contiene el alumno.
        // Si no existe, el Optional estará vacío.
        Optional<Alumno> optionalAlumno = alumnoRepository.findById(alumno1.getId());
        // Verificar que el Optional contenga un valor (es decir, que el alumno exista en la base de datos).
        // assertTrue espera que la condición dentro del paréntesis sea verdadera.
        assertTrue(optionalAlumno.isPresent());
        // Imprimir en la consola el contenido del Optional.
        // Esto nos muestra el objeto Alumno que se encontró en la base de datos.
        System.out.println(optionalAlumno.get());

        // Actualizar alumno
        // Si llegamos a este punto, significa que el alumno fue encontrado (ya verificamos que el Optional no está vacío).
        // Obtenemos el objeto Alumno desde el Optional utilizando el método get().
        Alumno alumno = optionalAlumno.get();
        // Cambiar el valor del atributo "nombre" del alumno.
        // Aquí estamos simulando una actualización al asignarle un nuevo valor.
        alumno.setNombre("NombreActualizado");
        // Cambiar el valor del atributo "apellido" del alumno.
        // Al igual que con el nombre, se asigna un nuevo valor para simular un cambio.
        alumno.setApellido("ApellidoActualizado");
        // Imprimir en la consola el objeto Alumno actualizado.
        // Esto nos permite verificar visualmente que los valores han cambiado correctamente.
        System.out.println(alumno);


        // Se guarda en la base de datos la copia del original obtenido posterior a haber sido modificado
        Alumno alumnoActualizado = alumnoRepository.save(alumno);
        System.out.println(alumnoActualizado);

        // Realizamps comprobaciones de que el nombre y apellido de la copia guardada sean los esperados
        assertEquals("NombreActualizado", alumnoActualizado.getNombre());
        assertEquals("ApellidoActualizado", alumnoActualizado.getApellido());


        // Obtenemos nuevamente el recurso de la base de datos a traves de su ID
        // Esto para confirmar que venga con los datos actualizados del paso anterior
        Optional<Alumno> alumnoDespuesDeActualizar = alumnoRepository.findById(alumno1.getId());
        assertTrue(alumnoDespuesDeActualizar.isPresent());
        assertEquals("NombreActualizado", alumnoDespuesDeActualizar.get().getNombre());
        assertEquals("ApellidoActualizado", alumnoDespuesDeActualizar.get().getApellido());
        System.out.println(alumnoDespuesDeActualizar.get());
    }

    @Test
    void testEliminarAlumno() {
        System.out.println("Antes de borrar: " + alumno1);
        // Eliminar alumno
        // Aquí eliminamos un alumno de la base de datos usando su ID.
        // deleteById busca en la base de datos un registro con el ID dado y lo elimina si existe.
        alumnoRepository.deleteById(alumno1.getId());

        // Verificar que fue eliminado
        // Después de eliminar, buscamos el alumno por su ID para confirmar que ya no existe.
        // Si el alumno fue correctamente eliminado, el Optional estará vacío.
        Optional<Alumno> alumnoEliminado = alumnoRepository.findById(alumno1.getId());
        System.out.println("Intentamos buscar en la BD el recurso borrado: " + alumnoEliminado);


        // assertFalse espera que el valor dentro del paréntesis sea falso.
        // En este caso, verificamos que el Optional no contiene ningún valor, lo que significa que el alumno ya no existe.
        assertFalse(alumnoEliminado.isPresent());

        // Verificar que solo se eliminó uno
        // Obtenemos la lista de todos los alumnos restantes en la base de datos.
        // Esto nos permite asegurarnos de que solo el alumno eliminado ya no está.
        List<Alumno> alumnosRestantes = alumnoRepository.findAll();

        // assertEquals compara dos valores: el esperado (1) y el valor real (alumnosRestantes.size()).
        // Aquí comprobamos que la base de datos solo contiene un alumno después de eliminar.
        assertEquals(1, alumnosRestantes.size());
    }

    //Etapa de test con SQl Nativo
    @Test
    void testFindAllAlumnosQuery() {
        // Recuperar todos los alumnos usando la consulta personalizada
        List<Alumno> alumnos = alumnoRepository.findAllAlumnosQuery();
        boolean primerAlumnoExiste = alumnos.stream()
                                            .anyMatch(alumno -> alumno.getNombre().equals("john"));

        boolean segundoAlumnoExiste = alumnos.stream()
                                             .anyMatch(alumno -> alumno.getNombre().equals("Jane"));

        alumnos.forEach(System.out::println);
        // Verificar que la lista contiene los dos alumnos insertados en setUp()
        assertEquals(2, alumnos.size());
        assertTrue(primerAlumnoExiste);
        assertTrue(segundoAlumnoExiste);

        // Imprimir cada alumno para confirmar los resultados
    }

    @Test
    void testFindAlumnoByIdQuery() {
        // Buscar un alumno por su ID utilizando la consulta personalizada
        Alumno alumnoEncontrado = alumnoRepository.findAlumnoByIdQuery(alumno1.getId());

        // Verificar que los datos del alumno encontrado coinciden con el registro esperado
        assertNotNull(alumnoEncontrado);
        assertEquals(alumno1.getNombre(), alumnoEncontrado.getNombre());
        assertEquals(alumno1.getApellido(), alumnoEncontrado.getApellido());
        assertEquals(alumno1.getEdad(), alumnoEncontrado.getEdad());
        assertEquals(alumno1.getCurso(), alumnoEncontrado.getCurso());

        // Imprimir el resultado para confirmar visualmente
        System.out.println(alumnoEncontrado);
    }

    @Test
    void testSaveAlumnoQuery() {
        // Crear un nuevo alumno
        String nombre = "Carlos";
        String apellido = "Perez";
        int edad = 21;
        String curso = "Historia";

        // Guardar el nuevo alumno usando la consulta personalizada
        alumnoRepository.saveAlumnoQuery(nombre, apellido, edad, curso);

        // Verificar que el alumno fue guardado recuperándolo por su nombre y curso
        List<Alumno> alumnosGuardados = alumnoRepository.findAlumnosByCursoQuery(curso);
        assertEquals(1, alumnosGuardados.size());
        Alumno alumnoGuardado = alumnosGuardados.get(0);
        assertEquals(nombre, alumnoGuardado.getNombre());
        assertEquals(apellido, alumnoGuardado.getApellido());
        assertEquals(edad, alumnoGuardado.getEdad());
        assertEquals(curso, alumnoGuardado.getCurso());

        // Imprimir los datos del alumno guardado
        System.out.println(alumnoGuardado);
    }

    @Test
    void testUpdateAlumnoQuery() {
        // Actualizar los datos del primer alumno
        String nuevoNombre = "Actualizado";
        String nuevoApellido = "Modificado";
        int nuevaEdad = 30;
        String nuevoCurso = "Química";

        // Ejecutar la actualización
        alumnoRepository.updateAlumnoQuery(alumno1.getId(), nuevoNombre, nuevoApellido, nuevaEdad, nuevoCurso);

        // Recuperar el alumno actualizado
        Alumno alumnoActualizado = alumnoRepository.findAlumnoByIdQuery(alumno1.getId());

        // Verificar que los datos se actualizaron correctamente
        assertEquals(nuevoNombre, alumnoActualizado.getNombre());
        assertEquals(nuevoApellido, alumnoActualizado.getApellido());
        assertEquals(nuevaEdad, alumnoActualizado.getEdad());
        assertEquals(nuevoCurso, alumnoActualizado.getCurso());

        // Imprimir el alumno actualizado
        System.out.println(alumnoActualizado);
    }

    @Test
    void testDeleteAlumnoByIdQuery() {
        // Eliminar el primer alumno usando la consulta personalizada
        alumnoRepository.deleteAlumnoByIdQuery(alumno1.getId());

        // Verificar que el alumno fue eliminado
        Optional<Alumno> alumnoEliminado = alumnoRepository.findById(alumno1.getId());
        assertFalse(alumnoEliminado.isPresent());

        // Verificar que solo queda un alumno en la base de datos
        List<Alumno> alumnosRestantes = alumnoRepository.findAll();
        assertEquals(1, alumnosRestantes.size());
        assertEquals(alumno2.getNombre(), alumnosRestantes.get(0).getNombre());

        // Imprimir los alumnos restantes
        alumnosRestantes.forEach(System.out::println);
    }

    @Test
    void testFindAlumnosByCursoQuery() {
        // Buscar alumnos por curso
        List<Alumno> alumnosMatematicas = alumnoRepository.findAlumnosByCursoQuery("Matematicas");

        // Verificar que se encuentra el alumno correspondiente al curso
        assertEquals(1, alumnosMatematicas.size());
        assertEquals(alumno1.getNombre(), alumnosMatematicas.get(0).getNombre());

        // Imprimir los resultados
        alumnosMatematicas.forEach(System.out::println);
    }

    @Test
    void testFindAlumnosByRangoEdad() {
        // Buscar alumnos cuyo rango de edad sea entre 18 y 22 años
        List<Alumno> alumnosRangoEdad = alumnoRepository.findAlumnosByRangoEdad(18, 22);

        boolean primerAlumnoExiste = alumnosRangoEdad.stream()
                                                     .anyMatch(alumno -> alumno.getNombre().equals("john"));

        boolean segundoAlumnoExiste = alumnosRangoEdad.stream()
                                                      .anyMatch(alumno -> alumno.getNombre().equals("Jane"));

        // Verificar que ambos alumnos se encuentran en el rango de edad
        assertEquals(2, alumnosRangoEdad.size());
        assertTrue(primerAlumnoExiste);
        assertTrue(segundoAlumnoExiste);

        // Buscar alumnos cuyo rango de edad sea entre 23 y 30 años
        List<Alumno> alumnosFueraDeRango = alumnoRepository.findAlumnosByRangoEdad(23, 30);

        // Verificar que no hay resultados fuera del rango especificado
        assertTrue(alumnosFueraDeRango.isEmpty());

        // Imprimir los resultados
        alumnosRangoEdad.forEach(System.out::println);
    }

}