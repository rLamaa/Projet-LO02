package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Classe représentant un "Jest" : un ensemble de cartes appartenant à un
 * joueur.
 * 
 * Un Jest accumule les cartes remportées par un joueur lors des différentes
 * phases de jeu. C'est la collection principale gérée par chaque joueur.
 * 
 * Chaque joueur possède deux Jest:
 * - Jest courant: Réinitialisé à chaque manche, contient les cartes de cette
 * manche
 * - Jest personnel: Conservé sur plusieurs manches, contient les cartes
 * remportées précédemment
 * 
 * Les Jest sont évalués selon les règles du jeu pour déterminer les scores
 * et les gagnants des trophées.
 * 
 * Implémente Serializable pour permettre la sauvegarde/chargement des parties.
 * 
 * @author David et Léna
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
