package org.springdemo.proyecto2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//Uso esta clase debido a que estoy usando Spring Security
//si no la utilizo me pediría usuario y contraseña para poder entrar
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll()
                );

        return http.build();
    }
    // uso esto porque se me ocurrió trabajar con spring security
    // y me pide un usuario y contraseña para poder
    //ingresar en la página web
    //este código lo deshabilita y pasa directo al listado de alumnos
    //pueden comentar o eliminar esta clase y ver qué pasa

}
