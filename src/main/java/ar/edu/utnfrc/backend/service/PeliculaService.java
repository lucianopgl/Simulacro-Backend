package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.model.Clasificacion;
import ar.edu.utnfrc.backend.model.Director;
import ar.edu.utnfrc.backend.model.Genero;
import ar.edu.utnfrc.backend.model.Pelicula;
import ar.edu.utnfrc.backend.repository.PeliculaRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;


public class PeliculaService {
    private final GeneroService generoService;
    private final DirectorService directorService;
    private final PeliculaRepository peliculaRepository;

    public PeliculaService(GeneroService generoService, DirectorService directorService) {
        this.generoService = generoService;
        this.directorService = directorService;
        this.peliculaRepository = new PeliculaRepository();
    }

    public void bulkInsert(File fileToImport) throws IOException {
        // CSV: titulo;fechaEstreno;precioBaseAlquiler;clasificacion;genero;director
        Files.lines(Paths.get(fileToImport.toURI()))
                .skip(1) // saltar encabezado
                .forEach(linea -> {
                    try {
                        Pelicula pelicula = procesarLinea(linea);
                        if (pelicula != null) {
                            peliculaRepository.add(pelicula);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                });
    }

    private Pelicula procesarLinea(String linea) {
        String[] valores = linea.split(";");
        if (valores.length < 6) return null;

        // 0=titulo | 1=fechaEstreno | 2=precioBaseAlquiler | 3=clasificacion | 4=genero | 5=director
        String titulo = valores[0].trim();
        String fechaStr = valores[1].trim();
        String precioStr = valores[2].trim();
        String clasifStr = valores[3].trim();
        String generoNombre = valores[4].trim();
        String directorRaw = valores[5].trim();

        if (titulo.isEmpty() || generoNombre.isEmpty() || directorRaw.isEmpty()) return null;

        LocalDate fechaEstreno = LocalDate.parse(fechaStr); // ISO yyyy-MM-dd
        double precioBase = Double.parseDouble(precioStr);
        if (precioBase <= 0) return null;

        Clasificacion clasificacion = parseClasificacion(clasifStr);
        if (clasificacion == null) return null;
        //ddl
        String directorNombre = sanitizeDirector(directorRaw);

        Genero genero = generoService.getOrCreateGenero(generoNombre);
        Director director = directorService.getOrCreateDirector(directorNombre);

        Pelicula p = new Pelicula();
        p.setTitulo(titulo);
        p.setFechaEstreno(fechaEstreno);
        p.setPrecioBaseAlquiler(precioBase);
        p.setClasificacion(clasificacion);
        p.setGenero(genero);
        p.setDirector(director);
        return p;
    }
    public Map<String, List<Pelicula>> peliculasPorDirectorStream() {
        List<Pelicula> peliculas = peliculaRepository.getAll();
        return peliculas.stream()
                .filter(p -> p.getDirector() != null)
                .collect(Collectors.groupingBy(p -> p.getDirector().getNombre()));
    }

    public long cantidadPeliculasRecientesStream() {
        LocalDate hoy = LocalDate.now();
        List<Pelicula> peliculas = peliculaRepository.getAll();
        return peliculas.stream()
                .filter(p -> p.getFechaEstreno() != null)
                .filter(p -> ChronoUnit.DAYS.between(p.getFechaEstreno(), hoy) <= 365)
                .count();
    }

    public Map<String, Double> promPrecioPorGeneroStream() {
        List<Pelicula> peliculas = peliculaRepository.getAll();
        // Si no hay películas, devolvemos un mapa vacío
        if (peliculas.isEmpty()) {
            return new HashMap<>();
        }
        // Agrupamos por nombre de género y calculamos el promedio
        Map<String, Double> promedioPorGenero = peliculas.stream()
                .filter(p -> p.getGenero() != null && p.getGenero().getNombre() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getGenero().getNombre(),              // key: nombre del género
                        Collectors.averagingDouble(Pelicula::calcularPrecioAlquiler) // value: promedio
                ));

        return promedioPorGenero;
    }

    public Pelicula masRecienteStream() {
        List<Pelicula> peliculas = peliculaRepository.getAll();
        return peliculas.stream()
                .filter(p -> p.getFechaEstreno() != null)
                .max(Comparator.comparing(Pelicula::getFechaEstreno))
                .orElse(null);
    }
    //si viene alguna clasificacion que no esta en mi enum, devuelvo una excepcion
    private Clasificacion parseClasificacion(String raw) {
        if (raw == null) return null;
        // Los valores del CSV deben venir EXACTOS al Enum (ATP, INFANTIL_7, ADOLESCENTES_13, ADOLESCENTES_16, ADULTOS_18)
        try {
            return Clasificacion.valueOf(raw.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    //me fijo si viene mas de un director, si viene mas de uno, me quedo con el primero
    private String sanitizeDirector(String raw) {
        if (raw == null) return "";
        String limpio = raw;
        int pos = raw.indexOf('&');
        if (pos >= 0) {
            limpio = raw.substring(0, pos);
        }
        return limpio.trim();
    }
}
