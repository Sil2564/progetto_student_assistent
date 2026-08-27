package com.silvianikikarim.studentassistant.model

/** Un "articolo" di consigli per lo studio: contenuto statico, scritto a mano nell'app. */
data class ArticoloConsiglio(
    val titolo: String,
    val sottotitolo: String,
    val corpo: String
)
