package de.iu.ghostnetfishing.dao;

import de.iu.ghostnetfishing.model.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Data Access Object für Person-Entitäten.
 * Zentralisiert alle Datenbankoperationen für Personen.
 */
@ApplicationScoped
public class PersonDAO {

    private static final String PERSISTENCE_UNIT = "GhostNetFishingPU";
    private EntityManagerFactory emf;

    public PersonDAO() {
        this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Speichert eine neue Person in der Datenbank.
     */
    public void speichern(Person person) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Aktualisiert eine bestehende Person.
     */
    public Person aktualisieren(Person person) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Person merged = em.merge(person);
            em.getTransaction().commit();
            return merged;
        } finally {
            em.close();
        }
    }

    /**
     * Findet eine Person anhand ihrer ID.
     */
    public Person findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Gibt alle Personen zurück.
     */
    public List<Person> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery(
                "SELECT p FROM Person p ORDER BY p.name",
                Person.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Findet Personen anhand des Namens.
     */
    public List<Person> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery(
                "SELECT p FROM Person p WHERE p.name LIKE :name",
                Person.class
            );
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Findet alle nicht-anonymen Personen (für Bergungspersonen-Auswahl).
     */
    public List<Person> findNichtAnonyme() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery(
                "SELECT p FROM Person p WHERE p.name IS NOT NULL AND p.name <> '' ORDER BY p.name",
                Person.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
