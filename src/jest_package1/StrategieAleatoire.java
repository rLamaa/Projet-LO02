package jest_package1;

import java.io.Serializable;
import java.util.List;

public class StrategieAleatoire implements Strategie, Serializable {
    private static final long serialVersionUID = 1L;

    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        return null;
    }

    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        return null;
    }

    public int evaluerOffre(Offre offre, Jest jest) {
        return 0;
    }
}