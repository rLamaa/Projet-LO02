package jest_package1;

import javax.swing.*;
import java.awt.*;

class FinPanel extends JPanel {
    private JTextArea scoresArea;
    private JLabel lblGagnant;
    private JPanel cartesGagnantPanel;

    public FinPanel(JeuGUI parent) {
        setBackground(new Color(34, 139, 34));
        setLayout(null);

        JLabel lblTitre = new JLabel("🏆 FIN DE LA PARTIE 🏆");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitre.setForeground(Color.YELLOW);
        lblTitre.setBounds(300, 30, 600, 50);
        add(lblTitre);

        scoresArea = new JTextArea();
        scoresArea.setEditable(false);
        scoresArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(scoresArea);
        scrollPane.setBounds(50, 120, 400, 300);
        add(scrollPane);

        lblGagnant = new JLabel("");
        lblGagnant.setFont(new Font("Arial", Font.BOLD, 28));
        lblGagnant.setForeground(Color.YELLOW);
        lblGagnant.setBounds(50, 450, 600, 40);
        add(lblGagnant);

        cartesGagnantPanel = new JPanel();
        cartesGagnantPanel.setBackground(new Color(34, 139, 34));
        cartesGagnantPanel.setBounds(500, 120, 650, 350);
        cartesGagnantPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2),
                "Cartes du Gagnant", 0, 0, new Font("Arial", Font.BOLD, 14), Color.YELLOW));
        add(cartesGagnantPanel);

        JButton btnMenu = new JButton("Retour au Menu");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 16));
        btnMenu.setBounds(475, 650, 250, 50);
        btnMenu.addActionListener(e -> parent.showPanel("MENU"));
        add(btnMenu);
    }

    public void afficherResultats(Partie partie) {
        scoresArea.setText("");
        VisiteurScore calc = new CalculateurScoreStandard();

        Joueur gagnant = null;
        int scoreMax = Integer.MIN_VALUE;

        StringBuilder sb = new StringBuilder();
        sb.append("=== SCORES FINAUX ===\n\n");

        for (Joueur j : partie.getJoueurs()) {
            int score = calc.calculerScore(j.getJestPerso());
            sb.append(String.format("%-15s : %3d points\n", j.getNom(), score));

            if (score > scoreMax) {
                scoreMax = score;
                gagnant = j;
            }
        }

        scoresArea.setText(sb.toString());
        lblGagnant.setText("🏆 GAGNANT: " + gagnant.getNom() + " avec " + scoreMax + " points! 🏆");

        // Afficher les cartes du gagnant
        cartesGagnantPanel.removeAll();
        cartesGagnantPanel.setLayout(new FlowLayout());

        for (Carte c : gagnant.getJestPerso().getCartes()) {
            JLabel lblCarte = new JLabel(JeuGUI.getImageCarte(c));
            lblCarte.setToolTipText(c.toString());
            cartesGagnantPanel.add(lblCarte);
        }

        cartesGagnantPanel.revalidate();
        cartesGagnantPanel.repaint();
    }
}