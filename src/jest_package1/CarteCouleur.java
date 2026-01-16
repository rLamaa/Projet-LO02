package jest_package1;

/**
 * Classe représentant une carte standard du jeu de Jest.
 * 
 * Étend la classe abstraite Carte et fournit l'implémentation 
 * pour une carte avec couleur et valeur.
 * 
 * Implémente le Patron Visitor en acceptant différents visiteurs de score
 * qui calculent la contribution de la carte au score final en fonction de sa
 * couleur.
 * 
 * @author David et Léna
 */
public class CarteCouleur extends Carte {
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur d'une carte couleur.
     * 
     * @param couleur la couleur de la carte
     * @param valeur  la valeur de la carte
     */
    public CarteCouleur(Couleur couleur, Valeur valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
    }

    /**
     * Représentation textuelle de la carte.
     * Format: Valeur + Symbole (ex: "4♠", "2♥")
     * 
     * @return la représentation textuelle
     */
    @Override
    public String toString() {
        return valeur + couleur.getSymbole();
    }

    /**
     * Accepte un visiteur de score basé sur la couleur de la carte.
     * Implémente le patron Visitor.
     * 
     * @param visiteur le visiteur de score
     * @param jest     le Jest dans lequel la carte est jouée
     * @return le score calculé par le visiteur
     */
    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        switch (couleur) {
            case PIQUE:
                return visiteur.visiterPique(this, jest);
            case TREFLE:
                return visiteur.visiterTrefle(this, jest);
            case CARREAU:
                return visiteur.visiterCarreau(this, jest);
            case COEUR:
                return visiteur.visiterCoeur(this, jest);
            case ETOILE:
                return visiteur.visiterEtoile(this, jest);
            case TRIANGLE:
                return visiteur.visiterTriangle(this, jest);
            case SOLEIL:
                return visiteur.visiterSoleil(this, jest);
        }
        return 0;
    }
}
