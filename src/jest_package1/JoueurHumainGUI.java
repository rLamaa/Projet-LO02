package jest_package1;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Classe représentant un joueur humain pouvant jouer via interface graphique ou
 * console.
 * 
 * Étend JoueurHumain pour ajouter le support d'une interface graphique Swing
 * tout en maintenant la compatibilité avec le mode console.
 * 
 * Responsabilités:
 * - Supporter le jeu en mode console pure (hérité de JoueurHumain)
 * - Supporter le jeu en mode GUI (avec boutons et dialogues)
 * - Supporter le mode hybride (GUI + console simultanément)
 * 
 * Mécanisme de synchronisation:
 * - Utilise CountDownLatch pour bloquer le thread de jeu
 * - Attend que l'utilisateur fasse son choix dans la GUI
 * - Reprend l'exécution une fois la décision prise
 * 
 * Transitions:
 * - faireOffre(): Crée une offre via GUI ou console
 * - choisirCarte(): Sélectionne une carte via GUI ou console
 * - setOffreGUI() et setChoixCarteGUI(): Appelées par le contrôleur
 * quand l'utilisateur a terminé
 * 
 * @author David et Léna
 */
public class JoueurHumainGUI extends JoueurHumain {
    private static final long serialVersionUID = 1L;

    private boolean utiliseGUI;
    private Offre offreEnAttente;
    private ChoixCarte choixEnAttente;
    private transient CountDownLatch verrouOffre;
    private transient CountDownLatch verrouChoix;

    public JoueurHumainGUI(String nom) {
        super(nom);
        this.utiliseGUI = false;
    }

    /**
     * Active ou désactive le mode GUI
     */
    public void setUtiliseGUI(boolean utilise) {
        this.utiliseGUI = utilise;
    }

    /**
     * Fait une offre : attend l'input de la GUI si activée, sinon utilise la
     * console
     */
    @Override
    public Offre faireOffre(boolean offresVisibles) {
        if (utiliseGUI) {
            // Mode GUI : attendre que le joueur crée l'offre via l'interface
            verrouOffre = new CountDownLatch(1);

            try {
                // Attendre que setOffreGUI() soit appelé par le contrôleur
                verrouOffre.await();
            } catch (InterruptedException e) {
                System.err.println("Interruption lors de l'attente de l'offre GUI");
                e.printStackTrace();
            }

            // Récupérer l'offre créée via la GUI
            Offre offre = offreEnAttente;
            offreEnAttente = null;
            this.offreCourante = offre;
            return offre;

        } else {
            // Mode console : utiliser la méthode standard
            return super.faireOffre(offresVisibles);
        }
    }

    /**
     * Choisit une carte : attend l'input de la GUI si activée, sinon utilise la
     * console
     */
    @Override
    public ChoixCarte choisirCarte(List<Offre> offres) {
        if (utiliseGUI) {
            // Mode GUI : attendre que le joueur choisisse via l'interface
            verrouChoix = new CountDownLatch(1);

            try {
                // Attendre que setChoixCarteGUI() soit appelé par le contrôleur
                verrouChoix.await();
            } catch (InterruptedException e) {
                System.err.println("Interruption lors de l'attente du choix GUI");
                e.printStackTrace();
            }

            // Récupérer le choix fait via la GUI
            ChoixCarte choix = choixEnAttente;
            choixEnAttente = null;
            return choix;

        } else {
            // Mode console : utiliser la méthode standard
            return super.choisirCarte(offres);
        }
    }

    /**
     * Définit l'offre créée via l'interface graphique
     * Appelé par le contrôleur quand l'utilisateur a terminé
     */
    public void setOffreGUI(Offre offre) {
        this.offreEnAttente = offre;
        this.offreCourante = offre;

        // Débloquer le thread principal qui attend dans faireOffre()
        if (verrouOffre != null) {
            verrouOffre.countDown();
        }
    }

    /**
     * Définit le choix de carte fait via l'interface graphique
     * Appelé par le contrôleur quand l'utilisateur a choisi
     */
    public void setChoixCarteGUI(ChoixCarte choix) {
        this.choixEnAttente = choix;

        // Débloquer le thread principal qui attend dans choisirCarte()
        if (verrouChoix != null) {
            verrouChoix.countDown();
        }
    }

    /**
     * Vérifie si le mode GUI est actif
     */
    public boolean utiliseGUI() {
        return utiliseGUI;
    }
}