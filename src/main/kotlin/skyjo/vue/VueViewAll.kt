import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.*
import skyjo.AppConfig
import skyjo.modele.Carte


class VueViewAll : StackPane() {
    private val arrayPlayers : MutableList<VBox> = mutableListOf()
    private val centerBox = VBox(10.0)
    private val hBoxCenter = HBox(20.0)
    private val vBoxDefausse = VBox(5.0)
    private val labelDefausse = Label("Defausse")
    private val vBoxPioche = VBox(5.0)
    private val labelPioche = Label("Pioche")
    private val labelTurn = Label()
    private val vBoxTurn = VBox().apply {
        alignment = Pos.CENTER
        children.add(labelTurn)
    }
    private val root = GridPane()

    init {
        labelDefausse.font = AppConfig.TEXT_FONT
        labelPioche.font = AppConfig.TEXT_FONT
        labelTurn.font = AppConfig.TEXT_FONT
        labelDefausse.alignment = Pos.CENTER
        labelPioche.alignment = Pos.CENTER
        labelTurn.alignment = Pos.CENTER
        hBoxCenter.alignment = Pos.CENTER
        centerBox.alignment = Pos.CENTER

        vBoxPioche.alignment = Pos.CENTER
        vBoxDefausse.alignment = Pos.CENTER
        hBoxCenter.children.addAll(vBoxDefausse, vBoxPioche)

        centerBox.children.add(hBoxCenter)

        if (arrayPlayers.size%2==0) {
            centerBox.children.add(vBoxTurn)
        }else {
            GridPane.setRowIndex(vBoxTurn,1)
            GridPane.setColumnIndex(vBoxTurn,2)
            root.children.add(vBoxTurn)
        }

        root.alignment = Pos.CENTER
        root.hgap = 25.0

        this.padding = Insets(10.0)

        this.children.addAll(root)
        this.alignment = Pos.TOP_RIGHT

    }


    fun createAllPlayer(tableauxCartes: MutableList<MutableList<MutableList<Carte?>>>, nomsJoueurs : MutableList<String>, booleansJoueurs: MutableList<Boolean>) {
        arrayPlayers.clear()
        root.children.clear()
        centerBox.children.clear()

        for (i in tableauxCartes.indices) {
            val deck = tableauxCartes[i]
            val playerName = nomsJoueurs[i]
            val isCurrentPlayer = booleansJoueurs[i]
            val vBoxPlayer = createPlayerBox(deck, playerName, isCurrentPlayer)
            VBox.setVgrow(vBoxPlayer, Priority.ALWAYS)
            vBoxPlayer.maxWidth = Double.MIN_VALUE
            arrayPlayers.add(vBoxPlayer)
        }
        centerBox.children.add(hBoxCenter)

        if (arrayPlayers.size%2==0) {
            centerBox.children.add(vBoxTurn)
        }else {
            GridPane.setRowIndex(vBoxTurn,1)
            GridPane.setColumnIndex(vBoxTurn,2)
            root.children.add(vBoxTurn)
        }



        root.alignment = Pos.CENTER
        root.hgap = 25.0
        arrangePlayers(arrayPlayers.size)
    }

    private fun createPlayerBox(deck : MutableList<MutableList<Carte?>>,playerName: String, isCurrentPlayer : Boolean): VBox {
        val playerBox = VBox(5.0).apply {
            alignment = Pos.CENTER
            children.addAll(
                GridPane().apply {
                    hgap = 5.0
                    vgap = 5.0
                    padding = Insets(2.5)
                    for (i in 0 until deck.size) {
                        for (j in 0 until deck[i].size) {
                            add(Card(AppConfig.VVA_WIDTH_CARD, AppConfig.VVA_HEIGHT_CARD, deck[i][j]?.donneValeur(),  deck[i][j]?.donneCouleur(),AppConfig.CARD_VVA_FONT_SIZE), i,j)
                        }
                    }
                    if (isCurrentPlayer) {
                        style = "-fx-border-color: green; -fx-border-width: 2; -fx-border-radius: 10px;"
                    }
                },
                Label(playerName).apply {
                    font = AppConfig.TEXT_FONT
                }
            )
        }
        return playerBox
    }

    private fun arrangePlayers(playerCount: Int) {
        val positions = when (playerCount) {
            2 -> listOf(Pair(0, 0), Pair(0, 2), Pair(0, 1))
            3 -> listOf(Pair(1, 0), Pair(0, 2), Pair(1, 4),Pair(2,2))
            4 -> listOf(Pair(1, 0), Pair(0, 2), Pair(1, 4), Pair(2, 2),Pair(1,2))
            5 -> listOf(Pair(1, 0), Pair(0, 1), Pair(0, 3), Pair(2, 3), Pair(2, 1),Pair(1,4))
            6 -> listOf(Pair(1, 0), Pair(0, 1), Pair(0, 3), Pair(1, 4), Pair(2, 3), Pair(2, 1),Pair(1,2))
            7 -> listOf(Pair(1, 0), Pair(0, 1),Pair(0, 2), Pair(0, 3), Pair(1, 4), Pair(2, 3), Pair(2, 1),Pair(2,2))
            else -> listOf(Pair(1, 0), Pair(0, 1),Pair(0, 2), Pair(0, 3), Pair(1, 4), Pair(2, 3), Pair(2, 2),Pair(2, 1),Pair(1,2))
        }

        for (i in 0 until playerCount) {
            val (row, col) = positions[i]
            if (!root.children.contains(arrayPlayers[i])) {
                GridPane.setRowIndex(arrayPlayers[i], row)
                GridPane.setColumnIndex(arrayPlayers[i], col)
                root.children.add(arrayPlayers[i])
            }
        }

        val (rowCenterBox, colCenterBox) = positions[playerCount]
        if (!root.children.contains(centerBox)) {
            GridPane.setRowIndex(centerBox, rowCenterBox)
            GridPane.setColumnIndex(centerBox, colCenterBox)
            root.children.add(centerBox)
        }
    }

    fun updateInfoPlayerLabel(newTurn: String) {
        labelTurn.text = newTurn
    }

    fun changePile(newCard : Carte?) {
        vBoxPioche.children.clear()
        vBoxPioche.children.addAll(Card(AppConfig.VVA_WIDTH_CARD, AppConfig.VVA_HEIGHT_CARD, newCard?.donneValeur(),newCard?.donneCouleur(),AppConfig.CARD_VVA_FONT_SIZE), labelPioche)
    }

    fun changeDefausse(newCard : Carte?) {
        vBoxDefausse.children.clear()
        vBoxDefausse.children.addAll(Card(AppConfig.VVA_WIDTH_CARD, AppConfig.VVA_HEIGHT_CARD, newCard?.donneValeur(),newCard?.donneCouleur() ,AppConfig.CARD_VVA_FONT_SIZE), labelDefausse)
    }
}
