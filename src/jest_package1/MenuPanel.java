package jest_package1;

import javax.swing.*;
import java.awt.*;

class MenuPanel extends JPanel {
    private JeuGUI parent;

    public MenuPanel(JeuGUI parent) {
        this.parent = parent;
        setBackground(new Color(34, 139, 34));
        setLayout(null);

        JLabel lblTitre = new JLabel("JEU DE JEST");
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitre.setBounds(400, 100, 400, 80);
        add(lblTitre);

        JButton btnNouvelle = new JButton("Nouvelle Partie");
        btnNouvelle.setFont(new Font("Arial", Font.BOLD, 16));
        btnNouvelle.setBounds(475, 250, 250, 50);
        btnNouvelle.addActionListener(e -> {
            parent.setJeu(new Jeu());
            parent.showPanel("CONFIG");
        });
        add(btnNouvelle);

        JButton btnCharger = new JButton("Charger une Partie");
        btnCharger.setFont(new Font("Arial", Font.BOLD, 16));
        btnCharger.setBounds(475, 320, 250, 50);
        btnCharger.addActionListener(e -> chargerPartie());
        add(btnCharger);

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.setFont(new Font("Arial", Font.BOLD, 16));
        btnQuitter.setBounds(475, 390, 250, 50);
        btnQuitter.addActionListener(e -> System.exit(0));
        add(btnQuitter);
    }

    private void chargerPartie() {
        Jeu jeu = Jeu.charger("sauvegarde_jeu.dat");
        if (jeu != null) {
            parent.setJeu(jeu);
            parent.showPanel("JEU");
            parent.getJeuPanel().chargerPartie();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement");
        }
    }
}

// ============================================================
// CONFIG PANEL
// ============================================================
class ConfigPanel extends JPanel {
    private JeuGUI parent;
    private JSpinner spinnerJoueurs;
    private JTextField[] champsNoms;
    private JComboBox<String> comboVariante;
    private JCheckBox checkExtension;

    public ConfigPanel(JeuGUI parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblTitre = new JLabel("Configuration de la Partie");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitre.setBounds(400, 30, 400, 40);
        add(lblTitre);

        JLabel lblNbJoueurs = new JLabel("Nombre de joueurs humains:");
        lblNbJoueurs.setBounds(300, 100, 250, 25);
        add(lblNbJoueurs);

        spinnerJoueurs = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        spinnerJoueurs.setBounds(550, 100, 100, 25);
        add(spinnerJoueurs);

        // Champs pour les noms
        champsNoms = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            JLabel lbl = new JLabel("Joueur " + (i + 1) + ":");
            lbl.setBounds(300, 150 + i * 40, 100, 25);
            add(lbl);

            champsNoms[i] = new JTextField();
            champsNoms[i].setBounds(400, 150 + i * 40, 250, 25);
            champsNoms[i].setEnabled(i == 0);
            add(champsNoms[i]);
        }

        spinnerJoueurs.addChangeListener(e -> {
            int nb = (Integer) spinnerJoueurs.getValue();
            for (int i = 0; i < 4; i++) {
                champsNoms[i].setEnabled(i < nb);
            }
        });

        JLabel lblVariante = new JLabel("Variante:");
        lblVariante.setBounds(300, 330, 100, 25);
        add(lblVariante);

        comboVariante = new JComboBox<>(new String[] {
                "Standard", "Rapide (3 manches)", "Stratégique (offres visibles)"
        });
        comboVariante.setBounds(400, 330, 250, 25);
        add(comboVariante);

        checkExtension = new JCheckBox("Activer l'extension (Nouvelles Cartes)");
        checkExtension.setBounds(300, 380, 350, 25);
        add(checkExtension);

        JButton btnDemarrer = new JButton("Démarrer la Partie");
        btnDemarrer.setFont(new Font("Arial", Font.BOLD, 16));
        btnDemarrer.setBounds(475, 450, 250, 50);
        btnDemarrer.addActionListener(e -> demarrerPartie());
        add(btnDemarrer);
    }

    private void demarrerPartie() {
        int nbJoueursHumains = (Integer) spinnerJoueurs.getValue();
        Jeu jeu = parent.getJeu();

        // Ajouter joueurs humains
        for (int i = 0; i < nbJoueursHumains; i++) {
            String nom = champsNoms[i].getText().trim();
            if (nom.isEmpty())
                nom = "Joueur" + (i + 1);
            jeu.ajouterJoueur(new JoueurHumain(nom));
        }

        // Ajouter bots
        int nbBots = Math.max(0, 3 - nbJoueursHumains);
        String[] nomsBots = { "Alpha", "Beta", "Gamma", "Delta" };
        Strategie[] strategies = {
                new StrategieOffensive(),
                new StrategieDefensive(),
                new StrategieAleatoire()
        };

        for (int i = 0; i < nbBots; i++) {
            JoueurVirtuel bot = new JoueurVirtuel("Bot_" + nomsBots[i]);
            bot.setStrategie(strategies[i % strategies.length]);
            jeu.ajouterJoueur(bot);
        }

        // Règle
        RegleJeu regle;
        switch (comboVariante.getSelectedIndex()) {
            case 1:
                regle = new VarianteRapide();
                break;
            case 2:
                regle = new RegleStrategique();
                break;
            default:
                regle = new RegleStandard();
        }
        jeu.choisirRegle(regle);

        boolean avecExtension = checkExtension.isSelected();

        // Initialiser partie
        Partie.reinitialiser();
        Partie partie = Partie.getInstance();
        partie.setJeuReference(jeu);
        partie.initialiser(jeu.getJoueurs(), regle, avecExtension);

        parent.getJeuPanel().demarrerPartie(partie);
        parent.showPanel("JEU");
    }
}