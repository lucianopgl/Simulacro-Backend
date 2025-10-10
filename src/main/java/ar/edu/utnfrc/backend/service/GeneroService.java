package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.model.Genero;
import ar.edu.utnfrc.backend.repository.GeneroRepository;

import java.util.HashMap;
import java.util.Map;

public class GeneroService {
    private final GeneroRepository generoRepository;
    private Map<String, Genero> generos;

    public GeneroService() {
        this.generoRepository = new GeneroRepository();
        generos = new HashMap<>();
    }

    public Genero getOrCreateGenero(String nombreGenero) {
        if (generos.containsKey(nombreGenero)) {
            return generos.get(nombreGenero);
        }
        Genero genero = new Genero(nombreGenero);
        generos.put(nombreGenero, genero);
        generoRepository.add(genero);
        return genero;
    }
}
