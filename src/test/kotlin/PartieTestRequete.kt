import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.serialization.decodeFromString
import skyjo.modele.*
import kotlin.test.assertEquals


class PartieTestRequete {
    private lateinit var partieJoueur1: Partie
    private lateinit var partieJoueur2: Partie
    private var idPartie : Int = 0
    private var client = HttpClient(CIO)
    private var idJoueurActu = 0


    @BeforeEach
    fun setUp() {
        partieJoueur1 = Partie()
        partieJoueur2 = Partie()

        partieJoueur1.definirServeur("http://172.26.82.23")
        partieJoueur2.definirServeur("http://172.26.82.23")

        runBlocking { partieJoueur1.nouveauJoueur("Test1") }
        runBlocking { partieJoueur1.commencerPartie(null, 2) }

        idPartie = partieJoueur1.donneIdPartie()

        runBlocking { partieJoueur2.nouveauJoueur("Test2") }
        runBlocking { partieJoueur2.commencerPartie(idPartie) }

        runBlocking { partieJoueur1.utiliserDebugMode() }
        runBlocking { partieJoueur1.majIdJoueurActu() }
        idPartie = partieJoueur2.donneIdPartie()
        idJoueurActu = partieJoueur2.donneIdJoueurActu()


    }

    @Test
    fun `test commencerPartie`() = runBlocking {
        var result = client.get("http://172.26.82.23/partie/$idPartie").body<String>()
        var etatPartie = Json.decodeFromString<EtatPartieBis>(result)
        var json = """
                    {
                        "nbJoueursMax": 2,
                        "etape": "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE",
                        "carteSommetDefausse": {"valeur": "1", "couleur": "X"},
                        "cartePiochee": null,
                        "indexJoueurCourant": 1,
                        "plateaux": [
                            {"idJoueur": ${partieJoueur1.donneJoueur().donneId()}, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]},
                            {"idJoueur": ${partieJoueur2.donneJoueur().donneId()}, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]}
                        ],
                        "indexJoueurTerminant": -1
                    }
                """
        var resultat = Json.decodeFromString<EtatPartieBis>(json)

        assertEquals(resultat, etatPartie)

    }

    @Test
    fun `test joueurPioche`() = runBlocking {

        val etatPartie = partieJoueur1.donneEtatPartie()
        val listeJoueur = partieJoueur2.recupererIdsJoueurs(etatPartie)
        idJoueurActu = listeJoueur[etatPartie.indexJoueurCourant]
        partieJoueur2.majIdJoueurActu()


        partieJoueur2.joueurPioche()
        partieJoueur2.recupEtapePartie()

        assertEquals("DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE", partieJoueur2.donneEtape())
        assertEquals(Carte(-2, "X"), partieJoueur2.donneJoueur().deck.donneMain())
    }


    @Test
    fun `test recupNomJoueurs`() = runBlocking {

        var oracle = mutableListOf("Test1", "Test2")
        var resultat = partieJoueur1.recupNomJoueurs()
        assertEquals(oracle, resultat)
    }

    @Test
    fun `test joueurDefaussePioche`() = runBlocking {

        var etatPartie = partieJoueur1.donneEtatPartie()
        val listeJoueur = partieJoueur2.recupererIdsJoueurs(etatPartie)
        idJoueurActu = listeJoueur[etatPartie.indexJoueurCourant]
        partieJoueur2.majIdJoueurActu()


        partieJoueur2.joueurPioche()


        partieJoueur2.joueurDefaussePioche(0,0)
        etatPartie = partieJoueur2.donneEtatPartie()
        var deck = etatPartie.plateaux.filter { it.idJoueur == partieJoueur2.donneJoueur().donneId() }
        var resultat = partieJoueur2.obtenirCarte(deck[0], 0,0)
        assertEquals(CarteBis("5", "X"), resultat)
        assertEquals(CarteBis("-2", "X"), etatPartie.carteSommetDefausse)
        assertEquals(null, partieJoueur2.donneJoueur().deck.donneMain())
        assertEquals(partieJoueur2.donneJoueur().deck.donneTableauCartes()[0][0], Carte(5, "X"))
    }


    @Test
    fun `test joueurEchangePioche`() = runBlocking {

        var etatPartie = partieJoueur1.donneEtatPartie()
        val listeJoueur = partieJoueur2.recupererIdsJoueurs(etatPartie)
        idJoueurActu = listeJoueur[etatPartie.indexJoueurCourant]
        partieJoueur2.majIdJoueurActu()
        var carteEchangee = CarteBis("5", "X")


        partieJoueur2.joueurPioche()


        partieJoueur2.joueurEchangePioche(0,0)
        etatPartie = partieJoueur2.donneEtatPartie()
        var deck = etatPartie.plateaux.filter { it.idJoueur == partieJoueur2.donneJoueur().donneId() }
        var resultat = partieJoueur2.obtenirCarte(deck[0], 0,0)
        assertEquals(CarteBis("-2", "X"), resultat)
        assertEquals("PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE", etatPartie.etape)
        assertEquals(partieJoueur1.donneJoueur().donneId(), partieJoueur2.donneIdJoueurActu())
        assertEquals(carteEchangee, etatPartie.carteSommetDefausse)
        assertEquals(partieJoueur2.donneJoueur().deck.donneTableauCartes()[0][0], Carte(-2, "X"))
    }

    @Test
    fun `test joueurEchangeDefausse`() = runBlocking {

        var etatPartie = partieJoueur1.donneEtatPartie()
        val listeJoueur = partieJoueur2.recupererIdsJoueurs(etatPartie)
        idJoueurActu = listeJoueur[etatPartie.indexJoueurCourant]
        partieJoueur2.majIdJoueurActu()
        var carteEchangee = CarteBis("5", "X")

        partieJoueur2.joueurEchangeDefausse(0,0)

        etatPartie = partieJoueur2.donneEtatPartie()
        var deck = etatPartie.plateaux.filter { it.idJoueur == partieJoueur2.donneJoueur().donneId() }
        var resultat = partieJoueur2.obtenirCarte(deck[0], 0,0)
        assertEquals(CarteBis("1", "X"), resultat)
        assertEquals("PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE", etatPartie.etape)
        assertEquals(0, etatPartie.indexJoueurCourant)
        assertEquals(carteEchangee, etatPartie.carteSommetDefausse)
        assertEquals(partieJoueur2.donneJoueur().deck.donneTableauCartes()[0][0], Carte(1, "X"))
    }



}