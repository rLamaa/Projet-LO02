package Vue;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

import Controleur.ControleurJest;
import jest_package1.*;

/**
 * Interface graphique pour le jeu de Jest.
 * 
 * Constitue la couche de présentation (Vue) du pattern MVC.
 * Implémente Observer pour être notifiée des changements dans le modèle
 * (Partie).
 * 
 * Responsabilités:
 * - Affichage du tableau de jeu (cartes, offres, joueurs, trophées)
 * - Gestion des interactions utilisateur (clics sur cartes, boutons)
 * - Mise à jour en temps réel lors des changements de partie
 * - Communication avec le contrôleur pour valider les actions
 * 
 * Composants principaux:
 * - Panneau des cartes du joueur
 * - Panneau des offres disponibles
 * - Panneau des joueurs avec leurs scores
 * - Panneau des trophées remportés
 * - Zone de log pour suivre le déroulement
 * 
 * Utilise Swing pour l'interface graphique multi-plateforme.
 * 
 * @author David et Léna
 */
public class InterfaceGraphiqueJest implements Observer {

    // Composants graphiques
    private JFrame frame;
    private JPanel panelCartesJoueur;
    private JPanel panelOffres;
    private JPanel panelJoueurs;
    private JPanel panelTrophees;
    private JTextArea textAreaLog;
    private JButton boutonFaireOffre;
    private JButton boutonChoisirCarte;
    private JLabel labelManche;
    private JLabel labelJoueurActif;
    private JLabel labelRegle;
    private JTextArea textDesc;

    // Références au modèle
    private Partie partie;
    private Joueur joueurHumain;

    /**
     * Constructeur de l'interface graphique
     */
    public InterfaceGraphiqueJest(Partie partie, Joueur joueurHumain) {
        this.partie = partie;
        this.joueurHumain = joueurHumain;
        initialize();
    }

    /**
     * Méthode update appelée quand le modèle change
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Partie) {
            // Utiliser SwingUtilities pour mettre à jour l'interface dans le thread EDT
            SwingUtilities.invokeLater(() -> rafraichirInterface());
        }
    }

    /**
     * Initialise les composants de l'interface
     */
    private void initialize() {
        frame = new JFrame("Jeu de Jest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel supérieur avec informations
        frame.add(creerPanelInfo(), BorderLayout.NORTH);

        // Panel central avec les offres
        creerPanelOffres();
        frame.add(panelOffres, BorderLayout.CENTER);

        // Panel gauche avec les joueurs
        creerPanelJoueurs();
        JScrollPane scrollJoueurs = new JScrollPane(panelJoueurs);
        scrollJoueurs.setPreferredSize(new Dimension(250, 600));
        frame.add(scrollJoueurs, BorderLayout.WEST);

        // Panel droit avec log et trophées
        frame.add(creerPanelDroit(), BorderLayout.EAST);

        // Panel inférieur avec les cartes du joueur et boutons
        frame.add(creerPanelBas(), BorderLayout.SOUTH);
    }

    /**
     * Crée le panel d'informations en haut
     */
    private JPanel creerPanelInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(240, 240, 255));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        labelManche = new JLabel("Manche 1");
        labelManche.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(labelManche);

        panel.add(new JLabel("|"));

        labelRegle = new JLabel("Règles Standard");
        labelRegle.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(labelRegle);

        panel.add(new JLabel("|"));

        labelJoueurActif = new JLabel("En attente...");
        labelJoueurActif.setFont(new Font("Arial", Font.PLAIN, 14));
        labelJoueurActif.setForeground(new Color(0, 128, 0));
        panel.add(labelJoueurActif);

