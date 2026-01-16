package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import jest_package1.*;

/**
 * Gestionnaire d'images pour les cartes du jeu de Jest.
 * 
 * Responsable du chargement, du cache et de l'affichage des images de cartes.
 * 
 * Responsabilités:
 * - Charger les images des cartes depuis le système de fichiers ou les
 * ressources
 * - Mettre en cache les images chargées pour améliorer les performances
 * - Fournir les images redimensionnées selon les besoins de l'interface
 * - Gérer les images manquantes avec des images par défaut
 * - Créer les représentations visuelles des cartes (symboles, couleurs,
 * valeurs)
 * 
 * Optimisations:
 * - Cache des images chargées
 * - Redimensionnement efficace
 * - Gestion des erreurs de chargement
 * 
 * @author David et Léna
 */
public class GestionnaireImages {

    private static final String CHEMIN_BASE = "/image/cartes/";
    private static final int LARGEUR_CARTE = 80;
    private static final int HAUTEUR_CARTE = 120;
    private static final int LARGEUR_GRANDE_CARTE = 150;
    private static final int HAUTEUR_GRANDE_CARTE = 225;
    private static final int LARGEUR_TRES_GRANDE_CARTE = 200;
    private static final int HAUTEUR_TRES_GRANDE_CARTE = 300;

    /**
     * Charge l'image d'une carte
     */
    public static ImageIcon chargerImageCarte(Carte carte, boolean petite) {
        String cheminImage = obtenirCheminImage(carte);
        return chargerEtRedimensionner(cheminImage, petite);
    }

    /**
     * Charge l'image du dos d'une carte (pour les cartes cachées)
     */
    public static ImageIcon chargerImageDos(boolean petite) {
        return chargerEtRedimensionner(CHEMIN_BASE + "dos.png", petite);
    }

    /**
     * Charge l'image grande d'une carte
     */
    public static ImageIcon chargerImageCarteGrande(Carte carte) {
        String cheminImage = obtenirCheminImage(carte);
        return chargerEtRedimensionnerPersonnalisee(cheminImage, LARGEUR_GRANDE_CARTE, HAUTEUR_GRANDE_CARTE);
    }

    /**
     * Charge l'image très grande d'une carte
     */
    public static ImageIcon chargerImageCarteTresGrande(Carte carte) {
        String cheminImage = obtenirCheminImage(carte);
        return chargerEtRedimensionnerPersonnalisee(cheminImage, LARGEUR_TRES_GRANDE_CARTE, HAUTEUR_TRES_GRANDE_CARTE);
    }

    /**
     * Charge l'image du dos d'une carte (grande)
     */
    public static ImageIcon chargerImageDosGrande() {
        return chargerEtRedimensionnerPersonnalisee(CHEMIN_BASE + "dos.png", LARGEUR_GRANDE_CARTE,
                HAUTEUR_GRANDE_CARTE);
    }

    /**
     * Détermine le chemin de l'image selon la carte
     */
    private static String obtenirCheminImage(Carte carte) {
        if (carte instanceof Joker) {
            return CHEMIN_BASE + "joker.png";
        }

        if (carte instanceof CarteCouleur) {
            CarteCouleur cc = (CarteCouleur) carte;
            String couleur = obtenirNomCouleur(cc.getCouleur());
            String valeur = obtenirNomValeur(cc.getValeur());
            return CHEMIN_BASE + couleur + "/" + valeur + ".png";
        }

        return null;
    }

    /**
     * Convertit la couleur enum en nom de dossier
     */
    private static String obtenirNomCouleur(Couleur couleur) {
        switch (couleur) {
            case PIQUE:
                return "pique";
            case COEUR:
                return "coeur";
            case CARREAU:
                return "carreau";
            case TREFLE:
                return "trefle";
            case ETOILE:
                return "etoile";
            case TRIANGLE:
                return "triangle";
            case SOLEIL:
                return "soleil";
            default:
                return "pique";
        }
    }

    /**
     * Convertit la valeur enum en nom de fichier
     */
    private static String obtenirNomValeur(Valeur valeur) {
        switch (valeur) {
            case AS:
                return "as";
            case DEUX:
                return "deux";
            case TROIS:
                return "trois";
            case QUATRE:
                return "quatre";
            default:
                return "as";
        }
    }

    /**
     * Charge et redimensionne une image
     */
    private static ImageIcon chargerEtRedimensionner(String chemin, boolean petite) {
        int largeur = petite ? 60 : LARGEUR_CARTE;
        int hauteur = petite ? 90 : HAUTEUR_CARTE;
        return chargerEtRedimensionnerPersonnalisee(chemin, largeur, hauteur);
    }

    /**
     * Charge et redimensionne une image avec dimensions personnalisées
     */
    private static ImageIcon chargerEtRedimensionnerPersonnalisee(String chemin, int largeur, int hauteur) {
        try {
            // Charger l'image depuis les resources
            URL url = GestionnaireImages.class.getResource(chemin);

            if (url == null) {
                System.err.println("⚠ Image non trouvée : " + chemin);
                return creerImageParDefaut(largeur, hauteur);
            }

            Image img = ImageIO.read(url);

            // Redimensionner
            Image imgRedimensionnee = img.getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
            return new ImageIcon(imgRedimensionnee);

        } catch (IOException e) {
            System.err.println("❌ Erreur chargement image : " + chemin);
            e.printStackTrace();
            return creerImageParDefaut(largeur, hauteur);
        }
    }

    /**
     * Crée une image par défaut si l'image n'est pas trouvée
     */
    private static ImageIcon creerImageParDefaut(boolean petite) {
        int largeur = petite ? 60 : LARGEUR_CARTE;
        int hauteur = petite ? 90 : HAUTEUR_CARTE;
        return creerImageParDefaut(largeur, hauteur);
    }

    /**
     * Crée une image par défaut si l'image n'est pas trouvée (dimensions
     * personnalisées)
     */
    private static ImageIcon creerImageParDefaut(int largeur, int hauteur) {
        BufferedImage img = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();

        // Fond gris avec gradient
        GradientPaint gradient = new GradientPaint(0, 0, new Color(200, 200, 200),
                largeur, hauteur, new Color(150, 150, 150));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, largeur, hauteur);

        // Bordure
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, largeur - 1, hauteur - 1);

        // Texte "?"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, Math.max(20, largeur / 4)));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "?";
        int x = (largeur - fm.stringWidth(text)) / 2;
        int y = ((hauteur - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);

        g2d.dispose();
        return new ImageIcon(img);
    }
}