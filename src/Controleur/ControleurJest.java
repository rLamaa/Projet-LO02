package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

import jest_package1.*;
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
            // Variante stratégique : les deux cartes sont visibles
            int choix = JOptionPane.showConfirmDialog(
                vue.getFrame(),
                "Mode Stratégique : Vos deux cartes seront visibles\n" +
                "Carte 1 : " + c1 + "\n" +
                "Carte 2 : " + c2 + "\n\n" +
                "Créer cette offre ?",
                "Offre - Variante Stratégique",
                JOptionPane.YES_NO_OPTION
            );
            
            if (choix != JOptionPane.YES_OPTION) return;
            
            // Retirer les cartes du jest temporaire
            joueurHumain.getJest().enleverCarte(c1);
            joueurHumain.getJest().enleverCarte(c2);
            
            Offre offre = new Offre(c1, c2, joueurHumain);
            joueurHumain.setOffreGUI(offre);
            
            vue.ajouterLog("[" + joueurHumain.getNom() + "] Offre créée - Carte 1: " + 
                          c1 + " | Carte 2: " + c2);
        } else {
            // Standard : choisir quelle carte cacher
            String[] options = new String[cartes.size()];
            for (int i = 0; i < cartes.size(); i++) {
                options[i] = (i + 1) + ". " + cartes.get(i).toString();
            }
            
            // Sélection de la carte cachée
            String choixCachee = (String) JOptionPane.showInputDialog(
                vue.getFrame(),
                "Quelle carte voulez-vous CACHER ?",
                "Sélection de la carte cachée",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choixCachee == null) return;
            
            int indexCachee = Integer.parseInt(choixCachee.substring(0, 1)) - 1;
            Carte carteCachee = cartes.get(indexCachee);
            
            // L'autre carte devient visible
            Carte carteVisible = cartes.get(1 - indexCachee);
            
            // Retirer les cartes du jest temporaire
            joueurHumain.getJest().enleverCarte(carteCachee);
            joueurHumain.getJest().enleverCarte(carteVisible);
            
            // Créer l'offre
            Offre offre = new Offre(carteCachee, carteVisible, joueurHumain);
            joueurHumain.setOffreGUI(offre);
            
            vue.ajouterLog("[" + joueurHumain.getNom() + "] Offre créée - Visible: " + 
                          carteVisible + " | Cachée: [?]");
        }
        
        // Désactiver le bouton
        vue.getBoutonFaireOffre().setEnabled(false);
        vue.setJoueurActif("Offre créée, en attente...");
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
            options[0]
        );
        
        if (choixOffre == null) {
            // Annulation
            joueurHumain.setChoixCarteGUI(null);
            return;
        }     
        
        int indexOffre = Integer.parseInt(choixOffre.substring(0, 1)) - 1;
        Offre offreChoisie = offresDisponibles.get(indexOffre);
        
        // Choisir la carte
        String[] choixCartes;
        if (offresVisibles) {
            choixCartes = new String[] {
                "1. Carte 1: " + offreChoisie.getCarteVisible(),
                "2. Carte 2: " + offreChoisie.getCarteCachee()
            };
        } else {
            choixCartes = new String[] {
                "1. Carte visible: " + offreChoisie.getCarteVisible(),
                "2. Carte cachée: [?]"
            };
        }
        
        String choix = (String) JOptionPane.showInputDialog(
            vue.getFrame(),
            "Quelle carte voulez-vous prendre ?",
            "Choix de carte",
            JOptionPane.QUESTION_MESSAGE,
            null,
            choixCartes,
            choixCartes[0]
        );
        
        if (choix == null) {
            // Annulation
            joueurHumain.setChoixCarteGUI(null);
            return;
        }    
        
        Carte carteChoisie;
        if (choix.startsWith("2")) {
            carteChoisie = offreChoisie.getCarteCachee();
        } else {
            carteChoisie = offreChoisie.getCarteVisible();
        }
        
        // Créer le choix et le transmettre
        ChoixCarte choixCarte = new ChoixCarte(offreChoisie, carteChoisie);
        joueurHumain.setChoixCarteGUI(choixCarte);
        
        vue.ajouterLog("[" + joueurHumain.getNom() + "] a choisi: " + carteChoisie + 
                      " de l'offre de " + offreChoisie.getProprietaire().getNom());
        
        // Désactiver le bouton
        vue.getBoutonChoisirCarte().setEnabled(false);
        vue.setJoueurActif("Carte choisie, en attente...");
    }
    
    /**
     * Gère le cas où le joueur doit choisir dans sa propre offre
     */
    private void gererChoixPropreOffre(Offre offre) {
        JOptionPane.showMessageDialog(vue.getFrame(),
            "Vous êtes le dernier joueur.\nVous devez choisir dans votre propre offre.",
            "Dernier joueur", JOptionPane.INFORMATION_MESSAGE);
        
        boolean offresVisibles = offre.getCarteCachee().estVisible();
        
        String[] choixCartes;
        if (offresVisibles) {
            choixCartes = new String[] {
                "1. Carte 1: " + offre.getCarteVisible(),
                "2. Carte 2: " + offre.getCarteCachee()
            };
        } else {
            choixCartes = new String[] {
                "1. Carte visible: " + offre.getCarteVisible(),
                "2. Carte cachée: [?]"
            };
        }
        
        String choix = (String) JOptionPane.showInputDialog(
            vue.getFrame(),
            "Quelle carte voulez-vous garder ?",
            "Choix dans votre offre",
            JOptionPane.QUESTION_MESSAGE,
            null,
            choixCartes,
            choixCartes[0]
        );
        
        if (choix == null) return;
        
        Carte carteChoisie;
        if (choix.startsWith("2")) {
            carteChoisie = offre.getCarteCachee();
        } else {
            carteChoisie = offre.getCarteVisible();
        }
        
        ChoixCarte choixCarte = new ChoixCarte(offre, carteChoisie);
        joueurHumain.setChoixCarteGUI(choixCarte);
        
        vue.ajouterLog("[" + joueurHumain.getNom() + "] a gardé: " + carteChoisie);
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