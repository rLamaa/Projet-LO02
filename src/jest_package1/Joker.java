package jest_package1;

public class Joker extends Carte {
    /**
     * Identifiant de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut du Joker.
     * Initialise la couleur à {@code null} car le Joker
     * n’a pas de couleur spécifique.
     */
    public Joker() {
        this.couleur = null; // Joker n’a pas de couleur
    }

    /**
     * Retourne la représentation textuelle de la carte.
     *
     * @return la chaîne {@code "Joker"}
     */
    @Override
    public String toString() {
        return "Joker";
    }

    /**
     * Accepte un visiteur de score pour calculer la valeur
     * du Joker dans une partie donnée de Jest.
     *
     * @param visiteur le visiteur de score qui calcule le score
     * @param jest     la partie de Jest dans laquelle la carte est jouée
     * @return le score calculé par le visiteur pour cette carte
     */
    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        return visiteur.visiterJoker(this, jest);
    }
}