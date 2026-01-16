package jest_package1;

import java.io.Serializable;

/**
 * Classe abstraite représentant une carte du jeu de Jest.
 * Une carte a une couleur et une valeur.
 * Implémente le pattern Visiteur pour le calcul des scores.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public abstract class Carte implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Couleur de la carte */
    protected Couleur couleur;

    /** Valeur numérique de la carte */
    protected Valeur valeur;

    /** Indique si la carte est visible ou cachée */
    private boolean visible = false;

    /**
     * Vérifie si la carte est visible.
     * 
     * @return true si la carte est visible, false sinon
     */
    public boolean estVisible() {
        return visible;
    }

    /**
     * Définit la visibilité de la carte.
     * 
     * @param visible true pour rendre la carte visible, false pour la cacher
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Accepte un visiteur de score pour calculer la valeur de la carte.
     * Implémente le pattern Visiteur.
     * 
     * @param visiteur le visiteur de score
     * @param jest     le Jest dans lequel la carte est jouée
     * @return le score calculé par le visiteur
     */
    public int accepter(VisiteurScore visiteur, Jest jest) {
        return 0;
    }

    /**
     * Obtient la couleur de la carte.
     * 
     * @return la couleur de la carte
     */
    public Couleur getCouleur() {
        return couleur;
    }

    /**
     * Obtient la valeur de la carte.
     * 
     * @return la valeur de la carte
     */
    public Valeur getValeur() {
        return valeur;
    }

    /**
     * Obtient la valeur numérique de la carte.
     * 
     * @return la valeur numérique
     */
    public int getValeurNumerique() {
        return valeur.getValeur();
    }

    /**
     * Représentation textuelle de la carte.
     * 
     * @return la chaîne représentant la carte
     */
    public String toString() {
        return "";
    }
}
