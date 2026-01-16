package jest_package1;

import java.io.Serializable;

/**
 * Classe représentant le choix d'une carte effectué par un joueur.
 * Encapsule l'offre choisie et la carte sélectionnée dans cette offre.
 * Implémente Serializable pour la sauvegarde/chargement de parties.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public class ChoixCarte implements Serializable {
	/**
	 * Identifiant de version pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;

	/** Offre choisie par le joueur */
	private Offre offreChoisie;

	/** Carte sélectionnée dans l'offre */
	private Carte carteChoisie;

	/**
	 * Constructeur du choix de carte.
	 * 
	 * @param offreChoisie l'offre choisie
	 * @param carteChoisie la carte sélectionnée
	 */
	public ChoixCarte(Offre offreChoisie, Carte carteChoisie) {
		this.offreChoisie = offreChoisie;
		this.carteChoisie = carteChoisie;
	}

	/**
	 * Obtient l'offre choisie.
	 * 
	 * @return l'offre choisie
	 */
	public Offre getOffre() {
		return offreChoisie;
	}

	/**
	 * Obtient la carte choisie.
	 * 
	 * @return la carte choisie
	 */
	public Carte getCarte() {
		return carteChoisie;
	}
}