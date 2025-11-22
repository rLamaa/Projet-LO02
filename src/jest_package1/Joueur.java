package jest_package1;

import java.util.List;

public abstract class Joueur {
    public Joueur(String nom) {

		this.nom = nom;
		this.jest = new Jest();
	}
	protected String nom;
    protected Jest jest;
    protected Offre offreCourante;

    public void faireOffre(Carte c1, Carte c2) {}
    public ChoixCarte choisirCarte(List<Offre> offres) { return null; }
    public void ajouterCarteJest(Carte carte) {}
    public Jest getJest() { return null; }
    public String getNom() { return nom; }
}


