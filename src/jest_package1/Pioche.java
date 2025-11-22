package jest_package1;

import java.util.*;


public class Pioche {
    // création de la pioche
	private Stack<Carte> pioche= new Stack<>();

    //ajouter les nouvelles cartes si on ajoute une extension
	public void initialiser(boolean avecExtension) {
    	for (Couleur c : Couleur.values()) {
            for (int v = 1; v <= 4; v++) {
                pioche.add(new CarteCouleur(c, v));
            }
    	}
        pioche.add(new Joker());
        melanger();
    }
	//on mélange la pioche
    public void melanger() {
    	Collections.shuffle(pioche);
    }
    //on pioche une carte
    public Carte piocher() { 
    	return pioche.pop();
    	}
    public List<Carte> piocher(int n) { return null; }
    public boolean estVide() { return false; }
    public void ajouterCartes(List<Carte> pioche) {}
    public int getTaille() { return 0; }
}