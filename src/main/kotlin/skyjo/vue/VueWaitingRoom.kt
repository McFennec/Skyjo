package skyjo.vue

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import skyjo.AppConfig
import kotlin.random.Random

class VueWaitingRoom : BorderPane() {

    private var hBoxTop = BorderPane()
    private var hBoxMiddle = HBox()
    private var hBoxBottom = BorderPane()

    private var hBoxTopVBoxLeft = VBox()
    private var hBoxTopVBoxCenter = VBox()
    private var hBoxMiddleHBoxMiddle = HBox()
    private var hBoxBottomVBoxLeft = VBox()
    private var hBoxBottomVBoxCenter = VBox()
    private var hBoxBottomVBoxRight = VBox()

    private var partieID = Label("Numero partie :")
    private var playerID = Label("ID Joueur :")
    private var namePlayer = Label("Pseudo :")
    private var numberPlayer = Label("Joueurs ")
    private var codePartie = Label("Code de partie : ")

    private var labelWaitingRoom = Label("En attente de tout les joueurs...")
    private var credit1 = Label("Skyjo Game - 2024")
    private var credit2 = Label("by Sky High")

    init {
        partieID.font = AppConfig.TEXT_FONT_REDUCED
        playerID.font = AppConfig.TEXT_FONT_REDUCED
        namePlayer.font = AppConfig.TEXT_FONT_REDUCED
        numberPlayer.font = AppConfig.TEXT_FONT
        codePartie.font = AppConfig.TEXT_FONT
        labelWaitingRoom.font = AppConfig.TEXT_FONT
        credit1.font = AppConfig.TEXT_FONT_REDUCED
        credit2.font = AppConfig.TEXT_FONT_REDUCED

        // VBox on the left
        hBoxTopVBoxLeft.children.addAll(partieID, playerID, namePlayer)
        hBoxTopVBoxLeft.alignment = Pos.TOP_LEFT

        // VBox in the center
        hBoxTopVBoxCenter.children.addAll(codePartie, numberPlayer)
        hBoxTopVBoxCenter.alignment = Pos.CENTER

        hBoxTopVBoxLeft.padding = Insets(20.0)

        // Add VBoxes to the HBox
        hBoxTop.top = hBoxTopVBoxLeft
        hBoxTop.center = hBoxTopVBoxCenter

        // Ensure VBox on the left stays left aligned
        HBox.setHgrow(hBoxTopVBoxLeft, Priority.NEVER)
        HBox.setHgrow(hBoxTopVBoxCenter, Priority.ALWAYS)

        // Bottom layout
        hBoxBottomVBoxLeft.children.add(credit1)
        hBoxBottomVBoxLeft.alignment = Pos.BOTTOM_LEFT
        hBoxBottomVBoxLeft.padding = Insets(10.0)

        hBoxBottomVBoxCenter.children.add(labelWaitingRoom)
        hBoxBottomVBoxCenter.alignment = Pos.CENTER
        hBoxBottomVBoxCenter.padding = Insets(40.0)

        hBoxBottomVBoxRight.children.add(credit2)
        hBoxBottomVBoxRight.alignment = Pos.BOTTOM_RIGHT
        hBoxBottomVBoxRight.padding = Insets(10.0)

        hBoxBottomVBoxCenter
        val hBoxCredits = HBox(hBoxBottomVBoxLeft, hBoxBottomVBoxRight)
        hBoxCredits.padding = Insets(10.0)

        HBox.setHgrow(hBoxBottomVBoxLeft, javafx.scene.layout.Priority.ALWAYS)
        HBox.setHgrow(hBoxBottomVBoxRight, javafx.scene.layout.Priority.ALWAYS)

        hBoxBottom.bottom = hBoxCredits
        hBoxBottom.center = hBoxBottomVBoxCenter

        // Style the codePartie label
        codePartie.style = "-fx-font-size: 20px; -fx-font-weight: bold;"

        val scrollPane = ScrollPane(hBoxMiddleHBoxMiddle).apply {
            isFitToWidth = true
            isFitToHeight = true
            isPannable = true
            hbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
            vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
            style = "-fx-border-radius: 20px; -fx-background-radius: 20px;"
        }


        // Style hBoxMiddleHBoxMiddle
        hBoxMiddleHBoxMiddle.style = "-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 20px;"
        hBoxMiddleHBoxMiddle.padding = Insets(10.0)
        hBoxMiddleHBoxMiddle.spacing = 10.0

        // Set hBoxMiddleHBoxMiddle to take the full width and align children to the right
        HBox.setHgrow(hBoxMiddleHBoxMiddle, Priority.ALWAYS)
        hBoxMiddleHBoxMiddle.maxWidth = Double.MAX_VALUE
        hBoxMiddleHBoxMiddle.maxHeight = Double.MAX_VALUE

        hBoxMiddle.children.setAll(scrollPane)

        hBoxMiddle.padding = Insets(20.0)
        hBoxMiddle.alignment = Pos.CENTER

        this.top = hBoxTop
        this.center = hBoxMiddle
        this.bottom = hBoxBottom

    }

    fun updateDataJoueurPanneauTop(newID: Int, newName: String, newCodePartie: Int, currentPlayer: Int, totalPlayers: Int, newPartieID : Int) {
        playerID.text = "Joueur numéro $newID"
        namePlayer.text = "Pseudo : $newName"
        codePartie.text = "Code de la partie : $newCodePartie"
        numberPlayer.text = "Joueurs dans la partie $currentPlayer/$totalPlayers"
        partieID.text = "Partie numéro $newPartieID"
    }

    private fun generateRandomColor(color: String): String {
        val rand = Random.Default
        return when (color) {
            "orange" -> "rgba(${rand.nextInt(256)}, ${rand.nextInt(128)}, 0, 1)"
            "yellow" -> "rgba(${rand.nextInt(256)}, ${rand.nextInt(256)}, 0, 1)"
            "green" -> "rgba(0, ${rand.nextInt(256)}, 0, 1)"
            "blue" -> "rgba(0, 0, ${rand.nextInt(256)}, 1)"
            else -> "rgba(255, 255, 255, 1)"
        }
    }

    private fun getRandomGradient(): String {
        val colors = listOf("orange", "yellow", "green", "blue")
        val color1 = generateRandomColor(colors.random())
        val color2 = generateRandomColor(colors.random())
        return "linear-gradient(to bottom, $color1, $color2)"
    }

    fun updateMiddleListPlayer(playerNames: MutableList<String>, gradient:  List<String> ) {
        hBoxMiddleHBoxMiddle.children.clear()

        for (playerNameIndex in 0 until playerNames.size) {
            val index = playerNameIndex
            val userTile = VBox()
            val userName = Label(playerNames[index])

            // Load an image (replace with your image path or URL)
            val imageUrl = javaClass.getResourceAsStream("/images/icons8-user-48.png")  // Replace with actual image URL or path
            val imageView = ImageView(Image(imageUrl)).apply {
                fitWidth = 50.0
                fitHeight = 50.0
                isPreserveRatio = true
            }

            // Style the card
            userTile.style = """
                -fx-background-color: ${gradient[index]};
                -fx-border-color: black;
                -fx-border-width: 1px;
                -fx-border-radius: 20px;
                -fx-background-radius: 20px;
            """.trimIndent()
            userTile.padding = Insets(0.0, 50.0, 0.0, 50.0)
            userTile.alignment = Pos.CENTER
            userTile.spacing = 15.0

            userTile.children.addAll(imageView, userName)
            userTile.spacing = 20.0
            hBoxMiddleHBoxMiddle.children.add(userTile)
        }
    }
}
