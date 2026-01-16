package jest_package1;

import java.io.Serializable;

/**
 * Représente une offre de deux cartes proposée par un joueur aux autres
 * joueurs.
 * 
 * Une offre est composée de:
 * - Une carte visible: Que tous les joueurs peuvent voir
 * - Une carte cachée: Dont la valeur est inconnue jusqu'au choix
 * - Un propriétaire: Le joueur qui propose l'offre
 * 
 * L'offre est le mécanisme central du jeu de Jest:
 * - Chaque joueur crée une offre avec ses deux cartes
 * - Les autres joueurs choisissent une carte de chaque offre
 * - Le dernier joueur doit prendre sa propre offre
 * 
 * En mode stratégique, les deux cartes peuvent être visibles,
 * changeant complètement la dynamique du jeu.
 * 
 * Implémente Serializable pour la sauvegarde/chargement des parties.
 * 
 * @author David et Léna
 */
public class Offre implements Serializable {
	private static final long serialVersionUID = 1L;
	private Carte carteVisible;
	private Carte carteCachee;
	private Joueur proprietaire;

	public Offre(Carte carteCachee, Carte carteVisible, Joueur proprietaire) {
		this.carteVisible = carteVisible;
		this.carteCachee = carteCachee;
		this.proprietaire = proprietaire;
	}

	public Carte getCarteVisible() {
		return carteVisible;
	}

	public Carte getCarteCachee() {
		return carteCachee;
	}

	/**
	 * Retire une carte de l'offre
	 * 
	 * @param carte La carte à retirer
	 * @return La carte retirée
	 */
	public Carte retirerCarte(Carte carte) {
		if (carte == carteVisible) {
			Carte temp = carteVisible;
			carteVisible = null;
			return temp;
		} else if (carte == carteCachee) {
			Carte temp = carteCachee;
			carteCachee = null;
			return temp;
		}
		return null;
	}

	/**
	 * Retire une carte selon si elle est visible ou cachée
	 * 
	 * @param visible true pour retirer la carte visible, false pour la cachée
	 * @return La carte retirée
	 */
	public Carte retirerCarte(boolean visible) {
		if (visible) {
			Carte temp = carteVisible;
			carteVisible = null;
			return temp;
		} else {
			Carte temp = carteCachee;
			carteCachee = null;
			return temp;
		}
	}

	/**
	 * Vérifie si l'offre contient encore 2 cartes
	 * 
	 * @return
	 */
	public boolean estComplete() {
		return this.carteCachee != null && this.carteVisible != null;
	}

	/**
	 * Compte le nombre de cartes restantes dans l'offre
	 * 
	 * @return
	 */
	public int compterCartes() {
		int count = 0;
		if (carteVisible != null)
			count++;
		if (carteCachee != null)
			count++;
		return count;
	}

	/**
	 * Getter du propriètaire de l'offre
	 * 
	 * @return
	 */
	public Joueur getProprietaire() {
		return proprietaire;
	}
}