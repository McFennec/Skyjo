package skyjo.vue

import VueGames
import VueSelectedMode
import VueSelectedModeModaleCreer
import VueSelectedModeModaleRejoindre
import VueViewAll
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

class VueGeneral(val mediaPlayer: MediaPlayer?) : StackPane() {
    var vueGames = VueGames()
    var vueRegles = VueRegles()
    var vueResultatJeu = VueResultatJeu()
    var vueSelectName = VueSelectName()
    var vueSelectNameModale = VueSelectNameModale()
    var vueSelectedMode = VueSelectedMode()
    var vueSelectedModeModaleCreer = VueSelectedModeModaleCreer()
    var vueSelectedModeModaleRejoidre = VueSelectedModeModaleRejoindre()
    var vueViewAll = VueViewAll()
    var vueWaitingRoom = VueWaitingRoom()

    init {
        mediaPlayer?.let {
            playMusic()
        }
        //vueWaitingRoom.updateDataJoueurPanneauTop(1, "toto", 5678, 2, 5, 9)
        this.children.add(vueSelectName)
    }

    fun changeVue(newVue: Node, modale: Node? = null) {
        this.children.clear()
        if (modale != null) {
            this.children.addAll(newVue, modale)
        } else {
            this.children.add(newVue)
        }
    }

    fun playMusic() {
        mediaPlayer?.cycleCount = MediaPlayer.INDEFINITE
        mediaPlayer?.play()
    }
}