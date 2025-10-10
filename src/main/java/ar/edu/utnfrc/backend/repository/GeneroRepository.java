package ar.edu.utnfrc.backend.repository;

import ar.edu.utnfrc.backend.model.Genero;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GeneroRepository extends Repository<Genero, Long> {

    public GeneroRepository() {
        super();
    }

    @Override
    public Genero getById(Long id) {
        return this.manager.find(Genero.class, id);
    }

    @Override
    public List<Genero> getAll() {
        return this.manager.createQuery("Select g FROM Genero g",Genero.class)
                .getResultList()
                .stream()
                .toList();
    }

    @Override
    public Stream<Genero> getAllStream() {
        return this.manager.createQuery("SELECT G FROM Genero g", Genero.class).getResultStream();
    }
}
