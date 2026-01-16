package jest_package1;

/**
 * Interface définissant le patron Visitor pour le calcul des scores.
 * 
 * Chaque méthode correspond à une couleur, cette approche permet 
 * d'ajouter ou modifier des règles sans difficulté.
 * 
 * @author David et Léna
 */
public interface VisiteurScore {
    /**
     * Visite une carte Pique et calcule sa contribution au score.
     * 
     * @param carte la carte Pique
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterPique(CarteCouleur carte, Jest jest);

    /**
     * Visite une carte Trèfle et calcule sa contribution au score.
     * 
     * @param carte la carte Trèfle
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterTrefle(CarteCouleur carte, Jest jest);

    /**
     * Visite une carte Carreau et calcule sa contribution au score.
     * 
     * @param carte la carte Carreau
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterCarreau(CarteCouleur carte, Jest jest);

    /**
     * Visite une carte Cœur et calcule sa contribution au score.
     * 
     * @param carte la carte Cœur
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterCoeur(CarteCouleur carte, Jest jest);

    /**
     * Visite une carte Étoile et calcule sa contribution au score.
     * 
     * @param carte la carte Étoile
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterEtoile(CarteCouleur carte, Jest jest);

    /**
     * Visite une carte Triangle et calcule sa contribution au score.
     * 
     * @param carte la carte Triangle
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterTriangle(CarteCouleur carte, Jest jest);

    /**
     * Visite une carte Soleil et calcule sa contribution au score.
     * 
     * @param carte la carte Soleil
     * @param jest  le Jest contenant la carte
     * @return la contribution au score
     */
    int visiterSoleil(CarteCouleur carte, Jest jest);

    /**
     * Visite un Joker et calcule sa contribution au score.
     * 
     * @param carte le Joker
     * @param jest  le Jest contenant le Joker
     * @return la contribution au score
     */
    int visiterJoker(Joker carte, Jest jest);

    /**
     * Calcule le score complet d'un Jest.
     * 
     * @param jest le Jest à évaluer
     * @return le score total du Jest
     */
    int calculerScore(Jest jest);
}
