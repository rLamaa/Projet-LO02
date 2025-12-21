package jest_package1;

public enum Couleur {
    PIQUE, TREFLE, CARREAU, COEUR, ETOILE, TRIANGLE, SOLEIL;

    /**
     * Getter de la force de la couleur
     * 
     * @return
     */
    public int getForce() {
        switch (this) {
            case PIQUE:
                return 7;
            case TREFLE:
                return 6;
            case ETOILE:
            	return 5;
            case CARREAU:
                return 4;
            case SOLEIL:
            	return 3;
            case COEUR:
                return 2;
            case TRIANGLE:
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
            case TRIANGLE:
            	return "▲";
            case SOLEIL:
            	return "☼";
        }
        return "";
    }
}
