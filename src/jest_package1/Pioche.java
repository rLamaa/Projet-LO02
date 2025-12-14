package jest_package1;

import java.util.*;

//finie sauf problèmes d'intégration
public class Pioche {
    // création de la pioche
    private Stack<Carte> pioche = new Stack<>();

    // ajouter les nouvelles cartes si on ajoute une extension
    public void initialiser(boolean avecExtension) {
        for (Couleur c : Couleur.values()) {
            for (Valeur v : Valeur.values()) {
                pioche.add(new CarteCouleur(c, v));
            }
        }
        pioche.add(new Joker());
        melanger();
    }
    
    public void afficherPioche() {
    	for(Carte c : pioche) {
    		System.out.println(c);
    	}
    }

    // on mélange la pioche
    public void melanger() {
        Collections.shuffle(pioche);
    }

    // on pioche *une* carte
    public Carte piocher() {
        // retourne la carte du dessus de la pioche
        if (this.pioche.isEmpty()) { // verification si la pioche est vide
            return null;
        }
        return this.pioche.pop();
    }

    // on pioche *n* cartes, utile pour la distribution initiale
    public List<Carte> piocher(int n) {
        // initialisation de la liste des cartes piochées
        List<Carte> piochees = new ArrayList<>();
        // boucle pour piocher n cartes
        for (int i = 0; i < n && !this.pioche.isEmpty(); i++) {
            piochees.add(this.pioche.pop());
        }
        return piochees;
    }

    public boolean estVide() {
        // retourne vrai si la pioche est vide
        return this.pioche.isEmpty();
    }

    // utile pour les extensions
    public void ajouterCartes(List<Carte> nouvelleCartes) {
        // ajoute une liste de cartes à la pioche
        this.pioche.addAll(nouvelleCartes);
    }

    public int getTaille() {
        // retourne la taille de la pioche
        return this.pioche.size();
    }
}