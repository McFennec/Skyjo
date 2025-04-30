package skyjo.vue

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
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

class VueSelectName : StackPane() {

    private val closeButton = Button("X").apply {
        style = "-fx-background-color: transparent; -fx-text-fill: white;-fx-font-size: 16px;"
    }

    private val titleError = Label("Veuillez renseigner un pseudo").apply {
        textFill = javafx.scene.paint.Color.WHITE
        font = AppConfig.TEXT_FONT_REDUCED
    }

    private val hBoxAlert = HBox(titleError, closeButton).apply {
        style = "-fx-background-color: #F13232;-fx-background-radius: 5;"
        maxWidth = Region.USE_PREF_SIZE
        maxHeight = Region.USE_PREF_SIZE
        alignment = Pos.CENTER
        padding = Insets(20.0)
        spacing = 20.0
    }
    private val hBoxAlertContainer = HBox(hBoxAlert).apply {
        isVisible = false
        maxWidth = Region.USE_PREF_SIZE
        maxHeight = Region.USE_PREF_SIZE
        alignment = Pos.CENTER
        padding = Insets(30.0)
    }

    private val vBoxAcceuil = VBox().apply {
        alignment = Pos.CENTER
        spacing = 40.0
    }
    private val title = Label("Skyjo Game")
    private val idTitle = Label("Pseudo")
    private val textFieldID = TextField()
    private val serverTitle = Label("Liste des serveurs")
    private val listeServer = FXCollections.observableArrayList("http://172.26.82.23","http://172.26.82.18","http://172.26.82.13")
    private val comboboxServer = ComboBox<String>(listeServer)
    private val validationButton = Button("Valider").apply { id = "ValiderSelectName" }
    private val label1 = Label("Skyjo Game - 2024")
    private val label2 = Label("by Sky High")
    private val vBoxContainAccueil = VBox().apply {
        alignment = Pos.CENTER
        spacing = 10.0
    }

    init {

        // Add animated GIF background
        val gifImage = Image(javaClass.getResourceAsStream("/gif/backgroundName.gif"))
        val imageView = ImageView(gifImage).apply {
            fitWidthProperty().bind(this@VueSelectName.widthProperty())
            fitHeightProperty().bind(this@VueSelectName.heightProperty())
            isPreserveRatio = false
            effect = GaussianBlur(40.0)
        }

        val darkenRectangle = Rectangle().apply {
            widthProperty().bind(this@VueSelectName.widthProperty())
            heightProperty().bind(this@VueSelectName.heightProperty())
            fill = Color.WHITE
            opacity = 0.5
        }
        children.addAll(imageView, darkenRectangle)

        // Load the custom font
        val arteriumFont = Font.loadFont(javaClass.getResourceAsStream("/fonts/ArteriumSideExtrude.ttf"), 140.0)

        comboboxServer.value = listeServer[0]
        label1.font = AppConfig.TEXT_FONT
        label2.font = AppConfig.TEXT_FONT
        vBoxAcceuil.maxWidth = Region.USE_COMPUTED_SIZE
        vBoxContainAccueil.maxWidth = 250.0
        title.font = arteriumFont
        title.textFill = javafx.scene.paint.Color.BLACK
        idTitle.font = AppConfig.TEXT_FONT
        serverTitle.font = AppConfig.TEXT_FONT
        validationButton.prefWidth = 200.0
        validationButton.prefHeight = 50.0
        validationButton.font = AppConfig.TEXT_FONT
        validationButton.styleClass.add("validationSelectName")
        comboboxServer.prefWidth = 250.0
        comboboxServer.prefHeight = 40.0
        textFieldID.prefWidth = 250.0
        textFieldID.prefHeight = 40.0
        comboboxServer.stylesheets.add(javaClass.getResource("/css/flatDesign.css").toExternalForm())
        textFieldID.stylesheets.add(javaClass.getResource("/css/flatDesign.css").toExternalForm())
        vBoxContainAccueil.children.addAll(idTitle, textFieldID, serverTitle, comboboxServer, validationButton)
        vBoxAcceuil.children.addAll(title, vBoxContainAccueil)

        val hboxBottom = HBox().apply {
            spacing = 15.0
            alignment = Pos.BOTTOM_CENTER
            children.addAll(label1, Pane(), label2)
            HBox.setHgrow(children[1], Priority.ALWAYS) // Allow the Pane to grow, pushing label1 and label2 to the edges
            padding = Insets(30.0)
        }

        children.addAll(BorderPane().apply {
            center = vBoxAcceuil
            bottom = hboxBottom
            BorderPane.setAlignment(hboxBottom, Pos.BOTTOM_CENTER)
        })

        // Add the red box to the StackPane
        children.add(hBoxAlertContainer)
        // Position the red box at the top right corner
        StackPane.setAlignment(hBoxAlertContainer, Pos.TOP_RIGHT)

        // Add an action to the close button to hide the red box when clicked
        closeButton.setOnAction {
            hBoxAlertContainer.isVisible = false
        }
    }

    // Method to show the red box
    fun showRedBox() {
        hBoxAlertContainer.isVisible = true
    }

    // Method to hide the red box
    fun hideRedBox() {
        hBoxAlertContainer.isVisible = false
    }

    fun getButtonValidate() : Button {
        return this.validationButton
    }

    fun fixButtonControleur(buttonFix: Button, controleur: EventHandler<ActionEvent>) {
        buttonFix.onAction = controleur
    }

    fun getPlayerName() : String {
        return textFieldID.text
    }

    fun getCombobox() : ComboBox<String> {
        return comboboxServer
    }
}
