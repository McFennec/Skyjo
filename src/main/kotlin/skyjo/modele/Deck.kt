package skyjo.modele

class Deck ( tableauCartes : MutableList<MutableList<Carte?>>, main : Carte? = null) {

    private var tableauCartes : MutableList<MutableList<Carte?>>
    private var main : Carte?
    private var points : Int
    private var nbreCartes : Int
    private var nbreCartesVisibles : Int

    init {
        this.main = main
        this.tableauCartes = tableauCartes
        this.points = 0
        this.nbreCartes = 12
        this.nbreCartesVisibles = 2
    }

    fun donneMain() : Carte? = main

    fun donneNombreCartes() = nbreCartes

    fun donneNombreCartesVisibles() = nbreCartesVisibles

    fun compareCarteVisibleNombreCarte() : Boolean {
        return nbreCartes == nbreCartesVisibles
    }

    fun estSupprimable() : MutableList<Int> {
        val res = mutableListOf<Int>()
        for (indiceColonne in tableauCartes.indices) {
            val col = tableauCartes[indiceColonne]
            if (col[0]!!.donneValeur() == col[1]!!.donneValeur() && col[1]!!.donneValeur() == col[2]!!.donneValeur() && col[0]!!.donneValeur() != null) {
                res.add(indiceColonne)
            }
        }
        return res
    }

    fun supprimerColonne(colonnes : MutableList<Int>) {
        val indicesTries = colonnes.sortedDescending()

        for (i in indicesTries) {
            if (estSupprimable().contains(i)) {
                tableauCartes.removeAt(i)
                nbreCartes -= 3
                nbreCartesVisibles -= 3
            }
        }
    }

    fun modifierMain(nouvelleMain : Carte? = null) {
        main = nouvelleMain
    }

    fun remplacerCarte(ligne : Int, colonne : Int) {
        if (colonne !in tableauCartes.indices) throw NoSuchElementException("Cette colonne n'existe pas")
        if (ligne !in tableauCartes[colonne].indices) throw NoSuchElementException("Cette ligne n'existe pas")

        val nouvelleMain = tableauCartes[colonne][ligne]
        if (!tableauCartes[colonne][ligne]!!.estVisible()) nbreCartesVisibles++
        tableauCartes[colonne][ligne] = main
        modifierMain(nouvelleMain)
    }

    fun retournerCarte(ligne : Int, colonne : Int, valeur : Int, couleur : String) {
        if (colonne !in tableauCartes.indices) throw NoSuchElementException("Cette colonne n'existe pas")
        if (ligne !in tableauCartes[colonne].indices) throw NoSuchElementException("Cette ligne n'existe pas")

        if (!tableauCartes[colonne][ligne]!!.estVisible()) {
            tableauCartes[colonne][ligne]!!.changeCarteVisibilite()
            tableauCartes[colonne][ligne]!!.changerCarteValeur(valeur)
            tableauCartes[colonne][ligne]!!.changerCarteCouleur(couleur)
            nbreCartesVisibles++
        }
    }


    fun donneTableauCartes() : MutableList<MutableList<Carte?>> = tableauCartes


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Deck

        return tableauCartes == other.tableauCartes
    }

    override fun hashCode(): Int {
        return tableauCartes.hashCode()
    }


}