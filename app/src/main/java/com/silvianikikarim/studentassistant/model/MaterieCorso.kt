package com.silvianikikarim.studentassistant.model

/** Una materia "ufficiale" del piano di studi, con l'anno a cui appartiene. */
data class MateriaSeed(val nome: String, val anno: Int)

/**
 * Tutte le materie del corso di laurea, divise per anno (1°, 2°, 3°). Vengono
 * usate per "seminare" la tabella Materia condivisa all'avvio dell'app, così
 * Appunti e Andamento partono già con l'elenco completo, senza che l'utente
 * debba ricrearle a mano.
 *
 * Le materie del 2° anno (quello corrente) sono le stesse che compaiono
 * nell'Orario delle lezioni (OrarioScreen.generateFakeLessons): se cambi i
 * nomi qui, aggiornali anche lì per restare coerenti.
 *
 * NOTA: se in futuro l'Orario diventa reale/editabile, questa lista statica
 * andrà sostituita leggendo le materie direttamente dalle lezioni salvate.
 */
object MaterieCorso {
    val tutte: List<MateriaSeed> = listOf(
        // ---- 1° Anno ----
        MateriaSeed("Analisi Matematica", 1),
        MateriaSeed("Programmazione I", 1),
        MateriaSeed("Fisica Generale", 1),
        MateriaSeed("Basi di Dati", 1),
        MateriaSeed("Elettronica Digitale", 1),
        MateriaSeed("Inglese Tecnico", 1),

        // ---- 2° Anno (anno corrente, allineato all'Orario) ----
        MateriaSeed("Ingegneria del Software", 2),
        MateriaSeed("Sistemi Cloud", 2),
        MateriaSeed("Reti di Calcolatori", 2),
        MateriaSeed("Sicurezza dei Sistemi", 2),
        MateriaSeed("Sistemi Mobili", 2),
        MateriaSeed("Tecnologie Web", 2),
        MateriaSeed("Data Science", 2),

        // ---- 3° Anno ----
        MateriaSeed("Intelligenza Artificiale", 3),
        MateriaSeed("Machine Learning", 3),
        MateriaSeed("Sviluppo Mobile Avanzato", 3),
        MateriaSeed("Cybersecurity Avanzata", 3),
        MateriaSeed("Cloud Native Architecture", 3),
        MateriaSeed("Project Work", 3)
    )
}
