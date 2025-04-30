import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import javafx.scene.effect.GaussianBlur
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import skyjo.AppConfig

class VueSelectedMode : StackPane() {

    // Load the custom font
    val arteriumFont = Font.loadFont(javaClass.getResourceAsStream("/fonts/ArteriumSideExtrude.ttf"), 140.0)

    // Création des éléments de l'interface utilisateur
    val mainLabel = Label("Player ID")
    val titleLabel = Label("Skyjo Game")
    val createButton = Button("Créer").apply {
        prefWidth = 250.0
        prefHeight = 50.0
        id = "creerModeButton"
        font = AppConfig.TEXT_FONT
    }
    val joinButton = Button("Rejoindre").apply {
        prefWidth = 250.0
        prefHeight = 50.0
        id = "rejoindreModeButton"
        font = AppConfig.TEXT_FONT
    }
    val creditsLeft = Label("Skyjo Game - 2024")
    val creditsRight = Label("by Sky High")

    init {
        mainLabel.font = AppConfig.TEXT_FONT
        creditsLeft.font = AppConfig.TEXT_FONT
        creditsRight.font = AppConfig.TEXT_FONT

        createButton.styleClass.add("CreerButton")
        joinButton.styleClass.add("RejoindreButton")

        titleLabel.font = arteriumFont
        titleLabel.textFill = javafx.scene.paint.Color.BLACK

        // Add animated GIF background
        val gifImage = Image(javaClass.getResourceAsStream("/gif/backgroundMode.gif"))
        val imageView = ImageView(gifImage).apply {
            fitWidthProperty().bind(this@VueSelectedMode.widthProperty())
            fitHeightProperty().bind(this@VueSelectedMode.heightProperty())
            isPreserveRatio = false
            effect = GaussianBlur(40.0)
        }

        val darkenRectangle = Rectangle().apply {
            widthProperty().bind(this@VueSelectedMode.widthProperty())
            heightProperty().bind(this@VueSelectedMode.heightProperty())
            fill = Color.WHITE
            opacity = 0.5
        }

        // Création des conteneurs pour les éléments
        val topBox = VBox(mainLabel, titleLabel)
        topBox.alignment = Pos.CENTER
        VBox.setMargin(topBox, Insets(0.0, 0.0, 40.0, 0.0))

        val buttonBox = VBox(20.0, createButton, joinButton)
        buttonBox.alignment = Pos.CENTER

        val centerBox = VBox(20.0, topBox, buttonBox)
        centerBox.alignment = Pos.CENTER

        // Crédit gauche et droit dans des HBox distinctes
        val leftCreditBox = HBox(creditsLeft)
        leftCreditBox.alignment = Pos.CENTER_LEFT
        HBox.setHgrow(leftCreditBox, Priority.ALWAYS)
        HBox.setMargin(leftCreditBox, Insets(0.0, 0.0, 20.0, 20.0))

        val rightCreditBox = HBox(creditsRight)
        rightCreditBox.alignment = Pos.CENTER_RIGHT
        HBox.setHgrow(rightCreditBox, Priority.ALWAYS)
        HBox.setMargin(rightCreditBox, Insets(0.0, 20.0, 20.0, 0.0))

        // Conteneur pour les crédits
        val creditsContainer = HBox(leftCreditBox, rightCreditBox)
        creditsContainer.alignment = Pos.CENTER

        // Conteneur principal pour les éléments
        val content = BorderPane()
        content.center = centerBox
        content.bottom = creditsContainer

        // Ajouter le WebView et les éléments au StackPane
        this.children.addAll(imageView, darkenRectangle, content)
    }

    fun fixController(button : Button, controller : EventHandler<ActionEvent>) {
        button.onAction = controller
    }

    fun getButtonRejoindre() : Button {
        return this.joinButton
    }

    fun getButtonCreate() : Button {
        return this.createButton
    }

    fun updateIdPartie(newId: Int) {
        mainLabel.text = "ID Joueur : ${newId}"
    }

}