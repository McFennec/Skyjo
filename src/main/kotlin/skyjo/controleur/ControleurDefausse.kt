package skyjo.controleur

import Card
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import skyjo.AppConfig
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ControleurDefausse (val vue : VueGeneral, val partie : Partie) : EventHandler<MouseEvent> {

    override fun handle(event: MouseEvent) {
        when (partie.donneEtape()) {
            "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE" -> {
                if (partie.donnePiocheDepuis() == "") {
                    vue.vueGames.defausseSelected()
                    partie.changePiocheDepuis("defausse")
                }
            }

            "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE" -> {
                if (partie.donnePiocheDepuis() == "pioche") {
                    partie.changePiocheDepuis("retourne")
                    val nouvelleMain = partie.donneJoueur().deck.donneMain()
                    vue.vueGames.changeDefausse(Card(120.0, 180.0, nouvelleMain?.donneValeur(),nouvelleMain?.donneCouleur() ,AppConfig.PD_FONT_SIZE), vue, partie)
                    vue.vueGames.changePile(Card(120.0, 180.0), vue, partie)
                }
            }
        }
    }
}