package ar.edu.utnfrc.backend.repository;

import ar.edu.utnfrc.backend.model.Director;

import java.util.List;
import java.util.stream.Stream;

public class DirectorRepository extends Repository<Director,Long>{

    public DirectorRepository() {
        super();
    }
    @Override
    public Director getById(Long id) {
        return this.manager.find(Director.class, id);
    }

    @Override
    public List<Director> getAll() {
        return this.manager.createQuery("SELECT d from Director d", Director.class).getResultList();
    }

    @Override
    public Stream<Director> getAllStream() {
        return this.manager.createQuery("SELECT d from Director d", Director.class).getResultStream();
    }
}
