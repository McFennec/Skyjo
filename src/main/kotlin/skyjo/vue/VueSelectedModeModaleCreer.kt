import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import javafx.scene.layout.*
import skyjo.AppConfig

class VueSelectedModeModaleCreer : StackPane() {

    private val cancelButton = Button("Annuler").apply {
        padding = Insets(10.0,20.0,10.0,20.0)
        id = "modalCreerCancelButton"
        font = AppConfig.TEXT_FONT
    }
    val validateButton = Button("Valider").apply {
        padding = Insets(10.0,20.0,10.0,20.0)
        id = "modalCreerValidateButton"
        font = AppConfig.TEXT_FONT
    }

    private val spinner = Spinner<Int>(2, 8, 2)
    private val createDialog = HBox().apply {
        alignment = Pos.CENTER

        cancelButton.styleClass.add("redButtonMode")
        val title = Label("Nombre de joueur :").apply {
            font = AppConfig.SUB_TITLE_FONT
        }


        val subtitle = Label("jouable de 2 à 8 joueurs").apply {
            font = AppConfig.TEXT_FONT
        }

        val textField = TextField()
        spinner.prefHeight = 40.0
        spinner.prefWidth = 200.0

        validateButton.styleClass.add("greenButtonMode")
        val buttonBox = HBox(cancelButton, validateButton).apply {
            alignment = Pos.CENTER
            spacing = 20.0
        }

        val vbox = VBox(title, subtitle, spinner, buttonBox).apply {
            alignment = Pos.CENTER
            spacing = 15.0
            style = "-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;"
            maxHeight = Region.USE_PREF_SIZE
            padding = Insets(50.0)
        }

        children.add(vbox)
    }

    private val overlay = Pane().apply {
        style = "-fx-background-color: rgba(0, 0, 0, 0.8);"
    }

    init {
        children.addAll(overlay, createDialog)
    }

    fun getButtonCancel() : Button {
        return cancelButton
    }

    fun getButtonValidate() : Button {
        return validateButton
    }

    fun getSpinner() : Spinner<Int> {
        return spinner
    }

    fun fixButtonControleur(buttonFix: Button, controleur: EventHandler<ActionEvent>) {
        buttonFix.onAction = controleur
    }

}