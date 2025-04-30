package skyjo.modele

class Carte (valeur : Int?, col : String) {

    private var valeur : Int?
    private var couleur : String
    private var visible : Boolean

    init {
        this.valeur = valeur
        this.couleur = col
        visible = valeur != null
    }

    fun donneValeur() : Int? = valeur

    fun donneCouleur() : String = couleur

    fun estVisible() : Boolean = visible

    fun changeCarteVisibilite() {
        visible = !visible
    }

    fun changerCarteValeur(nouvelleValeur : Int) {
        this.valeur = nouvelleValeur
    }

    fun changerCarteCouleur(nouvelleCouleur: String) {
        this.couleur = nouvelleCouleur
    }

    override fun toString(): String {
        return "(Carte:$valeur; $couleur; $visible)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Carte

        if (valeur != other.valeur) return false
        if (couleur != other.couleur) return false

        return true
    }

    override fun hashCode(): Int {
        var result = valeur ?: 0
        result = 31 * result + couleur.hashCode()
        return result
    }
}