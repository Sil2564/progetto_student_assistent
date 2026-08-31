package com.silvianikikarim.studentassistant.model

/**
 * Contenuto statico dei consigli di studio, scritto direttamente qui.
 * Non serve nessuna API: sono informazioni "evergreen" che non cambiano nel tempo,
 * quindi tenerle nell'app è più semplice e affidabile che scaricarle da remoto.
 * Per aggiungere un nuovo articolo basta aggiungere un elemento a questa lista.
 */
object ArticoliConsigli {

    val lista: List<ArticoloConsiglio> = listOf(
        ArticoloConsiglio(
            titolo = "La tecnica del Pomodoro",
            sottotitolo = "Studia a blocchi, non a maratona",
            corpo = "Dividi lo studio in blocchi da 25 minuti di concentrazione totale, " +
                "seguiti da una pausa di 5 minuti. Dopo 4 blocchi, fai una pausa più lunga " +
                "di 15-20 minuti. Funziona perché il cervello regge meglio brevi sprint " +
                "intensi rispetto a ore intere senza interruzioni, e la scadenza del " +
                "timer aiuta a non procrastinare l'inizio."
        ),
        ArticoloConsiglio(
            titolo = "Il metodo Feynman",
            sottotitolo = "Se non sai spiegarlo, non l'hai capito",
            corpo = "Prendi un argomento e prova a spiegarlo a voce alta con parole tue, " +
                "come se lo stessi insegnando a qualcuno che non ne sa nulla. Ogni volta " +
                "che ti blocchi o usi paroloni senza saperli tradurre in modo semplice, " +
                "hai trovato un punto che non hai davvero capito: torna sul libro proprio " +
                "su quel punto."
        ),
        ArticoloConsiglio(
            titolo = "Ripetizione spaziata",
            sottotitolo = "Ripassa poco, ma nel momento giusto",
            corpo = "Invece di ripassare tutto insieme la sera prima, rivedi lo stesso " +
                "argomento a distanza crescente di tempo: dopo un giorno, poi dopo tre, " +
                "poi dopo una settimana. Ogni ripasso rinforza la memoria a lungo termine " +
                "molto più di una rilettura ripetuta nello stesso giorno."
        ),
        ArticoloConsiglio(
            titolo = "Appunti efficaci",
            sottotitolo = "Scrivi per capire, non per copiare",
            corpo = "Durante la lezione non trascrivere tutto parola per parola: riassumi " +
                "i concetti con parole tue, usa frecce e schemi per collegare le idee, e " +
                "lascia spazio nei margini per aggiungere dubbi o domande da chiarire dopo. " +
                "Un appunto scritto in questo modo è già una prima forma di ripasso."
        ),
        ArticoloConsiglio(
            titolo = "Gestire l'ansia da esame",
            sottotitolo = "Prepararsi bene riduce l'ansia, non il contrario",
            corpo = "Arriva alla vigilia dell'esame avendo già ripassato, non avendo " +
                "ancora da studiare: pianifica il carico di lavoro con qualche giorno di " +
                "anticipo. La sera prima evita di aprire nuovi argomenti: rivedi solo un " +
                "riepilogo veloce e dormi a sufficienza, perché il sonno consolida la " +
                "memoria più di un'ora di studio in più."
        ),
        ArticoloConsiglio(
            titolo = "Elimina le distrazioni",
            sottotitolo = "Il telefono è il primo nemico della concentrazione",
            corpo = "Metti il telefono in un'altra stanza o in modalità aereo durante le " +
                "sessioni di studio: anche solo saperlo a portata di mano riduce la " +
                "concentrazione, anche se non lo usi. Studia in un ambiente ordinato e " +
                "dedicato solo a quello, così il cervello associa quel luogo alla " +
                "concentrazione."
        )
    )
}
