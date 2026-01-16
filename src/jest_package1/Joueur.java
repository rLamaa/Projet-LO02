package jest_package1;

import java.io.Serializable;
import java.util.List;

/**
 * Classe abstraite représentant un joueur du jeu de Jest.
 * 
 * Définit l'interface commune pour tous les types de joueurs.
 * Utilise le patron Template Method pour les décisions de jeu.
 * 
 * 
 * @author David et Léna
 */
public abstract class Joueur implements Serializable {

    /**
     * Identifiant de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nom du joueur.
     */
    protected String nom;

    /**
     * Jest courant du joueur (cartes remportées pendant la manche).
     */
    protected Jest jest;

    /**
     * Jest personnel du joueur (cartes conservées sur plusieurs manches).
     */
    protected Jest jestPerso;

    /**
     * Offre actuellement proposée par le joueur.
     */
    protected Offre offreCourante;

    /**
     * Constructeur d'un joueur.
     *
     * @param nom le nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
        this.jestPerso = new Jest();
    }

    /**
     * Demande au joueur de créer une offre à partir de sa main.
     *
     * @return l'offre créée par le joueur
     */
    public abstract Offre faireOffre(boolean offresVisibles);

    /**
     * Demande au joueur de choisir une carte parmi les offres disponibles.
     *
     * @param offres la liste des offres proposées par les joueurs
     * @return le choix de carte effectué par le joueur
     */
    public abstract ChoixCarte choisirCarte(List<Offre> offres);

    /**
     * Ajoute une carte au Jest courant du joueur.
     *
     * @param carte la carte à ajouter
     */
    public void ajouterCarteJest(Carte carte) {
        jest.ajouterCarte(carte);
    }

    /**
     * Ajoute une carte au Jest personnel du joueur.
     *
     * @param carte la carte à ajouter
     */
    public void ajouterCarteJestPerso(Carte carte) {
        jestPerso.ajouterCarte(carte);
    }

    /**
     * Retourne le Jest courant du joueur.
     *
     * @return le Jest courant
     */
    public Jest getJest() {
        return jest;
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne l'offre courante du joueur.
     *
     * @return l'offre courante
     */
    public Offre getOffreCourante() {
        return offreCourante;
    }

    /**
     * Retourne le Jest personnel du joueur.
     *
     * @return le Jest personnel
     */
    public Jest getJestPerso() {
        return jestPerso;
    }
}