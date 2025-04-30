package skyjo.controleur

import Card
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.*
import skyjo.AppConfig
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ControleurPioche(val vue: VueGeneral, val modele: Partie) : EventHandler<MouseEvent> {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun handle(event: MouseEvent) {
        if (modele.donneEtape() == "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE") {
            if (modele.donnePiocheDepuis() == "") {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) { modele.joueurPioche() }
                    modele.changePiocheDepuis("pioche")
                    val nouvelleMain = modele.donneJoueur().deck.donneMain()
                    vue.vueGames.changePile(
                        Card(
                            120.0,
                            180.0,
                            nouvelleMain?.donneValeur(),
                            nouvelleMain?.donneCouleur(),
                            fontSize = AppConfig.PD_FONT_SIZE
                        ),
                        vue,
                        modele
                    )
                }
            }
        }
        if (modele.donneIdJoueurActu() != modele.donneJoueur().donneId()) {
            this.cancelCoroutines()
        }
    }
    fun cancelCoroutines() {
        job.cancel()
    }
}
