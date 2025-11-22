package jest_package1;


public class Joker extends Carte {
    public Joker() {
        this.couleur = null;  // Joker n’a pas de couleur
        this.valeur = 0;      // Joker vaut 0
    }

    @Override
    public String toString() {
        return "Joker";
    }

    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        return visiteur.visiterJoker(this, jest);
    }
}
