import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

class Card(val cardWidth : Double, val cardHeight : Double, var cardValue: Int? = null, val cardColor : String? = null, val fontSize : Double? = null) : StackPane() {

    init {
        val color = when(cardColor) {
            "ROUGE" -> Color.RED
            "VERT" -> Color.GREEN
            "VIOLET" -> Color.PURPLE
            "JAUNE" -> Color.YELLOW
            "BLEU" -> Color.BLUE
            else -> null
        }
        if (cardValue != null) {
            val arteriumFont = Font.loadFont(javaClass.getResourceAsStream("/fonts/Lobster_Two/LobsterTwo-Regular.ttf"), fontSize!!)
            val rectangle = Rectangle(cardWidth, cardHeight).apply {
                arcWidth = 10.0
                arcHeight = 10.0
                fill = color
                stroke = Color.BLACK
                strokeWidth = 2.0
            }

            val textValeur = Text(cardValue.toString()).apply {
                font = arteriumFont
                fill = Color.BLACK
            }
            children.addAll(rectangle, textValeur)

        }
        else {
            val rectangle = Rectangle(cardWidth, cardHeight).apply {
                arcWidth = 10.0
                arcHeight = 10.0
                fill = Color.LIGHTGREY
                stroke = Color.BLACK
                strokeWidth = 2.0
            }
            children.add(rectangle)
        }
        alignment = Pos.CENTER
    }
}