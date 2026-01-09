package jest_package1;

public class CarteCouleur extends Carte {
    private static final long serialVersionUID = 1L;

    public CarteCouleur(Couleur couleur, Valeur valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return valeur + couleur.getSymbole();
    }

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
