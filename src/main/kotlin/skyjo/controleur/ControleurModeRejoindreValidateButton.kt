package skyjo.controleur

import Card
import ControleurCard
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import kotlinx.coroutines.*
import skyjo.AppConfig
import skyjo.modele.Carte
import skyjo.modele.Partie
import skyjo.vue.VueGeneral

class ControleurModeRejoindreValidateButton(val vue: VueGeneral, val modele: Partie) : EventHandler<ActionEvent> {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun handle(event: ActionEvent) {
        try{
            val partieID = vue.vueSelectedModeModaleRejoidre.getTextfield().text.toInt()
            try {
                runBlocking { modele.commencerPartie(idPartie = partieID) }
            } catch (e: IllegalArgumentException) {
                vue.vueSelectedModeModaleRejoidre.hideRedBox()
                vue.vueSelectedModeModaleRejoidre.updateMessageError("Veuillez renseigner \n un numéro de partie valide")
                vue.vueSelectedModeModaleRejoidre.showRedBox()
                return
            } catch (e: IllegalStateException) {
                vue.vueSelectedModeModaleRejoidre.hideRedBox()
                vue.vueSelectedModeModaleRejoidre.updateMessageError("La partie ne peut pas \n accepter de joueur supplémentaire")
                vue.vueSelectedModeModaleRejoidre.showRedBox()
                return
            }
        } catch (e: NumberFormatException) {
            vue.vueSelectedModeModaleRejoidre.hideRedBox()
            vue.vueSelectedModeModaleRejoidre.updateMessageError("Le format renseigné n'est pas compatible")
            vue.vueSelectedModeModaleRejoidre.showRedBox()
            return
        }

        scope.launch {
            while (isActive) {
                delay(800)
                withContext(Dispatchers.IO) { modele.recupEtapePartie() }

                if (modele.donneEtape() == "PARTIE_TERMINEE") {
                    withContext(Dispatchers.Main) {
                        val result = withContext(Dispatchers.IO) { modele.recupScore() }
                        val sortedResult = result?.sortedBy { it.second }
                        val sortedResultObs = FXCollections.observableArrayList(sortedResult)
                        vue.vueResultatJeu.updateListScore(sortedResultObs!!)
                        val highestScoringPlayer = sortedResultObs.minByOrNull { it.second }
                        val playerNameWithHighestScore = highestScoringPlayer?.first
                        vue.vueResultatJeu.updateNameWinner(playerNameWithHighestScore!!)
                        vue.vueResultatJeu.selectPlayer(0)
                        vue.changeVue(vue.vueResultatJeu)
                    }
                    break
                } else if (modele.donneEtape() != "AJOUT_DES_JOUEURS") {
                    withContext(Dispatchers.Main) {
                        updateGameView()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        updateWaitingRoomView()
                    }
                }
            }
        }
    }

    private suspend fun updateGameView() {
        withContext(Dispatchers.IO) {
            modele.majIdJoueurActu()
        }
        val newDeck = modele.donneJoueur().deck.donneTableauCartes()
        vue.vueGames.updateDeck(newDeck, ControleurCard(vue, modele))

        val etape = modele.donneEtape()
        val textEtape = when (etape) {
            "AJOUT_DES_JOUEURS" -> "Ajout des joueurs"
            "PIOCHE_CARTE_OU_ECHANGE_CARTE_DEFAUSSE" -> "Sélectionner une carte dans la pioche ou dans la défausse"
            "DEFAUSSE_OU_ECHANGE_CARTE_PIOCHEE" -> "Défaussez votre carte ou échangez-la avec votre deck"
            "PARTIE_TERMINEE" -> "Partie Terminée"
            else -> ""
        }

        if (modele.donneIdJoueurActu() != modele.donneJoueur().donneId()) {
            val textTurn = "Ce n'est pas à votre tour de jouer..."
            vue.vueGames.updateInfoPlayerLabel(newTurn = textTurn, newEtape = "")
            updateAllPlayerView()
            vue.vueGames.clearDefausseStyle()
            vue.changeVue(vue.vueViewAll)
        } else {
            val textTurn = "C'est à votre tour de jouer..."
            vue.vueGames.updateInfoPlayerLabel(newTurn = textTurn, newEtape = textEtape)
            vue.changeVue(vue.vueGames)
        }

        withContext(Dispatchers.IO) {
            val id = modele.donneIdJoueurActu()
            val nom = modele.recupererNomParId(modele.donneIdJoueurActu())
            withContext(Dispatchers.Main) {
                vue.vueGames.updateActualPlayerTurnLabel(id, nom)
            }
        }

        val etatPartie = modele.donneEtatPartie()
        if (etatPartie.cartePiochee == null && etatPartie.indexJoueurCourant != modele.donneJoueur().donneId()) {
            val valeurDefausse = etatPartie.carteSommetDefausse
            withContext(Dispatchers.Main) {
                vue.vueGames.changePile(Card(120.0, 180.0), vue, modele)
                vue.vueGames.changeDefausse(Card(120.0, 180.0, valeurDefausse?.valeur?.toIntOrNull(), valeurDefausse?.couleur,AppConfig.PD_FONT_SIZE), vue, modele)
            }
        }
    }

