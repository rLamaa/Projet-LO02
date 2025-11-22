package jest_package1;

import java.util.List;

public interface RegleJeu {
    int calculerValeurJest(Jest jest);
    boolean verifierConditionTrophee(Jest jest, Carte carte);
    List<Joueur> determinerOrdreJeu(List<Offre> offres);
    void appliquerReglesSpeciales(Jeu jeu);
}



