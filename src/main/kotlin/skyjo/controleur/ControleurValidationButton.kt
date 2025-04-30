package skyjo.controleur

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import kotlinx.coroutines.runBlocking
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ControleurValidationButton(val vue : VueGeneral, val  modele : Partie) : EventHandler<ActionEvent>{

    override fun handle(event: ActionEvent) {
        if (vue.vueSelectName.getPlayerName() == "") {
            vue.changeVue(vue.vueSelectName)
            vue.vueSelectName.showRedBox()
        } else {
            val selectedValue = vue.vueSelectName.getCombobox().value
            modele.definirServeur(selectedValue)
            runBlocking { modele.nouveauJoueur(vue.vueSelectName.getPlayerName()) }
            val sourceButton = event.source as Button
            when (sourceButton.id) {
                "ValiderSelectName" -> {
                    vue.changeVue(vue.vueSelectName, vue.vueSelectNameModale)
                }
            }
        }

    }
}