package skyjo.modele

class Joueur (private var nom: String, private var id: Int) {

    lateinit var deck : Deck
        private set

    fun donneNom() = nom

    fun donneId() = id

    fun ajouterDeck(nouveauDeck : Deck) {
        this.deck = nouveauDeck
    }
}