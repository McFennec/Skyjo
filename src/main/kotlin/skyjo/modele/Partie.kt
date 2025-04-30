package skyjo.modele
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString



class Partie {

    private var etape: String = "AJOUT_DES_JOUEURS"
    private var idPartie: Int = 0
    private val client = HttpClient(CIO)
    private var ipServeur: String = ""
    private lateinit var joueur: Joueur
    private var idJoueurActu: Int = 0
    private var nbreJoueurMax : Int = 0

    private var cartePiocheDepuis : String = ""

    fun donnePiocheDepuis() : String = cartePiocheDepuis

    fun changePiocheDepuis(nouvString : String) {
        cartePiocheDepuis = nouvString
    }
    
    /**
     * Démarre une nouvelle partie ou rejoint une partie existante.
     * Initialise l'état de la partie et le deck du joueur.
     */
    suspend fun commencerPartie(idPartie: Int? = null, nbreJoueur: Int? = null) {
        val url = if (idPartie != null) {
            "$ipServeur/partie/$idPartie/${joueur.donneId()}/rejoint"
        } else {
            if (nbreJoueur!! > 8 && nbreJoueur < 2) {
                throw IllegalArgumentException("nbreJoueur doit etre entre 2 et 8")
            }
            val creationPartie = client.get("$ipServeur/partie/nouvelle/${joueur.donneId()}/${nbreJoueur}")
            val idNouvellePartie = retourneNombreString(creationPartie.body<String>())
            this.idPartie = idNouvellePartie
            "$ipServeur/partie/$idNouvellePartie"
        }


        val response = client.get(url)
        if (response.body<String>().contains("plus d'ajout de joueurs à id partie=")) {
            throw IllegalStateException("La partie est pleine.")
        }
        val etatPartie : EtatPartieBis
        try {
            etatPartie = Json { ignoreUnknownKeys = true }.decodeFromString<EtatPartieBis>(response.body<String>())
        } catch (e: MissingFieldException) {
            throw IllegalArgumentException("L'ID de la partie est invalide ou la partie n'existe pas.", e)
        }

        if (idPartie != null) {
            this.idPartie = idPartie
        }

        val plateauJoueur = etatPartie.plateaux.find { it.idJoueur == joueur.donneId() }
        val nouveauDeck = conversionDeckBisVersDeck(plateauJoueur!!)
        nbreJoueurMax = etatPartie.nbJoueursMax
        joueur.ajouterDeck(nouveauDeck)
        idJoueurActu = obtenirIdJoueurCourant(etatPartie)!!
        etape = etatPartie.etape

    }

    /**
     * Crée un nouveau joueur avec le nom donné et l'ajoute au serveur.
     */
    suspend fun nouveauJoueur(nom: String) {
        var newNom = remplacerEspaces(nom)
        val response = client.get("$ipServeur/joueur/nouveau/$newNom").body<String>()
        val id = retourneNombreString(response)
        joueur = Joueur(nom, id)
    }

    /**
     * Permet au joueur actuel de piocher une carte.
     * Modifie l'étape de la partie et met à jour la main du joueur.
     */
    suspend fun joueurPioche() {
        recupEtapePartie()
        if (etape == "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE" && joueur.donneId() == idJoueurActu) {
            client.get("$ipServeur/partie/$idPartie/${joueur.donneId()}/pioche")
            val result = client.get("$ipServeur/partie/$idPartie")
            val etatPartie = Json.decodeFromString<EtatPartieBis>(result.body<String>())
            val cartePiochee = etatPartie.cartePiochee!!
            val newCarte = conversionCarteBisVersCarte(cartePiochee)
            joueur.deck.modifierMain(newCarte)
            etape = etatPartie.etape
        }
    }

    /**
     * Permet au joueur actuel d'échanger une carte avec celle de la défausse.
     * Met à jour l'état de la partie et la main du joueur.
     */
    suspend fun joueurEchangeDefausse(colonne: Int, ligne: Int) {
        recupEtapePartie()
        if (etape == "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE" && joueur.donneId() == idJoueurActu) {
            val response1 = client.get("$ipServeur/partie/$idPartie")
            val etatPartie = Json.decodeFromString<EtatPartieBis>(response1.body<String>())
            val carteDefausse = etatPartie.carteSommetDefausse
            val newCarte = conversionCarteBisVersCarte(carteDefausse!!)
            joueur.deck.modifierMain(newCarte)

            client.get("$ipServeur/partie/$idPartie/${joueur.donneId()}/echangedefausse/${colonne+1}/${ligne+1}")
            joueur.deck.remplacerCarte(ligne, colonne)
            joueur.deck.modifierMain()
            finDeTour(etatPartie)
        }
    }


