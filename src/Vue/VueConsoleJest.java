package Vue;

import java.util.Observable;
import java.util.Observer;
import jest_package1.*;

/**
 * Vue console pour le jeu de Jest.
 * 
 * Constitue une représentation textuelle alternative du jeu (pattern MVC).
 * Implémente Observer pour afficher les notifications du modèle dans la
 * console.
 * Fonctionne en parallèle avec l'interface graphique pour le mode hybride.
 * 
 * Responsabilités:
 * - Afficher l'état du jeu en texte formaté
 * - Montrer les actions des joueurs et les résultats
 * - Maintenir un log textuel de la partie
 * - Supporter la lecture simple des événements de jeu
 * 
 * Avantages:
 * - Débogage facile en voyant le flux d'exécution
 * - Support du mode console pur (sans interface graphique)
 * - Alternance console/GUI ou les deux simultanément
 * 
 * @author David et Léna
 */
@SuppressWarnings("deprecation")
public class VueConsoleJest implements Observer {

    private Partie partie;

    public VueConsoleJest(Partie partie) {
        this.partie = partie;

        // Enregistrement comme observateur de la partie
        partie.addObserver(this);

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   VUE CONSOLE INITIALISÉE          ║");
        System.out.println("╚════════════════════════════════════╝\n");
    }

    /**
     * Méthode appelée quand la partie change
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Partie) {
            afficherEtatPartie();
        }
    }

    /**
     * Affiche l'état actuel de la partie
     */
    private void afficherEtatPartie() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║  MISE À JOUR - Manche " + partie.getNumeroManche() + "                  ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // Affiche les joueurs et leurs cartes
        for (Joueur j : partie.getJoueurs()) {
            System.out.print("  [" + j.getNom() + "] ");

            if (j instanceof JoueurHumain) {
                System.out.print("(Humain) ");
            } else {
                System.out.print("(Bot) ");
            }

            System.out.println("Jest: " + j.getJestPerso().getCartes().size() + " carte(s)");

            // Affiche les cartes en main si disponibles
            if (!j.getJest().getCartes().isEmpty()) {
                System.out.println("    └─ En main: " + j.getJest().getCartes());
            }
        }

        // Affiche les offres actuelles si disponibles
        if (partie.getOffresActuelles() != null && !partie.getOffresActuelles().isEmpty()) {
            System.out.println("\n  📋 Offres actuelles:");
            for (Offre o : partie.getOffresActuelles()) {
                if (o.getCarteCachee().estVisible()) {
                    // Variante stratégique
                    System.out.println("    • [" + o.getProprietaire().getNom() + "] " +
                            "Carte 1: " + o.getCarteVisible() + " | Carte 2: " + o.getCarteCachee() +
                            " - " + (o.estComplete() ? "✓" : "✗"));
                } else {
                    // Standard
                    System.out.println("    • [" + o.getProprietaire().getNom() + "] " +
                            "Visible: " + o.getCarteVisible() + " | Cachée: [?] " +
                            "- " + (o.estComplete() ? "✓" : "✗"));
                }
            }
        }

        System.out.println("═══════════════════════════════════════════\n");
    }

    /**
     * Affiche un message dans la console
     */
    public void afficherMessage(String message) {
        System.out.println("[CONSOLE] " + message);
    }

    /**
     * Affiche les cartes d'un joueur
     */
    public void afficherCartesJoueur(Joueur joueur) {
        System.out.println("\n═══ Cartes de " + joueur.getNom() + " ═══");
        System.out.println("Jest temporaire: " + joueur.getJest().getCartes());
        System.out.println("Jest définitif: " + joueur.getJestPerso().getCartes());
    }

    /**
     * Affiche les offres actuelles avec détails
     */
    public void afficherOffresDetaillees() {
        System.out.println("\n═══ Offres actuelles ═══");

        if (partie.getOffresActuelles() == null || partie.getOffresActuelles().isEmpty()) {
            System.out.println("Aucune offre pour le moment");
            return;
        }

        for (int i = 0; i < partie.getOffresActuelles().size(); i++) {
            Offre offre = partie.getOffresActuelles().get(i);
            System.out.println("\nOffre " + (i + 1) + " - [" + offre.getProprietaire().getNom() + "]");

            if (offre.getCarteCachee().estVisible()) {
                System.out.println("  Carte 1 : " + offre.getCarteVisible());
                System.out.println("  Carte 2 : " + offre.getCarteCachee());
            } else {
                System.out.println("  Visible : " + offre.getCarteVisible());
                System.out.println("  Cachée  : [?]");
            }

            System.out.println("  Statut  : " + (offre.estComplete() ? "Complète" : "Incomplète"));
        }
    }
}