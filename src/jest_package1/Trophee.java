package jest_package1;

/**
 * Classe représentant un trophée dans le jeu de Jest.
 * 
 * Un trophée est une carte spéciale qui accorde des bonus de points
 * au joueur qui remplit les conditions associées.
 * 
 * Responsabilités:
 * - Vérifier si un Jest remplit les conditions du trophée
 * - Appliquer l'effet du trophée (modifier le score)
 * - Gérer les différents types de conditions (présence du Joker, meilleur Jest,
 * etc.)
 * 
 * Les trophées ajoutent une couche stratégique au jeu en:
 * - Créant des objectifs secondaires au-delà du simple accumulation de cartes
 * - Favorisant différents styles de jeu
 * - Récompensant les joueurs qui atteignent les conditions
 * 
 * @author David et Léna
 */
public class Trophee {
	/** Type de condition à vérifier pour le trophée */
	@SuppressWarnings("unused")
	private TypeCondition typeCondition;

	/** Couleur cible du trophée */
	@SuppressWarnings("unused")
	private Couleur couleurCible;

	/** Valeur cible du trophée */
	@SuppressWarnings("unused")
	private int valeurCible;

	/** Modificateur de score associé au trophée */
	private int modificateurScore;

	/**
	 * Constructeur d'un trophée.
	 * 
	 * @param chiffre la valeur de la carte trophée
	 * @param c       la couleur de la carte trophée
	 */
	public Trophee(Valeur chiffre, Couleur c) {
		super();
	}

	/**
	 * Vérifie si un Jest remplit les conditions du trophée.
	 * 
	 * @param jest le Jest à vérifier
	 * @return true si les conditions sont remplies, false sinon
	 */
	public boolean verifierCondition(Jest jest) {
		return false;

	}

	/**
	 * Applique l'effet du trophée et retourne le modificateur de score.
	 * 
	 * @param jest le Jest affecté
	 * @return le modificateur de score du trophée
	 */
	public int appliquerEffet(Jest jest) {
		return modificateurScore;

	}

}
