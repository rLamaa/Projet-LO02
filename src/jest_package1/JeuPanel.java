package jest_package1;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

class JeuPanel extends JPanel {
    private JeuGUI parent;
    private Partie partie;
    private JLabel lblManche;
    private JLabel lblMessagePhase;
    private JPanel tropheesPanel;
    private JPanel zoneInteractionPanel;
    private JTextArea logArea;
    private JButton btnAction;
    private JButton btnSauvegarder;

    // Variables pour gérer le flux du jeu
    private enum PhaseJeu {
        ATTENTE_DISTRIBUTION,
        CREATION_OFFRES,
        ATTENTE_JOUEUR_OFFRE,
        CHOIX_CARTES,
        ATTENTE_JOUEUR_CHOIX,
        FIN_MANCHE
    }

    private PhaseJeu phaseActuelle;
    private int indexJoueurActuel;
    private List<Carte> cartesSelectionneesOffre;

    public JeuPanel(JeuGUI parent) {
        this.parent = parent;
        setBackground(new Color(0, 100, 0));
        setLayout(new BorderLayout());

        // Panel trophées en haut
        tropheesPanel = new JPanel();
        tropheesPanel.setBackground(new Color(0, 100, 0));
        add(tropheesPanel, BorderLayout.NORTH);

        // Panel central pour l'interaction
        zoneInteractionPanel = new JPanel();
        zoneInteractionPanel.setBackground(new Color(0, 120, 0));
        zoneInteractionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2),
                "Zone de jeu", 0, 0, new Font("Arial", Font.BOLD, 16), Color.YELLOW));

        JScrollPane scrollZone = new JScrollPane(zoneInteractionPanel);
        add(scrollZone, BorderLayout.CENTER);

        // Panel de droite avec log
        JPanel rightPanel = new JPanel(new BorderLayout());

        lblManche = new JLabel("Manche 1", SwingConstants.CENTER);
        lblManche.setFont(new Font("Arial", Font.BOLD, 20));
        lblManche.setForeground(Color.YELLOW);
        rightPanel.add(lblManche, BorderLayout.NORTH);

        // Message de phase
        lblMessagePhase = new JLabel("", SwingConstants.CENTER);
        lblMessagePhase.setFont(new Font("Arial", Font.BOLD, 16));
        lblMessagePhase.setForeground(Color.WHITE);

        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.add(lblManche, BorderLayout.NORTH);
        topRightPanel.add(lblMessagePhase, BorderLayout.CENTER);
        rightPanel.add(topRightPanel, BorderLayout.NORTH);

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(logArea);
        rightPanel.add(scrollLog, BorderLayout.CENTER);

        // Boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 100, 0));

        btnAction = new JButton("Distribuer les cartes");
        btnAction.setFont(new Font("Arial", Font.BOLD, 14));
        btnAction.addActionListener(e -> gererActionPrincipale());
        buttonPanel.add(btnAction);

        btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.addActionListener(e -> {
            parent.getJeu().sauvegarder();
            JOptionPane.showMessageDialog(this, "Partie sauvegardée !");
        });
        buttonPanel.add(btnSauvegarder);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        phaseActuelle = PhaseJeu.ATTENTE_DISTRIBUTION;
        indexJoueurActuel = 0;
        cartesSelectionneesOffre = new ArrayList<>();
    }

    public void demarrerPartie(Partie partie) {
        this.partie = partie;
        phaseActuelle = PhaseJeu.ATTENTE_DISTRIBUTION;
        indexJoueurActuel = 0;
        afficherTrophees();
        lblManche.setText("Manche " + partie.getNumeroManche());
        lblMessagePhase.setText("Prêt à commencer ?");
        logArea.setText("=== Nouvelle Partie ===\nCliquez sur 'Distribuer les cartes' pour débuter.\n");
        viderZoneInteraction();
        btnAction.setEnabled(true);
        btnAction.setText("Distribuer les cartes");
    }

    public void chargerPartie() {
        this.partie = Partie.getInstance();
        phaseActuelle = PhaseJeu.ATTENTE_DISTRIBUTION;
        afficherTrophees();
        lblManche.setText("Manche " + partie.getNumeroManche());
        lblMessagePhase.setText("Partie chargée");
    }

    private void afficherTrophees() {
        tropheesPanel.removeAll();
        JLabel lbl = new JLabel("🏆 Trophées: ");
        lbl.setForeground(Color.YELLOW);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        tropheesPanel.add(lbl);

        for (Carte t : partie.getTrophees()) {
            JLabel lblCarte = new JLabel(JeuGUI.getImageCarte(t));
            lblCarte.setToolTipText(t.toString());
            tropheesPanel.add(lblCarte);
        }
        tropheesPanel.revalidate();
        tropheesPanel.repaint();
    }

    private void viderZoneInteraction() {
        zoneInteractionPanel.removeAll();
        zoneInteractionPanel.revalidate();
        zoneInteractionPanel.repaint();
    }

    private void gererActionPrincipale() {
        switch (phaseActuelle) {
            case ATTENTE_DISTRIBUTION:
                distribuerCartes();
                break;
            case ATTENTE_JOUEUR_OFFRE:
                afficherCartesJoueurPourOffre();
                break;
            case ATTENTE_JOUEUR_CHOIX:
                afficherOffresDisponibles();
                break;
            default:
                break;
        }
    }

    private void distribuerCartes() {
        logArea.append("\n>>> Distribution des cartes...\n");
        partie.distribuerCartes();

        for (Joueur j : partie.getJoueurs()) {
            logArea.append(j.getNom() + ": " + j.getJest().getCartes().size() + " cartes\n");
        }

        phaseActuelle = PhaseJeu.CREATION_OFFRES;
        indexJoueurActuel = 0;

        logArea.append("\n>>> Création des offres...\n");
        commencerCreationOffres();
    }

    private void commencerCreationOffres() {
        List<Joueur> joueurs = partie.getJoueurs();

        if (indexJoueurActuel >= joueurs.size()) {
            // Toutes les offres sont créées
            finCreationOffres();
            return;
        }

        Joueur joueurActuel = joueurs.get(indexJoueurActuel);

        // Si c'est un bot, créer l'offre automatiquement
        if (joueurActuel instanceof JoueurVirtuel) {
            boolean offresVisibles = partie.getRegleJeu().sontOffresVisibles();
            Offre offre = joueurActuel.faireOffre(offresVisibles);
            offre = partie.getRegleJeu().creerOffre(joueurActuel, offre.getCarteCachee(), offre.getCarteVisible());

            if (offresVisibles) {
                logArea.append("[" + joueurActuel.getNom() + "] Offre créée - Carte 1 : "
                        + offre.getCarteVisible() + " | Carte 2 : " + offre.getCarteCachee() + "\n");
            } else {
                logArea.append("[" + joueurActuel.getNom() + "] Offre créée - Visible : "
                        + offre.getCarteVisible() + " | Cachée : [?]\n");
            }

            indexJoueurActuel++;
            commencerCreationOffres();
        } else {
            // Joueur humain : attendre qu'il soit prêt
            phaseActuelle = PhaseJeu.ATTENTE_JOUEUR_OFFRE;
            lblMessagePhase.setText("À " + joueurActuel.getNom() + " de créer son offre");
            btnAction.setText("Je suis prêt !");

            viderZoneInteraction();
            JLabel lblMessage = new JLabel("<html><center>🎴 " + joueurActuel.getNom()
                    + ", c'est à vous !<br>Cliquez sur 'Je suis prêt' pour voir vos cartes</center></html>");
            lblMessage.setFont(new Font("Arial", Font.BOLD, 20));
            lblMessage.setForeground(Color.WHITE);
            lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
            zoneInteractionPanel.setLayout(new BorderLayout());
            zoneInteractionPanel.add(lblMessage, BorderLayout.CENTER);
            zoneInteractionPanel.revalidate();
            zoneInteractionPanel.repaint();
        }
    }

    private void afficherCartesJoueurPourOffre() {
        Joueur joueurActuel = partie.getJoueurs().get(indexJoueurActuel);
        Jest jestTemp = joueurActuel.getJest();

        // Vérifier si le joueur a des cartes dans jestTemp
        if (jestTemp != null && jestTemp.getCartes().size() == 2) {
            // Les 2 cartes sont déjà sélectionnées, demander laquelle cacher
            List<Carte> cartes = jestTemp.getCartes();
            Carte c1 = cartes.get(0);
            Carte c2 = cartes.get(1);

            viderZoneInteraction();
            zoneInteractionPanel.setLayout(new BorderLayout());

            JLabel lblInstruction = new JLabel("<html><center>" + joueurActuel.getNom()
                    + ", choisissez quelle carte cacher</center></html>");
            lblInstruction.setFont(new Font("Arial", Font.BOLD, 16));
            lblInstruction.setForeground(Color.WHITE);
            zoneInteractionPanel.add(lblInstruction, BorderLayout.NORTH);

            JPanel cartesPanel = new JPanel(new FlowLayout());
            cartesPanel.setBackground(new Color(0, 120, 0));

            // Bouton pour c1 à cacher
            JButton btn1 = new JButton(JeuGUI.getImageCarte(c1));
            btn1.setToolTipText("Cacher: " + c1);
            btn1.addActionListener(e -> {
                creerOffreeAvecChoix(joueurActuel, c1, c2);
                indexJoueurActuel++;
                commencerCreationOffres();
            });
            cartesPanel.add(btn1);

            // Bouton pour c2 à cacher
            JButton btn2 = new JButton(JeuGUI.getImageCarte(c2));
            btn2.setToolTipText("Cacher: " + c2);
            btn2.addActionListener(e -> {
                creerOffreeAvecChoix(joueurActuel, c2, c1);
                indexJoueurActuel++;
                commencerCreationOffres();
            });
            cartesPanel.add(btn2);

            zoneInteractionPanel.add(cartesPanel, BorderLayout.CENTER);
            zoneInteractionPanel.revalidate();
            zoneInteractionPanel.repaint();
        } else {
            // Fallback: afficher toutes les cartes et demander d'en sélectionner 2
            List<Carte> cartes = joueurActuel.getJest().getCartes();

            viderZoneInteraction();
            zoneInteractionPanel.setLayout(new BorderLayout());

            JLabel lblInstruction = new JLabel("<html><center>" + joueurActuel.getNom()
                    + ", choisissez 2 cartes pour votre offre</center></html>");
            lblInstruction.setFont(new Font("Arial", Font.BOLD, 16));
            lblInstruction.setForeground(Color.WHITE);
            zoneInteractionPanel.add(lblInstruction, BorderLayout.NORTH);

            JPanel cartesPanel = new JPanel(new FlowLayout());
            cartesPanel.setBackground(new Color(0, 120, 0));

            cartesSelectionneesOffre.clear();

            for (Carte carte : cartes) {
                JToggleButton btnCarte = new JToggleButton(JeuGUI.getImageCarte(carte));
                btnCarte.setToolTipText(carte.toString());
                btnCarte.addActionListener(e -> {
                    if (btnCarte.isSelected()) {
                        if (cartesSelectionneesOffre.size() < 2) {
                            cartesSelectionneesOffre.add(carte);
                        } else {
                            btnCarte.setSelected(false);
                            JOptionPane.showMessageDialog(this, "Vous ne pouvez sélectionner que 2 cartes !");
                        }
                    } else {
                        cartesSelectionneesOffre.remove(carte);
                    }

                    // Activer le bouton de validation si 2 cartes sélectionnées
                    btnAction.setEnabled(cartesSelectionneesOffre.size() == 2);
                });
                cartesPanel.add(btnCarte);
            }

            zoneInteractionPanel.add(cartesPanel, BorderLayout.CENTER);

            btnAction.setText("Valider l'offre");
            btnAction.setEnabled(false);
            btnAction.removeActionListener(btnAction.getActionListeners()[0]);
            btnAction.addActionListener(e -> validerOffre());

            zoneInteractionPanel.revalidate();
            zoneInteractionPanel.repaint();
        }
    }

    private void creerOffreeAvecChoix(Joueur joueur, Carte carteCachee, Carte carteVisible) {
        joueur.getJest().enleverCarte(carteCachee);
        joueur.getJest().enleverCarte(carteVisible);

        Offre offre = partie.getRegleJeu().creerOffre(joueur, carteCachee, carteVisible);
        logArea.append("[" + joueur.getNom() + "] Offre créée - Visible : "
                + offre.getCarteVisible() + " | Cachée : [?]\n");
    }

    private void validerOffre() {
        Joueur joueurActuel = partie.getJoueurs().get(indexJoueurActuel);

        if (cartesSelectionneesOffre.size() != 2) {
            JOptionPane.showMessageDialog(this, "Vous devez sélectionner exactement 2 cartes !");
            return;
        }

        Carte c1 = cartesSelectionneesOffre.get(0);
        Carte c2 = cartesSelectionneesOffre.get(1);

        // Retirer les cartes du Jest
        joueurActuel.getJest().enleverCarte(c1);
        joueurActuel.getJest().enleverCarte(c2);

        boolean offresVisibles = partie.getRegleJeu().sontOffresVisibles();

        if (offresVisibles) {
            // Variante stratégique : les deux cartes sont visibles
            Offre offre = partie.getRegleJeu().creerOffre(joueurActuel, c1, c2);
            logArea.append("[" + joueurActuel.getNom() + "] Offre créée - Carte 1 : "
                    + offre.getCarteVisible() + " | Carte 2 : " + offre.getCarteCachee() + "\n");
        } else {
            // Jeu standard : demander quelle carte cacher
            Object[] options = {
                    "<html><center>Cacher cette carte<br>" + c1 + "</center></html>",
                    "<html><center>Cacher cette carte<br>" + c2 + "</center></html>"
            };

            int choix = JOptionPane.showOptionDialog(this,
                    "Quelle carte voulez-vous cacher ?",
                    "Création de l'offre",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            Carte carteCachee = (choix == 0) ? c1 : c2;
            Carte carteVisible = (choix == 0) ? c2 : c1;

            Offre offre = partie.getRegleJeu().creerOffre(joueurActuel, carteCachee, carteVisible);
            logArea.append("[" + joueurActuel.getNom() + "] Offre créée - Visible : "
                    + offre.getCarteVisible() + " | Cachée : [?]\n");
        }

        indexJoueurActuel++;

        // Restaurer l'action du bouton principal
        btnAction.removeActionListener(btnAction.getActionListeners()[0]);
        btnAction.addActionListener(e -> gererActionPrincipale());

        commencerCreationOffres();
    }

    private void finCreationOffres() {
        logArea.append("\n>>> Toutes les offres sont créées !\n");
        phaseActuelle = PhaseJeu.CHOIX_CARTES;

        viderZoneInteraction();
        JLabel lblMessage = new JLabel(
                "<html><center>✅ Toutes les offres sont prêtes !<br>Phase de sélection des cartes</center></html>");
        lblMessage.setFont(new Font("Arial", Font.BOLD, 18));
        lblMessage.setForeground(Color.YELLOW);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        zoneInteractionPanel.setLayout(new BorderLayout());
        zoneInteractionPanel.add(lblMessage, BorderLayout.CENTER);

        Timer timer = new Timer(2000, e -> jouerTourChoixCartes());
        timer.setRepeats(false);
        timer.start();
    }

    private void jouerTourChoixCartes() {
        List<Joueur> joueurs = partie.getJoueurs();

        // Compter combien de cartes restent à distribuer
        int cartesRestantes = 0;
        for (Joueur j : joueurs) {
            if (j.getOffreCourante() != null && j.getOffreCourante().estComplete()) {
                cartesRestantes += j.getOffreCourante().compterCartes();
            }
        }

        if (cartesRestantes == 0) {
            // Fin de la manche
            finManche();
            return;
        }

        // Utiliser la logique du jeu pour déterminer le prochain joueur
        Joueur joueurActif = partie.determinerPremierJoueur();

        if (joueurActif instanceof JoueurVirtuel) {
            // Bot joue automatiquement
            List<Offre> offresDisponibles = new ArrayList<>();
            for (Joueur j : joueurs) {
                if (j.getOffreCourante() != null && j.getOffreCourante().estComplete() && j != joueurActif) {
                    offresDisponibles.add(j.getOffreCourante());
                }
            }

            if (!offresDisponibles.isEmpty()) {
                ChoixCarte choix = joueurActif.choisirCarte(offresDisponibles);
                if (choix != null) {
                    joueurActif.ajouterCarteJestPerso(choix.getCarte());
                    choix.getOffre().retirerCarte(choix.getCarte());
                    logArea.append("[" + joueurActif.getNom() + "] a pris: " + choix.getCarte() + "\n");
                }
            }

            // Continuer au prochain tour après un petit délai
            Timer timer = new Timer(500, e -> jouerTourChoixCartes());
            timer.setRepeats(false);
            timer.start();
        } else {
            // Joueur humain
            phaseActuelle = PhaseJeu.ATTENTE_JOUEUR_CHOIX;
            lblMessagePhase.setText("À " + joueurActif.getNom() + " de choisir");

            afficherOffresDisponiblesEtAttendre(joueurActif);
        }
    }

    private void afficherOffresDisponiblesEtAttendre(Joueur joueurActif) {
        List<Joueur> joueurs = partie.getJoueurs();
        List<Offre> offresDisponibles = new ArrayList<>();

        for (Joueur j : joueurs) {
            if (j.getOffreCourante() != null && j.getOffreCourante().estComplete() && j != joueurActif) {
                offresDisponibles.add(j.getOffreCourante());
            }
        }

        viderZoneInteraction();
        zoneInteractionPanel.setLayout(new BorderLayout());

        JLabel lblInstruction = new JLabel("<html><center>" + joueurActif.getNom()
                + ", choisissez une carte parmi les offres</center></html>");
        lblInstruction.setFont(new Font("Arial", Font.BOLD, 16));
        lblInstruction.setForeground(Color.WHITE);
        zoneInteractionPanel.add(lblInstruction, BorderLayout.NORTH);

        JPanel offresPanel = new JPanel(new FlowLayout());
        offresPanel.setBackground(new Color(0, 120, 0));

        boolean offresVisibles = partie.getRegleJeu().sontOffresVisibles();

        for (Offre offre : offresDisponibles) {
            JPanel offrePanel = new JPanel();
            offrePanel.setLayout(new BoxLayout(offrePanel, BoxLayout.Y_AXIS));
            offrePanel.setBackground(Color.WHITE);
            offrePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 2),
                    offre.getProprietaire().getNom()));

            // Carte visible
            JButton btnVisible = new JButton(JeuGUI.getImageCarte(offre.getCarteVisible()));
            btnVisible.setToolTipText("Carte visible: " + offre.getCarteVisible());
            btnVisible.addActionListener(e -> {
                joueurActif.ajouterCarteJestPerso(offre.getCarteVisible());
                offre.retirerCarte(offre.getCarteVisible());
                logArea.append("[" + joueurActif.getNom() + "] a pris: " + offre.getCarteVisible() + "\n");
                jouerTourChoixCartes();
            });
            offrePanel.add(new JLabel("Visible:"));
            offrePanel.add(btnVisible);

            // Carte cachée
            if (offresVisibles) {
                JButton btnCachee = new JButton(JeuGUI.getImageCarte(offre.getCarteCachee()));
                btnCachee.setToolTipText("Carte 2: " + offre.getCarteCachee());
                btnCachee.addActionListener(e -> {
                    joueurActif.ajouterCarteJestPerso(offre.getCarteCachee());
                    offre.retirerCarte(offre.getCarteCachee());
                    logArea.append("[" + joueurActif.getNom() + "] a pris: " + offre.getCarteCachee() + "\n");
                    jouerTourChoixCartes();
                });
                offrePanel.add(new JLabel("Carte 2:"));
                offrePanel.add(btnCachee);
            } else {
                JButton btnCachee = new JButton("❓");
                btnCachee.setFont(new Font("Arial", Font.BOLD, 60));
                btnCachee.setToolTipText("Carte cachée");
                btnCachee.addActionListener(e -> {
                    joueurActif.ajouterCarteJestPerso(offre.getCarteCachee());
                    offre.retirerCarte(offre.getCarteCachee());
                    logArea.append("[" + joueurActif.getNom() + "] a pris: " + offre.getCarteCachee() + "\n");
                    jouerTourChoixCartes();
                });
                offrePanel.add(new JLabel("Cachée:"));
                offrePanel.add(btnCachee);
            }

            offresPanel.add(offrePanel);
        }

        zoneInteractionPanel.add(offresPanel, BorderLayout.CENTER);
        zoneInteractionPanel.revalidate();
        zoneInteractionPanel.repaint();
    }

    private void afficherOffresDisponibles() {
        // Deprecated - use afficherOffresDisponiblesEtAttendre instead
    }

    private void finManche() {
        logArea.append("\n=== Fin de la manche ===\n");
        afficherTrophees();

        if (partie.verifierFinJeu()) {
            logArea.append("=== FIN DU JEU ===\n");
            partie.terminerPartie();
            parent.getFinPanel().afficherResultats(partie);

            Timer timer = new Timer(2000, e -> parent.showPanel("FIN"));
            timer.setRepeats(false);
            timer.start();
        } else {
            lblManche.setText("Manche " + partie.getNumeroManche());
            lblMessagePhase.setText("Manche suivante - Cliquez sur 'Distribuer les cartes'");
            phaseActuelle = PhaseJeu.ATTENTE_DISTRIBUTION;
            btnAction.setEnabled(true);
            btnAction.setText("Manche " + partie.getNumeroManche());

            viderZoneInteraction();
            JLabel lblMessage = new JLabel(
                    "<html><center>✅ Manche terminée !<br>Prêt pour la suivante ?</center></html>");
            lblMessage.setFont(new Font("Arial", Font.BOLD, 18));
            lblMessage.setForeground(Color.YELLOW);
            lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
            zoneInteractionPanel.setLayout(new BorderLayout());
            zoneInteractionPanel.add(lblMessage, BorderLayout.CENTER);
        }
    }
}