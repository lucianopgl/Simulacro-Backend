package ar.edu.utnfrc.backend.repository;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Stream;

public abstract class Repository<T, K> {

    protected EntityManager manager;

    public Repository() {
        manager = DbContext.getInstance().getManager();
    }

    public void add(T entity) {
        var transaction = manager.getTransaction();
        transaction.begin();
        manager.persist(entity);
        transaction.commit();
    }

    public void update(T entity) {
        var transaction = manager.getTransaction();
        transaction.begin();
        manager.merge(entity);
        transaction.commit();
    }

    public T delete(K id) {
        var transaction = manager.getTransaction();
        transaction.begin();
        var entity = this.getById(id);
        manager.remove(entity);
        transaction.commit();
        return entity;
    }

    public abstract T getById(K id);

    public abstract List<T> getAll();

    public abstract Stream<T> getAllStream();
}
