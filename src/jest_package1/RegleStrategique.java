package jest_package1;

import java.util.List;

public class RegleStrategique implements RegleJeu {

	private static final long serialVersionUID = 1L;

	@Override
	public int calculerValeurJest(Jest jest) {
		return new RegleStandard().calculerValeurJest(jest);
	}

	@Override
	public boolean verifierConditionTrophee(Jest jest, Carte carte) {
		return true;
	}

	@Override
	public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
		return new RegleStandard().determinerOrdreJeu(offres);
	}

	@Override
	public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
		return new RegleStandard().determinerGagnantTrophee(joueurs, trophee);
	}

	@Override
	public void appliquerReglesSpeciales(Jeu jeu) {
		//pas de règles spéciales, tout est dans la visibilité des cartes des offres
	}

	@Override
	public Offre creerOffre(Joueur joueur, Carte c1, Carte c2) {
		// Variante stratégique donc les deux cartes sont visibles
		c1.setVisible(true);
		c2.setVisible(true);
		return new Offre(c1, c2, joueur);
	}

	@Override
	public boolean sontOffresVisibles() {
		// TODO Auto-generated method stub
		return true; // variante où toutes les cartes des offres sont visibles
	}
	
}
