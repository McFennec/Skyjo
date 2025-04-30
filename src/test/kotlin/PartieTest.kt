import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import skyjo.modele.*
import kotlin.test.assertEquals


class PartieTest {

    private lateinit var partie : Partie
    private var idJoueur = 1
    private lateinit var deckBisStub : DeckBis

    @BeforeEach
    fun setUp() {
        partie = Partie()


        deckBisStub = DeckBis(idJoueur,
            colonnes = listOf(
                listOf(
                    CarteBis("1", "ROUGE"),
                    CarteBis("2", "BLEU"),
                    CarteBis("3", "VERT")
                ),
                listOf(
                    CarteBis("4", "JAUNE"),
                    CarteBis("5", "ROUGE"),
                    CarteBis("6", "BLEU")
                ),
                listOf(
                    CarteBis("7", "VERT"),
                    CarteBis("8", "JAUNE"),
                    CarteBis("9", "ROUGE")
                ),
                listOf(
                    CarteBis("10", "BLEU"),
                    CarteBis("11", "VERT"),
                    CarteBis("12", "JAUNE")
                )
            )
        )
    }

    @Test
    fun `test conversionDeckBisVersDeck`() {
        val deckBis = DeckBis(idJoueur,
            colonnes = listOf(
                listOf(CarteBis("1", "ROUGE"), CarteBis("2", "BLEU")),
                listOf(CarteBis("3", "VERT"), CarteBis("4", "JAUNE"))
            )
        )
        val deck = Deck(
            mutableListOf(
                mutableListOf(Carte(1, "ROUGE"), Carte(2, "BLEU")),
                mutableListOf(Carte(3, "VERT"), Carte(4, "JAUNE"))
            )
        )
        var newDeck = partie.conversionDeckBisVersDeck(deckBis)

        println(deck.donneTableauCartes()::class)
        println(newDeck.donneTableauCartes()::class)

        assertEquals(deck.donneTableauCartes(), newDeck.donneTableauCartes())
    }

    @Test
    fun `test conversionDeckBisVersDeck avec DeckBis vide`() {
        // Arrange
        val deckBis = DeckBis(idJoueur = 1, colonnes = emptyList())
        val partie = Partie()

        // Act
        val result = partie.conversionDeckBisVersDeck(deckBis)

        // Assert
        val expectedDeck = Deck(mutableListOf())
        assertEquals(expectedDeck, result)
    }

    @Test
    fun `test obtenirCarte`() {
        val carteCherchee = partie.obtenirCarte(deckBisStub, 2,1)
        assertEquals(CarteBis("8", "JAUNE"), carteCherchee)
    }


    @Test
    fun `recupererDeckAutreJoueur retourne les decks des autres joueurs`() {

        val deck1 = DeckBis(idJoueur = 1, colonnes = listOf(listOf(CarteBis("1", "ROUGE"))))
        val deck2 = DeckBis(idJoueur = 2, colonnes = listOf(listOf(CarteBis("2", "BLEU"))))
        val deck3 = DeckBis(idJoueur = 3, colonnes = listOf(listOf(CarteBis("3", "VERT"))))
        val etatPartie = EtatPartieBis(
            nbJoueursMax = 3,
            etape = "SOME_STEP",
            carteSommetDefausse = null,
            cartePiochee = null,
            indexJoueurCourant = 0,
            plateaux = listOf(deck1, deck2, deck3),
            indexJoueurTerminant = -1
        )


        // Act
        val result = partie.recupererDeckToutJoueur(etatPartie, 1)

        // Assert
        val expected = listOf(deck1, deck2, deck3)
        assertEquals(expected, result)
    }

    @Test
    fun `recupererDeckAutreJoueur retourne une liste vide si aucun joueur`() {

        val deck1 = DeckBis(idJoueur = 1, colonnes = listOf(listOf(CarteBis("1", "ROUGE"))))
        val etatPartie = EtatPartieBis(
            nbJoueursMax = 1,
            etape = "SOME_STEP",
            carteSommetDefausse = null,
            cartePiochee = null,
            indexJoueurCourant = 0,
            plateaux = listOf(),
            indexJoueurTerminant = -1
        )


        // Act
        val result = partie.recupererDeckToutJoueur(etatPartie, 1)

        // Assert
        val expected = emptyList<DeckBis>()
        assertEquals(expected, result)
    }


    @Test
    fun `test retourneNombreString avec une chaine normale`() {
        var chaine = "bonjour 123"
        var attendu = 123
        var resultat = partie.retourneNombreString(chaine)
        assertEquals(attendu, resultat)
    }


