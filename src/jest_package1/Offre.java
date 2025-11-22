package jest_package1;

public class Offre {
    private Carte carteVisible;
    private Carte carteCachee;
    private Joueur proprietaire;
    
    public Offre(Carte carteVisible, Carte carteCachee, Joueur proprietaire) {
		super();
		this.carteVisible = carteVisible;
		this.carteCachee = carteCachee;
		this.proprietaire = proprietaire;
	}
	public Carte getCarteVisible() { return carteVisible; }
    public Carte getCarteCachee() { return carteCachee; }
    public Carte retirerCarte(boolean visible) { return null; }
    public boolean estComplete() { 
    	return this.carteCachee!=null && this.carteVisible!=null; }
    public Joueur getProprietaire() { return proprietaire; }
}