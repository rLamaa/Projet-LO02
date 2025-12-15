package jest_package1;

import java.io.Serializable;
import java.util.List;

public class VarianteRapide implements RegleJeu {
    private static final long serialVersionUID = 1L;
    private int nombreManchesMax = 5;
    private int manchesJouees = 0;

    @Override
    public int calculerValeurJest(Jest jest) {
        // Utilise le même calcul que les règles standard
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        return calc.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerOrdreJeu(offres);
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        manchesJouees++;
        if (manchesJouees >= nombreManchesMax) {
            System.out.println("⏰ Limite de manches atteinte (" + nombreManchesMax + ")");
        }
    }

    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerGagnantTrophee(joueurs, trophee);
    }

    public boolean partiTerminee() {
        return manchesJouees >= nombreManchesMax;
    }
}
