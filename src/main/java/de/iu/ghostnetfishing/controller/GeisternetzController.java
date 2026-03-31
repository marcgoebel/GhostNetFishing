package de.iu.ghostnetfishing.controller;

import de.iu.ghostnetfishing.dao.GeisternetzDAO;
import de.iu.ghostnetfishing.model.Geisternetz;
import de.iu.ghostnetfishing.model.NetStatus;
import de.iu.ghostnetfishing.model.Person;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

/**
 * Controller-Bean für die Verwaltung von Geisternetzen.
 * Implementiert die MUST-Anforderungen:
 * - Geisternetze erfassen (auch anonym)
 * - Für Bergung eintragen
 * - Geisternetze als geborgen melden
 * - Liste der zu bergenden Netze anzeigen
 */
@Named
@SessionScoped
public class GeisternetzController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private GeisternetzDAO geisternetzDAO;

    // Formularfelder für neues Geisternetz
    private Double latitude;
    private Double longitude;
    private Integer groesse;
    private String melderName;
    private String melderTelefon;
    private boolean anonym;

    // Formularfelder für Bergung
    private String bergerName;
    private String bergerTelefon;
    private Long ausgewaehltesNetzId;

    // Liste der Netze
    private List<Geisternetz> zuBergendeNetze;
    private List<Geisternetz> alleNetze;

    @PostConstruct
    public void init() {
        aktualisiereListen();
    }

    /**
     * Aktualisiert alle Listen aus der Datenbank.
     */
    public void aktualisiereListen() {
        zuBergendeNetze = geisternetzDAO.findZuBergende();
        alleNetze = geisternetzDAO.findAll();
    }

    /**
     * MUST 1: Neues Geisternetz melden (anonym möglich).
     */
    public String netzMelden() {
        try {
            Geisternetz netz = new Geisternetz(latitude, longitude, groesse);

            // Meldende Person erstellen (anonym oder mit Daten)
            if (!anonym && melderName != null && !melderName.trim().isEmpty()) {
                Person melder = new Person(melderName, melderTelefon);
                netz.setMeldendePerson(melder);
            }

            geisternetzDAO.speichern(netz);

            addMessage(FacesMessage.SEVERITY_INFO, "Erfolg",
                "Geisternetz wurde erfolgreich gemeldet!");

            // Formular zurücksetzen
            resetMeldeFormular();
            aktualisiereListen();

            return "liste?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
                "Geisternetz konnte nicht gespeichert werden: " + e.getMessage());
            return null;
        }
    }

    /**
     * MUST 2: Sich für die Bergung eines Geisternetzes eintragen.
     */
    public String fuerBergungEintragen(Long netzId) {
        try {
            Geisternetz netz = geisternetzDAO.findById(netzId);
            if (netz == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Geisternetz nicht gefunden!");
                return null;
            }

            // Prüfen ob bereits jemand die Bergung angekündigt hat
            if (netz.getBergendePerson() != null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Hinweis",
                    "Dieses Netz wird bereits von einer anderen Person geborgen.");
                return null;
            }

            // Bergende Person muss Name und Telefonnummer angeben
            if (bergerName == null || bergerName.trim().isEmpty() ||
                bergerTelefon == null || bergerTelefon.trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
                    "Name und Telefonnummer sind für die Bergung erforderlich!");
                return null;
            }

            Person berger = new Person(bergerName, bergerTelefon);
            netz.setBergendePerson(berger);
            netz.setStatus(NetStatus.BERGUNG_BEVORSTEHEND);

            geisternetzDAO.aktualisieren(netz);

            addMessage(FacesMessage.SEVERITY_INFO, "Erfolg",
                "Sie haben sich erfolgreich für die Bergung eingetragen!");

            resetBergungsFormular();
            aktualisiereListen();

            return "liste?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
                "Eintragung fehlgeschlagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * MUST 4: Geisternetz als geborgen melden.
     */
    public String alsGeborgenMelden(Long netzId) {
        try {
            Geisternetz netz = geisternetzDAO.findById(netzId);
            if (netz == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Geisternetz nicht gefunden!");
                return null;
            }

            netz.setStatus(NetStatus.GEBORGEN);
            geisternetzDAO.aktualisieren(netz);

            addMessage(FacesMessage.SEVERITY_INFO, "Erfolg",
                "Geisternetz wurde als geborgen gemeldet! Vielen Dank!");

            aktualisiereListen();

            return "liste?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
                "Status konnte nicht geändert werden: " + e.getMessage());
            return null;
        }
    }

    /**
     * COULD 7: Geisternetz als verschollen melden.
     * (Nicht anonym möglich laut Fachkonzept)
     */
    public String alsVerschollenMelden(Long netzId, String melderName, String melderTelefon) {
        try {
            // Prüfen ob Name und Telefon angegeben (nicht anonym erlaubt)
            if (melderName == null || melderName.trim().isEmpty() ||
                melderTelefon == null || melderTelefon.trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
                    "Verschollen-Meldungen können nicht anonym erfolgen!");
                return null;
            }

            Geisternetz netz = geisternetzDAO.findById(netzId);
            if (netz == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Geisternetz nicht gefunden!");
                return null;
            }

            netz.setStatus(NetStatus.VERSCHOLLEN);
            geisternetzDAO.aktualisieren(netz);

            addMessage(FacesMessage.SEVERITY_INFO, "Hinweis",
                "Geisternetz wurde als verschollen gemeldet.");

            aktualisiereListen();

            return "liste?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
                "Status konnte nicht geändert werden: " + e.getMessage());
            return null;
        }
    }

    // Hilfsmethoden
    private void resetMeldeFormular() {
        latitude = null;
        longitude = null;
        groesse = null;
        melderName = null;
        melderTelefon = null;
        anonym = false;
    }

    private void resetBergungsFormular() {
        bergerName = null;
        bergerTelefon = null;
        ausgewaehltesNetzId = null;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(severity, summary, detail));
    }

    // Getter und Setter
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

    public String getMelderName() {
        return melderName;
    }

    public void setMelderName(String melderName) {
        this.melderName = melderName;
    }

    public String getMelderTelefon() {
        return melderTelefon;
    }

    public void setMelderTelefon(String melderTelefon) {
        this.melderTelefon = melderTelefon;
    }

    public boolean isAnonym() {
        return anonym;
    }

    public void setAnonym(boolean anonym) {
        this.anonym = anonym;
    }

    public String getBergerName() {
        return bergerName;
    }

    public void setBergerName(String bergerName) {
        this.bergerName = bergerName;
    }

    public String getBergerTelefon() {
        return bergerTelefon;
    }

    public void setBergerTelefon(String bergerTelefon) {
        this.bergerTelefon = bergerTelefon;
    }

    public Long getAusgewaehltesNetzId() {
        return ausgewaehltesNetzId;
    }

    public void setAusgewaehltesNetzId(Long ausgewaehltesNetzId) {
        this.ausgewaehltesNetzId = ausgewaehltesNetzId;
    }

    public List<Geisternetz> getZuBergendeNetze() {
        if (zuBergendeNetze == null) {
            aktualisiereListen();
        }
        return zuBergendeNetze;
    }

    public List<Geisternetz> getAlleNetze() {
        if (alleNetze == null) {
            aktualisiereListen();
        }
        return alleNetze;
    }

    // Statistik-Methoden
    public long getAnzahlGemeldet() {
        return geisternetzDAO.countByStatus(NetStatus.GEMELDET);
    }

    public long getAnzahlBergungBevorstehend() {
        return geisternetzDAO.countByStatus(NetStatus.BERGUNG_BEVORSTEHEND);
    }

    public long getAnzahlGeborgen() {
        return geisternetzDAO.countByStatus(NetStatus.GEBORGEN);
    }

    public long getAnzahlVerschollen() {
        return geisternetzDAO.countByStatus(NetStatus.VERSCHOLLEN);
    }

    public long getAnzahlGesamt() {
        return geisternetzDAO.count();
    }
}
