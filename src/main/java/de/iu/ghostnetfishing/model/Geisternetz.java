package de.iu.ghostnetfishing.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity-Klasse für Geisternetze.
 * Ein Geisternetz hat GPS-Koordinaten, eine geschätzte Größe und einen Status.
 */
@Entity
@Table(name = "geisternetz")
public class Geisternetz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "groesse")
    private Integer groesse; // geschätzte Größe in Quadratmetern

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NetStatus status = NetStatus.GEMELDET;

    @Column(name = "meldedatum")
    private LocalDateTime meldedatum;

    // Die Person, die das Netz gemeldet hat (kann anonym sein)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "meldende_person_id")
    private Person meldendePerson;

    // Die Person, die das Netz bergen möchte (kann null sein)
    @ManyToOne
    @JoinColumn(name = "bergende_person_id")
    private Person bergendePerson;

    // Konstruktoren
    public Geisternetz() {
        this.meldedatum = LocalDateTime.now();
        this.status = NetStatus.GEMELDET;
    }

    public Geisternetz(Double latitude, Double longitude, Integer groesse) {
        this();
        this.latitude = latitude;
        this.longitude = longitude;
        this.groesse = groesse;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getGroesse() {
        return groesse;
    }

    public void setGroesse(Integer groesse) {
        this.groesse = groesse;
    }

    public NetStatus getStatus() {
        return status;
    }

    public void setStatus(NetStatus status) {
        this.status = status;
    }

    public LocalDateTime getMeldedatum() {
        return meldedatum;
    }

    public void setMeldedatum(LocalDateTime meldedatum) {
        this.meldedatum = meldedatum;
    }

    public Person getMeldendePerson() {
        return meldendePerson;
    }

    public void setMeldendePerson(Person meldendePerson) {
        this.meldendePerson = meldendePerson;
    }

    public Person getBergendePerson() {
        return bergendePerson;
    }

    public void setBergendePerson(Person bergendePerson) {
        this.bergendePerson = bergendePerson;
    }

    // Hilfsmethode für die Koordinaten-Anzeige
    public String getKoordinatenString() {
        return String.format("%.4f, %.4f", latitude, longitude);
    }
}
