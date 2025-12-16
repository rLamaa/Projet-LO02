package jest_package1;

import java.io.Serializable;
import java.util.List;

public abstract class Joueur implements Serializable {
    /**
     * Identifiant de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;

    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
        this.jestPerso = new Jest();
    }

    protected String nom;
    protected Jest jest;
    protected Jest jestPerso;
    protected Offre offreCourante;

    public abstract Offre faireOffre();

    public abstract ChoixCarte choisirCarte(List<Offre> offres);

    public void ajouterCarteJest(Carte carte) {
        jest.ajouterCarte(carte);
    }

    public void ajouterCarteJestPerso(Carte carte) {
        jestPerso.ajouterCarte(carte);
    }

    public Jest getJest() {
        return jest;
    }

    public String getNom() {
        return nom;
    }

    public Offre getOffreCourante() {
        return offreCourante;
    }

    public Jest getJestPerso() {
        return jestPerso;
    }

}
