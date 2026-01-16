package jest_package1;

import java.util.List;

/**
 * Classe implémentant la variante rapide du jeu Jest.
 * Limite le nombre de manches à 3 pour accélérer le jeu.
 * Utilise les mêmes règles de scoring que la variante standard.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public class VarianteRapide implements RegleJeu {
    private static final long serialVersionUID = 1L;
    private int nombreManchesMax = 3;
    private int manchesJouees = 0;

    @Override
    public boolean sontOffresVisibles() {
        // TODO Auto-generated method stub
        return false; // dans la variante rapide, pareil que le jeu standard, une carte est cachée et
                      // l'autre visible
    }

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

    @Override
    public Offre creerOffre(Joueur joueur, Carte c1, Carte c2) {
        // TODO Auto-generated method stub
        return null;
    }
}
