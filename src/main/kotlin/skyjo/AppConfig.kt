package skyjo

import javafx.scene.text.Font

class AppConfig {
    companion object {
        // Taille de la fenêtre au démarrage
        const val INITIAL_WIDTH = 720.0
        const val INITIAL_HEIGHT = 480.0

        // Taille minimale des vues
        const val MIN_WIDTH = 900.0
        const val MIN_HEIGHT = 600.0

        // Dimension cartes deck VueViewAll
        const val VVA_WIDTH_CARD = 47.5
        const val VVA_HEIGHT_CARD = 65.0

        // Dimension cartes deck VueGames
        const val VG_WIDTH_CARD = 80.0
        const val VG_HEIGHT_CARD = 120.0

        //Taille de police des caractères de pioche et défausse
        const val PD_FONT_SIZE = 70.0

        //Taille de police des caractères des cartes de deck de VueGames
        const val CARD_VG_FONT_SIZE = 40.0

        //Taille de police des caractères des cartes de deck de VueViewAll
        const val CARD_VVA_FONT_SIZE = 25.0

        // police de texte pour les titres
        val TITLE_FONT: Font = loadFont("/fonts/Comfortaa/static/Comfortaa-Bold.ttf", 60.0)

        // police de texte pour les sous-titres
        val SUB_TITLE_FONT : Font = loadFont("/fonts/Comfortaa/static/Comfortaa-Bold.ttf",40.0)

        //police de texte normale
        val TEXT_FONT : Font = loadFont("/fonts/Comfortaa/static/Comfortaa-Regular.ttf", 20.0)

        //police de texte normale en gras
        val TEXT_FONT_BOLD : Font = loadFont("/fonts/Comfortaa/static/Comfortaa-Bold.ttf", 20.0)

        val TEXT_FONT_REDUCED : Font = loadFont("/fonts/Comfortaa/static/Comfortaa-Regular.ttf", 13.0)

        private fun loadFont(path : String, fontSize : Double): Font {
            return Font.loadFont(AppConfig::class.java.getResourceAsStream(path), fontSize)
                ?: throw IllegalArgumentException("Font not found")
        }
    }
}

