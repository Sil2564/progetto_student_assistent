package com.silvianikikarim.studentassistant.model

/** Una materia "ufficiale" del piano di studi, con l'anno a cui appartiene. */
data class MateriaSeed(val nome: String, val anno: Int)

/**
 * Tutte le materie del corso di laurea, divise per anno (1°, 2°, 3°). Vengono
 * usate per "seminare" la tabella Materia condivisa all'avvio dell'app, così
 * Appunti e Andamento partono già con l'elenco completo, senza che l'utente
 * debba ricrearle a mano.
 *
 * Le materie del 2° anno sono le stesse che compaiono nell'Orario delle
 * lezioni (OrarioScreen.generateFakeLessons): se cambi i nomi qui,
 * aggiornali anche lì per restare coerenti.
 */
object MaterieCorso {
    val tutte: List<MateriaSeed> = listOf(
        // ---- 1° Anno ----
        MateriaSeed("Elementi di Architetture degli Elaboratori e Sistemi Operativi", 1),
        MateriaSeed("Elementi di Matematica per l'Informatica", 1),
        MateriaSeed("Programmazione", 1),
        MateriaSeed("Reti di Calcolatori e Programmazione di Rete", 1),
        MateriaSeed("Basi di Dati", 1),
        MateriaSeed("Fondamenti di Sistemi Web", 1),
        MateriaSeed("Idoneità Lingua Inglese B - 1", 1),
        MateriaSeed("Sistemi Virtualizzati", 1),
        MateriaSeed("Sperimentazione Fisica, Elettronica e Sensoristica per Informatica", 1),

        // ---- 2° Anno (anno corrente, allineato all'Orario) ----
        MateriaSeed("Algoritmi e Strutture Dati", 2),
        MateriaSeed("Ingegneria dei Sistemi Web", 2),
        MateriaSeed("Laboratorio di Big Data, Data Mining e Data Analytics", 2),
        MateriaSeed("Laboratorio di Sistemi Embedded e IoT", 2),
        MateriaSeed("Progettazione e Sviluppo del Software", 2),
        MateriaSeed("Laboratorio di Ottimizzazione, Intelligenza Artificiale e Machine Learning", 2),
        MateriaSeed("Laboratorio di Piattaforme e Metodologie di Sviluppo Cloud", 2),
        MateriaSeed("Laboratorio di Programmazione di Sistemi Mobili", 2),
        MateriaSeed("Laboratorio di Sicurezza dei Sistemi e Privacy", 2),
        MateriaSeed("Laboratorio di Sistemi di Rete", 2),

        // ---- 3° Anno ----
        MateriaSeed("Laboratorio di Interfaccia Uomo-Macchina", 3),
        MateriaSeed("Primo Tirocinio Pratico Valutativo", 3),
        MateriaSeed("Secondo Tirocinio Pratico Valutativo", 3),
        MateriaSeed("Prova Finale", 3),
        MateriaSeed("Prova Pratica Valutativa (PPV) - Abilitante alla Sezione Informatica", 3),
        MateriaSeed("Laboratorio di Piattaforme di Sviluppo per Automazione - CE", 3)
    )
}