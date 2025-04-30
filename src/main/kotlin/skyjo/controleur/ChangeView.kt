import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ChangeView(var vue : VueGeneral, var partie : Partie) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent?) {
        val sourceButton = event?.source as Button
        when (sourceButton.id) {
            "OuiSelectName" -> {
                vue.changeVue(vue.vueRegles)
            }
            "NonSelectName" -> {
                vue.changeVue(vue.vueSelectedMode)
                vue.vueSelectedMode.updateIdPartie(partie.donneJoueur().donneId())
            }
            "okRegles" -> {
                vue.changeVue(vue.vueSelectedMode)
                vue.vueSelectedMode.updateIdPartie(partie.donneJoueur().donneId())
            }
            "creerModeButton" -> {
                vue.changeVue(vue.vueSelectedMode, vue.vueSelectedModeModaleCreer)
            }
            "rejoindreModeButton" -> {
                vue.changeVue(vue.vueSelectedMode, vue.vueSelectedModeModaleRejoidre)
            }
            "modalCreerCancelButton" -> {
                vue.changeVue(vue.vueSelectedMode)
            }
            "modalRejoindreCancelButton" -> {
                vue.changeVue(vue.vueSelectedMode)
            }
            "replayButton" -> {
                vue.changeVue(vue.vueSelectName)
            }
        }
    }

}
