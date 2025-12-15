package jest_package1;

import java.io.Serializable;
import java.util.List;

public interface RegleJeu extends Serializable {
    int calculerValeurJest(Jest jest);

    boolean verifierConditionTrophee(Jest jest, Carte carte);

    List<Joueur> determinerOrdreJeu(List<Offre> offres);

    void appliquerReglesSpeciales(Jeu jeu);
}
