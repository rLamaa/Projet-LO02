package jest_package1;

import java.util.List;

/**
 * Classe implémentant la variante stratégique du jeu Jest.
 * 
 * Variante où toutes les cartes des offres sont visibles à tous les joueurs.
 * 
 * Différences avec le jeu standard:
 * - Visibilité: TOUTES les cartes de chaque offre sont visibles
 * (pas de carte cachée mystérieuse)
 * - Stratégie: Les bots ne peuvent pas utiliser leur stratégie
 * (puisque les deux cartes sont connues)
 * - Décision: Plus difficile car aucune surprise possible
 * 
 * Impact stratégique:
 * - Le jeu devient plus déterministe
 * - L'information complète élimine le bluff
 * - Les joueurs humains ont avantage (pas d'IA sophistiquée possible)
 * - Focus sur l'évaluation correcte des cartes
 * 
 * Scoring: Identique aux règles standard
 * Trophées: Identiques au jeu standard
 * Ordre de jeu: Identique au jeu standard
 * 
 * @author David et Léna
 */
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
		// pas de règles spéciales, tout est dans la visibilité des cartes des offres
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
