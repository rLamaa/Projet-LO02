package jest_package1;

public class ChoixCarte {
    private Offre offreChoisie;
    private Carte carteChoisie;
    
    public ChoixCarte(Offre offreChoisie, Carte carteChoisie) {
		super();
		this.offreChoisie = offreChoisie;
		this.carteChoisie = carteChoisie;
	}
	public Offre getOffre() { return offreChoisie; }
    public Carte getCarte() { return carteChoisie; }
}