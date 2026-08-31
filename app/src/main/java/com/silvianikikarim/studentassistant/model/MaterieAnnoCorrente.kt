package com.silvianikikarim.studentassistant.model

/**
 * Le materie del corso di laurea, divise per anno. Vengono usate per
 * "seminare" la tabella Materia condivisa all'avvio dell'app, così sia
 * Appunti sia Andamento partono già con l'elenco completo, senza che
 * l'utente debba ricrearle a mano.
 *
 * NOTA: la tabella Materia (Room) non ha un campo "anno": qui la
 * suddivisione serve solo a tenere il codice leggibile e organizzato,
 * ma alla fine tutte le materie confluiscono in un'unica lista piatta
 * (vedi `nomi`) e in un'unica tabella nel database.
 */
object MaterieAnnoCorrente {

    private val primoAnno: List<String> = listOf(
        "Elementi di Architetture degli Elaboratori e Sistemi Operativi",
        "Elementi di Matematica per l'Informatica",
        "Programmazione",
        "Reti di Calcolatori e Programmazione di Rete",
        "Basi di Dati",
        "Fondamenti di Sistemi Web",
        "Idoneità Lingua Inglese B - 1",
        "Sistemi Virtualizzati",
        "Sperimentazione Fisica, Elettronica e Sensoristica per Informatica"
    )

    private val secondoAnno: List<String> = listOf(
        "Algoritmi e Strutture Dati",
        "Ingegneria dei Sistemi Web",
        "Laboratorio di Big Data, Data Mining e Data Analytics",
        "Laboratorio di Sistemi Embedded e IoT",
        "Progettazione e Sviluppo del Software",
        "Laboratorio di Ottimizzazione, Intelligenza Artificiale e Machine Learning",
        "Laboratorio di Piattaforme e Metodologie di Sviluppo Cloud",
        "Laboratorio di Programmazione di Sistemi Mobili",
        "Laboratorio di Sicurezza dei Sistemi e Privacy",
        "Laboratorio di Sistemi di Rete"
    )

    // "Tirocinio Pratico Valutativo" I e II resi univoci nel nome: getOrCreateMateria
    // confronta per nome (case-insensitive), quindi due voci identiche
    // collasserebbero in una sola materia nel database.
    private val terzoAnno: List<String> = listOf(
        "Laboratorio di Interfaccia Uomo-Macchina",
        "Primo Tirocinio Pratico Valutativo",
        "Secondo Tirocinio Pratico Valutativo",
        "Prova Finale",
        "Prova Pratica Valutativa (PPV) - Abilitante alla Sezione Informatica",
        "Laboratorio di Piattaforme di Sviluppo per Automazione - CE"
    )

    val nomi: List<String> = primoAnno + secondoAnno + terzoAnno
}
