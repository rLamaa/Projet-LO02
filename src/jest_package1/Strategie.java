package jest_package1;

import java.util.List;

public interface Strategie {
    ChoixCarte choisirCarte(List<Offre> offres, Jest jest);
    Offre choisirCartesOffre(Carte c1, Carte c2);
    int evaluerOffre(Offre offre, Jest jest);
}






