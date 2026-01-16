package jest_package1;

/**
 * Énumération représentant les valeurs possibles des cartes du jeu de Jest.
 * 
 * Le jeu utilise uniquement 4 valeurs au lieu des 13 standards:
 * - AS (valeur 1): L'as peut avoir une valeur spéciale dans certaines
 * conditions
 * - DEUX (valeur 2): Valeur de base
 * - TROIS (valeur 3): Valeur de base
 * - QUATRE (valeur 4): Valeur de base
 * 
 * Ces valeurs sont combinées avec les couleurs pour former les 16 à 28 cartes
 * du deck selon que l'extension est activée ou non.
 * 
 * @author David et Léna
 */
public enum Valeur {
    /** Valeur As (1 point) */
    AS,
    /** Valeur Deux (2 points) */
    DEUX,
    /** Valeur Trois (3 points) */
    TROIS,
    /** Valeur Quatre (4 points) */
    QUATRE;

    /**
     * Obtient la valeur numérique associée.
     * 
     * @return la valeur numérique (1-4)
     */
    public int getValeur() {
        switch (this) {
            case AS:
                return 1;
            case DEUX:
                return 2;
            case TROIS:
                return 3;
            case QUATRE:
                return 4;
        }
        return 0;
    }
}