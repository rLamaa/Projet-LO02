package jest_package1;

import java.util.List;

class VarianteRapide implements RegleJeu {
    private int nombreManchesMax;
    public int calculerValeurJest(Jest jest) { return 0; }
    public boolean verifierConditionTrophee(Jest jest, Carte carte) { return false; }
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) { return null; }
    public void appliquerReglesSpeciales(Jeu jeu) {}
}