    private suspend fun updateAllPlayerView() {
        withContext(Dispatchers.IO) {
            val nomsJoueurs = mutableListOf<String>()
            val booleansJoueurs = mutableListOf<Boolean>()
            val tableauxCartes = mutableListOf<MutableList<MutableList<Carte?>>>()
            val etat = modele.donneEtatPartie()
            val allDeck = modele.recupererDeckToutJoueur(etat)

            for (deck in allDeck) {
                val idJoueur = deck.idJoueur
                val idCourant = modele.obtenirIdJoueurCourant(etat)
                val nomJoueur = modele.recupererNomParId(idJoueur)
                val newDeck = modele.conversionDeckBisVersDeck(deck)

                nomsJoueurs.add(nomJoueur)
                booleansJoueurs.add(idJoueur == idCourant)
                tableauxCartes.add(newDeck.donneTableauCartes())
            }

            withContext(Dispatchers.Main) {
                vue.vueViewAll.createAllPlayer(tableauxCartes, nomsJoueurs, booleansJoueurs)
                vue.vueViewAll.updateInfoPlayerLabel(newTurn = "Ce n'est pas à votre \n tour de jouer...")
            }

            val etatPartie = modele.donneEtatPartie()
            val pioche = etatPartie.cartePiochee
            val piocheSommet = pioche?.let { modele.conversionCarteBisVersCarte(it) }
            val defausse = etatPartie.carteSommetDefausse
            val defausseSommet = defausse?.let { modele.conversionCarteBisVersCarte(it) }

            withContext(Dispatchers.Main) {
                vue.vueViewAll.changeDefausse(defausseSommet)
                vue.vueViewAll.changePile(piocheSommet)
            }
        }
    }

    private suspend fun updateWaitingRoomView() {
        withContext(Dispatchers.IO) {
            modele.majIdJoueurActu()
        }
        val player = modele.donneJoueur()
        val playerName: String = player.donneNom()
        val playerID: Int = player.donneId()
        val partieID: Int = modele.donneIdPartie()
        val joueurMax: Int = modele.donneNbreJoueurMax()

        withContext(Dispatchers.IO) {
            val nombreJoueur = modele.nombreDeJoueurs()
            withContext(Dispatchers.Main) {
                vue.vueWaitingRoom.updateDataJoueurPanneauTop(playerID, playerName, partieID, nombreJoueur, joueurMax, modele.donneIdPartie())
            }
        }

        withContext(Dispatchers.IO) {
            val listeJoueur = modele.recupNomJoueurs()
            val gradients = listOf(
                "linear-gradient(to bottom, #faf3d2, #f7d745)",  // Yellow gradient
                "linear-gradient(to bottom, #d8fad2, #68f54c)",  // Green gradient
                "linear-gradient(to bottom, #d5ecf5, #50cafa)",  // Blue gradient
                "linear-gradient(to bottom, #f5ced0, #fa5560)",
                "linear-gradient(to bottom, #faf3d2, #f7d745)",  // Yellow gradient
                "linear-gradient(to bottom, #d8fad2, #68f54c)",  // Green gradient
                "linear-gradient(to bottom, #d5ecf5, #50cafa)",  // Blue gradient
                "linear-gradient(to bottom, #f5ced0, #fa5560)",
            )
            withContext(Dispatchers.Main) {
                vue.vueWaitingRoom.updateMiddleListPlayer(listeJoueur, gradients)
            }
        }
        vue.changeVue(vue.vueWaitingRoom)
    }
}
