package de.iu.ghostnetfishing.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * CDI Producer für den EntityManager.
 * Erzeugt und verwaltet die EntityManagerFactory und EntityManager-Instanzen.
 */
@ApplicationScoped
public class EntityManagerProducer {

    private static final String PERSISTENCE_UNIT = "GhostNetFishingPU";
    private EntityManagerFactory emf;

    @Produces
    @ApplicationScoped
    public EntityManagerFactory createEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }

    @Produces
    public EntityManager createEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public void closeEntityManagerFactory(@Disposes EntityManagerFactory emf) {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
