package jest_package1;

import java.util.List;

public class StrategieDefensive implements Strategie {
    private static final long serialVersionUID = 1L;

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        if (offres.isEmpty())
            return null;

        // Choisir l'offre avec la carte visible la moins dangereuse
        Offre meilleureOffre = offres.get(0);
        int dangerMin = evaluerDanger(meilleureOffre.getCarteVisible());

        for (Offre o : offres) {
            int danger = evaluerDanger(o.getCarteVisible());
            if (danger < dangerMin) {
                dangerMin = danger;
                meilleureOffre = o;
            }
        }

        // Stratégie défensive : prendre la cachée (éviter les pièges)
        return new ChoixCarte(meilleureOffre, meilleureOffre.getCarteCachee());
    }

    @Override
    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        // Montrer la carte la moins dangereuse, cacher la plus dangereuse
        int danger1 = evaluerDanger(c1);
        int danger2 = evaluerDanger(c2);

        if (danger1 < danger2) {
            // c1 est moins dangereuse, on la montre
            return new Offre(c2, c1, null);
        } else {
            // c2 est moins dangereuse, on la montre
            return new Offre(c1, c2, null);
        }
    }

    @Override
    public int evaluerOffre(Offre offre, Jest jest) {
        return evaluerDanger(offre.getCarteVisible());
    }

    /**
     * Évalue le "danger" d'une carte (Carreau = danger, Pique/Trèfle = sûr)
     */
    private int evaluerDanger(Carte c) {
        if (c instanceof Joker) {
            return 2; // Danger moyen
        }
        if (c instanceof CarteCouleur) {
            CarteCouleur cc = (CarteCouleur) c;
            int valeur = cc.getValeurNumerique();

            switch (cc.getCouleur()) {
                case CARREAU:
                    return valeur * 2; // Très dangereux (points négatifs)
                case COEUR:
                    return valeur; // Danger moyen (dépend du Joker)
                case PIQUE:
                case TREFLE:
                    return -valeur; // Pas dangereux (valeur négative = bon)
            }
        }
        return 0;
    }
}