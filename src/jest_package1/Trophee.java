package jest_package1;

/**
 * Classe représentant un trophée dans le jeu de Jest.
 * Un trophée est une carte spéciale qui accorde des points bonus selon
 * certaines conditions.
 * 
 * @author LO02 Project Team
 * @version 1.0
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
