package jest_package1;

import java.io.Serializable;
import java.util.List;

/**
 * Interface définissant une stratégie de jeu pour les joueurs virtuels.
 * Chaque stratégie implémente la logique de décision pour les bots (choix des
 * cartes, création d'offres).
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public interface Strategie extends Serializable {
    /**
     * Choisit une carte parmi les offres disponibles selon la stratégie.
     * 
     * @param offres la liste des offres disponibles
     * @param jest   le Jest courant du joueur
     * @return le choix de carte effectué
     */
    ChoixCarte choisirCarte(List<Offre> offres, Jest jest);

    /**
     * Détermine quelles cartes cacher/afficher lors de la création d'une offre.
     * 
     * @param c1 première carte
     * @param c2 deuxième carte
     * @return une offre avec les cartes correctement positionnées
     */
    Offre choisirCartesOffre(Carte c1, Carte c2);

    /**
     * Évalue la valeur stratégique d'une offre.
     * 
     * @param offre l'offre à évaluer
     * @param jest  le Jest courant du joueur
     * @return une valeur numérique représentant l'intérêt de l'offre
     */
    int evaluerOffre(Offre offre, Jest jest);
}
