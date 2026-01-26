package de.iu.ghostnetfishing.model;

/**
 * Status eines Geisternetzes im System.
 * Definiert die möglichen Zustände gemäß Fachkonzept.
 */
public enum NetStatus {
    GEMELDET("Gemeldet"),
    BERGUNG_BEVORSTEHEND("Bergung bevorstehend"),
    GEBORGEN("Geborgen"),
    VERSCHOLLEN("Verschollen");

    private final String displayName;

    NetStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
