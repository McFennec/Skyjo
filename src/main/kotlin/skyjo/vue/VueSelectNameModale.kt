package skyjo.vue

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import skyjo.AppConfig

class VueSelectNameModale : StackPane() {
    private val vBoxAcceuil = VBox().apply {
        alignment = Pos.CENTER
        spacing = 10.0
    }
    private val yesButton = Button("Oui").apply {
        prefHeight = 40.0
        prefWidth = 100.0
        id = "OuiSelectName"
        font = AppConfig.TEXT_FONT

    }
    private val noButton = Button("Non").apply {
        prefHeight = 40.0
        prefWidth = 100.0
        id = "NonSelectName"
        font = AppConfig.TEXT_FONT
    }

    private val confirmationDialog = HBox().apply {
        alignment = Pos.CENTER

        val hbox = HBox(yesButton, noButton).apply {
            alignment = Pos.CENTER
            spacing = 20.0
            padding = Insets(20.0,0.0,0.0,0.0)
        }
        val titleRules = Label("Regles")
        titleRules.font = AppConfig.SUB_TITLE_FONT

        val vbox = VBox(titleRules,Label("Voulez vous suivre le didacticiel de jeu\n" +
                "pour apprendre les règles ?").apply { font = AppConfig.TEXT_FONT }, hbox).apply {
            alignment = Pos.CENTER
            spacing = 10.0
            style = "-fx-background-color: white;-fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 14px;"
            maxHeight = Region.USE_PREF_SIZE
            padding = Insets(50.0)
        }
        children.add(vbox)
    }

    private val overlay = Pane().apply {
        style = "-fx-background-color: rgba(0, 0, 0, 0.8);"
    }

    init {

        noButton.styleClass.add("redButton")
        yesButton.styleClass.add("greenButton")

        children.addAll(overlay, confirmationDialog)
    }


    fun getButtonYes() : Button {
        return this.yesButton
    }

    fun getButtonNo() : Button {
        return this.noButton
    }

    fun fixButtonControleur(buttonFix: Button, controleur: EventHandler<ActionEvent>) {
        buttonFix.onAction = controleur
    }
}