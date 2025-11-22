package jest_package1;

public class Trophee {
	private TypeCondition typeCondition;
	private Couleur couleurCible;
	private int valeurCible;
	private int modificateurScore;
	
	public boolean verifierCondition(Jest jest) {
		return false;
		
	}
	
	public int appliquerEffet(Jest jest) {
		return modificateurScore;
		
	}
	
}

