package de.iu.ghostnetfishing.dao;

import de.iu.ghostnetfishing.model.Geisternetz;
import de.iu.ghostnetfishing.model.NetStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Data Access Object für Geisternetz-Entitäten.
 * Zentralisiert alle Datenbankoperationen für Geisternetze.
 * Nutzt CDI zur Injection der EntityManagerFactory.
 */
@ApplicationScoped
public class GeisternetzDAO {

    @Inject
    private EntityManagerFactory emf;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Speichert ein neues Geisternetz in der Datenbank.
     */
    public void speichern(Geisternetz netz) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(netz);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Aktualisiert ein bestehendes Geisternetz.
     */
    public Geisternetz aktualisieren(Geisternetz netz) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Geisternetz merged = em.merge(netz);
            em.getTransaction().commit();
            return merged;
        } finally {
            em.close();
        }
    }

    /**
     * Findet ein Geisternetz anhand seiner ID.
     */
    public Geisternetz findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Geisternetz.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Gibt alle Geisternetze zurück.
     */
    public List<Geisternetz> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Geisternetz> query = em.createQuery(
                "SELECT g FROM Geisternetz g ORDER BY g.meldedatum DESC",
                Geisternetz.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Gibt alle Geisternetze zurück, die noch zu bergen sind.
     * (Status: GEMELDET oder BERGUNG_BEVORSTEHEND)
     */
    public List<Geisternetz> findZuBergende() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Geisternetz> query = em.createQuery(
                "SELECT g FROM Geisternetz g WHERE g.status = :status1 OR g.status = :status2 ORDER BY g.meldedatum ASC",
                Geisternetz.class
            );
            query.setParameter("status1", NetStatus.GEMELDET);
            query.setParameter("status2", NetStatus.BERGUNG_BEVORSTEHEND);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Gibt alle Geisternetze mit einem bestimmten Status zurück.
     */
    public List<Geisternetz> findByStatus(NetStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Geisternetz> query = em.createQuery(
                "SELECT g FROM Geisternetz g WHERE g.status = :status ORDER BY g.meldedatum DESC",
                Geisternetz.class
            );
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Zählt alle Geisternetze.
     */
    public long count() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(g) FROM Geisternetz g", Long.class)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Zählt Geisternetze nach Status.
     */
    public long countByStatus(NetStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(g) FROM Geisternetz g WHERE g.status = :status",
                Long.class
            );
            query.setParameter("status", status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
