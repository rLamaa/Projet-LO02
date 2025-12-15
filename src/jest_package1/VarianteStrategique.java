package jest_package1;

import java.io.Serializable;
import java.util.List;

public class VarianteStrategique implements RegleJeu, Serializable {
    private static final long serialVersionUID = 1L;
    private boolean offresVisibles;

    public int calculerValeurJest(Jest jest) {
        return 0;
    }

    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return false;
    }

    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        return null;
    }

    public void appliquerReglesSpeciales(Jeu jeu) {
    }
}
