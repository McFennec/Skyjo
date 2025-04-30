package skyjo.vue

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import skyjo.AppConfig

class VueRegles : BorderPane() {
    private val labelRule: Label
    private val buttonOk: Button
    private val labelTitle1: Label
    private val labelTitle2: Label
    private val labelTitle3: Label
    private val label1: Label
    private val label2: Label
    private val label3: Label
    private val labelCredits: Label
    private val labelTeamName: Label
    private val bottomHBox: HBox
    private val bottomLeftHBox: HBox
    private val bottomRightHBox: HBox
    private val bottomCenterHBox: HBox

    init {
        labelRule = Label("Règles du jeu")
        labelRule.font = AppConfig.TITLE_FONT
        val hbox = HBox()
        hbox.children.add(labelRule)
        hbox.alignment = Pos.CENTER
        this.top = hbox
        setMargin(hbox, Insets(20.0))

        labelTitle1 = Label("But du jeu")
        labelTitle1.font = AppConfig.SUB_TITLE_FONT
        label1 = Label(
            "Le but du jeu est de terminer la partie en ayant la plus petite valeur cumulée de ses douze cartes. Les douze cartes de chaque joueur sont initialement face cachée sur\n" +
                    "la table, le jeu consiste donc à tenter de se débarrasser des cartes à grosse valeur en les remplaçant progressivement par des plus petites cartes. Au fur et à mesure que le\n" +
                    "jeu progresse, de plus en plus de cartes sont révélées. Une partie se termine lorsque toutes les cartes d'un joueur sont retournées.\n"
        )
        label1.isWrapText = true
        label1.font = AppConfig.TEXT_FONT

        labelTitle2 = Label("Contenu du jeu")
        labelTitle2.font = AppConfig.SUB_TITLE_FONT
        label2 = Label(
            "Le jeu se compose de 150 cartes de valeurs de –2 à +12.\n" +
                    "\n" +
                    "La distribution des cartes de +1 à +12 est de dix fois chacune, le –1 apparaît également dix fois, tandis que le 0 apparaît quinze fois et que le –2 n'apparaît que cinq fois.\n"
        )
        label2.isWrapText = true
        label2.font = AppConfig.TEXT_FONT

        labelTitle3 = Label("Déroulement du jeu")
        labelTitle3.font = AppConfig.SUB_TITLE_FONT
        label3 = Label(
            "Au début d'une partie, les cartes sont mélangées et chaque joueur reçoit douze cartes qu'il dispose devant lui face cachée en quatre colonnes de trois cartes. Les cartes\n" +
                    "restantes forment une pioche face cachée qui est placée au milieu de la table, et la première carte de la pioche est placée face visible à côté comme pile de défausse\n" +
                    "\n" +
                    "Au début de la partie, chaque joueur retourne au hasard deux de ses cartes. En additionnant ces deux cartes, c'est le joueur ayant la plus grosse valeur cumulée qui doit commencer. Chaque joueur choisit ensuite, tour à tour, soit la première carte de la pioche (face cachée), soit la première carte de la défausse (face visible). S'il choisit la pioche, il la regarde et peut soit la défausser, soit l'échanger contre une de ses cartes (cachées ou visibles). S'il a choisi de la défausser, il doit révéler n'importe laquelle de ses cartes. Dans le cas de l'échange,\n" +
                    "la nouvelle carte doit être placée face visible dans son jeu et son ancienne carte est alors défaussée, face visible. S'il choisit la carte de la défausse, elle doit être placée face visible dans son jeu à la place d'une carte de son jeu (face visible ou cachée), puis la carte remplacée est alors défaussée, face visible.\n" +
                    "\n" +
                    "Si les trois même chiffres apparaissent dans une colonne, la colonne entière est supprimée du jeu, le joueur concerné a alors l'avantage de ne jouer qu'avec neuf cartes.\n" +
                    "\n" +
                    "Une manche se joue jusqu'à ce qu'un joueur retourne sa dernière carte face cachée. Le joueur lance alors le dernier tour et tous les autres joueurs jouent à nouveau une fois. Après ce tour, tous les joueurs révèlent leurs cartes et additionnent la valeur de leurs cartes.\n"
        )
        label3.isWrapText = true
        label3.font = AppConfig.TEXT_FONT

        bottomHBox = HBox()
        bottomHBox.padding = Insets(30.0)

        bottomCenterHBox = HBox()
        buttonOk = Button("OK").apply { id = "okRegles" }
        buttonOk.font = AppConfig.TEXT_FONT
        buttonOk.styleClass.add("greenButton")
        buttonOk.setPrefSize(120.0, 50.0)
        bottomCenterHBox.children.add(buttonOk)

        bottomLeftHBox = HBox()
        labelCredits = Label("Skyjo Game - 2024")
        labelCredits.font = AppConfig.TEXT_FONT
        bottomLeftHBox.children.add(labelCredits)

        bottomRightHBox = HBox()
        labelTeamName = Label("by Sky High")
        labelTeamName.font = AppConfig.TEXT_FONT
        bottomRightHBox.children.add(labelTeamName)

        // Add margins for better spacing
        this.padding = Insets(10.0)

        // Add elements to the VBox
        val labelVbox = VBox(10.0)
        labelVbox.children.addAll(labelTitle1, label1, labelTitle2, label2, labelTitle3, label3)
        labelVbox.padding = Insets(10.0)

        val scrollPane = ScrollPane(labelVbox).apply {
            vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
            hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
            fitToWidthProperty().set(true)
            style = "-fx-border-color: black;"
        }

        // Set the max width of the labels to the width of the ScrollPane
        label1.maxWidthProperty().bind(scrollPane.widthProperty().subtract(20))
        label2.maxWidthProperty().bind(scrollPane.widthProperty().subtract(20))
        label3.maxWidthProperty().bind(scrollPane.widthProperty().subtract(20))

        this.center = scrollPane
        labelVbox.spacing = 20.0
        setMargin(labelVbox, Insets(50.0, 50.0, 50.0, 50.0))

        // Add elements to the HBox
        bottomHBox.children.addAll(bottomLeftHBox, bottomCenterHBox, bottomRightHBox)
        bottomLeftHBox.alignment = Pos.BOTTOM_LEFT
        bottomRightHBox.alignment = Pos.BOTTOM_RIGHT
        bottomCenterHBox.alignment = Pos.BOTTOM_CENTER
        bottomHBox.alignment = Pos.CENTER
        HBox.setHgrow(bottomLeftHBox, Priority.ALWAYS)
        HBox.setHgrow(bottomCenterHBox, Priority.ALWAYS)
        HBox.setHgrow(bottomRightHBox, Priority.ALWAYS)

        this.bottom = bottomHBox
    }

    fun fixeListenerButton(eventHandler: EventHandler<ActionEvent>) {
        buttonOk.onAction = eventHandler
    }
}