        return panel;
    }

    /**
     * Crée le panel des offres au centre
     */
    private void creerPanelOffres() {
        panelOffres = new JPanel();
        panelOffres.setLayout(new GridLayout(2, 2, 15, 15));
        panelOffres.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Offres des joueurs",
                0, 0, new Font("Arial", Font.BOLD, 16)));
        panelOffres.setBackground(new Color(250, 250, 250));
    }

    /**
     * Crée le panel des joueurs à gauche
     */
    private void creerPanelJoueurs() {
        panelJoueurs = new JPanel();
        panelJoueurs.setLayout(new BoxLayout(panelJoueurs, BoxLayout.Y_AXIS));
        panelJoueurs.setBorder(BorderFactory.createTitledBorder("Joueurs"));
        panelJoueurs.setBackground(Color.WHITE);
    }

    /**
     * Crée le panel droit (log + trophées)
     */
    private JPanel creerPanelDroit() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(280, 600));

        // Trophées en haut
        panelTrophees = new JPanel();
        panelTrophees.setLayout(new BoxLayout(panelTrophees, BoxLayout.Y_AXIS));
        panelTrophees.setBorder(BorderFactory.createTitledBorder("🏆 Trophées"));
        panelTrophees.setBackground(new Color(255, 250, 205));
        JScrollPane scrollTrophees = new JScrollPane(panelTrophees);
        scrollTrophees.setPreferredSize(new Dimension(270, 200));
        panel.add(scrollTrophees, BorderLayout.NORTH);

        // Log en bas
        textAreaLog = new JTextArea();
        textAreaLog.setEditable(false);
        textAreaLog.setLineWrap(true);
        textAreaLog.setWrapStyleWord(true);
        textAreaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(textAreaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Journal"));
        panel.add(scrollLog, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crée le panel du bas (cartes + boutons)
     */
    private JPanel creerPanelBas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));

        // Cartes du joueur
        panelCartesJoueur = new JPanel();
        panelCartesJoueur.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelCartesJoueur.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2),
                "Vos cartes",
                0, 0, new Font("Arial", Font.BOLD, 14)));
        panelCartesJoueur.setPreferredSize(new Dimension(900, 150));
        panelCartesJoueur.setBackground(new Color(230, 240, 255));
        panel.add(panelCartesJoueur, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        boutonFaireOffre = new JButton("Faire une offre");
        boutonFaireOffre.setFont(new Font("Arial", Font.BOLD, 14));
        boutonFaireOffre.setPreferredSize(new Dimension(150, 40));
        boutonFaireOffre.setEnabled(true);
        panelBoutons.add(boutonFaireOffre);

        boutonChoisirCarte = new JButton("Choisir une carte");
        boutonChoisirCarte.setFont(new Font("Arial", Font.BOLD, 14));
        boutonChoisirCarte.setPreferredSize(new Dimension(150, 40));
        boutonChoisirCarte.setEnabled(true);
        panelBoutons.add(boutonChoisirCarte);

        panel.add(panelBoutons, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Rafraîchit l'affichage de l'interface
     */
    private void rafraichirInterface() {
        afficherCartesJoueur();
        afficherOffres();
        afficherJoueurs();
        afficherTrophees();
        labelManche.setText("Manche " + partie.getNumeroManche());

        // Afficher le type de règle
        RegleJeu regle = partie.getJoueurs().get(0).getOffreCourante() != null
                ? (partie.getJoueurs().get(0).getOffreCourante().getCarteCachee().estVisible() ? new RegleStrategique()
                        : new RegleStandard())
                : new RegleStandard();

        if (regle instanceof RegleStrategique) {
            labelRegle.setText("Variante Stratégique");
        } else if (regle instanceof VarianteRapide) {
            labelRegle.setText("Variante Rapide");
        } else {
            labelRegle.setText("Règles Standard");
        }
    }

    /**
     * Affiche les cartes du joueur humain
     */
    private void afficherCartesJoueur() {
        panelCartesJoueur.removeAll();

        List<Carte> cartes = joueurHumain.getJest().getCartes();
        if (cartes.isEmpty()) {
            JLabel label = new JLabel("Aucune carte en main");
            label.setFont(new Font("Arial", Font.ITALIC, 14));
            panelCartesJoueur.add(label);
        } else {
            for (Carte carte : cartes) {
                JButton btnCarte = creerBoutonCarte(carte, false);
                panelCartesJoueur.add(btnCarte);
            }
        }

        panelCartesJoueur.revalidate();
        panelCartesJoueur.repaint();
    }

    /**
     * Crée un label pour afficher une carte (sans bouton)
     */
    private JLabel creerLabelCarte(Carte carte, boolean petite) {
        JLabel label = new JLabel();

        // Tenter de charger l'image
        ImageIcon icone = GestionnaireImages.chargerImageCarte(carte, petite);

        if (icone != null) {
            label.setIcon(icone);
            label.setToolTipText(carte.toString());
        } else {
            // Fallback : mode texte
            String texte = carte.toString();
            label.setText(texte);
            label.setHorizontalAlignment(SwingConstants.CENTER);

            if (petite) {
                label.setPreferredSize(new Dimension(60, 90));
                label.setFont(new Font("Arial", Font.BOLD, 16));
            } else {
                label.setPreferredSize(new Dimension(80, 120));
                label.setFont(new Font("Arial", Font.BOLD, 20));
            }

            // Couleur de fond
            label.setOpaque(true);
            if (carte instanceof CarteCouleur) {
                CarteCouleur cc = (CarteCouleur) carte;
                switch (cc.getCouleur()) {
                    case PIQUE:
                        label.setBackground(new Color(220, 220, 220));
                        break;
                    case COEUR:
                        label.setBackground(new Color(255, 200, 200));
                        label.setForeground(Color.RED);
                        break;
                    case CARREAU:
                        label.setBackground(new Color(255, 220, 180));
                        label.setForeground(Color.RED);
                        break;
                    case TREFLE:
                        label.setBackground(new Color(200, 255, 200));
                        break;
                    case ETOILE:
                        label.setBackground(new Color(255, 255, 150));
                        break;
                    case TRIANGLE:
                        label.setBackground(new Color(200, 220, 255));
                        break;
                    case SOLEIL:
                        label.setBackground(new Color(255, 200, 100));
                        break;
                }
            } else if (carte instanceof Joker) {
                label.setBackground(new Color(255, 215, 0));
                label.setForeground(Color.BLACK);
            }
        }

        return label;
    }

    /**
     * Crée un bouton représentant une carte
     */
    private JButton creerBoutonCarte(Carte carte, boolean petite) {
        JButton btn = new JButton();

        // Tenter de charger l'image
        ImageIcon icone = GestionnaireImages.chargerImageCarte(carte, petite);

        if (icone != null) {
            // Mode image
            btn.setIcon(icone);
            // Ajouter le texte en tooltip
            btn.setToolTipText(carte.toString());
        } else {
            // Fallback : mode texte
            String texte = carte.toString();
            btn.setText("<html><center>" + texte + "</center></html>");

            if (petite) {
                btn.setPreferredSize(new Dimension(60, 90));
                btn.setFont(new Font("Arial", Font.BOLD, 16));
            } else {
                btn.setPreferredSize(new Dimension(80, 120));
                btn.setFont(new Font("Arial", Font.BOLD, 20));
            }

            // Couleur de fond
            if (carte instanceof CarteCouleur) {
                CarteCouleur cc = (CarteCouleur) carte;
                switch (cc.getCouleur()) {
                    case PIQUE:
                        btn.setBackground(new Color(220, 220, 220));
                        break;
                    case COEUR:
                        btn.setBackground(new Color(255, 200, 200));
                        btn.setForeground(Color.RED);
                        break;
                    case CARREAU:
                        btn.setBackground(new Color(255, 220, 180));
                        btn.setForeground(Color.RED);
                        break;
                    case TREFLE:
                        btn.setBackground(new Color(200, 255, 200));
                        break;
                    case ETOILE:
                        btn.setBackground(new Color(255, 255, 150));
                        break;
                    case TRIANGLE:
                        btn.setBackground(new Color(200, 220, 255));
                        break;
                    case SOLEIL:
                        btn.setBackground(new Color(255, 200, 100));
                        break;
                }
            } else if (carte instanceof Joker) {
                btn.setBackground(new Color(255, 215, 0));
                btn.setForeground(Color.BLACK);
            }
        }

        btn.setEnabled(true);
        return btn;
    }

    /**
     * Crée un bouton pour une carte cachée avec l'image dos
     */
    private JButton creerBoutonCarteCachee(boolean petite) {
        JButton btn = new JButton();

        ImageIcon icone = GestionnaireImages.chargerImageDos(petite);

        if (icone != null) {
            btn.setIcon(icone);
            btn.setToolTipText("Carte cachée");
        } else {
            // Fallback
            btn.setText("?");
            btn.setPreferredSize(new Dimension(petite ? 60 : 80, petite ? 90 : 120));
            btn.setFont(new Font("Arial", Font.BOLD, 30));
            btn.setBackground(new Color(100, 100, 100));
            btn.setForeground(Color.WHITE);
        }

        btn.setEnabled(true);
        return btn;
    }

    /**
     * Affiche les offres de tous les joueurs
     */
    private void afficherOffres() {
        panelOffres.removeAll();

        List<Offre> offres = partie.getOffresActuelles();
        if (offres == null || offres.isEmpty()) {
            JLabel label = new JLabel("En attente des offres...");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panelOffres.add(label);
        } else {
            for (Offre offre : offres) {
                panelOffres.add(creerPanelOffre(offre));
            }
        }

        panelOffres.revalidate();
        panelOffres.repaint();
    }

    /**
     * Crée un panel pour une offre
     */
    private JPanel creerPanelOffre(Offre offre) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.setBackground(Color.WHITE);

        // Nom du joueur
        JLabel labelJoueur = new JLabel(offre.getProprietaire().getNom());
        labelJoueur.setFont(new Font("Arial", Font.BOLD, 14));
        labelJoueur.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(labelJoueur, BorderLayout.NORTH);

        // Cartes
        JPanel panelCartes = new JPanel();
        panelCartes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelCartes.setBackground(Color.WHITE);

        if (offre.getCarteVisible() != null) {
            JPanel carte1Panel = new JPanel();
            carte1Panel.setLayout(new BorderLayout());
            carte1Panel.add(new JLabel("Visible:", SwingConstants.CENTER), BorderLayout.NORTH);
            carte1Panel.add(creerBoutonCarte(offre.getCarteVisible(), true), BorderLayout.CENTER);
            panelCartes.add(carte1Panel);
        }

        if (offre.getCarteCachee() != null) {
            JPanel carte2Panel = new JPanel();
            carte2Panel.setLayout(new BorderLayout());

            if (offre.getCarteCachee().estVisible()) {
                // Variante stratégique - montrer l'image
                carte2Panel.add(new JLabel("Carte 2:", SwingConstants.CENTER), BorderLayout.NORTH);
                carte2Panel.add(creerBoutonCarte(offre.getCarteCachee(), true), BorderLayout.CENTER);
            } else {
                // Standard - montrer le dos
                carte2Panel.add(new JLabel("Cachée:", SwingConstants.CENTER), BorderLayout.NORTH);
                carte2Panel.add(creerBoutonCarteCachee(true), BorderLayout.CENTER);
            }
            panelCartes.add(carte2Panel);
        }

        panel.add(panelCartes, BorderLayout.CENTER);

        // Statut
        String statut = offre.estComplete() ? "Complète" : "Incomplète";
        JLabel labelStatut = new JLabel(statut);
        labelStatut.setFont(new Font("Arial", Font.ITALIC, 11));
        labelStatut.setHorizontalAlignment(SwingConstants.CENTER);
        labelStatut.setForeground(offre.estComplete() ? new Color(0, 128, 0) : Color.RED);
        panel.add(labelStatut, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Affiche la liste des joueurs et leurs scores
     */
    private void afficherJoueurs() {
        panelJoueurs.removeAll();

        List<Joueur> joueurs = partie.getJoueurs();
        for (Joueur j : joueurs) {
            JPanel panelJoueur = new JPanel();
            panelJoueur.setLayout(new BoxLayout(panelJoueur, BoxLayout.Y_AXIS));
            panelJoueur.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            panelJoueur.setBackground(j == joueurHumain ? new Color(230, 255, 230) : Color.WHITE);

            JLabel labelNom = new JLabel(j.getNom());
            labelNom.setFont(new Font("Arial", Font.BOLD, 14));
            panelJoueur.add(labelNom);

            String type = (j instanceof JoueurHumain) ? "Humain" : "Bot";
            JLabel labelType = new JLabel("Type: " + type);
            labelType.setFont(new Font("Arial", Font.PLAIN, 11));
            panelJoueur.add(labelType);

            JLabel labelCartes = new JLabel("Jest: " + j.getJestPerso().getCartes().size() + " carte(s)");
            panelJoueur.add(labelCartes);

            panelJoueurs.add(panelJoueur);
            panelJoueurs.add(Box.createVerticalStrut(5));
        }

        panelJoueurs.revalidate();
        panelJoueurs.repaint();
    }

    /**
     * Affiche les trophées
     */
    private void afficherTrophees() {
        panelTrophees.removeAll();

        List<Carte> trophees = partie.getTrophees();
        for (int i = 0; i < trophees.size(); i++) {
            Carte trophee = trophees.get(i);
            JPanel panelTrophee = new JPanel();
            panelTrophee.setLayout(new BorderLayout());
            panelTrophee.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            panelTrophee.setBackground(new Color(255, 250, 220));

            JLabel labelNum = new JLabel("Trophée " + (i + 1));
            labelNum.setFont(new Font("Arial", Font.BOLD, 12));
            labelNum.setHorizontalAlignment(SwingConstants.CENTER);
            panelTrophee.add(labelNum, BorderLayout.NORTH);

            JLabel labelTrophee = creerLabelCarte(trophee, true);
            panelTrophee.add(labelTrophee, BorderLayout.CENTER);
            textDesc = new JTextArea(trophee.toString());
            textDesc.setEditable(false);
            textDesc.setFont(new Font("Arial", Font.PLAIN, 10));
            textDesc.setBackground(new Color(255, 250, 220));
            panelTrophee.add(textDesc, BorderLayout.SOUTH);

            panelTrophees.add(panelTrophee);
            panelTrophees.add(Box.createVerticalStrut(5));
        }

        panelTrophees.revalidate();
        panelTrophees.repaint();
    }

    /**
     * Ajoute un message au log
     */
    public void ajouterLog(String message) {
        textAreaLog.append(message + "\n");
        textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
    }

    /**
     * Rend la fenêtre visible
     */
    public void afficher() {
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Met à jour le label du joueur actif
     */
    public void setJoueurActif(String message) {
        labelJoueurActif.setText(message);
    }

    // Getters pour le contrôleur
    public JButton getBoutonFaireOffre() {
        return boutonFaireOffre;
    }

    public JButton getBoutonChoisirCarte() {
        return boutonChoisirCarte;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Partie getPartie() {
        return partie;
    }
}