    /**
     * Permet au joueur actuel de défausser une carte et d'en piocher une nouvelle.
     * Met à jour l'état de la partie et la main du joueur.
     */
    suspend fun joueurDefaussePioche(colonne: Int, ligne: Int) {
        recupEtapePartie()
        if (etape == "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE" && joueur.donneId() == idJoueurActu) {
            val response = client.get("$ipServeur/partie/$idPartie/${joueur.donneId()}/defaussepioche/${colonne+1}/${ligne+1}")
            val etatPartie = Json.decodeFromString<EtatPartieBis>(response.body<String>())
            val plateauJoueur = etatPartie.plateaux.find { it.idJoueur == joueur.donneId() }
            val carteRetournee = obtenirCarte(plateauJoueur, colonne, ligne)
            val nouvelleCarte = conversionCarteBisVersCarte(carteRetournee!!)
            val valeurCarteRetournee = nouvelleCarte.donneValeur()
            val couleurCarteRetournee = nouvelleCarte.donneCouleur()
            joueur.deck.retournerCarte(ligne, colonne, valeurCarteRetournee!!, couleurCarteRetournee)
            joueur.deck.modifierMain()

            finDeTour(etatPartie)
        }
    }

    /**
     * Permet au joueur actuel d'échanger une carte piochée avec une carte de sa main.
     * Met à jour l'état de la partie et la main du joueur.
     */
    suspend fun joueurEchangePioche(colonne: Int, ligne: Int) {
        recupEtapePartie()
        if (etape == "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE" && joueur.donneId() == idJoueurActu) {
            val response = client.get("$ipServeur/partie/$idPartie/${joueur.donneId()}/echangepioche/${colonne+1}/${ligne+1}")
            val etatPartie = Json.decodeFromString<EtatPartieBis>(response.body<String>())
            joueur.deck.remplacerCarte(ligne, colonne)
            joueur.deck.modifierMain()

            finDeTour(etatPartie)
        }
    }


    /**
     * Methode qui réinitialise la partie
     */
    fun reinitialisePartie() {
        this.etape = "AJOUT_DES_JOUEURS"
    }



    /**
     * Methode qui récupère les scores lorsque la partie est terminée
     */
    suspend fun recupScore() : ObservableList<Pair<String,Int>>? {
        recupEtapePartie()
        if (etape == "PARTIE_TERMINEE") {
            val response = client.get("$ipServeur/partie/$idPartie/score").body<String>()
            val scores: List<JoueurScore> = Json.decodeFromString(response)

            val scoresObservableList: ObservableList<Pair<String,Int>> = FXCollections.observableArrayList()
            for (score in scores) {
                val playerName = recupererNomParId(score.idJoueur)
                scoresObservableList.add(Pair(playerName, score.score))
            }

            return scoresObservableList
        }
        return null
    }


    /**
     * Récupère et met à jour l'étape actuelle de la partie.
     */
    suspend fun recupEtapePartie() {
        val response = client.get("$ipServeur/partie/$idPartie").body<String>()
        val etatPartie = Json.decodeFromString<EtatPartieBis>(response)
        etape = etatPartie.etape
    }

    /**
     * Récupère les decks de tous les joueurs de la partie.
     */
    fun recupererDeckToutJoueur(etatPartie: EtatPartieBis) : List<DeckBis> {
        return etatPartie.plateaux
    }

    /**
     * Obtient une carte spécifique d'un deck donné selon la colonne et la ligne.
     */
    fun obtenirCarte(deck: DeckBis?, colonne: Int, ligne: Int): CarteBis? {
        return deck?.colonnes?.getOrNull(colonne)?.getOrNull(ligne)
    }

    /**
     * Récupère la carte au sommet de la pile de défausse.
     */
    fun recupDefausse(etatPartie: EtatPartieBis) : CarteBis? {
        return etatPartie.carteSommetDefausse
    }

    /**
     * Récupère les noms de tous les joueurs de la partie.
     */
    suspend fun recupNomJoueurs(): MutableList<String> {
        val result = client.get("$ipServeur/partie/$idPartie").body<String>()
        val etatPartie = Json.decodeFromString<EtatPartieBis>(result)
        val listeJoueursID = recupererIdsJoueurs(etatPartie)
        val listeNomsJoueur: MutableList<String> = mutableListOf()

        for (id in listeJoueursID) {
            val response = client.get("$ipServeur/joueur/$id").body<String>()
            val joueurNom = Json.decodeFromString<JoueurNom>(response)
            listeNomsJoueur.add(joueurNom.nom)
        }
        return listeNomsJoueur
    }

    /**
     * Methode qui renvoie l'etat actuel de la partie
     */
    suspend fun donneEtatPartie() : EtatPartieBis {
        val result = client.get("$ipServeur/partie/$idPartie").body<String>()
        val etatPartie = Json.decodeFromString<EtatPartieBis>(result)
        return etatPartie
    }


    /**
     * Récupère les identifiants des joueurs de la partie.
     */
    fun recupererIdsJoueurs(etatPartie: EtatPartieBis): List<Int> {
        return etatPartie.plateaux.map { it.idJoueur }
    }

