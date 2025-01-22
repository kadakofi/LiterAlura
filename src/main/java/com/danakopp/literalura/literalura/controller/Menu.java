package com.danakopp.literalura.literalura.controller;

import com.danakopp.literalura.literalura.model.*;
import com.danakopp.literalura.literalura.service.AutorService;
import com.danakopp.literalura.literalura.service.ConsumoAPI;
import com.danakopp.literalura.literalura.service.ConvierteDatos;
import com.danakopp.literalura.literalura.service.LibroService;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Menu {

    // URL base de la API de Gutendex
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos conversor;
    private final Scanner teclado;
    private final LibroService libroServicio;
    private final AutorService autorServicio;

    // Constructor que inicializa los servicios necesarios
    public Menu(LibroService libroService, AutorService autorService) {
        this.libroServicio = libroService;
        this.autorServicio = autorService;
        this.consumoAPI = new ConsumoAPI();
        this.conversor = new ConvierteDatos();
        this.teclado = new Scanner(System.in);
    }

    // Método que muestra el menú principal
    public void muestraMenu() {
        int opcion = -1;
        while (opcion != 0) {
            try {
                String menu = """
                --------------
                Bienvenido a Literalura
                1.- Buscar libro por título
                2.- Listar libros registrados
                3.- Listar autores registrados
                4.- Listar autores vivos en un determinado año
                5.- Listar libros por idioma
                6.- Estadísticas de libros por número de descargas
                7.- Top 10 libros más descargados
                8.- Buscar autor por nombre
                0.- Salir
                --------------

                Elige una opción a través de un número:""";

                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> buscarAutoresVivosPorAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> estadisticasLibrosPorNumDescargas();
                    case 7 -> top10LibrosMasDescargados();
                    case 8 -> buscarAutorPorNombre();
                    case 0 -> System.out.println("Saliendo de la aplicación...");
                    default -> System.out.println("Opción inválida. Introduzca un número válido del menú.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Introduzca un número válido.");
                teclado.nextLine();
            }
        }
    }

    // Método para consultar información de libros en la API
    private DatosResultados obtenerDatosResultados(String tituloLibro) {
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "%20"));
        return conversor.obtenerDatos(json, DatosResultados.class);
    }

    // Método para buscar un libro por su título
    private void buscarLibroPorTitulo() {
        System.out.print("Introduce el título del libro: ");
        String tituloLibro = teclado.nextLine().toUpperCase();

        Optional<Libro> libroRegistrado = libroServicio.buscarLibroPorTitulo(tituloLibro);

        if (libroRegistrado.isPresent()) {
            System.out.println("El libro ya está registrado.");
        } else {
            DatosResultados datos = obtenerDatosResultados(tituloLibro);

            if (datos.listaLibros().isEmpty()) {
                System.out.println("No se encontró el libro en la API de Gutendex.");
            } else {
                DatosLibros datosLibros = datos.listaLibros().get(0);
                DatosAutores datosAutores = datosLibros.autores().get(0);
                Idiomas idioma = Idiomas.fromString(datosLibros.idiomas().get(0));

                Libro libro = new Libro(datosLibros);
                libro.setIdiomas(idioma);

                Optional<Autor> autorRegistrado = autorServicio.buscarAutorRegistrado(datosAutores.nombre());

                if (autorRegistrado.isPresent()) {
                    System.out.println("El autor ya está registrado.");
                    libro.setAutor(autorRegistrado.get());
                } else {
                    Autor autor = new Autor(datosAutores);
                    autor = autorServicio.guardarAutor(autor);
                    libro.setAutor(autor);
                    autor.getLibros().add(libro);
                }

                try {
                    libroServicio.guardarLibro(libro);
                    System.out.println("\nLibro encontrado y registrado con éxito.\n");
                    System.out.println(libro + "\n");
                } catch (DataIntegrityViolationException e) {
                    System.out.println("El libro ya existe en la base de datos.");
                }
            }
        }
    }

    // Método para listar los libros registrados
    private void listarLibrosRegistrados() {
        List<Libro> libros = libroServicio.listarLibrosRegistrados();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    // Método para listar autores registrados con sus libros
    private void listarAutoresRegistrados() {
        List<Autor> autores = autorServicio.listarAutoresRegistrados();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados.");
        } else {
            for (Autor autor : autores) {
                List<Libro> libros = libroServicio.buscarLibrosPorAutorId(autor.getId());
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Fecha de Nacimiento: " + autor.getFechaNacimiento());
                System.out.println("Fecha de Fallecimiento: " + autor.getFechaFallecido());
                String librosRegistrados = libros.stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", "));
                System.out.println("Libros: " + librosRegistrados);
            }
        }
    }

    // Método para listar autores vivos en un año determinado
    private void buscarAutoresVivosPorAnio() {
        System.out.print("Introduce el año para buscar autores vivos: ");
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorServicio.buscarAutoresVivosPorAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año especificado.");
        } else {
            autores.forEach(autor -> System.out.println("Autor: " + autor.getNombre()));
        }
    }

    // Método para listar libros por idioma
    private void listarLibrosPorIdioma() {
        System.out.print("Introduce el código de idioma (es, en, fr, pt): ");
        String idioma = teclado.nextLine();

        try {
            List<Libro> libros = libroServicio.buscarLibroPorIdiomas(Idiomas.fromString(idioma));
            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma especificado.");
            } else {
                libros.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Idioma no válido. Introduzca un código correcto.");
        }
    }

    // Método para mostrar estadísticas de descargas de libros
    private void estadisticasLibrosPorNumDescargas() {
        List<Libro> libros = libroServicio.listarLibrosRegistrados();
        DoubleSummaryStatistics stats = libros.stream()
                .collect(Collectors.summarizingDouble(Libro::getNumeroDescargas));

        System.out.println("Descargas promedio: " + stats.getAverage());
        System.out.println("Máximo de descargas: " + stats.getMax());
        System.out.println("Mínimo de descargas: " + stats.getMin());
    }

    // Método para mostrar el top 10 de libros más descargados
    private void top10LibrosMasDescargados() {
        List<Libro> libros = libroServicio.listarTop10LibrosMasDescargados();
        libros.forEach(System.out::println);
    }

    // Método para buscar un autor por nombre
    private void buscarAutorPorNombre() {
        System.out.print("Introduce el nombre del autor: ");
        String nombre = teclado.nextLine().toUpperCase();
        List<Autor> autores = autorServicio.buscarAutorPorNombre(nombre);
        autores.forEach(System.out::println);
    }
}
