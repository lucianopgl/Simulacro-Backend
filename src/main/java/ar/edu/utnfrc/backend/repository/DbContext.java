package ar.edu.utnfrc.backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbContext {
    private final EntityManager manager;

    public static DbContext instance = null;

    private DbContext() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BlockBuster");
        manager = emf.createEntityManager();
    }

    public static DbContext getInstance() {
        if (instance == null) {
            instance = new DbContext();
        }
        return instance;
    }

    public EntityManager getManager() {
        return this.manager;
    }
}


