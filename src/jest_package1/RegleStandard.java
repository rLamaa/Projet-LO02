package jest_package1;

import java.util.*;

/**
 * Implémentation des règles standard du jeu Jest
 */
public class RegleStandard implements RegleJeu {
    private static final long serialVersionUID = 1L;

    @Override
    public int calculerValeurJest(Jest jest) {
        CalculateurScoreStandard calculateur = new CalculateurScoreStandard();
        return calculateur.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte trophee) {
        // Cette méthode pourrait être utilisée pour des vérifications spécifiques
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        // Trier les joueurs par valeur de carte visible (ordre décroissant)
        List<Joueur> ordre = new ArrayList<>();
        List<Offre> offresTriees = new ArrayList<>(offres);

        offresTriees.sort((o1, o2) -> {
            int val1 = o1.getCarteVisible() instanceof Joker ? 0 : o1.getCarteVisible().getValeurNumerique();
            int val2 = o2.getCarteVisible() instanceof Joker ? 0 : o2.getCarteVisible().getValeurNumerique();

            if (val1 != val2) {
                return Integer.compare(val2, val1); // Ordre décroissant
            }

            // En cas d'égalité, comparer les couleurs
            if (o1.getCarteVisible() instanceof Joker)
                return 1;
            if (o2.getCarteVisible() instanceof Joker)
                return -1;

            return Integer.compare(
                    o2.getCarteVisible().getCouleur().getForce(),
                    o1.getCarteVisible().getCouleur().getForce());
        });

        for (Offre o : offresTriees) {
            ordre.add(o.getProprietaire());
        }

        return ordre;
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        // Pas de règles spéciales pour la version standard
    }

    /**
     * Détermine le gagnant d'un trophée selon les règles standard
     */
    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        if (trophee instanceof Joker) {
            // Le Joker va au joueur avec le PIRE Jest
            return determinerPireJest(joueurs);
        }

        if (trophee instanceof CarteCouleur) {
            CarteCouleur ct = (CarteCouleur) trophee;

            // Trophée va au joueur avec la majorité de cette couleur
            return determinerMajoriteCouleur(joueurs, ct.getCouleur());
        }

        return null;
    }

    /**
     * Trouve le joueur avec le pire Jest
     */
    private Joueur determinerPireJest(List<Joueur> joueurs) {
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        int scoreMin = Integer.MAX_VALUE;
        Joueur pire = null;

        for (Joueur j : joueurs) {
            int score = calc.calculerScore(j.getJest());
            if (score < scoreMin) {
                scoreMin = score;
                pire = j;
            }
        }

        return pire;
    }

    /**
     * Trouve le joueur avec la majorité d'une couleur donnée
     */
    private Joueur determinerMajoriteCouleur(List<Joueur> joueurs, Couleur couleur) {
        int maxCount = 0;
        Joueur gagnant = null;
        int maxValeur = 0;

        for (Joueur j : joueurs) {
            int count = 0;
            int valeurMax = 0;

            for (Carte c : j.getJest().getCartes()) {
                if (c instanceof CarteCouleur) {
                    CarteCouleur cc = (CarteCouleur) c;
                    if (cc.getCouleur() == couleur) {
                        count++;
                        if (cc.getValeurNumerique() > valeurMax) {
                            valeurMax = cc.getValeurNumerique();
                        }
                    }
                }
            }

            // En cas d'égalité, comparer les valeurs les plus élevées
            if (count > maxCount || (count == maxCount && valeurMax > maxValeur)) {
                maxCount = count;
                maxValeur = valeurMax;
                gagnant = j;
            }
        }

        return gagnant;
    }
}