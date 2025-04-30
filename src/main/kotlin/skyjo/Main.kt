package kot.skyjo

import ChangeView
import ControleurCard
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.stage.Stage
import skyjo.AppConfig
import skyjo.controleur.*
import skyjo.modele.Partie
import skyjo.vue.VueGeneral
import java.nio.file.Paths

class Main : Application() {

    override fun start(primaryStage: Stage) {
        var vue: VueGeneral

        try {
            val musicFile = Paths.get("src/main/resources/audio/TROUBADOUR.mp3").toUri().toString() // Remplacez par le chemin de votre fichier audio
            val media = Media(musicFile)
            val mediaPlayer = MediaPlayer(media)
            vue = VueGeneral(mediaPlayer)
            vue.playMusic()
        } catch (e: Exception) {
            println("Erreur de configuration de l'audio : ${e.message}")
            vue = VueGeneral(null)
        } catch (e: java.io.IOException) {
            println("Erreur de configuration de l'audio : ${e.message}")
            vue = VueGeneral(null)
        }


        vue.stylesheets.add(javaClass.getResource("/css/flatDesign.css").toExternalForm())
        val modele = Partie()
        val scene = Scene(vue, AppConfig.INITIAL_WIDTH, AppConfig.INITIAL_HEIGHT)


        vue.vueSelectName.fixButtonControleur(vue.vueSelectName.getButtonValidate(), ControleurValidationButton(vue, modele))
        vue.vueSelectNameModale.fixButtonControleur(vue.vueSelectNameModale.getButtonYes(), ChangeView(vue, modele))
        vue.vueSelectName.fixButtonControleur(vue.vueSelectNameModale.getButtonNo(), ChangeView(vue, modele))
        vue.vueRegles.fixeListenerButton(ChangeView(vue, modele))

        vue.vueSelectedMode.fixController(vue.vueSelectedMode.getButtonCreate(), ChangeView(vue, modele))
        vue.vueSelectedMode.fixController(vue.vueSelectedMode.getButtonRejoindre(), ChangeView(vue, modele))
        vue.vueSelectedModeModaleCreer.fixButtonControleur(vue.vueSelectedModeModaleCreer.getButtonCancel(),ChangeView(vue, modele))
        vue.vueSelectedModeModaleCreer.fixButtonControleur(vue.vueSelectedModeModaleCreer.getButtonValidate(),ControleurModeCreerValidateButton(vue,modele))
        vue.vueSelectedModeModaleRejoidre.fixButtonControleur(vue.vueSelectedModeModaleRejoidre.getButtonCancel(), ChangeView(vue, modele))
        vue.vueSelectedModeModaleRejoidre.fixButtonControleur(vue.vueSelectedModeModaleRejoidre.getButtonValidate(), ControleurModeRejoindreValidateButton(vue, modele))

        vue.vueGames.fixDeckControleur(ControleurCard(vue,modele))
        vue.vueGames.fixCardControleur(vue.vueGames.getPioche(), ControleurPioche(vue, modele))
        vue.vueGames.fixCardControleur(vue.vueGames.getDefausse(), ControleurDefausse(vue, modele))
        vue.vueResultatJeu.fixButtonControleur(vue.vueResultatJeu.getReplayButton(), ControleurRestart(vue, modele))
        vue.vueResultatJeu.fixButtonControleurQuitter()

        primaryStage.minWidth = AppConfig.MIN_WIDTH
        primaryStage.minHeight = AppConfig.MIN_HEIGHT
        primaryStage.title = "Skyjo"
        primaryStage.scene = scene
        primaryStage.isMaximized = true
        primaryStage.show()
    }
}


fun main(){
    Application.launch(Main::class.java)
}