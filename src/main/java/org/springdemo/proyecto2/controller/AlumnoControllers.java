package org.springdemo.proyecto2.controller;

import org.springframework.ui.Model;
import org.springdemo.proyecto2.service.AlumnoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlumnoControllers {

    private final AlumnoService alumnoService;

    public AlumnoControllers(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("alumnos", alumnoService.obtenerTodosLosAlumnos());
        return "index";

    }

    }
