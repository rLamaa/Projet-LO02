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

    public abstract Offre faireOffre();
    public abstract ChoixCarte choisirCarte(List<Offre> offres);
    public void ajouterCarteJest(Carte carte) { 
        jest.ajouterCarte(carte); 
    }
    public Jest getJest() { return jest; }
    public String getNom() { return nom; }
    public Offre getOffreCourante() {
        return offreCourante;
    }
}


