package jest_package1;

public enum Couleur {
    PIQUE, TREFLE, CARREAU, COEUR, ETOILE;

    /**
     * Getter de la force de la couleur
     * 
     * @return
     */
    public int getForce() {
        switch (this) {
            case PIQUE:
                return 4;
            case TREFLE:
                return 3;
            case CARREAU:
                return 2;
            case COEUR:
                return 1;
        }
        return 0;
    }

    /**
     * Getter du symbole de la couleur
     * 
     * @return
     */
    public String getSymbole() {
        switch (this) {
            case PIQUE:
                return "♠";
            case TREFLE:
                return "♣";
            case CARREAU:
                return "♦";
            case COEUR:
                return "♥";
            case ETOILE:
            	return "☆";
        }
        return "";
    }
}
