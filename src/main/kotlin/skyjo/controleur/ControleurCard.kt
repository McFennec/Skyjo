import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ControleurCard(val vue: VueGeneral, val modele: Partie) : EventHandler<MouseEvent> {
    // Job parent pour gérer toutes les coroutines de cette instance
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun handle(event: MouseEvent?) {
        when (modele.donneEtape()) {
            "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE" -> {
                when (modele.donnePiocheDepuis()) {
                    "defausse" -> {
                        val card = event?.source as Card
                        val row: Int = GridPane.getRowIndex(card)
                        val column: Int = GridPane.getColumnIndex(card)
                        scope.launch {
                            modele.joueurEchangeDefausse(column, row)
                            vue.vueGames.updateDeck(
                                modele.donneJoueur().deck.donneTableauCartes(),
                                ControleurCard(vue, modele)
                            )
                            modele.changePiocheDepuis("")
                        }
                    }
                }
            }

            "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE" -> {
                when (modele.donnePiocheDepuis()) {
                    "pioche" -> {
                        val card = event?.source as Card
                        val row: Int = GridPane.getRowIndex(card)
                        val column: Int = GridPane.getColumnIndex(card)
                        scope.launch {
                            modele.joueurEchangePioche(column, row)
                            vue.vueGames.updateDeck(
                                modele.donneJoueur().deck.donneTableauCartes(),
                                ControleurCard(vue, modele)
                            )
                            modele.changePiocheDepuis("")
                        }
                    }

                    "retourne" -> {
                        val card = event?.source as Card
                        val row: Int = GridPane.getRowIndex(card)
                        val column: Int = GridPane.getColumnIndex(card)
                        if (card.cardValue == null) {
                            scope.launch {
                                modele.joueurDefaussePioche(column, row)
                                vue.vueGames.updateDeck(
                                    modele.donneJoueur().deck.donneTableauCartes(),
                                    ControleurCard(vue, modele)
                                )
                                modele.changePiocheDepuis("")
                            }
                        }
                    }
                }
            }
        }
        if (modele.donneIdJoueurActu() != modele.donneJoueur().donneId()) {
            this.cancelCoroutines()
        }
    }

    // Méthode pour annuler toutes les coroutines de cette instance
    fun cancelCoroutines() {
        job.cancel()
    }
}
