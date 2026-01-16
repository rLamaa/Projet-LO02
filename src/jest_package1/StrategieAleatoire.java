package jest_package1;

import java.util.List;
import java.util.Random;

/**
 * Stratégie de jeu aléatoire pour les joueurs virtuels.
 * 
 * Cette stratégie prend des décisions complètement aléatoires sans aucune
 * logique ou évaluation des cartes.
 * 
 * Utilité:
 * - Imprévisibilité: Offre une variabilité complète du gameplay
 * - Équilibre: Permet de tester l'équilibre sans stratégie dominante
 * 
 * 
 * @author David et Léna
 */
public class StrategieAleatoire implements Strategie {
    private static final long serialVersionUID = 1L;
    private Random random = new Random();

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        if (offres.isEmpty())
            return null;

        // Choisir une offre au hasard
        Offre offreChoisie = offres.get(random.nextInt(offres.size()));

        // Choisir visible ou cachée au hasard
        Carte carteChoisie = random.nextBoolean() ? offreChoisie.getCarteVisible() : offreChoisie.getCarteCachee();

        return new ChoixCarte(offreChoisie, carteChoisie);
    }

    @Override
    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        // Choisir aléatoirement quelle carte montrer
        if (random.nextBoolean()) {
            return new Offre(c2, c1, null); // Montrer c1
        } else {
            return new Offre(c1, c2, null); // Montrer c2
        }
    }

    @Override
    public int evaluerOffre(Offre offre, Jest jest) {
        return random.nextInt(10); // Évaluation aléatoire
    }
}