    @Test
    fun `test retourneNombreString avec une chaine vide`() {
        var chaine = ""
        assertThrows<NumberFormatException> { partie.retourneNombreString(chaine) }
    }

    @Test
    fun `test retourneNombreString avec une chaine sans chiffre`() {
        var chaine = "bonjour"
        assertThrows<NumberFormatException> { partie.retourneNombreString(chaine) }
    }


    @Test
    fun `test conversionCarteBisVersCarte avec un nombre valide`() {
        val carteBis = CarteBis("2", "ROUGE")
        val carte = Carte(2, "ROUGE")
        val resultat = partie.conversionCarteBisVersCarte(carteBis)
        assertEquals(carte, resultat)
    }

    @Test
    fun `test conversionCarteBisVersCarte avec carteBis une carte retournée`() {
        val carteBis = CarteBis("X", "ROUGE")
        val carte = Carte(null, "ROUGE")
        val resultat = partie.conversionCarteBisVersCarte(carteBis)
        assertEquals(carte, resultat)
    }


    @Test
    fun `test conversionCarteBisVersCarte avec une valeur negative`() {
        val carteBis = CarteBis("-1", "ROUGE")
        val carte = Carte(-1, "ROUGE")
        val resultat = partie.conversionCarteBisVersCarte(carteBis)
        assertEquals(carte, resultat)
    }

    @Test
    fun `test conversionCarteBisVersCarte avec un string vide`() {
        val carteBis = CarteBis("", "ROUGE")
        val carte = Carte(null, "ROUGE")
        val resultat = partie.conversionCarteBisVersCarte(carteBis)
        assertEquals(carte, resultat)
    }

    @Test
    fun `test definirIpServeur classique`() {
        var chaine = "http://172.26.82.23"
        partie.definirServeur(chaine)
        assertEquals(chaine, partie.donneIpServeur())
    }


    @Test
    fun `test definirIpServeur avec chaine vide`() {
        var chaine = ""
       assertThrows<IllegalArgumentException> { partie.definirServeur(chaine) }
    }

    @Test
    fun `test obtenirIdJoueurCourant`() {
        var json = """
                    {
                        "nbJoueursMax": 2,
                        "etape": "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE",
                        "carteSommetDefausse": {"valeur": "1", "couleur": "X"},
                        "cartePiochee": {"valeur": "-2", "couleur": "X"},
                        "indexJoueurCourant": 1,
                        "plateaux": [
                            {"idJoueur": 618, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]},
                            {"idJoueur": 619, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]}
                        ],
                        "indexJoueurTerminant": -1
                    }
                """
        var etatPartie = Json{ignoreUnknownKeys = true}.decodeFromString<EtatPartieBis>(json)
        var resultat = partie.obtenirIdJoueurCourant(etatPartie)
        assertEquals(resultat, 619)
    }

    @Test
    fun `test obtenirIdJoueurCourant ct2`() {
        var json = """
                    {
                        "nbJoueursMax": 2,
                        "etape": "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE",
                        "carteSommetDefausse": {"valeur": "1", "couleur": "X"},
                        "cartePiochee": {"valeur": "-2", "couleur": "X"},
                        "indexJoueurCourant": 0,
                        "plateaux": [
                            {"idJoueur": 618, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]},
                            {"idJoueur": 619, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]}
                        ],
                        "indexJoueurTerminant": -1
                    }
                """
        var etatPartie = Json{ignoreUnknownKeys = true}.decodeFromString<EtatPartieBis>(json)
        var resultat = partie.obtenirIdJoueurCourant(etatPartie)
        assertEquals(resultat, 618)
    }

    @Test
    fun `test recupDefausse`() {
        var json = """
                    {
                        "nbJoueursMax": 2,
                        "etape": "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE",
                        "carteSommetDefausse": {"valeur": "1", "couleur": "X"},
                        "cartePiochee": {"valeur": "-2", "couleur": "X"},
                        "indexJoueurCourant": 0,
                        "plateaux": [
                            {"idJoueur": 618, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]},
                            {"idJoueur": 619, "colonnes": [
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}],
                                [{"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}, {"valeur": "_", "couleur": "X"}]
                            ]}
                        ],
                        "indexJoueurTerminant": -1
                    }
                """
        var etatPartie = Json{ignoreUnknownKeys = true}.decodeFromString<EtatPartieBis>(json)
        var resultat = partie.recupDefausse(etatPartie)
        assertEquals(resultat, CarteBis("1", "X"))

    }


}



