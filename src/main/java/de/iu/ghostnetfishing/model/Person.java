package de.iu.ghostnetfishing.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity-Klasse für meldende und bergende Personen.
 * Meldende Personen können anonym bleiben (name und telefonnummer dann null).
 * Bergende Personen müssen Name und Telefonnummer angeben.
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "telefonnummer")
    private String telefonnummer;

    // Geisternetze, die diese Person gemeldet hat
    @OneToMany(mappedBy = "meldendePerson", cascade = CascadeType.ALL)
    private List<Geisternetz> gemeldetNetze = new ArrayList<>();

    // Geisternetze, die diese Person bergen möchte
    @OneToMany(mappedBy = "bergendePerson")
    private List<Geisternetz> zuBergendeNetze = new ArrayList<>();

    // Konstruktoren
    public Person() {
    }

    public Person(String name, String telefonnummer) {
        this.name = name;
        this.telefonnummer = telefonnummer;
    }

    // Prüft ob Person anonym ist
    public boolean isAnonym() {
        return (name == null || name.trim().isEmpty())
            && (telefonnummer == null || telefonnummer.trim().isEmpty());
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public List<Geisternetz> getGemeldetNetze() {
        return gemeldetNetze;
    }

    public void setGemeldetNetze(List<Geisternetz> gemeldetNetze) {
        this.gemeldetNetze = gemeldetNetze;
    }

    public List<Geisternetz> getZuBergendeNetze() {
        return zuBergendeNetze;
    }

    public void setZuBergendeNetze(List<Geisternetz> zuBergendeNetze) {
        this.zuBergendeNetze = zuBergendeNetze;
    }

    @Override
    public String toString() {
        if (isAnonym()) {
            return "Anonym";
        }
        return name != null ? name : "Unbekannt";
    }
}
