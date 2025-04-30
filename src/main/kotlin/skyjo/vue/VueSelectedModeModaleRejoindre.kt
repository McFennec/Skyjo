import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.paint.Paint
import skyjo.AppConfig
import java.awt.Color

class VueSelectedModeModaleRejoindre : StackPane() {

    private val cancelButton = Button("Annuler").apply {
        padding = Insets(10.0,20.0,10.0,20.0)
        id = "modalRejoindreCancelButton"
        font = AppConfig.TEXT_FONT
    }

    private val validateButton = Button("Valider").apply {
        padding = Insets(10.0,20.0,10.0,20.0)
        id = "modalRejoindreValidateButton"
        font = AppConfig.TEXT_FONT
    }

    private val closeButton = Button("X").apply {
        // Style the close button
        style = "-fx-background-color: transparent; -fx-text-fill: white;-fx-font-size: 16px;"
    }

    private val titleError = Label("Veuillez renseigner \n un numero de partie valide").apply {
        // Style the title
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

    private val textField = TextField()

    private val joinDialog = HBox().apply {
        alignment = Pos.CENTER

        cancelButton

        val title = Label("ID de la partie à rejoindre :").apply {
            font = AppConfig.TEXT_FONT_BOLD
        }

        validateButton

        val buttonBox = HBox(cancelButton, validateButton).apply {
            alignment = Pos.CENTER
            spacing = 20.0
        }

        val vbox = VBox(title, textField, buttonBox).apply {
            alignment = Pos.CENTER
            spacing = 15.0
            style = "-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;"
            maxHeight = Region.USE_PREF_SIZE
            padding = Insets(50.0)
        }

        children.add(vbox)
        style = "-fx-padding: 2; -fx-border-color: black; -fx-border-width: 2;"
    }

    private val overlay = Pane().apply {
        style = "-fx-background-color: rgba(0, 0, 0, 0.8);"
    }

    init {
        titleError.textFill = javafx.scene.paint.Color.WHITE

        textField.prefHeight = 40.0

        children.addAll(overlay, joinDialog, hBoxAlertContainer)
        // Position the red box at the top right corner
        StackPane.setAlignment(hBoxAlertContainer, Pos.TOP_RIGHT)

        // Add an action to the close button to hide the red box when clicked
        cancelButton.styleClass.add("redButtonMode")
        validateButton.styleClass.add("greenButtonMode")
        closeButton.setOnAction {
            hBoxAlertContainer.isVisible = false
        }
    }

    // Method to show the red box
    fun showRedBox() {
        hBoxAlertContainer.isVisible = true
    }

    fun updateMessageError (error : String) {
        titleError.text = error
    }

    // Method to hide the red box
    fun hideRedBox() {
        hBoxAlertContainer.isVisible = false
    }

    fun getButtonCancel() : Button {
        return cancelButton
    }

    fun getButtonValidate() : Button {
        return validateButton
    }

    fun getTextfield() : TextField {
        return textField
    }

    fun fixButtonControleur(buttonFix: Button, controleur: EventHandler<ActionEvent>) {
        buttonFix.onAction = controleur
    }

}