package skyjo.vue

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.effect.GaussianBlur
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import skyjo.AppConfig

class VueResultatJeu : StackPane() {

    private val centerPane = VBox().apply {
        alignment = Pos.CENTER
        spacing = 10.0
    }

    private val listViewResultats = ListView<String>().apply {
        style = """
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-padding: 10;
        """.trimIndent()
    }

    private val replayButton = Button("Rejouer").apply { id = "replayButton" }
    private val returnButton = Button("Quitter")
    private val nameWinner = Label("")


    init {

        replayButton.styleClass.add("greenButtonMode")
        replayButton.font = AppConfig.TEXT_FONT
        returnButton.styleClass.add("redButtonMode")
        returnButton.font = AppConfig.TEXT_FONT

        // Add animated GIF background
        val gifImage = Image(javaClass.getResourceAsStream("/gif/backgroundResultats.gif"))
        val imageView = ImageView(gifImage).apply {
            fitWidthProperty().bind(this@VueResultatJeu.widthProperty())
            fitHeightProperty().bind(this@VueResultatJeu.heightProperty())
            isPreserveRatio = false
            effect = GaussianBlur(20.0)
        }

        val darkenRectangle = Rectangle().apply {
            widthProperty().bind(this@VueResultatJeu.widthProperty())
            heightProperty().bind(this@VueResultatJeu.heightProperty())
            fill = Color.BLACK
            opacity = 0.8
        }

        val title = Label("Résultats du jeu")
        title.font = AppConfig.TITLE_FONT
        title.textFill = Color.WHITE


        nameWinner.font = AppConfig.SUB_TITLE_FONT
        nameWinner.textFill = Color.YELLOW

        // Désactiver la sélection de la ListView
        listViewResultats.isFocusTraversable = false
        listViewResultats.isMouseTransparent = true // Désactive les événements de souris

        val playerScorePane = BorderPane().apply {
            center = listViewResultats
            maxWidth = 500.0 // Limiter la largeur de la liste
            maxHeight = 200.0 // Limiter la hauteur de la liste
        }

        replayButton.padding = Insets(10.0)
        returnButton.padding = Insets(10.0)

        val bottomPane = HBox().apply {
            spacing = 60.0
            alignment = Pos.CENTER
            children.addAll(replayButton, returnButton)
            padding = Insets(10.0)
        }

        centerPane.children.addAll(title, nameWinner, playerScorePane, bottomPane)
        children.addAll(imageView, darkenRectangle,centerPane)
        StackPane.setAlignment(centerPane, Pos.CENTER)

        this.style = "-fx-background-color: black;"
    }

    fun fixButtonControleur(button: Button, controleur: EventHandler<ActionEvent>) {
        button.onAction = controleur
    }

    fun fixButtonControleurQuitter() {
        returnButton.setOnAction { Platform.exit() }
    }

    fun getReplayButton(): Button {
        return replayButton
    }

    fun updateListScore(scores : ObservableList<Pair<String, Int>>) {
        listViewResultats.items.clear()
        for (score in scores) {
            listViewResultats.items.add("${score.first}: ${score.second} points")
        }
    }

    // Méthode pour sélectionner un joueur dans la ListView
    fun selectPlayer(playerIndex: Int) {
        listViewResultats.selectionModel.clearAndSelect(playerIndex)
    }

    fun updateNameWinner(name: String) {
        nameWinner.text = "Le gagnant est ${name}"
    }
}
