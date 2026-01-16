package jest_package1;

import java.io.Serializable;
import java.util.List;

/**
 * Interface définissant les règles du jeu de Jest.
 * Chaque variante du jeu (Standard, Rapide, Stratégique) implémente cette
 * interface.
 * Gère le calcul des scores, la vérification des conditions de trophée, et
 * l'ordre de jeu.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public interface RegleJeu extends Serializable {
	/**
	 * Calcule la valeur d'un Jest selon les règles.
	 * 
	 * @param jest le Jest à évaluer
	 * @return la valeur numérique du Jest
	 */
	int calculerValeurJest(Jest jest);

	/**
	 * Vérifie si une carte remplit les conditions pour être un trophée.
	 * 
	 * @param jest  le Jest à vérifier
	 * @param carte la carte à tester
	 * @return true si la carte est un trophée valide, false sinon
	 */
	boolean verifierConditionTrophee(Jest jest, Carte carte);

	/**
	 * Détermine l'ordre de jeu basé sur les offres disponibles.
	 * 
	 * @param offres la liste des offres
	 * @return la liste des joueurs ordonnée
	 */
	List<Joueur> determinerOrdreJeu(List<Offre> offres);

	/**
	 * Détermine le gagnant du trophée parmi les joueurs.
	 * 
	 * @param joueurs la liste des joueurs candidats
	 * @param trophee la carte trophée
	 * @return le joueur gagnant du trophée
	 */
	Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee);

	/**
	 * Applique les règles spéciales du jeu.
	 * 
	 * @param jeu le jeu auquel appliquer les règles
	 */
	void appliquerReglesSpeciales(Jeu jeu);

	/**
	 * Crée une offre selon les règles.
	 * 
	 * @param joueur       le joueur qui crée l'offre
	 * @param carteCachee  la carte cachée
	 * @param carteVisible la carte visible
	 * @return l'offre créée
	 */
	Offre creerOffre(Joueur joueur, Carte carteCachee, Carte carteVisible);

	/**
	 * Vérifie si les offres sont visibles (pour la variante stratégique).
	 * 
	 * @return true si les offres sont visibles, false sinon
	 */
	boolean sontOffresVisibles();
}