    /**
     * Methode qui renvoie le nom d'un joueur a partir de son ID
     */
    suspend fun recupererNomParId(id : Int) : String {
        val response = client.get("$ipServeur/joueur/$id").body<String>()
        val joueurNom = Json.decodeFromString<JoueurNom>(response)
        return joueurNom.nom
    }

    /**
     * Remplace les espaces d'un pseudo par un "_"
     */
    fun remplacerEspaces(chaine: String): String {
        return chaine.replace(" ", "_")
    }

    /**
     * Cette méthode prend une chaîne de caractères en entrée et retourne un entier qui représente les nombres trouvés dans la chaîne.
     */
    fun retourneNombreString(chaine: String): Int {
        var res = ""
        for (chara in chaine) {
            if (chara.isDigit()) {
                res += chara
            }
        }
        return res.toInt()
    }

    /**
     * Cette méthode convertit un objet de type DeckBis en un objet de type Deck.
     */
    fun conversionDeckBisVersDeck(deck: DeckBis): Deck {
        val tabCartes: MutableList<MutableList<Carte?>> = mutableListOf()
        for (colonne in deck.colonnes) {
            val cartesColonne: MutableList<Carte?> = mutableListOf()
            for (carte in colonne) {
                val newCarte: Carte = conversionCarteBisVersCarte(carte)
                cartesColonne.add(newCarte)
            }
            tabCartes.add(cartesColonne)
        }
        val nouveauDeck = Deck(tabCartes)

        return nouveauDeck
    }

    /**
     * Cette méthode convertit un objet de type CarteBis en un objet de type Carte.
     */
    fun conversionCarteBisVersCarte(carte: CarteBis): Carte {
       return Carte(carte.valeur.toIntOrNull(), carte.couleur)
    }

    /**
     * Cette méthode définit l'adresse IP du serveur en vérifiant si elle est dans un format valide.
     */
    fun definirServeur(ipServeur: String) {
        val ipRegex = """^http://(\d{1,3}\.){3}\d{1,3}$""".toRegex()
        if (ipServeur.matches(ipRegex)) {
            this.ipServeur = ipServeur
        } else {
            throw IllegalArgumentException("Format d'adresse IP invalide. Utilisez le format 'http://X.X.X.X' avec X étant des nombres.")
        }
    }

    /**
     * Cette méthode retourne l'ID du joueur actuellement en cours.
     */
    fun obtenirIdJoueurCourant(etatPartie: EtatPartieBis): Int? {
        val index = etatPartie.indexJoueurCourant
        return etatPartie.plateaux.getOrNull(index)?.idJoueur
    }

    /**
     * Cette methode met a jour le joueur actuel
     */
    suspend fun majIdJoueurActu() {
        val result = client.get("$ipServeur/partie/$idPartie").body<String>()
        val etatPartie = Json.decodeFromString<EtatPartieBis>(result)
        idJoueurActu = obtenirIdJoueurCourant(etatPartie)!!
    }

    /**
     * Cette méthode gère la fin du tour, supprime les colonnes de cartes sur le deck du joueur et met à jour l'étape de la partie.
     */
    private suspend fun finDeTour(etatPartie: EtatPartieBis) {
        idJoueurActu = this.obtenirIdJoueurCourant(etatPartie)!!
        etape = etatPartie.etape
        val colonnesSurppimables: MutableList<Int> = joueur.deck.estSupprimable()
        joueur.deck.supprimerColonne(colonnesSurppimables)
        recupEtapePartie()
    }

    /**
     * Cette méthode récupère le nombre de joueurs dans la partie en cours.
     */
    suspend fun nombreDeJoueurs() : Int {
        val result = client.get("$ipServeur/partie/$idPartie").body<String>()
        val etatPartie = Json.decodeFromString<EtatPartieBis>(result)
        val nbreJoueur = etatPartie.plateaux.size
        return nbreJoueur

    }

    /**
     * Cette méthode active le mode debug de la partie en cours.
     */
    suspend fun utiliserDebugMode() {
        client.get("$ipServeur/partie/$idPartie/debug")
    }

    /**
     * Renvoie l'objet Joueur
     */
    fun donneJoueur(): Joueur {
        return this.joueur
    }

    /**
     * Renvoie la chaine qui représente l'ip du serveur
     */
    fun donneIpServeur(): String {
        return this.ipServeur
    }

    /**
     * renvoie l'id de la partie
     */
    fun donneIdPartie(): Int = this.idPartie

    /**
     * renvoie l'id du joueur actuel
     */
    fun donneIdJoueurActu(): Int = this.idJoueurActu

    /**
     * renvoie l'etape actuel de la partie
     */
    fun donneEtape(): String = this.etape

    /**
     * renvoie le nombre maximum de joueur dans la partie
     */
    fun donneNbreJoueurMax(): Int = this.nbreJoueurMax
}

