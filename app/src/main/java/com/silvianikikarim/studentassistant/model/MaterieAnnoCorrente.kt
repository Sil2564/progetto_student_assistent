package com.silvianikikarim.studentassistant.model

/**
 * Le materie dell'anno accademico corrente, così come compaiono nell'Orario
 * delle lezioni (OrarioScreen.generateFakeLessons). Vengono usate per
 * "seminare" la tabella Materia condivisa all'avvio dell'app, così sia
 * Appunti sia Andamento partono già con lo stesso elenco, senza che l'utente
 * debba ricrearle a mano.
 *
 * NOTA: se in futuro l'Orario diventa reale/editabile, questa lista statica
 * andrà sostituita leggendo le materie direttamente dalle lezioni salvate.
 * Per ora deve restare allineata manualmente ai nomi usati in OrarioScreen.
 */
object MaterieAnnoCorrente {
    val nomi: List<String> = listOf(
        "Ingegneria del Software",
        "Sistemi Cloud",
        "Reti di Calcolatori",
        "Sicurezza dei Sistemi",
        "Sistemi Mobili",
        "Tecnologie Web",
        "Data Science"
    )
}
