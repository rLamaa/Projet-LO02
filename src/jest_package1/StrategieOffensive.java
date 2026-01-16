package jest_package1;

import java.util.List;

/**
 * Stratégie de jeu offensive pour les joueurs virtuels.
 * Le bot cherche à prendre les cartes les plus fortes visibles.
 * Montre ses meilleures cartes pour attirer les adversaires.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public class StrategieOffensive implements Strategie {
    private static final long serialVersionUID = 1L;

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        if (offres.isEmpty())
            return null;

        // Choisir l'offre avec la carte visible la plus forte
        Offre meilleureOffre = offres.get(0);
        int valeurMax = evaluerCarte(meilleureOffre.getCarteVisible());

        for (Offre o : offres) {
            int valeur = evaluerCarte(o.getCarteVisible());
            if (valeur > valeurMax) {
                valeurMax = valeur;
                meilleureOffre = o;
            }
        }

        // Prendre la carte visible (stratégie offensive = prendre ce qu'on voit de
        // bien)
        return new ChoixCarte(meilleureOffre, meilleureOffre.getCarteVisible());
    }

    @Override
    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        // Cacher la carte la plus faible, montrer la plus forte
        int val1 = evaluerCarte(c1);
        int val2 = evaluerCarte(c2);

        if (val1 > val2) {
            // c1 est meilleure, on la montre
            return new Offre(c2, c1, null);
        } else {
            // c2 est meilleure, on la montre
            return new Offre(c1, c2, null);
        }
    }

    @Override
    public int evaluerOffre(Offre offre, Jest jest) {
        return evaluerCarte(offre.getCarteVisible());
    }

    /**
     * Évalue la valeur d'une carte (positive pour Pique/Trèfle, négative pour
     * Carreau)
     */
    private int evaluerCarte(Carte c) {
        if (c instanceof Joker) {
            return 4; // Le Joker vaut 4 points sans cœur
        }
        if (c instanceof CarteCouleur) {
            CarteCouleur cc = (CarteCouleur) c;
            int valeur = cc.getValeurNumerique();

            switch (cc.getCouleur()) {
                case ETOILE:
                    return valeur * 2;
                case PIQUE:
                case TREFLE:
                case TRIANGLE:
                    return valeur; // Positif
                case CARREAU:
                    return -valeur; // Négatif
                case COEUR:
                case SOLEIL:
                    return 0; // Neutre (dépend du Joker)
            }
        }
        return 0;
    }
}