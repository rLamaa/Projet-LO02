package jest_package1;

import java.io.Serializable;

/**
 * Classe représentant le choix d'une carte effectué par un joueur.
 * 
 * Encapsule les deux éléments critiques d'un choix de jeu:
 * - L'offre sélectionnée parmi les offres disponibles
 * - La carte spécifique choisie dans cette offre (visible ou cachée)
 * 
 * Cette classe est utilisée pour:
 * - Enregistrer les choix faits par les joueurs
 * - Passer les informations de choix entre les composants du jeu
 * - Valider que les choix sont légaux (offre disponible, carte existante)
 * 
 * Implémente Serializable pour la sauvegarde/chargement des parties.
 * 
 * @author David et Léna
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