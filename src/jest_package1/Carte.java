package jest_package1;

public abstract class Carte {
    protected Couleur couleur;
    protected int valeur;

    public int accepter(VisiteurScore visiteur, Jest jest) { return 0; }
    public Couleur getCouleur() { return couleur; }
    public int getValeur() { return valeur; }
    public boolean estAs() { 
    	return false; 
    	}
    public String toString() { return ""; }
}

