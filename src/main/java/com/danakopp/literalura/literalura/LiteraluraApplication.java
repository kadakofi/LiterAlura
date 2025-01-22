package com.danakopp.literalura.literalura;

import com.danakopp.literalura.literalura.controller.Menu;
import com.danakopp.literalura.literalura.service.AutorService;
import com.danakopp.literalura.literalura.service.LibroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraluraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ApplicationContext context) {
        return args -> {
            AutorService autorService = context.getBean(AutorService.class);
            LibroService libroService = context.getBean(LibroService.class);

            Menu menu = new Menu(libroService, autorService);
            menu.muestraMenu();
        };
    }
}
