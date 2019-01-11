# Confluence Space Migrator

This script supports moving Confluence spaces from one Confluence to another instance with a different username convention.

### Bereich im Ausgangs-Confluence einfrieren

* Zu migrierenden Bereich im Ausgangs-Confluence öffnen
* `Bereich konfigurieren` unten links auf der Seite anklicken und `Berechtigungen` auswählen
* Ausschließlich Leseberechtigungen für alle Benutzer und Gruppen belassen

## Export des Bereiches im Ausgangs-Confluence

* In der Konfiguration des Bereiches auf den Reiter `Inhalte` wechseln
* Hier den Punkt `Expor`t und die Option `XML-Format` auswählen
* Auf der Folgeseite die Option `Vollständiger Export (einschließlich Seiten, die nicht für Sie sichtbar sind)` verwenden und bestätigen
* Nach Generierung der Datei (dies kann etwas dauern) den Download starten

## Mapping von Benutzern und XML-Bereinigung im Export

* Exportierte Datei entpacken
* Groovy-Skript `space-migrator.groovy` mittels Befehl `groovy space-migrator.groovy` ausführen und auf die `entities.xml` des Exports bei Aufforderungen referenzieren
* Ausgabe des Groovy-Skriptes beachten und ggf. weitere Benutzermappings in `user-mappings.csv` aufnehmen und Groovy-Skript erneut ausführen
* XML-Cleaner `atlassian-xml-cleaner-0.1.jar` mittels `java -jar atlassian-xml-cleaner-0.1.jar entities.xml.migrated > entities-clean.xml` ausführen
* Dateien `entities.xml` und `entities.xml.migrated` wegwerfen und `entities-clean.xml` in `entities.xml` umbenennen
* Den Ordner `attachments` und die beiden Dateien `entities.xml` und `exportDescriptor.properties` zippen (Wichtig: diese drei Files liegen im Root des Zip-Files!)

## Import im Ziel-Confluence

* Upload des Files in das Dateisystem unter `/vol1/homes/confluence/restore`
* Anmeldung am Ziel-Confluence mit dem System-Administrator
* Navigation in die `Allgemeine Konfiguration` und hier in den Abschnitt `Sichern und Wiederherstellen`
* Auf dieser Seite gibt es als letzte Option `Eine Sicherung aus dem Confluence-Home-Verzeichnis wiederherstellen`
* Nach Auswahl des Zip-File des Bereiches mit Angehakter Option `Index erstelle`n kann der Import mit dem Button `Wiederherstellen` gestartet werden

Geschafft!
