package ar.edu.utnfrc.backend.repository;

import ar.edu.utnfrc.backend.model.Pelicula;

import java.util.List;
import java.util.stream.Stream;

public class PeliculaRepository extends Repository<Pelicula, Long> {

    public PeliculaRepository() {
        super();
    }

    @Override
    public Pelicula getById(Long id) {
        return this.manager.find(Pelicula.class, id);
    }

    @Override
    public List<Pelicula> getAll() {
        return this.manager.createQuery("SELECT d from Pelicula d", Pelicula.class).getResultList();
    }

    @Override
    public Stream<Pelicula> getAllStream() {
        return this.manager.createQuery("SELECT d from Pelicula d", Pelicula.class).getResultStream();
    }
}
