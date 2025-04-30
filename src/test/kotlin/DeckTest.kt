import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import skyjo.modele.Carte
import skyjo.modele.Deck

class DeckTest {

    lateinit var d: Deck
    @BeforeEach
    fun setUp() {
        var c = mutableListOf<MutableList<Carte?>>()
        for (i in 0..11){
            if (i%3 == 0) c.add(mutableListOf<Carte?>())
            c[i/3].add(Carte(null, "Bleue"))
            if (i<2) {
                c[0][i]!!.changeCarteVisibilite()
                c[0][i]!!.changerCarteValeur(0)
            }
        }
        d = Deck(c, null)
    }


    @Test
    fun test1DonneNombrecartes() {
        val res = d.donneNombreCartes()
        val ora = 12
        assertEquals(ora, res, "il y a 12 cartes dans le deck au départ")
    }

    @Test
    fun test2DonneNombrecartes() {
        d.supprimerColonne(mutableListOf(1))
        val res = d.donneNombreCartes()
        val ora = 9
        assertEquals(ora, res, "il y a 9 cartes dans le deck après avoir supprimer une colonne")
    }

    @Test
    fun test3DonneNombrecartes() {
        for (i in d.donneTableauCartes().indices) {
            for (j in d.donneTableauCartes()[i].indices) {
                d.retournerCarte(j,i,i)
            }
        }
        d.supprimerColonne(mutableListOf(0,1,2,3))
        val res = d.donneNombreCartes()
        val ora = 0
        assertEquals(ora, res, "il n'y a plus de cartes après avoir supprimer toute les colonnes")
    }

    @Test
    fun test1DonneNombreCarteVisible() {
        var res = d.donneNombreCartesVisibles()
        var ora = 2
        assertEquals(ora, res, "il n'y a que 2 cartes retournées au début")
    }

    @Test
    fun test2DonneNombreCarteVisible() {
        d.retournerCarte(0,1,5)
        d.retournerCarte(1,1,-2)
        var res = d.donneNombreCartesVisibles()
        var ora = 4
        assertEquals(ora, res, "il n'y a que 4 cartes retournées au début après avoir retourné 2 cartes")
    }

    @Test
    fun test3DonneNombreCarteVisible() {
        d.retournerCarte(0,1,5)
        d.retournerCarte(0,1,-2)
        var res = d.donneNombreCartesVisibles()
        var ora = 3
        assertEquals(ora, res, "il n'y a que 3 cartes retournées, une carte n'est retournée qu'un fois")
    }

    @Test
    fun test1CompareCarteVisibleNombreCarte() {
        var res = d.compareCarteVisibleNombreCarte()
        var ora = false
        assertEquals(ora, res, "au départ, toute les cartes ne sont pas retournées")
    }

    @Test
    fun test3CompareCarteVisibleNombreCarte() {
        for (i in d.donneTableauCartes().indices) {
            for (j in d.donneTableauCartes()[i].indices) {
                d.retournerCarte(j, i, 3)
            }
        }
        var res = d.compareCarteVisibleNombreCarte()
        var ora = true
        assertEquals(ora, res, "après avoir retourné toute les cartes, il y a autent de cartes retournées que de cartes dans le deck")
    }

    @Test
    fun test1SupprimerColonne() {
        for (i in d.donneTableauCartes().indices) {
            for (j in d.donneTableauCartes()[i].indices) {
                d.retournerCarte(j,i,i)
            }
        }
        d.supprimerColonne(mutableListOf(0,1,2,3))
        val res = d.donneNombreCartes()
        val ora = 0
        assertEquals(ora, res, "les colonnes ont toutes été supprimées")
    }

    @Test
    fun test2SupprimerColonne() {
        for (i in d.donneTableauCartes().indices) {
            for (j in 0..2) {
                d.retournerCarte(j,i,i)
            }
        }
        d.supprimerColonne(mutableListOf(0,1,2))
        val res = d.donneNombreCartes()
        val ora = 3
        assertEquals(ora,res,"les colonnes ont bien été supprimées")
    }

    @Test
    fun test3SupprimerColonne() {
        for (i in d.donneTableauCartes().indices) {
            for (j in 0..2) {
                d.retournerCarte(j,i,i*3+j)
            }
        }
        d.supprimerColonne(mutableListOf(0,1,2,3))
        val res = d.donneNombreCartes()
        val ora = 12
        assertEquals(ora, res, "Aucune colonne n'a été supprimée")
    }
/*
    fun modifierMain()

    fun remplacerCarte()

    fun comptePoints()
*/

}
