package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.model.Director;
import ar.edu.utnfrc.backend.model.Genero;
import ar.edu.utnfrc.backend.repository.DirectorRepository;

import java.util.HashMap;
import java.util.Map;

public class DirectorService {
    private final DirectorRepository directorRepository;
    private Map<String, Director> directores;

    public DirectorService() {
        this.directorRepository = new DirectorRepository();
        this.directores = new HashMap<>();
    }
    public Director getOrCreateDirector(String nombreDirector) {
        if (directores.containsKey(nombreDirector)) {
            return directores.get(nombreDirector);
        }
        Director director = new Director(nombreDirector);
        directores.put(nombreDirector, director);
        directorRepository.add(director);
        return director;
    }
}
