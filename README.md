# 🎓 Student Assistant

**Student Assistant** è un'applicazione Android moderna progettata per aiutare gli studenti universitari a organizzare, tracciare e ottimizzare il proprio percorso accademico. Dimentica i fogli sparsi e i calendari disordinati: tutto il tuo mondo universitario è ora in un'unica app!

##  Funzionalità Principali

- ** Orario Lezioni**: Tieni sempre sotto controllo le tue lezioni settimanali.
- ** Andamento**: Inserisci i tuoi voti, tieni traccia degli esami passati e calcola automaticamente la tua media.
- ** I miei appunti**: Uno spazio dedicato per prendere nota di scadenze, compiti o concetti chiave.
- **️ Calendario Studio**: Pianifica le tue sessioni di studio in modo strategico.
- ** Consigli Studio**: Scopri tecniche e suggerimenti per massimizzare la tua produttività e superare l'ansia da esame.
- **️ Impostazioni Personalizzabili**: Supporto completo alla **Dark Mode** e gestione dati sicura (tutto salvato in locale sul tuo telefono).

##  Tecnologie Utilizzate

Il progetto è costruito seguendo le ultime e migliori pratiche di sviluppo Android promosse da Google:

*   **Linguaggio:** [Kotlin](https://kotlinlang.org/)
*   **Architettura:** MVVM (Model-View-ViewModel) per una netta separazione tra dati, logica e interfaccia utente.
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - Interfacce dichiarative scritte interamente in Kotlin (Material Design 3).
*   **Database:** [Room Database](https://developer.android.com/training/data-storage/room) per il salvataggio persistente dei voti e degli eventi sul dispositivo.
*   **Navigazione:** Compose Navigation per transizioni fluide tra le schermate senza l'uso di Fragment.
*   **DataStore:** Preferences DataStore per il salvataggio del tema (Light/Dark).

##  Screenshot (Anteprima)

*(Inserire qui gli screenshot dell'applicazione, ad esempio la Home Screen, il Calendario e le Impostazioni)*

## Il Team di Sviluppo

Il progetto è stato realizzato in collaborazione da:
- **Silvia (sil):** Architettura database (Room), logica dell'Andamento, setup iniziale del progetto.
- **Karim Gouda Said Hessan:** Prototipazione funzionalità (WebView orario), refactoring, bug-fixing e stabilizzazione dipendenze.
- **Niki (nhammond):** Design UI/UX in Jetpack Compose, implementazione navigazione globale, e ottimizzazione schermate (Home, Impostazioni, Calendario).

## ️ Come Avviare il Progetto

1. Clona questa repository:
   ```bash
   git clone https://github.com/TUO-USERNAME/progetto_student_assistent.git
   ```
2. Apri il progetto in **Android Studio**.
3. Attendi la sincronizzazione di Gradle.
4. Avvia il progetto cliccando su **Run 'app'** (Shift + F10) su un Emulatore (API 34/35 consigliata) o un dispositivo fisico.

---
*Progetto Universitario realizzato per scopi didattici.*
