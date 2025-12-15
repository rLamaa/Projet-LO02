package jest_package1;

import java.io.Serializable;

public abstract class Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Couleur couleur;
    protected Valeur valeur;

    public int accepter(VisiteurScore visiteur, Jest jest) {
        return 0;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Valeur getValeur() {
        return valeur;
    }

    public int getValeurNumerique() {
        return valeur.getValeur();
    }

    // C'est quoi l'utilité de cette methode ?
    /*
     * public boolean estAs() {
     * return this.valeur == 1;
     * }
     */

    public String toString() {
        return "";
    }
}
