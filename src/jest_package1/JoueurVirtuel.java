package jest_package1;

import java.util.List;

public class JoueurVirtuel extends Joueur {
	public JoueurVirtuel(String nom) {
		super(nom);
	}
    private Strategie strategie;
    public ChoixCarte choisirCarte(List<Offre> offres) { return null; }
    public Offre faireOffre(Carte c1, Carte c2) { return null; }
    public void setStrategie(Strategie strategie) {}
}
