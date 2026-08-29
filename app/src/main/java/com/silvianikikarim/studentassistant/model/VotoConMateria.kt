package com.silvianikikarim.studentassistant.model

/**
 * Un Voto arricchito con il nome della sua materia, pronto per la UI.
 * Evita di dover fare il "join" a mano in ogni schermata: lo fa una volta
 * sola VotoRepository, combinando i Flow di voti e materie.
 */
data class VotoConMateria(
    val voto: Voto,
    val nomeMateria: String
)
