package jest_package1;

import java.io.Serializable;
import java.util.List;

/**
 * 
 */
public interface RegleJeu extends Serializable {
	int calculerValeurJest(Jest jest);

	boolean verifierConditionTrophee(Jest jest, Carte carte);

	List<Joueur> determinerOrdreJeu(List<Offre> offres);

	Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee);

	void appliquerReglesSpeciales(Jeu jeu);
	
	Offre creerOffre(Joueur joueur, Carte carteCachee, Carte carteVisible);
	
	// va permettre de changer les règles du jeu si la variante stratégique est choisie
	boolean sontOffresVisibles();
}