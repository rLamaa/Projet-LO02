package jest_package1;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principale de l'interface graphique
 * À ouvrir avec Window Builder dans Eclipse
 */
public class JeuGUI extends JFrame {
    private JPanel contentPane;
    private CardLayout cardLayout;

    // Référence aux différents panels
    private MenuPanel menuPanel;
    private ConfigPanel configPanel;
    private JeuPanel jeuPanel;
    private FinPanel finPanel;

    private Jeu jeu;

    /**
     * Launch the application - MODE GRAPHIQUE
     */
    public static void main(String[] args) {
        // Vérifier si on veut le mode console
        if (args.length > 0 && args[0].equals("--console")) {
            // Mode console
            Jeu.main(new String[0]);
        } else {
            // Mode graphique
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        JeuGUI frame = new JeuGUI();
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Create the frame.
     */
    public JeuGUI() {
        setTitle("Jeu de JEST");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 800);

        cardLayout = new CardLayout();
        contentPane = new JPanel();
        contentPane.setLayout(cardLayout);
        setContentPane(contentPane);

        // Initialiser les panels
        menuPanel = new MenuPanel(this);
        configPanel = new ConfigPanel(this);
        jeuPanel = new JeuPanel(this);
        finPanel = new FinPanel(this);

        // Ajouter les panels au CardLayout
        contentPane.add(menuPanel, "MENU");
        contentPane.add(configPanel, "CONFIG");
        contentPane.add(jeuPanel, "JEU");
        contentPane.add(finPanel, "FIN");

        // Afficher le menu principal
        showPanel("MENU");
    }

    public void showPanel(String panelName) {
        cardLayout.show(contentPane, panelName);
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public JeuPanel getJeuPanel() {
        return jeuPanel;
    }

    public FinPanel getFinPanel() {
        return finPanel;
    }

    /**
     * Utilitaire pour charger les images des cartes
     */
    public static ImageIcon getImageCarte(Carte carte) {
        String nomFichier = "";

        if (carte instanceof Joker) {
            nomFichier = "joker.png";
        } else if (carte instanceof CarteCouleur) {
            CarteCouleur cc = (CarteCouleur) carte;
            String valeur = "";
            String couleur = "";

            // Valeur
            switch (cc.getValeur()) {
                case AS:
                    valeur = "as";
                    break;
                case DEUX:
                    valeur = "deux";
                    break;
                case TROIS:
                    valeur = "trois";
                    break;
                case QUATRE:
                    valeur = "quatre";
                    break;
            }

            // Couleur
            switch (cc.getCouleur()) {
                case PIQUE:
                    couleur = "pique";
                    break;
                case TREFLE:
                    couleur = "trefle";
                    break;
                case CARREAU:
                    couleur = "carreau";
                    break;
                case COEUR:
                    couleur = "coeur";
                    break;
                default:
                    couleur = "pique"; // Par défaut
            }

            nomFichier = valeur + "_" + couleur + ".png";
        }

        try {
            // Chemin relatif depuis la racine du projet
            ImageIcon icon = new ImageIcon("images/" + nomFichier);

            // Redimensionner l'image
            Image img = icon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            // Si l'image n'est pas trouvée, retourner une image par défaut
            System.err.println("Image non trouvée: " + nomFichier);
            return createDefaultCardIcon(carte);
        }
    }

    /**
     * Crée une icône par défaut si l'image n'est pas trouvée
     */
    private static ImageIcon createDefaultCardIcon(Carte carte) {
        JLabel label = new JLabel(carte.toString());
        label.setPreferredSize(new Dimension(80, 120));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        // Créer une image à partir du label
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                80, 120, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        label.paint(g2d);
        g2d.dispose();

        return new ImageIcon(img);
    }
}