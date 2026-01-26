# Ghost Net Fishing

Web-Anwendung für das Melden und Bergen von Geisternetzen (herrenlosen Fischernetzen).

## Projektbeschreibung

Diese Anwendung wurde im Rahmen des Kurses **"Programmierung von industriellen Informationssystemen mit Java EE"** an der IU Internationalen Hochschule entwickelt.

Geisternetze sind herrenlose Fischernetze, die im Meer treiben und eine große Gefahr für die Meeresfauna darstellen. Mit dieser Anwendung können Nutzer:
- Geisternetze melden (auch anonym)
- Sich für die Bergung eines Geisternetzes eintragen
- Den Status von Geisternetzen aktualisieren (geborgen/verschollen)

## Technologie-Stack

- **Frontend:** JSF (Jakarta Server Faces), PrimeFaces
- **Backend:** CDI Beans, JPA (Jakarta Persistence API)
- **Datenbank:** MySQL mit Hibernate als JPA-Provider
- **Build-Tool:** Maven

## Implementierte User Stories (MoSCoW)

1. ✅ **MUST:** Als meldende Person möchte ich Geisternetze (anonym) erfassen können.
2. ✅ **MUST:** Als bergende Person will ich mich für die Bergung eines Geisternetzes eintragen können.
3. ✅ **MUST:** Als bergende Person möchte ich sehen, welche Geisternetze noch zu bergen sind.
4. ✅ **MUST:** Als bergende Person möchte ich Geisternetze als geborgen melden können.
5. ✅ **COULD:** Als beliebige Person möchte ich Geisternetze als verschollen melden können.

## Projektstruktur

```
GhostNetFishing/
├── pom.xml
├── src/main/java/de/iu/ghostnetfishing/
│   ├── model/
│   │   ├── Geisternetz.java      # Entity: Geisternetz
│   │   ├── Person.java           # Entity: Person
│   │   └── NetStatus.java        # Enum: Status-Werte
│   ├── dao/
│   │   ├── GeisternetzDAO.java   # Data Access Object
│   │   ├── PersonDAO.java        # Data Access Object
│   │   └── EntityManagerProducer.java
│   └── controller/
│       └── GeisternetzController.java  # CDI Controller Bean
├── src/main/resources/META-INF/
│   └── persistence.xml           # JPA Konfiguration
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── web.xml
    │   ├── faces-config.xml
    │   └── beans.xml
    ├── resources/css/style.css
    ├── index.xhtml               # Startseite
    ├── melden.xhtml              # Geisternetz melden
    ├── liste.xhtml               # Übersicht aller Netze
    └── bergung.xhtml             # Bergung verwalten
```

## Installation & Setup

### Voraussetzungen
- Java 17 oder höher
- Maven 3.x
- MySQL Server (z.B. via XAMPP)
- Application Server (z.B. Apache Tomcat 10+, WildFly, GlassFish)

### Datenbank einrichten

1. MySQL starten (z.B. über XAMPP)
2. Datenbank erstellen:
   ```sql
   CREATE DATABASE ghostnetfishing;
   ```

### Projekt bauen

```bash
mvn clean package
```

### Deployment

Die generierte WAR-Datei (`target/ghostnetfishing.war`) auf dem Application Server deployen.

## Autor

Entwickelt als Fallstudie für den Kurs IPWA02-01 an der IU Internationalen Hochschule.
