package jest_package1;

import java.io.Serializable;
import java.util.List;

public class JoueurVirtuel extends Joueur implements Serializable {
    private static final long serialVersionUID = 1L;

    public JoueurVirtuel(String nom) {
        super(nom);
    }

    private Strategie strategie;

    @Override
    public Offre faireOffre() {
        List<Carte> cartes = jest.getCartes();
        if (cartes.size() < 2) {
            throw new IllegalStateException("Pas assez de cartes pour faire une offre");
        }

        // On choisit la carte de plus faible valeur pour la cacher
        Carte c1 = cartes.get(0);
        Carte c2 = cartes.get(1);

        if (c1.getValeurNumerique() > c2.getValeurNumerique()) {
            Carte temp = c1;
            c1 = c2;
            c2 = temp;
        }

        this.offreCourante = new Offre(c1, c2, this);
        return this.offreCourante;
    }

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres) {
        // Stratégie simple: choisir la carte face-up avec la plus grande valeur
        Carte meilleureCarte = null;
        Offre offreChoisie = null;

        for (Offre o : offres) {
            Carte c = o.getCarteVisible();
            if (meilleureCarte == null || c.getValeurNumerique() > meilleureCarte.getValeurNumerique()) {
                meilleureCarte = c;
                offreChoisie = o;
            }
        }

        return new ChoixCarte(offreChoisie, meilleureCarte);
    }

    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

}
