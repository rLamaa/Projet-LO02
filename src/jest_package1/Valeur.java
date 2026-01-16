package jest_package1;

/**
 * Énumération représentant les valeurs possibles des cartes du jeu de Jest.
 * Les valeurs numériques associées sont : AS(1), DEUX(2), TROIS(3), QUATRE(4).
 * 
 * @author LO02 Project Team
 * @version 1.0
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