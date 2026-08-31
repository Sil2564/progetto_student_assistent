package com.silvianikikarim.studentassistant.util

import com.silvianikikarim.studentassistant.model.MateriaDao
import com.silvianikikarim.studentassistant.model.Nota
import com.silvianikikarim.studentassistant.model.NotaDao
import com.silvianikikarim.studentassistant.model.TipoNota
import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.model.VotoDao
import kotlinx.coroutines.flow.first

/**
 * Popola l'app con voti e appunti di esempio "come se" lo studente li avesse
 * davvero inseriti, per non partire da una UI completamente vuota. Il
 * contenuto rispecchia una situazione plausibile per un secondo anno appena
 * iniziato (coerente con l'Orario, che parte a Settembre 2026):
 *
 * - 1° Anno: concluso, quasi tutti gli esami sostenuti con voti realistici,
 *   più qualche appunto di ripasso rimasto utile per gli anni successivi.
 * - 2° Anno: appena iniziato, quindi NESSUN voto ancora (sarebbe irrealistico
 *   il contrario), ma già alcuni appunti delle primissime lezioni.
 * - 3° Anno: futuro, resta completamente vuoto.
 *
 * Si esegue una sola volta: se esiste già almeno un voto o un appunto (anche
 * uno solo, inserito manualmente dall'utente), non tocca nulla. Va chiamato
 * DOPO aver seminato le materie (MateriaRepository.seedMaterieSeNecessario),
 * perché ha bisogno che le materie esistano già per trovarne l'id.
 */
object DatiEsempioSeeder {

    suspend fun popolaSeVuoto(materiaDao: MateriaDao, votoDao: VotoDao, notaDao: NotaDao) {
        val esisteGiaUnVoto = votoDao.getAllVoti().first().isNotEmpty()
        val esisteGiaUnaNota = notaDao.getUnaQualsiasiNota() != null
        if (esisteGiaUnVoto || esisteGiaUnaNota) return

        suspend fun idDi(nomeMateria: String): Long? = materiaDao.getByNomeIgnoreCase(nomeMateria)?.id

        // ---------------- 1° ANNO: voti (esami tutti sostenuti) ----------------
        val votiPrimoAnno = listOf(
            Triple("Elementi di Architetture degli Elaboratori e Sistemi Operativi", 27, "15/01/2026"),
            Triple("Elementi di Matematica per l'Informatica", 24, "22/01/2026"),
            Triple("Programmazione", 29, "05/02/2026"),
            Triple("Reti di Calcolatori e Programmazione di Rete", 26, "12/02/2026"),
            Triple("Basi di Dati", 28, "20/06/2026"),
            Triple("Fondamenti di Sistemi Web", 31, "27/06/2026"), // 31 = 30 e Lode
            Triple("Sistemi Virtualizzati", 25, "10/07/2026"),
            Triple("Sperimentazione Fisica, Elettronica e Sensoristica per Informatica", 27, "15/07/2026")
            // "Idoneità Lingua Inglese B - 1" lasciata senza voto di proposito: idoneità,
            // non ha senso dargli un voto in trentesimi finché non gestiamo anche quel caso.
        )
        val tipologiePrimoAnno = listOf("Scritto", "Scritto", "Pratico", "Orale", "Scritto", "Pratico", "Orale", "Pratico")

        votiPrimoAnno.forEachIndexed { index, (nomeMateria, voto, data) ->
            idDi(nomeMateria)?.let { materiaId ->
                votoDao.insertVoto(
                    Voto(
                        materiaId = materiaId,
                        voto = voto,
                        data = data,
                        descrizione = tipologiePrimoAnno[index],
                        note = ""
                    )
                )
            }
        }

        // ---------------- 1° ANNO: qualche appunto di ripasso ----------------
        inserisciNotaTesto(
            notaDao, idDi("Programmazione"),
            "Riassunto ricorsione e liste concatenate",
            "Ricorsione: una funzione che chiama se stessa, serve sempre un caso base per fermarsi. " +
                "Utile per alberi, liste e problemi divide-et-impera (es. quicksort, mergesort). " +
                "Liste concatenate: ogni nodo ha un valore e un riferimento al successivo; inserimento " +
                "e rimozione in testa sono O(1), l'accesso per indice è O(n)."
        )
        inserisciNotaTesto(
            notaDao, idDi("Basi di Dati"),
            "Normalizzazione - 1NF, 2NF, 3NF",
            "1NF: nessun attributo multivalore, valori atomici. 2NF: 1NF + nessuna dipendenza parziale " +
                "dalla chiave primaria (rilevante solo con chiavi composte). 3NF: 2NF + nessuna dipendenza " +
                "transitiva tra attributi non chiave. Da ripassare bene prima dell'orale: portare esempi " +
                "di tabelle non normalizzate e la loro scomposizione."
        )
        inserisciNotaTesto(
            notaDao, idDi("Elementi di Matematica per l'Informatica"),
            "Formulario derivate e limiti notevoli",
            "Limiti notevoli: sin(x)/x -> 1 per x->0, (1+1/x)^x -> e per x->infinito. " +
                "Derivate principali: d/dx(x^n) = n*x^(n-1), d/dx(e^x) = e^x, d/dx(ln x) = 1/x. " +
                "Regola della catena per funzioni composte: f(g(x))' = f'(g(x)) * g'(x)."
        )

        // ---------------- 2° ANNO: solo appunti, NESSUN voto (anno appena iniziato) ----------------
        inserisciNotaTesto(
            notaDao, idDi("Algoritmi e Strutture Dati"),
            "Introduzione a Dijkstra e grafi pesati",
            "Prima lezione: differenza tra BFS (grafi non pesati) e Dijkstra (grafi pesati, pesi non " +
                "negativi). Dijkstra usa una coda di priorità per estrarre sempre il nodo con distanza " +
                "minima non ancora visitato. Complessità O((V+E) log V) con heap binario."
        )
        inserisciNotaTesto(
            notaDao, idDi("Progettazione e Sviluppo del Software"),
            "Pattern MVVM - appunti prima lezione",
            "MVVM separa Model (dati), View (UI) e ViewModel (logica di presentazione, sopravvive ai " +
                "cambi di configurazione). La View osserva lo stato esposto dal ViewModel (es. StateFlow) " +
                "e non parla mai direttamente col Model. Da confrontare con MVC e MVP nella prossima lezione."
        )
        inserisciNotaTesto(
            notaDao, idDi("Laboratorio di Piattaforme e Metodologie di Sviluppo Cloud"),
            "Setup ambiente Docker - checklist",
            "Checklist consegnata a lezione: installare Docker Desktop, verificare con 'docker --version', " +
                "fare pull dell'immagine base del corso, testare un container con 'docker run hello-world'. " +
                "Portare il laptop già configurato alla prossima esercitazione in laboratorio."
        )
    }

    private suspend fun inserisciNotaTesto(
        notaDao: NotaDao,
        materiaId: Long?,
        titolo: String,
        testo: String
    ) {
        if (materiaId == null) return
        notaDao.insertNota(
            Nota(
                materiaId = materiaId,
                tipo = TipoNota.TESTO,
                titolo = titolo,
                testo = testo
            )
        )
    }
}
