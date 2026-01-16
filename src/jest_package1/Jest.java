package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Classe représentant un "Jest" : un ensemble de cartes appartenant à un
 * joueur.
 * Un Jest contient les cartes remportées par un joueur lors d'une phase de jeu.
 * Implémente Serializable pour la sauvegarde/chargement de parties.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public class Jest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Carte> cartes;

    /**
     * Constructeur du Jest.
     * Initialise une liste vide de cartes.
     */
    public Jest() {
        this.cartes = new ArrayList<>();
    }

    /**
     * Ajoute une carte au Jest.
     * 
     * @param carte la carte à ajouter
     */
    public void ajouterCarte(Carte carte) {
        cartes.add(carte);
    }

    /**
     * Vide la liste de cartes du Jest.
     * Note : Non utilisé dans la version courante.
     */
    public void vider() {
        cartes.clear();
    }

    /**
     * Enlève une carte du Jest.
     * 
     * @param carte la carte à enlever
     */
    public void enleverCarte(Carte carte) {
        cartes.remove(carte);
    }

    /**
     * Obtient la liste de cartes du Jest.
     * 
     * @return la liste des cartes du Jest
     */
    public List<Carte> getCartes() {
        return cartes;
    }

    /**
     * Obtient toutes les cartes du Jest.
     * Permet de compiler les cartes pour le calcul du score final.
     * 
     * @return la liste complète des cartes du Jest
     */
    public List<Carte> getToutesLesCartes() {
        List<Carte> toutes = new ArrayList<>();
        toutes.addAll(this.cartes);

        return toutes;
    }
}
