package jest_package1;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Pioche {
    private Stack<Carte> cartes= new Stack<>();

    public void initialiser(boolean avecExtension) {
    	for (Couleur c : Couleur.values()) {
            for (int v = 1; v <= 4; v++) {
                cartes.add(new CarteCouleur(c, v));
            }
    	}
        cartes.add(new Joker());
        melanger();
    }
    public void melanger() {
    	Collections.shuffle(cartes);
    }
    
    public Carte piocher() { 
    	return cartes.pop();
    	}
    public List<Carte> piocher(int n) { return null; }
    public boolean estVide() { return false; }
    public void ajouterCartes(List<Carte> cartes) {}
    public int getTaille() { return 0; }
}