# Police Prison Mod (Minecraft Forge 1.20.1)

**Police Prison Mod** ist eine Roleplay-/Server-Mod für Minecraft Java **1.20.1 (Forge)**.
Sie fügt Polizei- und Gefängnismechaniken hinzu (Fesseln, Schlüssel, Zellenblöcke, Kommandos).

> ⚠️ Diese Mod greift direkt in die Bewegungs- und Aktionsfreiheit von Spielern ein. Nutzt sie auf Servern nur mit klaren Regeln und Einverständnis aller Beteiligten.

## Funktionen

- Items: Handschellen, Handschellenschlüssel, Gefängnisschlüssel, Schlagstock, Polizeimarke, Taschenlampe, Zwangsjacke
- Blöcke: verstärkter Gefängnisziegel, Gefängnisgitter, Zellentür, Fesselstuhl, Gefängnisbett, Gefängnislampe, Sicherheitskamera
- Serverseitiger Zustand (persistent): gefesselt/eingesperrt bleibt über Save/Reload erhalten
- Fesselmechanik:
  - Mit **Handschellen** auf anderen Spieler klicken → Ziel wird gefesselt
  - Ziel kann sich **nicht selbst** mit Schlüssel befreien
  - Nur ein **anderer Spieler** mit dem passenden Schlüssel kann befreien
- Einschränkungen für gefesselte oder zwangsjacken-tragende Spieler:
  - kein Angreifen
  - kein normaler Item-Einsatz
  - kein Öffnen/Interagieren mit Blöcken
  - kein Block abbauen/platzieren
  - eingeschränkte Bewegung gemäß Konfiguration

## Kommandos (OP-Rechte, Permission-Level 2)

- `/police arrest <spieler>` – Spieler fesseln
- `/police release <spieler>` – Fesselung lösen
- `/police jail <spieler>` – Spieler einsperren (nutzt konfigurierte Jail-Position)
- `/police unjail <spieler>` – Spieler aus Jail-Zustand holen
- `/police status <spieler>` – Status anzeigen
- `/police setjail <dimension>` – aktuelle Position des ausführenden Spielers als Jail speichern (z. B. `minecraft:overworld`)

### Erststart-Verhalten (sicher)

Solange keine Jail-Position gesetzt wurde, teleportiert `/police jail` **nicht** und meldet stattdessen eine klare Fehlermeldung.

## Installation für Spieler/Server

1. Forge **1.20.1** installieren (passende Forge-Version zur Mod).
2. Die gebaute Datei aus `build/libs/` nehmen (z. B. `policeprison-1.0.0.jar`).
3. Datei in den `mods`-Ordner kopieren:
   - Client: `.minecraft/mods`
   - Server: `<serverordner>/mods`
4. Minecraft/Server neu starten.

## Build (für Entwickler)

```bash
./gradlew build
```

Windows:

```bat
gradlew.bat build
```

Das fertige JAR liegt danach in:

```text
build/libs/
```

## Hinweise

- Die Mod ist für Forge **1.20.1** gebaut.
- Die Schlüssel-/Fessel-Logik ist serverautoritativ und wird an Clients synchronisiert.
- Platzhaltermodelle verwenden Vanilla-Texturen (keine urheberrechtlich problematischen Assets enthalten).
