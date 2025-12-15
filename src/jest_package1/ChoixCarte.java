package jest_package1;

import java.io.Serializable;

public class ChoixCarte implements Serializable {
	private static final long serialVersionUID = 1L;
	private Offre offreChoisie;
	private Carte carteChoisie;

	public ChoixCarte(Offre offreChoisie, Carte carteChoisie) {
		this.offreChoisie = offreChoisie;
		this.carteChoisie = carteChoisie;
	}

	public Offre getOffre() {
		return offreChoisie;
	}

	public Carte getCarte() {
		return carteChoisie;
	}
}