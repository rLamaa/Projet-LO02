package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

import jest_package1.*;
import Vue.GestionnaireImages;
import Vue.InterfaceGraphiqueJest;

/**
 * Contrôleur pour l'interface graphique du jeu de Jest
 * Gère les interactions entre la vue (GUI) et le modèle (Partie, Joueur)
 */
public class ControleurJest {

    private Partie partie;
    private JoueurHumainGUI joueurHumain;
    private InterfaceGraphiqueJest vue;

    /**
     * Constructeur du contrôleur
     */
    public ControleurJest(Partie partie, JoueurHumainGUI joueurHumain, InterfaceGraphiqueJest vue) {
        this.partie = partie;
        this.joueurHumain = joueurHumain;
        this.vue = vue;

        initialiserEcouteurs();
    }

    /**
     * Initialise les écouteurs d'événements
     */
    private void initialiserEcouteurs() {
        // Écouteur pour le bouton "Faire une offre"
        vue.getBoutonFaireOffre().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gererFaireOffre();
            }
        });

        // Écouteur pour le bouton "Choisir une carte"
        vue.getBoutonChoisirCarte().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gererChoisirCarte();
            }
        });
    }

    /**
     * Gère la création d'une offre par le joueur humain
     */
    private void gererFaireOffre() {
        List<Carte> cartes = joueurHumain.getJest().getCartes();

        if (cartes.size() < 2) {
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Vous n'avez pas assez de cartes pour faire une offre.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            vue.getBoutonFaireOffre().setEnabled(true);
            return;
        }

        // Déterminer si c'est la variante stratégique
        boolean offresVisibles = false;
        List<Offre> offresActuelles = partie.getOffresActuelles();
        if (offresActuelles != null && !offresActuelles.isEmpty()) {
            Offre premiere = offresActuelles.get(0);
            if (premiere.getCarteCachee() != null) {
                offresVisibles = premiere.getCarteCachee().estVisible();
            }
        }

        Carte c1 = cartes.get(0);
        Carte c2 = cartes.get(1);

        if (offresVisibles) {
            // Variante stratégique : créer l'offre automatiquement
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Mode Stratégique : Vos deux cartes seront visibles !",
                    "Offre Créée",
                    JOptionPane.INFORMATION_MESSAGE);

            joueurHumain.getJest().enleverCarte(c1);
            joueurHumain.getJest().enleverCarte(c2);

            Offre offre = new Offre(c1, c2, joueurHumain);
            joueurHumain.setOffreGUI(offre);

            vue.ajouterLog("[" + joueurHumain.getNom() + "] Offre créée (stratégique)");
        } else {
            // Standard : dialogue avec images de cartes
            JDialog dialog = new JDialog(vue.getFrame(), "Quelle carte voulez-vous CACHER ?", true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLayout(new java.awt.GridLayout(1, 2, 20, 20));
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(vue.getFrame());
            dialog.getContentPane().setBackground(new java.awt.Color(240, 240, 240));

            // Bouton Carte 1 (sera cachée)
            JButton btn1 = creerBoutonCarteChoix(c1);
            btn1.addActionListener(e -> {
                joueurHumain.getJest().enleverCarte(c1);
                joueurHumain.getJest().enleverCarte(c2);
                Offre offre = new Offre(c1, c2, joueurHumain);
                joueurHumain.setOffreGUI(offre);
                vue.ajouterLog("[" + joueurHumain.getNom() + "] a caché la première carte");
                dialog.dispose();
            });
            dialog.add(btn1);

            // Bouton Carte 2 (sera cachée)
            JButton btn2 = creerBoutonCarteChoix(c2);
            btn2.addActionListener(e -> {
                joueurHumain.getJest().enleverCarte(c2);
                joueurHumain.getJest().enleverCarte(c1);
                Offre offre = new Offre(c2, c1, joueurHumain);
                joueurHumain.setOffreGUI(offre);
                vue.ajouterLog("[" + joueurHumain.getNom() + "] a caché la deuxième carte");
                dialog.dispose();
            });
            dialog.add(btn2);

            dialog.setVisible(true);
        }

        // Désactiver le bouton
        vue.getBoutonFaireOffre().setEnabled(false);
        vue.setJoueurActif("Offre créée, en attente...");
    }

    /**
     * Crée un bouton pour choisir une carte (avec image)
     */
    private JButton creerBoutonCarteChoix(Carte carte) {
        JButton btn = new JButton();
        btn.setLayout(new java.awt.BorderLayout());

        // Essayer de charger l'image
        ImageIcon icone = GestionnaireImages.chargerImageCarte(carte, false);
        if (icone != null) {
            btn.setIcon(icone);
        } else {
            btn.setText(carte.toString());
            btn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
            btn.setBackground(new java.awt.Color(200, 200, 200));
        }
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Gère le choix d'une carte parmi les offres
     */
    private void gererChoisirCarte() {
        List<Offre> offresActuelles = partie.getOffresActuelles();
        if (offresActuelles == null) {
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Aucune offre disponible pour le moment.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            vue.getBoutonChoisirCarte().setEnabled(true);
            return;
        }

        // Filtrer les offres disponibles
        List<Offre> offresDisponibles = new ArrayList<>();
        Offre offreJoueur = null;

        for (Offre o : offresActuelles) {
            if (o.getProprietaire() == joueurHumain) {
                offreJoueur = o;
            } else if (o.estComplete()) {
                offresDisponibles.add(o);
            }
        }

        // Vérification si le joueur doit prendre de sa propre offre
        // Dernier joueur donc aucune offre disponible sauf la sienne
        if (offresDisponibles.isEmpty() && offreJoueur != null && offreJoueur.estComplete()) {
            gererChoixPropreOffre(offreJoueur);
            return;
        }

        // Vraiment aucune offre
        if (offresDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(vue.getFrame(),
                    "Aucune offre disponible.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);

            // Débloquer quand même pour éviter le blocage
            joueurHumain.setChoixCarteGUI(null);
            vue.getBoutonChoisirCarte().setEnabled(true);
            return;
        }

        // Déterminer si les offres sont visibles
        boolean offresVisibles = offresDisponibles.get(0).getCarteCachee().estVisible();

        // Créer les options
        String[] options = new String[offresDisponibles.size()];
        for (int i = 0; i < offresDisponibles.size(); i++) {
            Offre o = offresDisponibles.get(i);
            if (offresVisibles) {
                options[i] = (i + 1) + ". [" + o.getProprietaire().getNom() +
                        "] Carte 1: " + o.getCarteVisible() + " | Carte 2: " + o.getCarteCachee();
            } else {
                options[i] = (i + 1) + ". [" + o.getProprietaire().getNom() +
                        "] Visible: " + o.getCarteVisible() + " | Cachée: [?]";
            }
        }

        // Choisir l'offre
        String choixOffre = (String) JOptionPane.showInputDialog(
                vue.getFrame(),
                "Choisissez une offre :",
                "Sélection d'offre",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choixOffre == null) {
            // Annulation
            joueurHumain.setChoixCarteGUI(null);
            vue.getBoutonChoisirCarte().setEnabled(true);
            return;
        }

        int indexOffre = Integer.parseInt(choixOffre.substring(0, 1)) - 1;
        Offre offreChoisie = offresDisponibles.get(indexOffre);

        // Choisir la carte avec images visuelles
        JDialog dialogCarte = new JDialog(vue.getFrame(), "Quelle carte voulez-vous prendre ?", true);
        dialogCarte.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogCarte.setLayout(new java.awt.GridLayout(1, 2, 20, 20));
        dialogCarte.setSize(500, 350);
        dialogCarte.setLocationRelativeTo(vue.getFrame());
        dialogCarte.getContentPane().setBackground(new java.awt.Color(240, 240, 240));

        // Bouton carte visible
        JButton btnVisible = creerBoutonCarteChoix(offreChoisie.getCarteVisible());
        btnVisible.addActionListener(e -> {
            ChoixCarte choixCarte = new ChoixCarte(offreChoisie, offreChoisie.getCarteVisible());
            joueurHumain.setChoixCarteGUI(choixCarte);
            vue.ajouterLog("[" + joueurHumain.getNom() + "] a choisi: " + offreChoisie.getCarteVisible());
            dialogCarte.dispose();
        });
        dialogCarte.add(btnVisible);

        // Bouton carte cachée
        Carte carteCachee = offreChoisie.getCarteCachee();
        JButton btnCachee = creerBoutonCarteChoix(carteCachee);
        btnCachee.addActionListener(e -> {
            ChoixCarte choixCarte = new ChoixCarte(offreChoisie, carteCachee);
            joueurHumain.setChoixCarteGUI(choixCarte);
            vue.ajouterLog("[" + joueurHumain.getNom() + "] a choisi une carte cachée");
            dialogCarte.dispose();
        });
        dialogCarte.add(btnCachee);

        dialogCarte.setVisible(true);

        // Désactiver le bouton
        vue.getBoutonChoisirCarte().setEnabled(false);
        vue.setJoueurActif("Carte choisie, en attente...");
    }

    /**
     * Gère le cas où le joueur doit choisir dans sa propre offre
     */
    private void gererChoixPropreOffre(Offre offre) {
        // Dialog visuelle avec les 2 cartes comme boutons
        JDialog dialogPropreOffre = new JDialog(vue.getFrame(), "Votre propre offre", true);
        dialogPropreOffre.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogPropreOffre.setLayout(new java.awt.GridLayout(1, 2, 20, 20));
        dialogPropreOffre.setSize(500, 350);
        dialogPropreOffre.setLocationRelativeTo(vue.getFrame());
        dialogPropreOffre.getContentPane().setBackground(new java.awt.Color(240, 240, 240));

        // Bouton carte visible
        JButton btnVisible = creerBoutonCarteChoix(offre.getCarteVisible());
        btnVisible.addActionListener(e -> {
            ChoixCarte choixCarte = new ChoixCarte(offre, offre.getCarteVisible());
            joueurHumain.setChoixCarteGUI(choixCarte);
            vue.ajouterLog("[" + joueurHumain.getNom() + "] a gardé: " + offre.getCarteVisible());
            dialogPropreOffre.dispose();
        });
        dialogPropreOffre.add(btnVisible);

        // Bouton carte cachée
        Carte carteCachee = offre.getCarteCachee();
        JButton btnCachee = creerBoutonCarteChoix(carteCachee);
        btnCachee.addActionListener(e -> {
            ChoixCarte choixCarte = new ChoixCarte(offre, carteCachee);
            joueurHumain.setChoixCarteGUI(choixCarte);
            vue.ajouterLog("[" + joueurHumain.getNom() + "] a gardé une carte cachée");
            dialogPropreOffre.dispose();
        });
        dialogPropreOffre.add(btnCachee);

        dialogPropreOffre.setVisible(true);
        vue.getBoutonChoisirCarte().setEnabled(false);
        vue.setJoueurActif("Carte choisie, en attente...");
    }

    /**
     * Active/désactive les boutons selon l'état du jeu
     */
    public void activerBoutonFaireOffre(boolean actif) {
        vue.getBoutonFaireOffre().setEnabled(actif);
        if (actif) {
            vue.setJoueurActif("À vous de faire une offre !");
        }
    }

    public void activerBoutonChoisirCarte(boolean actif) {
        vue.getBoutonChoisirCarte().setEnabled(actif);
        if (actif) {
            vue.setJoueurActif("À vous de choisir une carte !");
        }
    }
}