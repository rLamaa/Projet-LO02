package jest_package1;

import java.util.List;

/**
 * Classe représentant un joueur virtuel (bot) du jeu de Jest.
 * Utilise une stratégie pour automatiser les décisions de jeu.
 * Permet à plusieurs bots avec différentes stratégies de jouer ensemble.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public class JoueurVirtuel extends Joueur {
    private static final long serialVersionUID = 1L;
    private Strategie strategie;
    private transient Partie partieReference;

    public JoueurVirtuel(String nom) {
        super(nom);
        // Stratégie par défaut: offensive
        this.strategie = new StrategieOffensive();
    }

    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public Offre faireOffre(boolean offresVisibles) {
        List<Carte> cartes = jest.getCartes();
        if (cartes.size() < 2) {
            throw new IllegalStateException("Pas assez de cartes pour faire une offre");
        }

        Carte c1 = cartes.remove(0);
        Carte c2 = cartes.remove(0);

        if (offresVisibles) {
            // Variante stratégique : pas de stratégie pour les bot car les deux cartes sont
            // visibles
            this.offreCourante = new Offre(c1, c2, this);
            String message = "[" + nom + "] (Bot) Offre créée - Carte 1 : " + c1 + " | Carte 2 : " + c2;
            System.out.println(message);
            logToGUI(message);
        } else {
            // Jeu standard : Utiliser la stratégie pour choisir quelle carte cacher
            Offre offre = strategie.choisirCartesOffre(c1, c2);
            this.offreCourante = new Offre(offre.getCarteCachee(), offre.getCarteVisible(), this);

            String message = "[" + nom + "] (Bot) Offre créée - Visible: " + this.offreCourante.getCarteVisible();
            System.out.println(message);
            logToGUI(message);

        }

        return this.offreCourante;
    }

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres) {
        // Filtrer les offres complètes qui ne sont pas les nôtres
        List<Offre> offresDisponibles = new java.util.ArrayList<>();
        for (Offre o : offres) {
            if (o.estComplete() && o.getProprietaire() != this) {
                offresDisponibles.add(o);
            }
        }

        if (offresDisponibles.isEmpty()) {
            return null;
        }

        // Utiliser la stratégie pour choisir
        ChoixCarte choix = strategie.choisirCarte(offresDisponibles, jest);

        if (choix != null) {
            String message = "[" + nom + "] (Bot) choisit la carte " +
                    choix.getCarte() + " de l'offre de " +
                    choix.getOffre().getProprietaire().getNom();
            System.out.println(message);
            logToGUI(message);
        }

        return choix;
    }

    /**
     * Envoie un message au log de la GUI si disponible
     */
    private void logToGUI(String message) {
        try {
            // Récupérer la partie via réflexion pour accéder à l'interface
            if (partieReference != null) {
                java.lang.reflect.Field field = Partie.class.getDeclaredField("jeuReference");
                field.setAccessible(true);
                Jeu jeu = (Jeu) field.get(partieReference);

                if (jeu != null) {
                    java.lang.reflect.Field guiField = Jeu.class.getDeclaredField("interfaceGraphique");
                    guiField.setAccessible(true);
                    Object gui = guiField.get(jeu);

                    if (gui != null) {
                        java.lang.reflect.Method method = gui.getClass().getMethod("ajouterLog", String.class);
                        method.invoke(gui, message);
                    }
                }
            }
        } catch (Exception e) {
            // Silencieux si la GUI n'est pas disponible
        }
    }

    /**
     * Définit la référence à la partie (appelé par Partie)
     */
    public void setPartieReference(Partie partie) {
        this.partieReference = partie;
    }

    /**
     * Setter de la stratégie du robot
     * 
     * @param strategie la stratégie que l'on souhaite qu'il adopte
     */
    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

    /**
     * Getter de la stratégie du robot
     * 
     * @return
     */
    public Strategie getStrategie() {
        return strategie;
    }
}