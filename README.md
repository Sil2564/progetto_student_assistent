# Student Assistant

Student Assistant e' un'applicazione Android nativa sviluppata per supportare gli studenti universitari nella gestione quotidiana del loro percorso accademico: lezioni, esami, appunti e sessioni di studio in un'unica interfaccia pulita e moderna.

---

## Funzionalita

* **Orario Lezioni:** Calendario didattico con orari delle lezioni, aule e docenti di riferimento, consultabili tramite popup interattivi.
* **Andamento Esami:** Registrazione dei voti con calcolo dinamico della media e dei crediti formativi (CFU) acquisiti.
* **Appunti Multimediali:** Creazione di note di testo, scatto foto con la fotocamera (salvate su storage interno), selezione immagini da galleria e importazione di file PDF per materia.
* **Calendario Studio:** Pianificazione delle sessioni di studio, ripasso ed esami con calcolo automatico della durata, consigli metodologici (tecnica Pomodoro) e visualizzazione delle festivita nazionali italiane.
* **Consigli Studio:** Sezione con suggerimenti per l'apprendimento e frase motivazionale del giorno aggiornata quotidianamente.
* **Impostazioni & Statistiche:** Riepilogo statistico dell'account e supporto completo a Tema Chiaro e Tema Scuro (Dark Mode).

---

## Architettura e Scelte Tecniche

L'app segue l'architettura MVVM (Model-View-ViewModel) raccomandata da Google, garantendo la separazione delle responsabilita (Separation of Concerns) e un flusso unidirezionale dei dati:

* **Linguaggio:** Kotlin
* **UI Toolkit:** Jetpack Compose (100% dichiarativo, Material Design 3)
* **Gestione Stato & Asincronia:** Kotlin Coroutines, Flow e StateFlow
* **Storage Locale:**
  * **Room Database:** Persistenza per voti, materie, appunti ed eventi di studio.
  * **Preferences DataStore:** Salvataggio delle preferenze utente (tema scuro).
  * **Internal Storage:** Memorizzazione sicura di foto e PDF tramite FileProvider.
* **Rete (API Remote):**
  * **ZenQuotes API:** Recupero della citazione giornaliera (con cache locale per evitare chiamate ripetute).
  * **Nager.Date Public Holidays API:** Recupero delle festivita pubbliche per il calendario.
* **Permessi a Runtime:** Gestione permessi per l'utilizzo della fotocamera (CAMERA) con rememberLauncherForActivityResult.

---

## Mappatura Requisiti d'Esame

* **Requisiti Minimi:**
  * Struttura in package dedicati (model, repository, viewmodel, ui, network, util).
  * Utilizzo dei componenti di Lifecycle (ViewModel, StateFlow, collectAsState).
  * Coroutine per le operazioni su background thread (Dispatchers.IO).
  * Storage locale (Room Database + DataStore).
* **Requisiti Opzionali:**
  * 2 Chiamate ad API remote (ZenQuotes e Nager.Date Holidays tramite Retrofit).
  * Gestione permessi a runtime (Fotocamera con scatto e salvataggio foto su disco).

---

## Team di Sviluppo

Progetto sviluppato per il corso di Laboratorio di Programmazione di Sistemi Mobili (Corso di Laurea in Tecnologie dei Sistemi Informatici, Universita di Bologna - Campus di Cesena) da:
* Silvia
* Niki

---

## Come Eseguire il Progetto

1. Clona il repository:
   ```bash
   git clone https://github.com/Sil2564/progetto_student_assistent.git
   ```
2. Apri la cartella in Android Studio.
3. Attendi il completamento della sincronizzazione Gradle.
4. Avvia l'app su un emulatore o dispositivo fisico con Android 8.0 (API 26) o superiore.
