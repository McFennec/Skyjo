import javafx.event.EventHandler
import javafx.geometry.*
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import skyjo.AppConfig
import skyjo.controleur.ControleurDefausse
import skyjo.controleur.ControleurPioche
import skyjo.modele.Carte
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class VueGames : GridPane() {

    val cardsBox = GridPane().apply {
        hgap = 10.0
        vgap = 10.0
        alignment = Pos.CENTER
    }

    // Création de 2 labels
    private val playerTurnLabel = Label().apply {
        font = AppConfig.SUB_TITLE_FONT
        padding = Insets(30.0, 0.0, 0.0, 0.0)
    }
    private val turnInfoLabel = Label().apply {
        font = AppConfig.TEXT_FONT
    }

    private val turnEtapeLabel = Label().apply {
        style = "-fx-text-fill: green;"
        font = AppConfig.TEXT_FONT
        prefWidth = 680.0
    }

    // Création de la fausse et de la pile + label. Positionnement de la fausse au centre de sa colonne
    private var fausseCard = Card(120.0, 180.0)
    private var pileCard = Card(120.0, 180.0)
    private var fausseLabel = Label("Défausse")
    private var pileLabel = Label("Pile")
    private val pileBox = VBox(10.0, pileCard, pileLabel).apply {
        alignment = Pos.CENTER
    }
    private val fausseBox = VBox(10.0, fausseCard, fausseLabel).apply {
        alignment = Pos.CENTER
    }

    init {
        fausseLabel.font = AppConfig.TEXT_FONT
        pileLabel.font = AppConfig.TEXT_FONT

        fausseLabel.padding = Insets(3.0)
        pileLabel.padding = Insets(3.0)
        padding = Insets(20.0)
        hgap = 20.0
        vgap = 20.0
    }

    private class Spacer : Region() {
        init {
            VBox.setVgrow(this, Priority.ALWAYS)
        }
    }

    fun changePile(newCard : Card,vue : VueGeneral, modele : Partie) {
        pileBox.children.clear()
        fixCardControleur(newCard, ControleurPioche(vue, modele))
        pileBox.children.addAll(newCard,pileLabel)
    }

    fun changeDefausse(newCard : Card, vue : VueGeneral, modele : Partie) {
        fausseBox.children.clear()
        fixCardControleur(newCard, ControleurDefausse(vue, modele))
        fausseBox.children.addAll(newCard,fausseLabel)
    }


    fun fixDeckControleur(controleur: EventHandler<MouseEvent>) {
        for (node in cardsBox.children) {
            node.onMouseClicked = controleur
        }
    }



    fun getPioche() : Card = pileCard

    fun getDefausse() : Card = fausseCard

    fun fixCardControleur(cardFix:Card,controleur: EventHandler<MouseEvent>) {
        cardFix.onMouseClicked = controleur
    }

    fun updateDeck(newDeck : MutableList<MutableList<Carte?>>, controleur : EventHandler<MouseEvent>) {
        cardsBox.children.clear()
        cardsBox.apply {
            for (i in 0 until newDeck.size) {
                for (j in 0 until newDeck[i].size) {
                    val cardData = newDeck[i][j]
                    val cardValue = cardData?.donneValeur()
                    val cardColor = cardData?.donneCouleur()
                    val card = Card(AppConfig.VG_WIDTH_CARD, AppConfig.VG_HEIGHT_CARD, cardValue, cardColor,AppConfig.CARD_VG_FONT_SIZE)
                    card.onMouseClicked = controleur
                    add(card, i, j)
                }
            }
        }

        children.clear()
        add(cardsBox, 1, 0)

        val pileFausseBox = HBox(20.0, fausseBox, pileBox).apply {
            alignment = Pos.CENTER
            VBox.setVgrow(this, Priority.ALWAYS)
        }
        val labelsBox = VBox(20.0, playerTurnLabel, turnInfoLabel,turnEtapeLabel).apply {
            alignment = Pos.CENTER
        }
        val leftContent = VBox(20.0, labelsBox, pileFausseBox).apply {
            alignment = Pos.TOP_LEFT
        }
        add(leftContent, 0, 0)
        val rightContent = VBox(10.0).apply {
            children.addAll(
                Spacer(),
                Spacer()
            )
            alignment = Pos.TOP_RIGHT
        }

        add(rightContent, 2, 0)

        setHgrow(leftContent, Priority.ALWAYS)
        setVgrow(leftContent, Priority.ALWAYS)

        setHgrow(rightContent, Priority.ALWAYS)
        setVgrow(rightContent, Priority.ALWAYS)

    }

    fun defausseSelected() {
        fausseLabel.background = Background.fill(Color.LIGHTGREEN)
    }

    fun clearDefausseStyle() {
        fausseLabel.background = Background.fill(Color.TRANSPARENT)
    }

    fun updateInfoPlayerLabel(newTurn: String, newEtape: String) {
        turnInfoLabel.text = newTurn
        turnEtapeLabel.text = newEtape
    }

    fun updateActualPlayerTurnLabel(newId : Int, newPseudo : String) {
        playerTurnLabel.text = "Tour de ${newPseudo} (id:${newId})"
    }
}