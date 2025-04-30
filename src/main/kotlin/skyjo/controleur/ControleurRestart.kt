package skyjo.controleur

import javafx.event.ActionEvent
import javafx.event.EventHandler
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ControleurRestart(val vue: VueGeneral,val modele: Partie) : EventHandler<ActionEvent> {

    override fun handle(p0: ActionEvent?) {
        modele.reinitialisePartie()
        vue.changeVue(vue.vueSelectName)
    }
}