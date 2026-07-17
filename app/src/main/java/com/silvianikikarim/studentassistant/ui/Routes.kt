package com.silvianikikarim.studentassistant.ui

object Routes {
    const val HOME = "home"
    const val ORARIO = "orario"

    const val APPUNTI = "appunti"
    // {materiaId} = id della Materia di cui mostrare la lista di note
    const val APPUNTI_MATERIA = "appunti/{materiaId}"
    // {notaId} = 0L per una nota NUOVA, altrimenti id della nota da modificare
    const val APPUNTI_NOTA = "appunti/{materiaId}/nota/{notaId}"

    const val CALENDARIO_STUDIO = "calendario_studio"
    const val ANDAMENTO = "andamento"
    const val CONSIGLI = "consigli"
    const val IMPOSTAZIONI = "impostazioni"

    // Costante usata al posto di un id reale per indicare "nota non ancora creata"
    const val NOTA_ID_NUOVA = 0L

    fun appuntiMateria(materiaId: Long) = "appunti/$materiaId"

    fun appuntiNota(materiaId: Long, notaId: Long = NOTA_ID_NUOVA) =
        "appunti/$materiaId/nota/$notaId"
}
