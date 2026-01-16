package jest_package1;

/**
 * Énumération représentant les différentes couleurs de cartes du jeu de Jest.
 * 
 * Comprend 7 couleurs réparties en deux catégories:
 * 
 * Couleurs Standards (jeu de base):
 * - PIQUE (♠): Couleur positive, force 7
 * - TREFLE (♣): Couleur positive, force 6
 * - CARREAU (♦): Couleur négative, force 4
 * - COEUR (♥): Couleur neutre, force 2
 * 
 * Couleurs d'Extension (si extension activée):
 * - ETOILE (☆): Double les points, force 5
 * - TRIANGLE (▲): Couleur spéciale, force 1
 * - SOLEIL (☼): Points selon pair/impair, force 3
 * 
 * Chaque couleur possède une force utilisée pour déterminer l'ordre de jeu
 * et un symbole Unicode pour l'affichage.
 * 
 * @author David et Léna
 */
public enum Couleur {
    /** Couleur Pique ♠ */
    PIQUE,
    /** Couleur Trèfle ♣ */
    TREFLE,
    /** Couleur Carreau ♦ */
    CARREAU,
    /** Couleur Cœur ♥ */
    COEUR,
    /** Couleur Étoile ☆ (extension) */
    ETOILE,
    /** Couleur Triangle ▲ (extension) */
    TRIANGLE,
    /** Couleur Soleil ☼ (extension) */
    SOLEIL;

    /**
     * Obtient la force/priorité de la couleur pour les comparaisons.
     * Plus la valeur est élevée, plus la couleur est forte.
     * 
     * @return la force de la couleur (1-7)
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
     * Obtient le symbole Unicode de la couleur.
     * 
     * @return le symbole de la couleur
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
