package jest_package1;

import java.util.List;

public class VarianteStrategique implements RegleJeu {
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

    @Override
    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'determinerGagnantTrophee'");
    }
}
