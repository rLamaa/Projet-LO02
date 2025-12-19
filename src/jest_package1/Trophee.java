package jest_package1;

public class Trophee extends Carte {
	@SuppressWarnings("unused")
	private TypeCondition typeCondition;
	@SuppressWarnings("unused")
	private Couleur couleurCible;
	@SuppressWarnings("unused")
	private int valeurCible;
	private int modificateurScore;

	public Trophee(Valeur chiffre, Couleur c) {
		super();
	}

	public boolean verifierCondition(Jest jest) {
		return false;

	}

	public int appliquerEffet(Jest jest) {
		return modificateurScore;

	}

}
