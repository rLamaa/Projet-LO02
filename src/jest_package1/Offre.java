package jest_package1;

public class Offre {
	private Carte carteVisible;
	private Carte carteCachee;
	private Joueur proprietaire;

	public Offre(Carte carteCachee, Carte carteVisible, Joueur proprietaire) {
		super();
		this.carteVisible = carteVisible;
		this.carteCachee = carteCachee;
		this.proprietaire = proprietaire;
	} // fini

	public Carte getCarteVisible() {
		return carteVisible;
	} // fini

	public Carte getCarteCachee() {
		return carteCachee;
	} // fini

	public Carte retirerCarte(boolean visible) {
		Carte carteChoisie = null; // null à remplacer par la carte choisie
		return carteChoisie;
	}

	public boolean estComplete() {
		return this.carteCachee != null && this.carteVisible != null;
	} // fini

	public Joueur getProprietaire() {
		return proprietaire;
	} // fini
}