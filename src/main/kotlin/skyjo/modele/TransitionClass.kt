package skyjo.modele

import kotlinx.serialization.Serializable

@Serializable
data class EtatPartieBis(
    val nbJoueursMax: Int,
    val etape: String,
    val carteSommetDefausse: CarteBis?,
    val cartePiochee: CarteBis?,
    val indexJoueurCourant: Int,
    val plateaux: List<DeckBis>,
    val indexJoueurTerminant: Int
)

@Serializable
data class CarteBis(
    val valeur: String,
    val couleur: String
)

@Serializable
data class DeckBis(
    val idJoueur: Int,
    val colonnes: List<List<CarteBis>>
)

@Serializable
data class JoueurNom(val nom: String)


@Serializable
data class JoueurScore(
    val idJoueur: Int,
    val score: Int
)