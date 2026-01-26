# Ghost Net Fishing - Setup-Anleitung

## Voraussetzungen

- **Java JDK 17** oder höher
- **Maven 3.x**
- **MySQL Server** (empfohlen: XAMPP)
- **Application Server** (empfohlen: Apache Tomcat 10.1+)
- **IDE** (empfohlen: Eclipse IDE for Enterprise Java Developers)

---

## 1. MySQL-Datenbank einrichten

### Option A: Mit XAMPP (empfohlen)

1. **XAMPP herunterladen und installieren:** https://www.apachefriends.org/

2. **XAMPP Control Panel starten** und MySQL starten (Start-Button klicken)

3. **phpMyAdmin öffnen:** http://localhost/phpmyadmin

4. **Neue Datenbank erstellen:**
   - Klicke auf "Neu" in der linken Sidebar
   - Datenbankname: `ghostnetfishing`
   - Kollation: `utf8mb4_general_ci`
   - Klicke auf "Erstellen"

### Option B: Über MySQL-Kommandozeile

```sql
CREATE DATABASE ghostnetfishing CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

---

## 2. Projekt in Eclipse importieren

1. **Eclipse öffnen** (Eclipse IDE for Enterprise Java Developers)

2. **Projekt importieren:**
   - File → Import → Maven → Existing Maven Projects
   - Root Directory: Pfad zum `GhostNetFishing`-Ordner auswählen
   - Finish klicken

3. **Maven Dependencies laden:**
   - Rechtsklick auf Projekt → Maven → Update Project
   - "Force Update of Snapshots/Releases" aktivieren
   - OK klicken

---

## 3. Application Server konfigurieren

### Tomcat 10.1+ einrichten

1. **Tomcat herunterladen:** https://tomcat.apache.org/download-10.cgi

2. **In Eclipse hinzufügen:**
   - Window → Preferences → Server → Runtime Environments
   - Add → Apache Tomcat v10.1
   - Tomcat-Installationsverzeichnis auswählen
   - Finish

3. **Server erstellen:**
   - Window → Show View → Servers
   - Rechtsklick im Servers-View → New → Server
   - Apache Tomcat v10.1 auswählen → Next
   - GhostNetFishing-Projekt hinzufügen → Finish

---

## 4. Datenbank-Verbindung prüfen

Die Datei `src/main/resources/META-INF/persistence.xml` enthält die Datenbank-Konfiguration:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/ghostnetfishing"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value=""/>
```

**Falls nötig anpassen:**
- Port: Standard ist 3306 (XAMPP)
- User: Standard ist "root"
- Passwort: Standard ist leer bei XAMPP

---

## 5. Anwendung starten

1. **MySQL sicherstellen** (XAMPP → MySQL gestartet)

2. **Server starten:**
   - Rechtsklick auf Server im Servers-View
   - "Start" oder "Debug" wählen

3. **Anwendung öffnen:** http://localhost:8080/ghostnetfishing/

---

## 6. Fehlerbehebung

### "Port already in use"
- Server-Einstellungen ändern: Doppelklick auf Server → Ports ändern (z.B. 8081)

### "Table doesn't exist"
- Hibernate erstellt die Tabellen automatisch (`hibernate.hbm2ddl.auto=update`)
- Prüfen, ob MySQL läuft und Datenbank `ghostnetfishing` existiert

### "No Persistence provider"
- Maven Update durchführen (Rechtsklick → Maven → Update Project)
- Project → Clean

### ClassNotFoundException
- Projekt neu bauen: Project → Clean → Build Project
- Tomcat Server "Clean" und "Publish"

---

## Projektstruktur

```
GhostNetFishing/
├── pom.xml                          # Maven Konfiguration
├── src/main/java/de/iu/ghostnetfishing/
│   ├── model/                       # JPA Entities
│   │   ├── Geisternetz.java
│   │   ├── Person.java
│   │   └── NetStatus.java
│   ├── dao/                         # Data Access Objects
│   │   ├── GeisternetzDAO.java
│   │   └── PersonDAO.java
│   └── controller/                  # CDI Beans
│       └── GeisternetzController.java
├── src/main/resources/META-INF/
│   └── persistence.xml              # JPA Konfiguration
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── web.xml
    │   ├── faces-config.xml
    │   └── beans.xml
    ├── index.xhtml                  # Startseite
    ├── melden.xhtml                 # Netz melden
    ├── liste.xhtml                  # Alle Netze
    └── bergung.xhtml                # Bergung verwalten
```
