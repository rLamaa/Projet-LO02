package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Classe représentant la pioche du jeu de Jest.
 * 
 * Gère l'ensemble du deck de cartes avec les responsabilités suivantes:
 * - Initialisation du deck complet 
 * - Distribution des cartes aux joueurs
 * - Mélange aléatoire des cartes
 * 
 * Implémente Serializable pour permettre la sauvegarde/chargement des parties.
 * 
 * @author David et Léna
 */
public class Pioche implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Pile contenant les cartes de la pioche */
    private Stack<Carte> pioche = new Stack<>();

    /**
     * Initialise la pioche avec toutes les cartes standards et optionnellement les
     * cartes d'extension.
     * Mélange automatiquement les cartes après initialisation.
     * 
     * @param avecExtension true pour ajouter les cartes d'extension (Étoiles,
     *                      Triangles, Soleils)
     */
    public void initialiser(boolean avecExtension) {
        pioche.clear();
        // Cartes pour le jeu standard
        List<Couleur> couleurs = new ArrayList<>();
        couleurs.add(Couleur.PIQUE);
        couleurs.add(Couleur.TREFLE);
        couleurs.add(Couleur.CARREAU);
        couleurs.add(Couleur.COEUR);

        if (avecExtension) {
            couleurs.add(Couleur.ETOILE);
            couleurs.add(Couleur.TRIANGLE);
            couleurs.add(Couleur.SOLEIL);
        }

        for (Couleur c : couleurs) {
            for (Valeur v : Valeur.values()) {
                pioche.add(new CarteCouleur(c, v));
            }
        }
        pioche.add(new Joker());
        melanger();
    }

    /**
     * Affiche toutes les cartes de la pioche dans la console.
     */
    public void afficherPioche() {
        for (Carte c : pioche) {
            System.out.println(c);
        }
    }

    /**
     * Mélange les cartes de la pioche de manière aléatoire.
     */
    public void melanger() {
        Collections.shuffle(pioche);
    }

    /**
     * Pioche une carte du dessus de la pile.
     * 
     * @return la carte piochée ou null si la pioche est vide
     */
    public Carte piocher() {
        if (this.pioche.isEmpty()) {
            return null;
        }
        return this.pioche.pop();
    }

    /**
     * Pioche un nombre spécifié de cartes de la pioche.
     * Utile pour la distribution initiale aux joueurs.
     * 
     * @param n le nombre de cartes à piocher
     * @return la liste des cartes piochées
     */
    public List<Carte> piocher(int n) {
        List<Carte> piochees = new ArrayList<>();
        for (int i = 0; i < n && !this.pioche.isEmpty(); i++) {
            piochees.add(this.pioche.pop());
        }
        return piochees;
    }

    /**
     * Vérifie si la pioche est vide.
     * 
     * @return true si la pioche est vide, false sinon
     */
    public boolean estVide() {
        return this.pioche.isEmpty();
    }

    /**
     * Ajoute une liste de cartes à la pioche.
     * Utile pour les extensions.
     * 
     * @param nouvelleCartes la liste des cartes à ajouter
     */
    public void ajouterCartes(List<Carte> nouvelleCartes) {
        this.pioche.addAll(nouvelleCartes);
    }

    /**
     * Obtient la taille actuelle de la pioche.
     * 
     * @return le nombre de cartes restantes
     */
    public int getTaille() {
        return this.pioche.size();
    }
}