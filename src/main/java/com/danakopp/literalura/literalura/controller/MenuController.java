package com.danakopp.literalura.literalura.controller;

import com.danakopp.literalura.literalura.model.Libro;
import com.danakopp.literalura.literalura.service.LibroService;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final LibroService libroService;

    public MenuController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/listarLibros")
    public List<Libro> listarLibrosRegistrados() {
        return libroService.listarLibrosRegistrados();
    }

    @GetMapping("/buscarLibroPorTitulo")
    public Libro buscarLibroPorTitulo(@RequestParam String titulo) {
        return libroService.buscarLibroPorTitulo(titulo)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }
}
