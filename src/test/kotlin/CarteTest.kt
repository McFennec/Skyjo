import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import skyjo.modele.Carte

class CarteTest {

    lateinit var c : Carte
    @BeforeEach
    fun setUp() {
        c = Carte(12, "ROUGE")
    }

    @Test
    fun testChangerCarteVisibilite() {
        c.changeCarteVisibilite()
        var res = c.estVisible()
        assertEquals(true, res, "La carte est retournée, visible = \"true\"")
    }

    @Test
    fun testChangerCarteValeur() {
        c.changerCarteValeur(5)
        var res = c.donneValeur()
        var ora = 5
        assertEquals(ora, res, "La carte a pour valeur 5 après changement")
    }
}

