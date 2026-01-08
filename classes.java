package jest_package1;

import java.util.*;

/**
 * Implémentation du patron Visitor pour calculer les scores selon les règles
 * standards.
 */
public class CalculateurScoreStandard implements VisiteurScore {

	@Override
	public int calculerScore(Jest j) {
		int scoreTotal = 0;

		// Calculer le score de toutes les cartes = Jest final avec les trophées
		for (Carte c : j.getCartes()) {
			scoreTotal += c.accepter(this, j);
		}

		// Ajouter les bonus des paires noires
		scoreTotal += calculerBonusPairesNoires(j);

		return scoreTotal;
	}

	@Override
	public int visiterPique(CarteCouleur c, Jest j) {
		// Les Piques augmentent toujours le score
		return calculerValeurAs(c, j);
	}

	@Override
	public int visiterTrefle(CarteCouleur c, Jest j) {
		// Les Trèfles augmentent toujours le score
		return calculerValeurAs(c, j);
	}

	@Override
	public int visiterCarreau(CarteCouleur c, Jest j) {
		// Les Carreaux diminuent toujours le score
		return -calculerValeurAs(c, j);
	}

	public int visiterEtoile(CarteCouleur c, Jest j) {
		// Les Etoiles doublent toujours le score
		return 2 * (calculerValeurAs(c, j));
	}

	public int visiterSoleil(CarteCouleur c, Jest j) {
		// Les Soleils augmentent le score quand ils sont impairs sinon, diminuent
		int valeur = 0;
		if (calculerValeurAs(c, j) % 2 != 0) {
			valeur = calculerValeurAs(c, j);
		} else if (calculerValeurAs(c, j) % 2 == 0) {
			valeur = -calculerValeurAs(c, j);
		}
		return valeur;
	}

	@Override
	public int visiterCoeur(CarteCouleur c, Jest j) {
		boolean aJoker = false;
		int nbCoeurs = compterCoeurs(j);

		// Vérifier si le joueur a le Joker
		for (Carte carte : j.getToutesLesCartes()) {
			if (carte instanceof Joker) {
				aJoker = true;
				break;
			}
		}
		if (!aJoker) {
			// Sans Joker, les Cœurs valent 0
			return 0;
		} else if (nbCoeurs == 4) {
			// Avec Joker et 4 Cœurs, les Cœurs ajoutent leur valeur
			return calculerValeurAs(c, j);
		} else {
			// Avec Joker et 1-3 Cœurs, les Cœurs diminuent le score
			return -calculerValeurAs(c, j);
		}
	}

	public int visiterTriangle(CarteCouleur c, Jest j) {
		boolean aJoker = false;
		int nbTriangles = compterTriangles(j);

		// Vérifier si le joueur a le Joker
		for (Carte carte : j.getToutesLesCartes()) {
			if (carte instanceof Joker) {
				aJoker = true;
				break;
			}
		}
		if (!aJoker) {
			// Sans Joker, les Triangles valent 0
			return 0;
		} else if (nbTriangles == 4) {
			// Avec Joker et 4 Triangles, les Triangles perdent leur valeur
			return -calculerValeurAs(c, j);
		} else {
			// Avec Joker et 1-3 Triangles, les Triangles augmentent le score
			return calculerValeurAs(c, j);
		}
	}

	@Override
	public int visiterJoker(Joker c, Jest j) {
		int nbCoeurs = compterCoeurs(j);

		if (nbCoeurs == 0) {
			// Joker sans Cœur = +4 points
			return 4;
		} else {
			// Joker avec 1-4 Cœurs = 0 points
			return 0;
		}
	}

	/*@Override
	public int visiterExtension(CarteExtension c, Jest j) {
		// À implémenter selon les cartes d'extension
		return 0;
	}*/

	/**
	 * Calcule le bonus pour les paires noires (Pique + Trèfle de même valeur)
	 */
	private int calculerBonusPairesNoires(Jest j) {
		int bonus = 0;
		Map<Integer, Boolean> piques = new HashMap<>();
		Map<Integer, Boolean> trefles = new HashMap<>();

		for (Carte carte : j.getToutesLesCartes()) {
			if (carte instanceof CarteCouleur) {
				CarteCouleur cc = (CarteCouleur) carte;
				int valeur = cc.getValeurNumerique();

				if (cc.getCouleur() == Couleur.PIQUE) {
					piques.put(valeur, true);
				} else if (cc.getCouleur() == Couleur.TREFLE) {
					trefles.put(valeur, true);
				}
			}
		}

		// Vérifier les paires
		for (Integer valeur : piques.keySet()) {
			if (trefles.containsKey(valeur)) {
				bonus += 2; // +2 points par paire noire
			}
		}

		return bonus;
	}

	/**
	 * Calcule la valeur d'un As (5 si seul de sa couleur, sinon 1)
	 */
	private int calculerValeurAs(CarteCouleur c, Jest j) {
		if (c.getValeur() != Valeur.AS) {
			return c.getValeurNumerique();
		}

		// Compter les cartes de la même couleur
		int compteur = 0;
		for (Carte carte : j.getToutesLesCartes()) {
			if (carte instanceof CarteCouleur) {
				CarteCouleur cc = (CarteCouleur) carte;
				if (cc.getCouleur() == c.getCouleur()) {
					compteur++;
				}
			}
		}

		// Si l'As est seul de sa couleur, il vaut 5
		return (compteur == 1) ? 5 : 1;
	}

	/**
	 * Compte le nombre de Cœurs dans le Jest
	 */
	private int compterCoeurs(Jest j) {
		int compte = 0;
		for (Carte carte : j.getToutesLesCartes()) {
			if (carte instanceof CarteCouleur) {
				CarteCouleur cc = (CarteCouleur) carte;
				if (cc.getCouleur() == Couleur.COEUR) {
					compte++;
				}
			}
		}
		return compte;
	}

	private int compterTriangles(Jest j) {
		int compte = 0;
		for (Carte carte : j.getToutesLesCartes()) {
			if (carte instanceof CarteCouleur) {
				CarteCouleur cc = (CarteCouleur) carte;
				if (cc.getCouleur() == Couleur.TRIANGLE) {
					compte++;
				}
			}
		}
		return compte;
	}
}package jest_package1;

import java.io.Serializable;

public abstract class Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Couleur couleur;
    protected Valeur valeur;
    private boolean visible = false;
    
    public boolean estVisible() {
    	return visible;
    }

    public void setVisible(boolean visible) {
    	this.visible = visible;
    }
    
    public int accepter(VisiteurScore visiteur, Jest jest) {
        return 0;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Valeur getValeur() {
        return valeur;
    }

    public int getValeurNumerique() {
        return valeur.getValeur();
    }

    // C'est quoi l'utilité de cette methode ?
    /*
     * public boolean estAs() {
     * return this.valeur == 1;
     * }
     */

    public String toString() {
        return "";
    }
}
package jest_package1;

public class CarteCouleur extends Carte {
    private static final long serialVersionUID = 1L;

    public CarteCouleur(Couleur couleur, Valeur valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return valeur + couleur.getSymbole();
    }

    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        switch (couleur) {
            case PIQUE:
                return visiteur.visiterPique(this, jest);
            case TREFLE:
                return visiteur.visiterTrefle(this, jest);
            case CARREAU:
                return visiteur.visiterCarreau(this, jest);
            case COEUR:
                return visiteur.visiterCoeur(this, jest);
            case ETOILE:
                return visiteur.visiterEtoile(this, jest);
            case TRIANGLE:
                return visiteur.visiterTriangle(this, jest);
            case SOLEIL:
                return visiteur.visiterSoleil(this, jest);
        }
        return 0;
    }
}
package jest_package1;

import java.io.Serializable;

public class ChoixCarte implements Serializable {
	/**
	 * Identifiant de version pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;
	private Offre offreChoisie;
	private Carte carteChoisie;

	/**
	 * Constructeur de la classe ChoixCarte
	 * 
	 * @param offreChoisie
	 * @param carteChoisie
	 * 
	 */
	public ChoixCarte(Offre offreChoisie, Carte carteChoisie) {
		this.offreChoisie = offreChoisie;
		this.carteChoisie = carteChoisie;
	}

	/**
	 * Getter de l'offre choisie
	 * 
	 * @return
	 */
	public Offre getOffre() {
		return offreChoisie;
	}

	/**
	 * Getter de la carte choisie
	 * 
	 * @return
	 */
	public Carte getCarte() {
		return carteChoisie;
	}
}package jest_package1;

public enum Couleur {
    PIQUE, TREFLE, CARREAU, COEUR, ETOILE, TRIANGLE, SOLEIL;

    /**
     * Getter de la force de la couleur
     * 
     * @return
     */
    public int getForce() {
        switch (this) {
            case PIQUE:
                return 7;
            case TREFLE:
                return 6;
            case ETOILE:
            	return 5;
            case CARREAU:
                return 4;
            case SOLEIL:
            	return 3;
            case COEUR:
                return 2;
            case TRIANGLE:
            	return 1;
        }
        return 0;
    }

    /**
     * Getter du symbole de la couleur
     * 
     * @return
     */
    public String getSymbole() {
        switch (this) {
            case PIQUE:
                return "♠";
            case TREFLE:
                return "♣";
            case CARREAU:
                return "♦";
            case COEUR:
                return "♥";
            case ETOILE:
            	return "☆";
            case TRIANGLE:
            	return "▲";
            case SOLEIL:
            	return "☼";
        }
        return "";
    }
}
package jest_package1;

/**
 * Types d'effets possibles des extensions
 */
// à changer selon nos choix
public enum EffetExtension {
    DOUBLEMENT,
    INVERSION,
    MIROIR
}
package jest_package1;

/**
 * États possibles d'une partie de Jest
 */

public enum EtatPartie {
	CONFIGURATION, // etat de configuration du jeu, pour le choix des parametres, joueurs,
					// extensions...
	EN_COURS, // etat de marche du jeu
	TERMINEE, // etat de fin de jeu
	SUSPENDUE // etat de jen en suspens
}package jest_package1;

import javax.swing.*;
import java.awt.*;

class FinPanel extends JPanel {
    private JeuGUI parent;
    private JTextArea scoresArea;
    private JLabel lblGagnant;
    private JPanel cartesGagnantPanel;

    public FinPanel(JeuGUI parent) {
        this.parent = parent;
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
}package jest_package1;

import java.io.Serializable;
import java.util.*;

public class Jest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Carte> cartes;


    /**
     * Constructeur de la fonction
     */
    public Jest() {
        this.cartes = new ArrayList<>();
    }

    public void ajouterCarte(Carte carte) {
        cartes.add(carte); // permet d'ajouter une carte au jest
    }

    /**
     * Fonction permettant de vider la liste de cartes
     * NON UTILISE
     */
    public void vider() {
        cartes.clear();
    }

    public void enleverCarte(Carte carte) {
        cartes.remove(carte);// permet d'enlever une carte au jest
    }

  
    /**
     * Getter de la liste de cartes du jest
     * 
     * @return
     */
    public List<Carte> getCartes() {
        return cartes;
    }

    /**
     * Getter de la liste de trophées du jest
     * 
     * @return
     */
 
    
    // Permet de faire un seul jest final : en ajoutant les trophées au jestPerso pour pouvoir compter le score final
    public List<Carte> getToutesLesCartes() {
		List<Carte> toutes = new ArrayList<>();
		toutes.addAll(this.cartes);
		
		return toutes;
	}
}
package jest_package1;

import java.io.*;
import java.util.*;

public class Jeu implements Serializable {
	/**
	 * Identifiant de version pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;
	private List<Joueur> joueurs; // liste des joueurs de la partie
	private RegleJeu regleJeu; // regle du jeu de Jest
	private boolean avecExtension = false; // boolean qui permet de savoir si le jeu se joue avec ou sans extension
	private Partie partieCourante; // variable qui fait reference à l'unique instance partie (permet d'avoir une
									// seule partie par jeu)
	private EtatPartie etat; // etat de la partie, utile pour la sauvegarde/configuration pour éviter les
								// bugs
	public static Scanner scanner = new Scanner(System.in); // NE PAS TOUCHER, buffer commun pour lire les input de
															// l'utilisateur

	/**
	 * Constructeur de la classe Jeu
	 * 
	 * 
	 */
	public Jeu() {
		this.joueurs = new ArrayList<>();
		this.etat = EtatPartie.CONFIGURATION;
	}

	public RegleJeu getRegleJeu() {
		return regleJeu;
	}

	public void configurerJeu() {
		System.out.println("\n╔════════════════════════════════════╗");
		System.out.println("║   Configuration du jeu JEST        ║");
		System.out.println("╚════════════════════════════════════╝\n");

		// Configuration des joueurs
		configurerJoueurs();

		// Choix de la variante
		choisirVariante();

		// Choix de l'extension
		choisirExtension();

		System.out.println("\n✓ Configuration terminée!");
		afficherRecapitulatif();
	}

	private void configurerJoueurs() {
		int nbJoueurs = 0;
		while (nbJoueurs < 1 || nbJoueurs > 4) { // condition qui permet de ne pas avoir aucun joueur ou plus de
													// 4 joueurs
			System.out.print("Nombre de joueurs humains (1-4): ");
			// les try/catch sont indispensable pour verifier les inputs et permettre à
			// l'utilisateur de recommencer
			try {
				nbJoueurs = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("⚠ Veuillez entrer un nombre valide");
			}
		}

		// Joueurs humains
		for (int i = 1; i <= nbJoueurs; i++) {
			System.out.print("Nom du joueur " + i + ": ");
			String nom = scanner.nextLine().trim();
			if (nom.isEmpty()) // condition pour ne pas avoir des noms vide
				nom = "Joueur" + i;
			ajouterJoueur(new JoueurHumain(nom));
		}

		// Compléter avec des bots jusqu'à 3 joueurs minimum
		int nbBots = Math.max(0, 3 - nbJoueurs);
		String[] nomsBots = { "Alpha", "Beta", "Gamma", "Delta" }; // noms des bots qui sont appender
		/*
		 * String info;
		 * Random va = new Random();
		 * int nombre;
		 * nombre=va.nextInt(3);
		 * // System.out.println(nombre);
		 * switch(nombre) {
		 * case 0:
		 * info="SPORT";
		 * break;
		 * case 1:
		 * info="POLITQUE";
		 * break;
		 * case 2:
		 * info="ECONOMIE";
		 * break;
		 * default:
		 * info="SPORT"; //cas par défaut mit à sport pour eviter les erreurs
		 * }
		 */
		Strategie[] strategies = {
				new StrategieOffensive(), // 1
				new StrategieDefensive(), // 2
				new StrategieAleatoire() // 3
		};
		// la boucle attribue aux bots la strategie dans l'ordre, càd le bot 1 aura la
		// strat 1, le 2 la 2, le 3 la 3, (le 4 la 1)
		for (int i = 0; i < nbBots; i++) {
			String nomBot = "Bot_" + nomsBots[i];
			JoueurVirtuel bot = new JoueurVirtuel(nomBot);
			bot.setStrategie(strategies[i % strategies.length]);
			ajouterJoueur(bot);
			System.out.println("✓ " + nomBot + " ajouté (Stratégie: " +
					bot.getStrategie().getClass().getSimpleName() + ")");
		}
	}

	private void choisirVariante() {
		System.out.println("\n=== Choix de la variante ===");
		System.out.println("1. Règles Standards");
		System.out.println("2. Variante Rapide (3 manches max)");
		System.out.println("3. Variante Stratégique (offres visibles)");
		System.out.print("Votre choix (1-3): ");

		int choix = 1;
		try {
			choix = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			scanner.nextLine();
		}

		switch (choix) {
			case 2:
				this.regleJeu = new VarianteRapide();
				System.out.println("✓ Variante Rapide sélectionnée");
				break;
			case 3:
				this.regleJeu = new RegleStrategique();
				System.out.println("✓ Variante Stratégique sélectionnée");
				break;
			default:
				this.regleJeu = new RegleStandard();
				System.out.println("✓ Règles Standard sélectionnées");
		}
	}

	private void choisirExtension() {
		System.out.println("\n=== Extension ===");
		System.out.print("Activer l'extension 'Nouvelles Cartes' ? (o/n): ");
		String reponse = scanner.nextLine().trim().toLowerCase();

		if (reponse.equals("o") || reponse.equals("oui")) {
			// this.extension = Extension.creerExtensionStandard();
			avecExtension = true;
			System.out.println("✓ Extension activée!");
			System.out.println("  Cartes ajoutées: Etoiles, Triangles, Soleils");
		} else {
			avecExtension = false;
			System.out.println("✓ Pas d'extension");
		}
	}

	// utile pour que le joueur verifie qu'il a bien tout fait commeil voulait et
	// pour nous debug
	private void afficherRecapitulatif() {
		System.out.println("\n╔════════════════════════════════════╗");
		System.out.println("║        RÉCAPITULATIF               ║");
		System.out.println("╠════════════════════════════════════╣");
		System.out.println("  Joueurs:");
		for (Joueur j : joueurs) {
			String type = (j instanceof JoueurHumain) ? "Humain" : "Bot";
			System.out.println("    • " + j.getNom() + " (" + type + ")");
		}
		System.out.println("  Règles: " + regleJeu.getClass().getSimpleName());
		if (avecExtension == true) {
			System.out.println("  Extension: Oui");
		} else {
			System.out.println("  Extension: Non");
		}
		System.out.println("╚════════════════════════════════════╝\n");
	}

	/**
	 * Ajoute un joueur à la partie
	 * 
	 * @param joueur le joueur que l'on souhaite ajouter à la partie
	 */
	public void ajouterJoueur(Joueur joueur) {
		if (etat != EtatPartie.CONFIGURATION) { // verification si le jeu est en config
			System.out.println("Impossible d'ajouter des joueurs : jeu déjà démarré.");
			return;
		}
		this.joueurs.add(joueur);
	}

	/**
	 * Choisit la regle du jeu
	 * 
	 * @param regleJeu les règles du jeu choisit
	 */
	public void choisirRegle(RegleJeu regleJeu) {
		if (etat != EtatPartie.CONFIGURATION) {
			System.out.println("Impossible de changer les règles : jeu déjà démarré.");
			return;
		}
		this.regleJeu = regleJeu;
	}

	/**
	 * Demarre la partie, en créant l'instance de la classe et permet alors de
	 * configurer comme que le joueur souhaite
	 */
	public void demarrer() {

		if (partieCourante == null) {
			// NOUVELLE PARTIE UNIQUEMENT
			Partie.reinitialiser();
			partieCourante = Partie.getInstance();
			partieCourante.setJeuReference(this);
			partieCourante.initialiser(joueurs, regleJeu, avecExtension);
		} else {
			// PARTIE CHARGÉE
			partieCourante.setJeuReference(this);
		}
		this.etat = EtatPartie.EN_COURS;
		afficherTrophees();

		// Boucle principale du jeu
		while (!partieCourante.verifierFinJeu()) {
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║   MANCHE " + partieCourante.getNumeroManche() + "                          ║");
			System.out.println("╚════════════════════════════════════╝");

			partieCourante.jouerManche();

			if (etat == EtatPartie.SUSPENDUE) {
				return;
			}

			if (partieCourante.verifierFinJeu()) {
				break;
			}
		}

		// Fin de partie
		partieCourante.terminerPartie();
		this.etat = EtatPartie.TERMINEE;
	}

	/**
	 * Permet d'afficher les throphées de maniere clair
	 */
	/**
	 * Permet d'afficher les trophées de manière claire avec leurs conditions
	 */
	private void afficherTrophees() {
		System.out.println("\n╔════════════════════════════════════════╗");
		System.out.println("║  🏆 TROPHÉES DE LA PARTIE 🏆         ║");
		System.out.println("╚════════════════════════════════════════╝");

		List<Carte> trophees = partieCourante.getTrophees();

		for (int i = 0; i < trophees.size(); i++) {
			Carte c = trophees.get(i);
			String description = RegleStandard.getDescriptionTrophee(c);

			System.out.println("\n  Trophée " + (i + 1) + ": " + c);
			System.out.println("  ┗━━ " + description);
		}
		System.out.println("╚═══════════════════════════════════════╝\n");
		System.out.println("\n╔═══════════════════════════════════════════════════════╗");
		System.out.println("║  ℹ️  RAPPEL DES RÈGLES                                ║");
		System.out.println("╠═══════════════════════════════════════════════════════╣");
		System.out.println("║    Piques ♠ & Trèfles ♣ : +points                     ║");
		System.out.println("║    Carreaux ♦ : -points                               ║");
		System.out.println("║    Cœurs ♥ : 0 pts (sauf avec Joker)                  ║");
		System.out.println("║    Joker seul : +4 pts                                ║");
		System.out.println("║    Joker + 4 Cœurs ♥ : Cœurs positifs!                ║");
		System.out.println("║    Joker + 1 à 3 Cœurs ♥ : Cœurs négatifs...          ║");
		System.out.println("║    Paire noire (♠ + ♣ même valeur): +2                ║");
		System.out.println("║    As seul de sa couleur : vaut 5                     ║");
		if (avecExtension == true) {
			System.out.println("║    Etoiles ☆ : +2*points                              ║");
			System.out.println("║    Triangles ▲ : 0 pts (sauf avec Joker)              ║");
			System.out.println("║    Joker + 1 à 3 Triangles ▲ : Triangles positifs!    ║");
			System.out.println("║    Joker + 4 Triangles ▲ : Triangles négatifs...      ║");
			System.out.println("║    Soleils ☼ chiffre impair : +points                 ║");
			System.out.println("║    Soleils ☼ chiffre pair :  -points                  ║");
		}
		System.out.println("╚═══════════════════════════════════════════════════════╝\n");
	}

	/**
	 * La fonction demande à l'utilisateur si il souahite sauvegarder puis quitter
	 * 
	 * @return
	 */
	public boolean proposerSauvegardeOuQuitter() {
		System.out.print("\n💾 Sauvegarder la partie ? (o/n): ");
		String rep = scanner.nextLine().trim().toLowerCase();

		if (rep.equals("o") || rep.equals("oui")) {
			this.etat = EtatPartie.SUSPENDUE;

			sauvegarder();

			System.out.print("Quitter la partie ? (o/n): ");
			String quitter = scanner.nextLine().trim().toLowerCase();

			if (quitter.equals("o") || quitter.equals("oui")) {
				this.etat = EtatPartie.SUSPENDUE;
				// System.out.println(this.etat);
				System.out.println("✓ Partie sauvegardée et arrêtée");
				return true;
			} else {
				this.etat = EtatPartie.EN_COURS;
				// System.out.println(this.etat);
			}
		}

		return false;
	}

	/**
	 * Sauvegarde la partie en cours
	 */
	public void sauvegarder() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream("sauvegarde_jeu.dat"))) {
			oos.writeObject(this);
			System.out.println("✓ Partie sauvegardée dans 'sauvegarde_jeu.dat'");
		} catch (IOException e) {
			System.err.println("❌ Erreur lors de la sauvegarde");
			e.printStackTrace();
		}
	}

	/**
	 * Charge une partie depuis
	 * 
	 * @param fichier correspond au fichier de sauvegarde du jeu, pour l'instant on
	 *                ne donne pas le choix à l'utilisateur de choisir du nom
	 * @return le jeu chargé à travers le fichier
	 */
	public static Jeu charger(String fichier) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
			// fait tout en fait pas besoin de partie.init....
			Jeu jeu = (Jeu) ois.readObject();

			jeu.partieCourante.setJeuReference(jeu);

			System.out.println("✓ Partie chargée depuis '" + fichier + "'");
			return jeu;

		} catch (IOException | ClassNotFoundException e) {
			System.err.println("❌ Erreur lors du chargement");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Fonction principale du jeu, point d'accés
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("\n╔════════════════════════════════════╗");
		System.out.println("║          JEU DE JEST               ║");
		System.out.println("╚════════════════════════════════════╝\n");

		System.out.println("1. Nouvelle partie");
		System.out.println("2. Charger une partie");
		System.out.print("Votre choix: ");

		int choix = 1;
		try {
			choix = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			scanner.nextLine();
		}

		Jeu jeu;
		if (choix == 2) {
			jeu = Jeu.charger("sauvegarde_jeu.dat");
			if (jeu == null) {
				System.out.println("Création d'une nouvelle partie...");
				jeu = new Jeu();
				jeu.configurerJeu();
			}
		} else {
			jeu = new Jeu();
			jeu.configurerJeu();
		}

		jeu.demarrer();
	}

	public List<Joueur> getJoueurs() {
		return joueurs;
	}
}package jest_package1;

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
}package jest_package1;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class JeuPanel extends JPanel {
    private JeuGUI parent;
    private Partie partie;
    private JLabel lblManche;
    private JLabel lblMessagePhase;
    private JPanel tropheesPanel;
    private JPanel cartesPanel;
    private JPanel offresPanel;
    private JTextArea logArea;
    private JButton btnDistribuer;
    private JButton btnSauvegarder;
    private boolean mancheEnCours = false;

    public JeuPanel(JeuGUI parent) {
        this.parent = parent;
        setBackground(new Color(0, 100, 0));
        setLayout(new BorderLayout());

        // Panel trophées en haut
        tropheesPanel = new JPanel();
        tropheesPanel.setBackground(new Color(0, 100, 0));
        add(tropheesPanel, BorderLayout.NORTH);

        // Panel central avec split
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Panel des offres (haut)
        offresPanel = new JPanel();
        offresPanel.setBackground(new Color(0, 120, 0));
        offresPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2),
                "Offres", 0, 0, new Font("Arial", Font.BOLD, 16), Color.YELLOW));
        JScrollPane scrollOffres = new JScrollPane(offresPanel);
        splitPane.setTopComponent(scrollOffres);

        // Panel des cartes du joueur (bas)
        cartesPanel = new JPanel();
        cartesPanel.setBackground(new Color(0, 80, 0));
        cartesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2),
                "Vos cartes", 0, 0, new Font("Arial", Font.BOLD, 16), Color.YELLOW));
        JScrollPane scrollCartes = new JScrollPane(cartesPanel);
        splitPane.setBottomComponent(scrollCartes);

        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

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
        rightPanel.add(lblMessagePhase, BorderLayout.NORTH);

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(logArea);
        rightPanel.add(scrollLog, BorderLayout.CENTER);

        // Boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 100, 0));

        btnDistribuer = new JButton("Distribuer les cartes");
        btnDistribuer.setFont(new Font("Arial", Font.BOLD, 14));
        btnDistribuer.addActionListener(e -> distribuerEtJouer());
        buttonPanel.add(btnDistribuer);

        btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.addActionListener(e -> {
            parent.getJeu().sauvegarder();
            JOptionPane.showMessageDialog(this, "Partie sauvegardée !");
        });
        buttonPanel.add(btnSauvegarder);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    public void demarrerPartie(Partie partie) {
        this.partie = partie;
        mancheEnCours = false;
        afficherTrophees();
        lblManche.setText("Manche " + partie.getNumeroManche());
        lblMessagePhase.setText("Prêt à commencer ?");
        logArea.setText("=== Nouvelle Partie ===\nCliquez sur 'Distribuer les cartes' pour débuter.\n");
        cartesPanel.removeAll();
        offresPanel.removeAll();
        cartesPanel.revalidate();
        offresPanel.revalidate();
        btnDistribuer.setEnabled(true);
        btnDistribuer.setText("Distribuer les cartes");
    }

    public void chargerPartie() {
        this.partie = Partie.getInstance();
        mancheEnCours = false;
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

    private void distribuerEtJouer() {
        if (!mancheEnCours) {
            // Première étape: distribuer les cartes
            logArea.append("\n>>> Distribution des cartes...\n");
            partie.distribuerCartes();

            for (Joueur j : partie.getJoueurs()) {
                logArea.append(j.getNom() + ": " + j.getJest().getCartes().size() + " cartes\n");
            }

            btnDistribuer.setEnabled(false);
            mancheEnCours = true;
            lblMessagePhase.setText("Cartes distribuées. Création des offres en cours...");

            // Attendre un peu avant de créer les offres
            Timer timer = new Timer(1000, e -> creerOffresAutomatiquement());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void creerOffresAutomatiquement() {
        logArea.append("\n>>> Création des offres...\n");
        partie.creerOffres();
        afficherOffresTousLesJoueurs();
        lblMessagePhase.setText("Offres créées !");
        logArea.append("Offres créées. Jeu en cours...\n");

        // Attendre avant de commencer les tours
        Timer timer = new Timer(2000, e -> jouerManches());
        timer.setRepeats(false);
        timer.start();
    }

    private void afficherOffresTousLesJoueurs() {
        offresPanel.removeAll();
        offresPanel.setLayout(new FlowLayout());

        List<Joueur> joueurs = partie.getJoueurs();
        for (Joueur j : joueurs) {
            Offre offre = j.getOffreCourante();

            JPanel offrePanel = new JPanel();
            offrePanel.setLayout(new BoxLayout(offrePanel, BoxLayout.Y_AXIS));
            offrePanel.setBackground(Color.WHITE);
            offrePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 2), j.getNom()));

            JLabel lblVisible = new JLabel(JeuGUI.getImageCarte(offre.getCarteVisible()));
            lblVisible.setAlignmentX(Component.CENTER_ALIGNMENT);
            offrePanel.add(new JLabel("Visible:"));
            offrePanel.add(lblVisible);

            if (offre.getCarteCachee().estVisible()) {
                JLabel lblCachee = new JLabel(JeuGUI.getImageCarte(offre.getCarteCachee()));
                lblCachee.setAlignmentX(Component.CENTER_ALIGNMENT);
                offrePanel.add(new JLabel("Carte 2:"));
                offrePanel.add(lblCachee);
            } else {
                JLabel lblCachee = new JLabel("❓");
                lblCachee.setFont(new Font("Arial", Font.BOLD, 60));
                lblCachee.setAlignmentX(Component.CENTER_ALIGNMENT);
                offrePanel.add(new JLabel("Cachée:"));
                offrePanel.add(lblCachee);
            }

            offresPanel.add(offrePanel);
        }

        offresPanel.revalidate();
        offresPanel.repaint();
    }

    private void jouerManches() {
        // Jouer les tours jusqu'à fin de manche
        while (!partie.verifierFinManche()) {
            logArea.append("\n>>> Tour en cours...\n");
            partie.resoudreTour();
            afficherOffresTousLesJoueurs();

            // Petit délai pour voir les changements
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Fin de la manche
        logArea.append("\n=== Fin de la manche ===\n");
        afficherTrophees();

        if (partie.verifierFinJeu()) {
            logArea.append("=== FIN DU JEU ===\n");
            partie.terminerPartie();
            mancheEnCours = false;
            parent.getFinPanel().afficherResultats(partie);

            Timer timer = new Timer(2000, e -> parent.showPanel("FIN"));
            timer.setRepeats(false);
            timer.start();
        } else {
            lblManche.setText("Manche " + partie.getNumeroManche());
            lblMessagePhase.setText("Manche suivante - Cliquez sur 'Distribuer les cartes'");
            btnDistribuer.setEnabled(true);
            btnDistribuer.setText("Manche " + partie.getNumeroManche());
            mancheEnCours = false;
        }
    }
}package jest_package1;

public class Joker extends Carte {
	/**
	 * Identifiant de version pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur par défaut du Joker. Initialise la couleur à {@code null} car le
	 * Joker n’a pas de couleur spécifique.
	 */
	public Joker() {
		this.couleur = null; // Joker n’a pas de couleur
	}

	/**
	 * Retourne la représentation textuelle de la carte.
	 *
	 * @return la chaîne {@code "Joker"}
	 */
	@Override
	public String toString() {
		return "Joker";
	}

	/**
	 * Accepte un visiteur de score pour calculer la valeur du Joker dans une partie
	 * donnée de Jest.
	 *
	 * @param visiteur le visiteur de score qui calcule le score
	 * @param jest     la partie de Jest dans laquelle la carte est jouée
	 * @return le score calculé par le visiteur pour cette carte
	 */
	@Override
	public int accepter(VisiteurScore visiteur, Jest jest) {
		return visiteur.visiterJoker(this, jest);
	}
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public abstract class Joueur implements Serializable {

    /**
     * Identifiant de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nom du joueur.
     */
    protected String nom;

    /**
     * Jest courant du joueur (cartes remportées pendant la manche).
     */
    protected Jest jest;

    /**
     * Jest personnel du joueur (cartes conservées sur plusieurs manches).
     */
    protected Jest jestPerso;

    /**
     * Offre actuellement proposée par le joueur.
     */
    protected Offre offreCourante;

    /**
     * Constructeur d'un joueur.
     *
     * @param nom le nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
        this.jestPerso = new Jest();
    }

    /**
     * Demande au joueur de créer une offre à partir de sa main.
     *
     * @return l'offre créée par le joueur
     */
    public abstract Offre faireOffre(boolean offresVisibles);
    

    /**
     * Demande au joueur de choisir une carte parmi les offres disponibles.
     *
     * @param offres la liste des offres proposées par les joueurs
     * @return le choix de carte effectué par le joueur
     */
    public abstract ChoixCarte choisirCarte(List<Offre> offres);

    /**
     * Ajoute une carte au Jest courant du joueur.
     *
     * @param carte la carte à ajouter
     */
    public void ajouterCarteJest(Carte carte) {
        jest.ajouterCarte(carte);
    }

    /**
     * Ajoute une carte au Jest personnel du joueur.
     *
     * @param carte la carte à ajouter
     */
    public void ajouterCarteJestPerso(Carte carte) {
        jestPerso.ajouterCarte(carte);
    }

    /**
     * Retourne le Jest courant du joueur.
     *
     * @return le Jest courant
     */
    public Jest getJest() {
        return jest;
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne l'offre courante du joueur.
     *
     * @return l'offre courante
     */
    public Offre getOffreCourante() {
        return offreCourante;
    }

    /**
     * Retourne le Jest personnel du joueur.
     *
     * @return le Jest personnel
     */
    public Jest getJestPerso() {
        return jestPerso;
    }
}package jest_package1;

import java.util.*;

public class JoueurHumain extends Joueur {
	/**
	 * Identifiant de version pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;

	public JoueurHumain(String nom) {
		super(nom);
	}

	public ChoixCarte choisirCarte(List<Offre> offres) {
		// Filtrer les offres disponibles (complètes et pas du joueur lui-même)
		List<Offre> offresDisponibles = new ArrayList<>();
		for (Offre o : offres) {
			if (o.estComplete() && o.getProprietaire() != this) {
				offresDisponibles.add(o);
			}
		}

		// CAS SPÉCIAL : Si aucune offre disponible SAUF la sienne
		if (offresDisponibles.isEmpty()) {
			// Vérifier si sa propre offre est complète
			for (Offre o : offres) {
				if (o.getProprietaire() == this && o.estComplete()) {
					System.out.println("\n[" + this.nom + "] Vous êtes le dernier joueur.");
					System.out.println("Vous devez choisir dans votre propre offre.");
					boolean offresVisibles = false;
					if(o.getCarteCachee().estVisible()) {
						//Variante stratégique, toutes les cartes sont visibles
						offresVisibles = true;
					}

					if(offresVisibles) {
						System.out.println("\n[" + this.nom + "] Quelle carte voulez-vous ?");
						System.out.println("  1. Carte 1 : " + o.getCarteVisible());
						System.out.println("  2. Carte 2 : " + o.getCarteCachee());
					} else {
						System.out.println("\n[" + this.nom + "] Quelle carte voulez-vous ?");
						System.out.println("  1. Visible : " + o.getCarteVisible());
						System.out.println("  2. Cachée : [?] ");
					}
					System.out.print("[" + this.nom + "] Votre choix (1 ou 2): ");
					

					String choixCarte = "";
					try {
						choixCarte = Jeu.scanner.nextLine().trim();
					} catch (NoSuchElementException e) {
						choixCarte = "1";
					}

					Carte carteChoisie;
					if (choixCarte.equals("2")) {
						carteChoisie = o.getCarteCachee();
					} else {
						carteChoisie = o.getCarteVisible();
					}

					return new ChoixCarte(o, carteChoisie);
				}
			}
			// Vraiment aucune offre disponible
			return null;
		}

		// Afficher les offres disponibles
		System.out.println("\n[" + this.nom + "] Offres disponibles:");
		boolean offresVisibles = false;
		
		for (int i = 0; i < offresDisponibles.size(); i++) {
			Offre o = offresDisponibles.get(i);
			// Afficher la carte cachée si elle est visible (variante stratégique)
			if(o.getCarteCachee().estVisible()) {
				//Variante stratégique, toutes les cartes sont visibles
				offresVisibles = true;
				System.out.println("  " + (i + 1) + ". [" + o.getProprietaire().getNom() + "] Carte 1 : " + o.getCarteVisible() + " | Carte 2 : " + o.getCarteCachee());
			} else {
				//Jeu standard donc une carte cachée, une carte visible
				System.out.println("  " + (i + 1) + ". [" + o.getProprietaire().getNom() + "] Visible : " + o.getCarteVisible() + " | Cachée : [?]");

			}
		}

		// Demander le choix de l'offre
		int choixOffre = 0;
		System.out.print("\n[" + this.nom + "] Choisissez l'offre (1-" + offresDisponibles.size() + "): ");
		try {
			String input = Jeu.scanner.nextLine().trim();
			if (!input.isEmpty()) {
				choixOffre = Integer.parseInt(input);
			} else {
				choixOffre = 1;
			}
		} catch (NumberFormatException e) {
			choixOffre = 1;
		}

		// Valider le choix
		if (choixOffre < 1 || choixOffre > offresDisponibles.size()) {
			choixOffre = 1;
		}

		Offre offreChoisie = offresDisponibles.get(choixOffre - 1);

		// Demander le choix de la carte (visible ou cachée (standard) ou 1 ou 2 (variante stratégique))
		if(offresVisibles) {
			System.out.println("\n[" + this.nom + "] Quelle carte voulez-vous ?");
			System.out.println("  1. Carte 1 : " + offreChoisie.getCarteVisible());
			System.out.println("  2. Carte 2 : " + offreChoisie.getCarteCachee());
		} else {
			System.out.println("\n[" + this.nom + "] Quelle carte voulez-vous ?");
			System.out.println("  1. Visible : " + offreChoisie.getCarteVisible());
			System.out.println("  2. Cachée : [?] ");
		}
		System.out.print("[" + this.nom + "] Votre choix (1 ou 2): ");

		String choixCarte = "";
		try {
			choixCarte = Jeu.scanner.nextLine().trim();
		} catch (NoSuchElementException e) {
			choixCarte = "1";
		}

		Carte carteChoisie;
		if (choixCarte.equals("2")) {
			carteChoisie = offreChoisie.getCarteCachee();
		} else {
			carteChoisie = offreChoisie.getCarteVisible();
		}

		return new ChoixCarte(offreChoisie, carteChoisie);
	}

	public Offre faireOffre(boolean offresVisibles) {
		List<Carte> cartes = this.jest.getCartes();
		Carte c1;
		Carte c2;
		c1 = cartes.get(0);
		c2 = cartes.get(1);
		
		// On enlève les cartes du Jest TEMPORAIRE
		this.jest.enleverCarte(c1);
		this.jest.enleverCarte(c2);
					
		if (offresVisibles) {
			// Variante stratégique, pas besoin de choisir quelle carte est cachée et l'autre visible : les deux visibles
			System.out.println("\\n[" + this.nom + "] Mode Stratégique : Les cartes sont toutes visibles donc vous n'avez pas à créer d'offre");
			System.out.println("  Carte 1 : " + c1);
			System.out.println("  Carte 2 : " + c2);
			
			this.offreCourante = new Offre(c1, c2, this);
			return this.offreCourante;
		} else {
			// Jeu standard donc il faut choisir quelle carte est cachée
			String choix = "0";
			System.out.println("\n[" + this.nom + "] Quelle carte doit être cachée ?");
			if (!(c1 instanceof Joker)) {
				System.out.println("  Choix 1 : " + c1);
			} else {
				System.out.println("  Choix 1 : Joker");
			}
			if (!(c2 instanceof Joker)) {
				System.out.println("  Choix 2 : " + c2);
			} else {
				System.out.println("  Choix 2 : Joker");
			}
			
			System.out.print("[" + this.nom + "] La 1 ou la 2? ");
			choix = Jeu.scanner.nextLine().trim();
			Carte carteCachee;
			Carte carteVisible;
			if (choix.equals("1")) {
				carteCachee = c1;
				carteVisible = c2;
			} else if (choix.equals("2")) {
				carteCachee = c2;
				carteVisible = c1;
			} else {
				System.out.println("Mauvais choix, première carte mise par défaut");
				carteCachee = c1;
				carteVisible = c2;
			}
			
			this.offreCourante = new Offre(carteCachee, carteVisible, this);
			return this.offreCourante;
		}
	}
}package jest_package1;

import java.util.List;

/**
 * Joueur virtuel utilisant une stratégie pour prendre ses décisions
 */
public class JoueurVirtuel extends Joueur {
    private static final long serialVersionUID = 1L;
    private Strategie strategie;

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
        
        if(offresVisibles) {
        	// Variante stratégique : pas de stratégie pour les bot car les deux cartes sont visibles
        	this.offreCourante = new Offre(c1, c2, this);
        	System.out.println("[" + nom + "] (Bot) Offre créée - Carte 1 : " + c1 + " | Carte 2 : " + c2);
        } else {
        	// Jeu standard : Utiliser la stratégie pour choisir quelle carte cacher
            Offre offre = strategie.choisirCartesOffre(c1, c2);
            this.offreCourante = new Offre(offre.getCarteCachee(), offre.getCarteVisible(), this);

            System.out.println("[" + nom + "] (Bot) Offre créée - Visible: " +
                    this.offreCourante.getCarteVisible());

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
            System.out.println("[" + nom + "] (Bot) choisit la carte " +
                    choix.getCarte() + " de l'offre de " +
                    choix.getOffre().getProprietaire().getNom());
        }

        return choix;
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
}package jest_package1;

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
}package jest_package1;

import java.io.Serializable;

/**
 * Représente une offre de deux cartes (une visible, une cachée)
 */
public class Offre implements Serializable {
	private static final long serialVersionUID = 1L;
	private Carte carteVisible;
	private Carte carteCachee;
	private Joueur proprietaire;

	public Offre(Carte carteCachee, Carte carteVisible, Joueur proprietaire) {
		this.carteVisible = carteVisible;
		this.carteCachee = carteCachee;
		this.proprietaire = proprietaire;
	}

	public Carte getCarteVisible() {
		return carteVisible;
	}

	public Carte getCarteCachee() {
		return carteCachee;
	}

	/**
	 * Retire une carte de l'offre
	 * 
	 * @param carte La carte à retirer
	 * @return La carte retirée
	 */
	public Carte retirerCarte(Carte carte) {
		if (carte == carteVisible) {
			Carte temp = carteVisible;
			carteVisible = null;
			return temp;
		} else if (carte == carteCachee) {
			Carte temp = carteCachee;
			carteCachee = null;
			return temp;
		}
		return null;
	}

	/**
	 * Retire une carte selon si elle est visible ou cachée
	 * 
	 * @param visible true pour retirer la carte visible, false pour la cachée
	 * @return La carte retirée
	 */
	public Carte retirerCarte(boolean visible) {
		if (visible) {
			Carte temp = carteVisible;
			carteVisible = null;
			return temp;
		} else {
			Carte temp = carteCachee;
			carteCachee = null;
			return temp;
		}
	}

	/**
	 * Vérifie si l'offre contient encore 2 cartes
	 * 
	 * @return
	 */
	public boolean estComplete() {
		return this.carteCachee != null && this.carteVisible != null;
	}

	/**
	 * Compte le nombre de cartes restantes dans l'offre
	 * 
	 * @return
	 */
	public int compterCartes() {
		int count = 0;
		if (carteVisible != null)
			count++;
		if (carteCachee != null)
			count++;
		return count;
	}

	/**
	 * Getter du propriètaire de l'offre
	 * 
	 * @return
	 */
	public Joueur getProprietaire() {
		return proprietaire;
	}
}package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Classe Partie implémentant le patron Singleton. Gère le déroulement d'une
 * partie de Jest.
 */
public class Partie implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Partie instance;

	private Pioche pioche = new Pioche();
	private List<Carte> trophees;
	private List<Joueur> joueurs;
	private RegleJeu regleJeu;
	private int numeroManche;
	private List<Offre> offresActuelles;
	private transient Jeu jeuReference;
	private boolean mancheEnCours;

	/**
	 * Constructeur privé pour le pattern Singleton
	 */
	private Partie() {
		this.numeroManche = 0;
	}

	/**
	 * Retourne l'instance unique de Partie
	 */
	public static Partie getInstance() {
		if (instance == null) {
			instance = new Partie();
		}
		return instance;
	}

	/**
	 * Réinitialise l'instance (utile pour démarrer une nouvelle partie)
	 */
	public static void reinitialiser() {
		instance = new Partie();
	}

	public void initialiser(List<Joueur> joueurs, RegleJeu regleJeu, boolean avecExtension) {
		List<Joueur> joueursInitialises = new ArrayList<>();

		for (Joueur p : joueurs) {
			Joueur joueur = null;
			if (p instanceof JoueurHumain) {
				joueur = new JoueurHumain(p.getNom());
			} else if (p instanceof JoueurVirtuel) {
				joueur = new JoueurVirtuel(p.getNom());
			}
			joueursInitialises.add(joueur);
		}

		// Initialisation de la pioche avec extension si présente
		pioche.initialiser(avecExtension);
		System.out.println("Pioche initialisée avec " + pioche.getTaille() + " cartes.");
		pioche.melanger();
		System.out.println("Pioche mélangée.");

		this.joueurs = joueursInitialises;
		this.regleJeu = regleJeu;
		this.numeroManche = 1;

		initialiserTrophees();

	}

	private void initialiserTrophees() {
		trophees = new ArrayList<>();
		int nbTrophees = (joueurs != null && joueurs.size() == 4) ? 1 : 2;
		for (int i = 0; i < nbTrophees; i++) {
			trophees.add(pioche.piocher());
		}
	}

	public List<Carte> getTrophees() {
		return trophees;
	}

	public void jouerManche() {
		if (!mancheEnCours) {
			distribuerCartes();
			mancheEnCours = true;
		}

		if (jeuReference != null && jeuReference.proposerSauvegardeOuQuitter()) {
			return;
		}

		creerOffres();

		while (!verifierFinManche()) {
			resoudreTour();
		}

		mancheEnCours = false;

		// Appliquer les règles spéciales (ex: incrémenter le compteur de manches pour
		// VarianteRapide)
		if (jeuReference != null) {
			regleJeu.appliquerReglesSpeciales(jeuReference);
		}

		numeroManche++;
	}

	/**
	 * Distribue 2 cartes à chaque joueur. Première manche: cartes piochées
	 * directement Autres manches: récupère les cartes non choisies + nouvelles
	 * cartes de la pioche
	 */
	public void distribuerCartes() {

		if (numeroManche == 1) {
			// Première manche
			for (Joueur j : joueurs) {
				List<Carte> cartesInitiales = pioche.piocher(2);
				for (Carte c : cartesInitiales) {
					j.ajouterCarteJest(c);
				}
			}
		} else {
			// Récupérer les cartes non choisies
			List<Carte> cartesRestantes = new ArrayList<>();
			for (Offre o : offresActuelles) {
				if (o.getCarteVisible() != null) {
					cartesRestantes.add(o.getCarteVisible());
				}
				if (o.getCarteCachee() != null) {
					cartesRestantes.add(o.getCarteCachee());
				}
			}

			int nbCartesAPiocher = joueurs.size();
			cartesRestantes.addAll(pioche.piocher(nbCartesAPiocher));

			Collections.shuffle(cartesRestantes);

			for (int i = 0; i < joueurs.size(); i++) {
				joueurs.get(i).ajouterCarteJest(cartesRestantes.get(i * 2));
				joueurs.get(i).ajouterCarteJest(cartesRestantes.get(i * 2 + 1));
			}
		}

		System.out.println("Les cartes sont distribuées");

		// DEBUG: Afficher clairement les deux Jest de chaque joueur
		System.out.println("\n=== DEBUG - Distribution des cartes ===");
		for (Joueur j : joueurs) {
			System.out.println("[DEBUG] " + j.getNom() + ":");
			System.out.println("  ├─ jest (temporaire)     : " + j.getJest().getCartes());
			System.out.println("  └─ jestPerso (définitif) : " + j.getJestPerso().getCartes());

		}
	}

	/**
	 * Crée les offres de chaque joueur (1 carte visible + 1 carte cachée) Chaque
	 * joueur choisit 2 cartes de sa main pour faire une offre
	 */
	public void creerOffres() {
		offresActuelles = new ArrayList<>();
		System.out.println("\n=== Création des offres ===");

		boolean offresVisibles = regleJeu.sontOffresVisibles();
		
		// Boucle: chaque joueur propose ses 2 cartes (1 visible, 1 cachée)
		for (Joueur j : joueurs) {
			Offre offre = j.faireOffre(offresVisibles);
			// La visibilité change selon la variante
			offre = regleJeu.creerOffre(j, offre.getCarteCachee(), offre.getCarteVisible());
			offresActuelles.add(offre);

			// Affichage différent suivant la variante
			if (offresVisibles) {
				System.out.println("[" + j.getNom() + "] Offre créée - Carte 1 : " + offre.getCarteVisible()
						+ " | Carte 2 : " + offre.getCarteCachee());
			} else {
				System.out.println(
						"[" + j.getNom() + "] Offre créée - Visible : " + offre.getCarteVisible() + " | Cachée : [?]");
			}

			// DEBUG: Afficher l'état des deux Jest après création offre
			System.out.println("[DEBUG] " + j.getNom() + " APRÈS offre:");
			System.out.println("  ├─ jest (temporaire)     : " + j.getJest().getCartes() + " (cartes pour l'offre)");
			System.out.println("  ├─ Carte visible : " + offre.getCarteVisible() + " (visible="
					+ offre.getCarteVisible().estVisible() + ")");
			System.out.println("  ├─ Carte cachée  : " + offre.getCarteCachee() + " (visible="
					+ offre.getCarteCachee().estVisible() + ")");
			System.out.println("  └─ jestPerso (définitif) : " + j.getJestPerso().getCartes());
		}
	}

	/**
	 * Gère un tour complet: chaque joueur choisit une carte jusqu'à fin de la
	 * manche L'ordre des joueurs dépend de la valeur de leurs cartes visibles
	 * NOUVELLE VERSION : Gère correctement l'ordre des joueurs selon les règles
	 */
	public void resoudreTour() {
		Set<Joueur> joueursAyantJoue = new HashSet<>();
		Joueur joueurActif = determinerPremierJoueur();

		// Boucle: tant que tous les joueurs n'ont pas joué et la manche n'est pas finie
		while (joueursAyantJoue.size() < joueurs.size() && !verifierFinManche()) {
			System.out.println("\n--- Tour de " + joueurActif.getNom() + " ---");

			// Le joueur actif choisit une carte parmi les offres disponibles
			ChoixCarte choix = joueurActif.choisirCarte(offresActuelles);

			if (choix != null) {
				Carte carteChoisie = choix.getCarte();
				Offre offreChoisie = choix.getOffre();

				joueurActif.ajouterCarteJestPerso(carteChoisie);
				offreChoisie.retirerCarte(carteChoisie);

				System.out.println("[" + joueurActif.getNom() + "] a pris: " + carteChoisie);
				// DEBUG: Afficher l'état des deux Jest après prise de carte
				System.out.println("[DEBUG] " + joueurActif.getNom() + " APRÈS avoir pris la carte:");
				System.out.println("  ├─ jest (temporaire)     : " + joueurActif.getJest().getCartes());
				System.out.println("  └─ jestPerso (définitif) : " + joueurActif.getJestPerso().getCartes());
				// Marquer ce joueur comme ayant joué
				joueursAyantJoue.add(joueurActif);

				// Déterminer le prochain joueur
				joueurActif = determinerProchainJoueur(offreChoisie.getProprietaire(), joueursAyantJoue);
			} else {
				// Cas spécial : dernier joueur, doit prendre de sa propre offre
				Offre offreJoueur = trouverOffreDeJoueur(joueurActif);
				if (offreJoueur != null && offreJoueur.estComplete()) {
					System.out.println(
							"[" + joueurActif.getNom() + "] est le dernier, doit choisir dans sa propre offre");

					// Demander quelle carte prendre de sa propre offre
					Carte carteChoisie;
					if (joueurActif instanceof JoueurHumain) {
						System.out.println("  1. Visible: " + offreJoueur.getCarteVisible());
						System.out.println("  2. Cachée: [?]");
						System.out.print("[" + joueurActif.getNom() + "] Votre choix (1 ou 2): ");
						String choixCarte = Jeu.scanner.nextLine().trim();
						carteChoisie = choixCarte.equals("2") ? offreJoueur.getCarteCachee()
								: offreJoueur.getCarteVisible();
					} else {
						// Bot prend la visible par défaut
						carteChoisie = offreJoueur.getCarteVisible();
					}

					joueurActif.ajouterCarteJestPerso(carteChoisie);
					offreJoueur.retirerCarte(carteChoisie);
					System.out.println("[" + joueurActif.getNom() + "] a pris: " + carteChoisie);
				}

				joueursAyantJoue.add(joueurActif);
				break; // Fin du tour
			}
		}
	}

	/**
	 * Détermine le premier joueur selon les règles : - Celui avec la carte visible
	 * de plus grande valeur - En cas d'égalité, celui avec la couleur la plus forte
	 * - Joker = valeur 0 Boucle: compare chaque joueur pour trouver celui avec la
	 * meilleure carte
	 */
	private Joueur determinerPremierJoueur() {
		Joueur premier = joueurs.get(0);
		int valeurMax = getValeurCarteVisible(premier);
		Couleur couleurMax = getCouleurCarteVisible(premier);

		// Boucle: parcourt tous les joueurs pour trouver celui avec la carte la plus
		// forte
		for (int i = 1; i < joueurs.size(); i++) {
			Joueur joueur = joueurs.get(i);
			int valeur = getValeurCarteVisible(joueur);
			Couleur couleur = getCouleurCarteVisible(joueur);

			// Condition: si la valeur est supérieure, ce joueur devient le premier
			if (valeur > valeurMax) {
				valeurMax = valeur;
				couleurMax = couleur;
				premier = joueur;
			} else if (valeur == valeurMax && couleur != null && couleurMax != null) {
				// En cas d'égalité, comparer les couleurs (Pique > Trèfle > Carreau > Coeur)
				if (couleur.getForce() > couleurMax.getForce()) {
					couleurMax = couleur;
					premier = joueur;
				}
			}
		}

		System.out.println("[DEBUG] Premier joueur: " + premier.getNom());
		return premier;
	}

	/**
	 * Détermine le prochain joueur selon les règles : - C'est le propriétaire de
	 * l'offre choisie (s'il n'a pas encore joué) - Sinon, c'est le joueur restant
	 * avec la plus grande carte visible Boucle: parcourt les joueurs restants pour
	 * trouver celui avec la meilleure carte
	 */
	private Joueur determinerProchainJoueur(Joueur proprietaireOffre, Set<Joueur> joueursAyantJoue) {
		// Si le propriétaire n'a pas encore joué, c'est à lui
		if (!joueursAyantJoue.contains(proprietaireOffre)) {
			return proprietaireOffre;
		}

		// Sinon, trouver parmi les joueurs restants celui avec la plus grande carte
		// visible
		Joueur prochain = null;
		int valeurMax = -1;
		Couleur couleurMax = null;

		// Boucle: parcourt tous les joueurs qui n'ont pas encore joué
		for (Joueur joueur : joueurs) {
			if (!joueursAyantJoue.contains(joueur)) {
				int valeur = getValeurCarteVisible(joueur);
				Couleur couleur = getCouleurCarteVisible(joueur);

				// Condition: si c'est le premier ou si la valeur est supérieure
				if (prochain == null || valeur > valeurMax) {
					valeurMax = valeur;
					couleurMax = couleur;
					prochain = joueur;
				} else if (valeur == valeurMax && couleur != null && couleurMax != null) {
					if (couleur.getForce() > couleurMax.getForce()) {
						couleurMax = couleur;
						prochain = joueur;
					}
				}
			}
		}

		return prochain;
	}

	/**
	 * Retourne la valeur de la carte visible d'un joueur (Joker = 0) Condition: si
	 * c'est un Joker, retourne 0; sinon retourne sa valeur numérique
	 */
	private int getValeurCarteVisible(Joueur joueur) {
		Offre offre = trouverOffreDeJoueur(joueur);
		if (offre == null || offre.getCarteVisible() == null) {
			return 0;
		}

		Carte carte = offre.getCarteVisible();
		if (carte instanceof Joker) {
			return 0;
		}
		return carte.getValeurNumerique();
	}

	/**
	 * Retourne la couleur de la carte visible d'un joueur (null si Joker)
	 * Condition: si c'est un Joker, retourne null; sinon retourne sa couleur
	 */
	private Couleur getCouleurCarteVisible(Joueur joueur) {
		Offre offre = trouverOffreDeJoueur(joueur);
		if (offre == null || offre.getCarteVisible() == null) {
			return null;
		}

		Carte carte = offre.getCarteVisible();
		if (carte instanceof Joker) {
			return null;
		}
		return carte.getCouleur();
	}

	/**
	 * Trouve l'offre d'un joueur donné Boucle: parcourt toutes les offres pour
	 * trouver celle appartenant au joueur
	 */
	private Offre trouverOffreDeJoueur(Joueur joueur) {
		// Boucle: parcourt les offres jusqu'à trouver celle du joueur
		for (Offre offre : offresActuelles) {
			if (offre.getProprietaire() == joueur) {
				return offre;
			}
		}
		return null;
	}

	public boolean verifierFinManche() {
		for (Offre o : offresActuelles) {
			if (o.estComplete()) {
				return false;
			}
		}
		return true;
	}

	public boolean verifierFinJeu() {
		// Vérifier d'abord si la règle de jeu spécifique dit que la partie est terminée
		if (regleJeu instanceof VarianteRapide) {
			VarianteRapide variante = (VarianteRapide) regleJeu;
			if (variante.partiTerminee()) {
				return true;
			}
		}
		// Sinon, vérifier si la pioche est vide (comportement standard)
		return pioche.estVide();
	}

	/**
	 * Termine la partie: récupère les dernières cartes, attribue trophées et
	 * calcule le gagnant
	 */
	public void terminerPartie() {
		System.out.println("\n=== FIN DE LA PARTIE ===");

		// Boucle: chaque joueur prend sa dernière carte restante
		for (int i = 0; i < joueurs.size(); i++) {
			Offre offre = offresActuelles.get(i);
			Carte derniereCarte = offre.getCarteVisible() != null ? offre.getCarteVisible() : offre.getCarteCachee();
			joueurs.get(i).ajouterCarteJestPerso(derniereCarte);
		}

		// DEBUG: Afficher le jest final de chaque joueur
		System.out.println("\n=== DEBUG - Jest final de chaque joueur ===");
		for (Joueur j : joueurs) {
			System.out.println("[DEBUG] " + j.getNom() + " - Jest: " + j.getJestPerso().getCartes());
		}

		attribuerTrophees();
		calculerGagnant();
	}

	/**
	 * Attribue les trophées aux joueurs selon les règles de jeu Boucle: pour chaque
	 * trophée, détermine le gagnant et l'ajoute à son jest
	 */
	/**
	 * Attribue les trophées aux joueurs selon les règles de jeu Boucle: pour chaque
	 * trophée, détermine le gagnant et l'ajoute à son jest
	 */
	public void attribuerTrophees() {
		System.out.println("\n╔════════════════════════════════════════╗");
		System.out.println("║  🏆 ATTRIBUTION DES TROPHÉES 🏆       ║");
		System.out.println("╚════════════════════════════════════════╝\n");

		// Boucle: parcourt chaque trophée et le donne au gagnant
		for (Carte trophee : trophees) {
			String description = RegleStandard.getDescriptionTrophee(trophee);

			System.out.println("┌────────────────────────────────────────");
			System.out.println("│ Trophée: " + trophee);
			System.out.println("│ Condition: " + description);

			Joueur gagnant = regleJeu.determinerGagnantTrophee(joueurs, trophee);

			if (gagnant != null) {
				gagnant.getJestPerso().ajouterCarte(trophee);
				System.out.println("│ ✅ Attribué à: " + gagnant.getNom());

				// Afficher pourquoi ce joueur a gagné (debug utile)
				afficherRaisonAttribution(gagnant, trophee);
			} else {
				System.out.println("│ ⚠️  Aucun gagnant trouvé");
			}
			System.out.println("└────────────────────────────────────────\n");
		}
	}

	/**
	 * Affiche la raison pour laquelle un joueur a gagné un trophée
	 */
	private void afficherRaisonAttribution(Joueur gagnant, Carte trophee) {
		Jest jest = gagnant.getJestPerso();

		if (trophee instanceof Joker) {
			CalculateurScoreStandard calc = new CalculateurScoreStandard();
			int score = calc.calculerScore(jest);
			System.out.println("│   → Score: " + score + " points");
		} else if (trophee instanceof CarteCouleur) {
			CarteCouleur ct = (CarteCouleur) trophee;
			Couleur couleur = ct.getCouleur();
			Valeur valeur = ct.getValeur();

			// Compter selon la condition
			if (couleur == Couleur.COEUR) {
				System.out.println("│   → Possède le Joker");
			} else if (couleur == Couleur.CARREAU && valeur == Valeur.QUATRE) {
				CalculateurScoreStandard calc = new CalculateurScoreStandard();
				int score = calc.calculerScore(jest);
				System.out.println("│   → Meilleur Jest sans Joker: " + score + " points");
			} else {
				// Afficher les cartes pertinentes
				int count = compterCartesRelevantes(jest, trophee);
				System.out.println("│   → Nombre trouvé: " + count);
			}
		}
	}

	/**
	 * Compte les cartes relevantes pour un trophée donné
	 */
	private int compterCartesRelevantes(Jest jest, Carte trophee) {
		if (!(trophee instanceof CarteCouleur))
			return 0;

		CarteCouleur ct = (CarteCouleur) trophee;
		Couleur couleur = ct.getCouleur();
		Valeur valeur = ct.getValeur();
		int count = 0;

		// Déterminer ce qu'on compte selon le trophée
		Couleur couleurAChercher = null;
		Valeur valeurAChercher = null;

		// CARREAUX
		if (couleur == Couleur.CARREAU) {
			if (valeur == Valeur.AS) {
				valeurAChercher = Valeur.QUATRE;
			} else if (valeur == Valeur.DEUX || valeur == Valeur.TROIS) {
				couleurAChercher = Couleur.CARREAU;
			}
		}
		// PIQUES
		else if (couleur == Couleur.PIQUE) {
			if (valeur == Valeur.TROIS) {
				valeurAChercher = Valeur.DEUX;
			} else if (valeur == Valeur.DEUX) {
				valeurAChercher = Valeur.TROIS;
			} else if (valeur == Valeur.QUATRE || valeur == Valeur.AS) {
				couleurAChercher = Couleur.TREFLE;
			}
		}
		// TRÈFLES
		else if (couleur == Couleur.TREFLE) {
			if (valeur == Valeur.QUATRE || valeur == Valeur.AS) {
				couleurAChercher = Couleur.PIQUE;
			} else if (valeur == Valeur.DEUX || valeur == Valeur.TROIS) {
				couleurAChercher = Couleur.COEUR;
			}
		}

		// Compter les cartes correspondantes
		for (Carte c : jest.getCartes()) {
			if (c instanceof CarteCouleur) {
				CarteCouleur cc = (CarteCouleur) c;
				if (couleurAChercher != null && cc.getCouleur() == couleurAChercher) {
					count++;
				} else if (valeurAChercher != null && cc.getValeur() == valeurAChercher) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Calcule le score final de chaque joueur et détermine le gagnant Boucle:
	 * parcourt chaque joueur et calcule son score, puis détermine le meilleur
	 */
	public Joueur calculerGagnant() {
		System.out.println("\n=== Calcul des scores ===");

		VisiteurScore calculateur = new CalculateurScoreStandard();
		int scoreMax = Integer.MIN_VALUE;
		Joueur gagnant = null;

		// Boucle: calcule le score de chaque joueur
		for (Joueur j : joueurs) {
			int score = calculateur.calculerScore(j.getJestPerso());
			System.out.println("[" + j.getNom() + "] Score: " + score);
			// DEBUG: Afficher le jest du joueur avant calcul final
			System.out.println("[DEBUG] Jest final de " + j.getNom() + ": " + j.getJestPerso().getCartes());

			// Condition: si le score est meilleur, ce joueur devient le gagnant
			if (score > scoreMax) {
				scoreMax = score;
				gagnant = j;
			}
		}

		System.out.println("\n🏆 GAGNANT: " + gagnant.getNom() + " avec " + scoreMax + " points!");
		return gagnant;
	}

	public int getNumeroManche() {
		return numeroManche;
	}

	public void setJeuReference(Jeu jeu) {
		this.jeuReference = jeu;
	}

	public List<Joueur> getJoueurs() {
		return joueurs;
	}
}package jest_package1;

import java.io.Serializable;
import java.util.*;

//finie sauf problèmes d'intégration
public class Pioche implements Serializable {
    private static final long serialVersionUID = 1L;
    // création de la pioche
    private Stack<Carte> pioche = new Stack<>();
    
        
    // ajouter les nouvelles cartes si on ajoute une extension
    public void initialiser(boolean avecExtension) {
    	pioche.clear();
    	//Cartes pour le jeu standard
        List<Couleur> couleurs = new ArrayList<>();
        couleurs.add(Couleur.PIQUE);
        couleurs.add(Couleur.TREFLE);
        couleurs.add(Couleur.CARREAU);
        couleurs.add(Couleur.COEUR);
        
        if(avecExtension) {
            couleurs.add(Couleur.ETOILE);
            couleurs.add(Couleur.TRIANGLE);
            couleurs.add(Couleur.SOLEIL);
        }
        
        for (Couleur c : couleurs) {
            for (Valeur v : Valeur.values()) {
                pioche.add(new CarteCouleur(c, v));
            }
        }
        pioche.add(new Joker());
        melanger();
    }

    public void afficherPioche() {
        for (Carte c : pioche) {
            System.out.println(c);
        }
    }

    // on mélange la pioche
    public void melanger() {
        Collections.shuffle(pioche);
    }

    // on pioche *une* carte
    public Carte piocher() {
        // retourne la carte du dessus de la pioche
        if (this.pioche.isEmpty()) { // verification si la pioche est vide
            return null;
        }
        return this.pioche.pop();
    }

    // on pioche *n* cartes, utile pour la distribution initiale
    public List<Carte> piocher(int n) {
        // initialisation de la liste des cartes piochées
        List<Carte> piochees = new ArrayList<>();
        // boucle pour piocher n cartes
        for (int i = 0; i < n && !this.pioche.isEmpty(); i++) {
            piochees.add(this.pioche.pop());
        }
        return piochees;
    }

    public boolean estVide() {
        // retourne vrai si la pioche est vide
        return this.pioche.isEmpty();
    }

    // utile pour les extensions
    public void ajouterCartes(List<Carte> nouvelleCartes) {
        // ajoute une liste de cartes à la pioche
        this.pioche.addAll(nouvelleCartes);
    }

    public int getTaille() {
        // retourne la taille de la pioche
        return this.pioche.size();
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

/**
 * 
 */
public interface RegleJeu extends Serializable {
	int calculerValeurJest(Jest jest);

	boolean verifierConditionTrophee(Jest jest, Carte carte);

	List<Joueur> determinerOrdreJeu(List<Offre> offres);

	Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee);

	void appliquerReglesSpeciales(Jeu jeu);
	
	Offre creerOffre(Joueur joueur, Carte carteCachee, Carte carteVisible);
	
	// va permettre de changer les règles du jeu si la variante stratégique est choisie
	boolean sontOffresVisibles();
}package jest_package1;

import java.util.*;

/**
 * Implémentation des règles standard du jeu Jest
 */
public class RegleStandard implements RegleJeu {
	private static final long serialVersionUID = 1L;
	
	

	@Override
	public boolean sontOffresVisibles() {
		// TODO Auto-generated method stub
		return false; // dans le jeu standard, une carte est cachée et l'autre visible
	}

	@Override
	public int calculerValeurJest(Jest jest) {
		CalculateurScoreStandard calculateur = new CalculateurScoreStandard();
		return calculateur.calculerScore(jest);
	}

	@Override
	public boolean verifierConditionTrophee(Jest jest, Carte trophee) {
		return true;
	}
	
	public Offre creerOffre(Joueur joueur, Carte carteCachee, Carte carteVisible) {
		carteVisible.setVisible(true);
		carteCachee.setVisible(false);
		return new Offre(carteCachee, carteVisible, joueur);
	}

	@Override
	public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
		List<Joueur> ordre = new ArrayList<>();
		List<Offre> offresTriees = new ArrayList<>(offres);

		offresTriees.sort((o1, o2) -> {
			int val1 = o1.getCarteVisible() instanceof Joker ? 0 : o1.getCarteVisible().getValeurNumerique();
			int val2 = o2.getCarteVisible() instanceof Joker ? 0 : o2.getCarteVisible().getValeurNumerique();

			if (val1 != val2) {
				return Integer.compare(val2, val1);
			}

			if (o1.getCarteVisible() instanceof Joker)
				return 1;
			if (o2.getCarteVisible() instanceof Joker)
				return -1;

			return Integer.compare(o2.getCarteVisible().getCouleur().getForce(),
					o1.getCarteVisible().getCouleur().getForce());
		});

		for (Offre o : offresTriees) {
			ordre.add(o.getProprietaire());
		}

		return ordre;
	}

	@Override
	public void appliquerReglesSpeciales(Jeu jeu) {
		// Pas de règles spéciales pour la version standard
	}

	/**
	 * Retourne la description de la condition pour gagner un trophée
	 */
	public static String getDescriptionTrophee(Carte trophee) {
		// JOKER
		if (trophee instanceof Joker) {
			return "⭐ Meilleur Jest (score le plus élevé)";
		}

		// Cas des cartes de couleur
		if (trophee instanceof CarteCouleur) {
			CarteCouleur ct = (CarteCouleur) trophee;
			Couleur couleur = ct.getCouleur();
			Valeur valeur = ct.getValeur();

			// CŒURS - Tous vont au joueur avec le Joker
			if (couleur == Couleur.COEUR) {
				return "🃏 Possède le Joker";
			}

			// CARREAUX
			if (couleur == Couleur.CARREAU) {
				if (valeur == Valeur.QUATRE) {
					return "⭐ Meilleur Jest SANS Joker";
				} else if (valeur == Valeur.AS) {
					return "📊 Le plus de cartes 4";
				} else if (valeur == Valeur.DEUX) {
					return "📊 Le plus de Carreaux ♦";
				} else if (valeur == Valeur.TROIS) {
					return "📊 Le MOINS de Carreaux ♦";
				}
					
			}

			// PIQUES
			if (couleur == Couleur.PIQUE) {
				if (valeur == Valeur.TROIS) {
					return "📊 Le plus de cartes 2";
				} else if (valeur == Valeur.DEUX) {
					return "📊 Le plus de cartes 3";
				} else if (valeur == Valeur.QUATRE) {
					return "📊 Le MOINS de Trèfles ♣";
				} else if (valeur == Valeur.AS) {
					return "📊 Le plus de Trèfles ♣";
				}
			}

			// TRÈFLES
			if (couleur == Couleur.TREFLE) {
				if (valeur == Valeur.QUATRE) {
					return "📊 Le MOINS de Piques ♠";
				} else if (valeur == Valeur.AS) {
					return "📊 Le plus de Piques ♠";
				} else if (valeur == Valeur.DEUX) {
					return "📊 Le MOINS de Cœurs ♥";
				} else if (valeur == Valeur.TROIS) {
					return "📊 Le plus de Cœurs ♥";
				} 
			}
			// ETOILES
			if (couleur == Couleur.ETOILE) {
				if (valeur == Valeur.QUATRE) {
					return "📊 Le plus de cartes 4";
				} else if (valeur == Valeur.AS) {
					return "📊 Le plus de Cœurs ♥";
				} else if (valeur == Valeur.DEUX) {
					return "📊 Le MOINS de Piques ♠";
				} else if (valeur == Valeur.TROIS) {
					return "📊 Le plus de Trèfles ♣";
				} 
			}
			// TRIANGLES
			if (couleur == Couleur.TRIANGLE) {
				if (valeur == Valeur.QUATRE) {
					return "📊 Le MOINS de Trèfles ♣";
				} else if (valeur == Valeur.AS) {
					return "⭐ Meilleur Jest SANS Joker";
				} else if (valeur == Valeur.DEUX) {
					return "📊 Le plus de cartes 2";
				} else if (valeur == Valeur.TROIS) {
					return "⭐ Meilleur Jest SANS Joker";
				} 
			}
			// SOLEILS
			if (couleur == Couleur.SOLEIL) {
				if (valeur == Valeur.QUATRE) {
					return "📊 Le plus de cartes As";
				} else if (valeur == Valeur.AS) {
					return "📊 Le MOINS de Cœurs ♥";
				} else if (valeur == Valeur.DEUX) {
					return "⭐ Meilleur Jest SANS Joker";
				} else if (valeur == Valeur.TROIS) {
					return "📊 Le plus de Piques ♠";
				} 
			}
		}

		return "❓ Condition inconnue";
	}

	/**
	 * Détermine le gagnant d'un trophée selon les règles spécifiques
	 */
	@Override
	public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
		// JOKER
		if (trophee instanceof Joker) {
			return determinerMeilleurJest(joueurs, false);
		}

		// Cas des cartes de couleur
		if (trophee instanceof CarteCouleur) {
			CarteCouleur ct = (CarteCouleur) trophee;
			Couleur couleur = ct.getCouleur();
			Valeur valeur = ct.getValeur();

			// CŒURS - Tous vont au joueur avec le Joker
			if (couleur == Couleur.COEUR) {
				return determinerJoueurAvecJoker(joueurs);
			}

			// CARREAUX
			if (couleur == Couleur.CARREAU) {
				if (valeur == Valeur.QUATRE) {
					// 4♦ → Meilleur Jest SANS Joker
					return determinerMeilleurJest(joueurs, true);
				} else if (valeur == Valeur.AS) {
					// A♦ → Le plus de 4
					return determinerMajoriteValeur(joueurs, Valeur.QUATRE);
				} else if (valeur == Valeur.DEUX) {
					// 2♦ → Le plus de Carreaux
					return determinerMajoriteCouleur(joueurs, Couleur.CARREAU);
				} else if (valeur == Valeur.TROIS) {
					// 3♦ → Le moins de Carreaux
					return determinerMinoriteCouleur(joueurs, Couleur.CARREAU);
				}
			}

			// PIQUES
			if (couleur == Couleur.PIQUE) {
				if (valeur == Valeur.TROIS) {
					// 3♠ → Le plus de 2
					return determinerMajoriteValeur(joueurs, Valeur.DEUX);
				} else if (valeur == Valeur.DEUX) {
					// 2♠ → Le plus de 3
					return determinerMajoriteValeur(joueurs, Valeur.TROIS);
				} else if (valeur == Valeur.QUATRE) {
					// 4♠ → Le MOINS de Trèfles
					return determinerMinoriteCouleur(joueurs, Couleur.TREFLE);
				} else if (valeur == Valeur.AS) {
					// A♠ → Le plus de Trèfles
					return determinerMajoriteCouleur(joueurs, Couleur.TREFLE);
				}
			}

			// TRÈFLES
			if (couleur == Couleur.TREFLE) {
				if (valeur == Valeur.QUATRE) {
					// 4♣ → Le moins de Piques
					return determinerMinoriteCouleur(joueurs, Couleur.PIQUE);
				} else if (valeur == Valeur.AS) {
					// A♣ → Le plus de Piques
					return determinerMajoriteCouleur(joueurs, Couleur.PIQUE);
				} else if (valeur == Valeur.DEUX) {
					// 2♣ → Le moins de Cœurs
					return determinerMinoriteCouleur(joueurs, Couleur.COEUR);
				} else if (valeur == Valeur.TROIS) {
					// 3♣ → Le plus de Cœurs
					return determinerMajoriteCouleur(joueurs, Couleur.COEUR);
				}
			}
			// ETOILES
			if (couleur == Couleur.ETOILE) {
				if (valeur == Valeur.QUATRE) {
					// 4☆ → Le plus de cartes 4
					return determinerMajoriteValeur(joueurs, Valeur.QUATRE);
				} else if (valeur == Valeur.AS) {
					// A☆ → Le plus de Cœurs ♥
					return determinerMajoriteCouleur(joueurs, Couleur.COEUR);
				} else if (valeur == Valeur.DEUX) {
					// 2☆ → Le MOINS de Piques ♠
					return determinerMinoriteCouleur(joueurs, Couleur.PIQUE);
				} else if (valeur == Valeur.TROIS) {
					// 3☆ → Le plus de Trèfles ♣
					return determinerMajoriteCouleur(joueurs, Couleur.TREFLE);
				}
			}
			// SOLEIL
			if (couleur == Couleur.SOLEIL) {
				if (valeur == Valeur.QUATRE) {
					// 4☼ → Le plus de cartes As
					return determinerMajoriteValeur(joueurs, Valeur.AS);
				} else if (valeur == Valeur.AS) {
					// A☼ → Le MOINS de Cœurs ♥
					return determinerMinoriteCouleur(joueurs, Couleur.COEUR);
				} else if (valeur == Valeur.DEUX) {
					// 2☼ → Meilleur Jest SANS Joker
					return determinerMeilleurJest(joueurs, true);
				} else if (valeur == Valeur.TROIS) {
					// 3☼ → Le plus de Piques ♠
					return determinerMajoriteCouleur(joueurs, Couleur.PIQUE);
				}
			}	
			// TRIANGLES
			if (couleur == Couleur.TRIANGLE) {
				if (valeur == Valeur.QUATRE) {
					// 4▲ → Le moins de Trèfles ♣
					return determinerMinoriteCouleur(joueurs, Couleur.TREFLE);
				} else if (valeur == Valeur.AS) {
					// A▲ → Meilleur Jest SANS Joker
					return determinerMeilleurJest(joueurs, true);
				} else if (valeur == Valeur.DEUX) {
					// 2▲ → Le plus de 2
					return determinerMajoriteValeur(joueurs, Valeur.DEUX);
				} else if (valeur == Valeur.TROIS) {
					// 3▲ → Meilleur Jest SANS Joker
					return determinerMeilleurJest(joueurs, true);
				}
			}			
		}

		return null;
	}

	// ==================== MÉTHODES UTILITAIRES ====================

	/**
	 * Trouve le joueur avec le meilleur Jest
	 * 
	 * @param sansJoker si true, ignore les joueurs ayant le Joker
	 */
	private Joueur determinerMeilleurJest(List<Joueur> joueurs, boolean sansJoker) {
		CalculateurScoreStandard calc = new CalculateurScoreStandard();
		int scoreMax = Integer.MIN_VALUE;
		Joueur gagnant = null;
		int valeurCarteMax = 0;
		Couleur couleurMax = null;

		for (Joueur j : joueurs) {
			// Si on veut sans Joker, vérifier que le joueur n'a pas de Joker
			if (sansJoker && aJoker(j.getJestPerso())) {
				continue;
			}

			int score = calc.calculerScore(j.getJestPerso());

			if (score > scoreMax) {
				scoreMax = score;
				gagnant = j;
				// Trouver la carte de plus haute valeur
				ResultatCarteForte resultat = trouverCarteLaPlusForte(j.getJestPerso());
				valeurCarteMax = resultat.valeur;
				couleurMax = resultat.couleur;
			} else if (score == scoreMax) {
				// Tie-breaker: carte de plus haute valeur
				ResultatCarteForte resultat = trouverCarteLaPlusForte(j.getJestPerso());
				if (resultat.valeur > valeurCarteMax
						|| (resultat.valeur == valeurCarteMax && resultat.couleur.getForce() > couleurMax.getForce())) {
					gagnant = j;
					valeurCarteMax = resultat.valeur;
					couleurMax = resultat.couleur;
				}
			}
		}

		return gagnant;
	}

	/**
	 * Trouve le joueur qui possède le Joker
	 */
	private Joueur determinerJoueurAvecJoker(List<Joueur> joueurs) {
		for (Joueur j : joueurs) {
			if (aJoker(j.getJestPerso())) {
				return j;
			}
		}
		return null;
	}

	/**
	 * Vérifie si un Jest contient le Joker
	 */
	private boolean aJoker(Jest jest) {
		for (Carte c : jest.getCartes()) {
			if (c instanceof Joker) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Trouve le joueur avec la majorité d'une couleur donnée
	 */
	private Joueur determinerMajoriteCouleur(List<Joueur> joueurs, Couleur couleur) {
		int maxCount = 0;
		Joueur gagnant = null;
		int valeurMax = 0;
		Couleur couleurCarteMax = null;

		for (Joueur j : joueurs) {
			int count = 0;
			int valeurDeCetteCouleur = 0;
			Couleur couleurDeCetteValeur = null;

			for (Carte c : j.getJestPerso().getCartes()) {
				if (c instanceof CarteCouleur) {
					CarteCouleur cc = (CarteCouleur) c;
					if (cc.getCouleur() == couleur) {
						count++;
						// Trouver la carte de cette couleur avec la plus haute valeur
						if (cc.getValeurNumerique() > valeurDeCetteCouleur) {
							valeurDeCetteCouleur = cc.getValeurNumerique();
							couleurDeCetteValeur = couleur;
						}
					}
				}
			}

			if (count > maxCount) {
				maxCount = count;
				gagnant = j;
				valeurMax = valeurDeCetteCouleur;
				couleurCarteMax = couleurDeCetteValeur;
			} else if (count == maxCount && count > 0) {
				// Tie-breaker: celui avec la carte de cette valeur dans la couleur la plus
				// forte
				if (valeurDeCetteCouleur > valeurMax
						|| (valeurDeCetteCouleur == valeurMax && couleur.getForce() > couleurCarteMax.getForce())) {
					gagnant = j;
					valeurMax = valeurDeCetteCouleur;
					couleurCarteMax = couleurDeCetteValeur;
				}
			}
		}

		return gagnant;
	}

	/**
	 * Trouve le joueur avec la minorité (le moins) d'une couleur donnée
	 */
	private Joueur determinerMinoriteCouleur(List<Joueur> joueurs, Couleur couleur) {
		int minCount = Integer.MAX_VALUE;
		Joueur gagnant = null;
		int valeurMax = 0;
		Couleur couleurCarteMax = null;

		for (Joueur j : joueurs) {
			int count = 0;
			int valeurDeCetteCouleur = 0;

			for (Carte c : j.getJestPerso().getCartes()) {
				if (c instanceof CarteCouleur) {
					CarteCouleur cc = (CarteCouleur) c;
					if (cc.getCouleur() == couleur) {
						count++;
						if (cc.getValeurNumerique() > valeurDeCetteCouleur) {
							valeurDeCetteCouleur = cc.getValeurNumerique();
						}
					}
				}
			}

			if (count < minCount) {
				minCount = count;
				gagnant = j;
				valeurMax = valeurDeCetteCouleur;
				couleurCarteMax = couleur;
			} else if (count == minCount) {
				// Tie-breaker similaire
				if (valeurDeCetteCouleur > valeurMax
						|| (valeurDeCetteCouleur == valeurMax && couleur.getForce() > couleurCarteMax.getForce())) {
					gagnant = j;
					valeurMax = valeurDeCetteCouleur;
					couleurCarteMax = couleur;
				}
			}
		}

		return gagnant;
	}

	/**
	 * Trouve le joueur avec la majorité d'une valeur donnée
	 */
	private Joueur determinerMajoriteValeur(List<Joueur> joueurs, Valeur valeur) {
		int maxCount = 0;
		Joueur gagnant = null;
		Couleur couleurMax = null;

		for (Joueur j : joueurs) {
			int count = 0;
			Couleur couleurDeCetteValeur = null;

			for (Carte c : j.getJestPerso().getCartes()) {
				if (c instanceof CarteCouleur) {
					CarteCouleur cc = (CarteCouleur) c;
					if (cc.getValeur() == valeur) {
						count++;
						// Garder la couleur la plus forte de cette valeur
						if (couleurDeCetteValeur == null
								|| cc.getCouleur().getForce() > couleurDeCetteValeur.getForce()) {
							couleurDeCetteValeur = cc.getCouleur();
						}
					}
				}
			}

			if (count > maxCount) {
				maxCount = count;
				gagnant = j;
				couleurMax = couleurDeCetteValeur;
			} else if (count == maxCount && count > 0) {
				// Tie-breaker: couleur la plus forte
				if (couleurDeCetteValeur != null
						&& (couleurMax == null || couleurDeCetteValeur.getForce() > couleurMax.getForce())) {
					gagnant = j;
					couleurMax = couleurDeCetteValeur;
				}
			}
		}

		return gagnant;
	}

	/**
	 * Trouve la carte de plus haute valeur dans un Jest
	 */
	private ResultatCarteForte trouverCarteLaPlusForte(Jest jest) {
		int valeurMax = 0;
		Couleur couleurMax = null;

		for (Carte c : jest.getCartes()) {
			if (c instanceof CarteCouleur) {
				CarteCouleur cc = (CarteCouleur) c;
				int valeur = cc.getValeurNumerique();

				if (valeur > valeurMax || (valeur == valeurMax && cc.getCouleur().getForce() > couleurMax.getForce())) {
					valeurMax = valeur;
					couleurMax = cc.getCouleur();
				}
			}
		}

		return new ResultatCarteForte(valeurMax, couleurMax);
	}

	/**
	 * Classe interne pour retourner valeur + couleur
	 */
	private static class ResultatCarteForte {
		int valeur;
		Couleur couleur;

		ResultatCarteForte(int valeur, Couleur couleur) {
			this.valeur = valeur;
			this.couleur = couleur;
		}
	}
}package jest_package1;

import java.util.List;

public class RegleStrategique implements RegleJeu {

    private static final long serialVersionUID = 1L;

    @Override
    public int calculerValeurJest(Jest jest) {
        return new RegleStandard().calculerValeurJest(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        return new RegleStandard().determinerOrdreJeu(offres);
    }

    @Override
    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        return new RegleStandard().determinerGagnantTrophee(joueurs, trophee);
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        // pas de règles spéciales, tout est dans la visibilité des cartes des offres
    }

    @Override
    public Offre creerOffre(Joueur joueur, Carte c1, Carte c2) {
        // Variante stratégique donc les deux cartes sont visibles
        c1.setVisible(true);
        c2.setVisible(true);
        return new Offre(c1, c2, joueur);
    }

    @Override
    public boolean sontOffresVisibles() {
        // TODO Auto-generated method stub
        return true; // variante où toutes les cartes des offres sont visibles
    }

}package jest_package1;

import java.io.Serializable;
import java.util.List;

public interface Strategie extends Serializable {
    ChoixCarte choisirCarte(List<Offre> offres, Jest jest);

    Offre choisirCartesOffre(Carte c1, Carte c2);

    int evaluerOffre(Offre offre, Jest jest);
}
package jest_package1;

import java.util.List;
import java.util.Random;

public class StrategieAleatoire implements Strategie {
    private static final long serialVersionUID = 1L;
    private Random random = new Random();

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        if (offres.isEmpty())
            return null;

        // Choisir une offre au hasard
        Offre offreChoisie = offres.get(random.nextInt(offres.size()));

        // Choisir visible ou cachée au hasard
        Carte carteChoisie = random.nextBoolean() ? offreChoisie.getCarteVisible() : offreChoisie.getCarteCachee();

        return new ChoixCarte(offreChoisie, carteChoisie);
    }

    @Override
    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        // Choisir aléatoirement quelle carte montrer
        if (random.nextBoolean()) {
            return new Offre(c2, c1, null); // Montrer c1
        } else {
            return new Offre(c1, c2, null); // Montrer c2
        }
    }

    @Override
    public int evaluerOffre(Offre offre, Jest jest) {
        return random.nextInt(10); // Évaluation aléatoire
    }
}package jest_package1;

import java.util.List;

public class StrategieDefensive implements Strategie {
    private static final long serialVersionUID = 1L;

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        if (offres.isEmpty())
            return null;

        // Choisir l'offre avec la carte visible la moins dangereuse
        Offre meilleureOffre = offres.get(0);
        int dangerMin = evaluerDanger(meilleureOffre.getCarteVisible());

        for (Offre o : offres) {
            int danger = evaluerDanger(o.getCarteVisible());
            if (danger < dangerMin) {
                dangerMin = danger;
                meilleureOffre = o;
            }
        }

        // Stratégie défensive : prendre la cachée (éviter les pièges)
        return new ChoixCarte(meilleureOffre, meilleureOffre.getCarteCachee());
    }

    @Override
    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        // Montrer la carte la moins dangereuse, cacher la plus dangereuse
        int danger1 = evaluerDanger(c1);
        int danger2 = evaluerDanger(c2);

        if (danger1 < danger2) {
            // c1 est moins dangereuse, on la montre
            return new Offre(c2, c1, null);
        } else {
            // c2 est moins dangereuse, on la montre
            return new Offre(c1, c2, null);
        }
    }

    @Override
    public int evaluerOffre(Offre offre, Jest jest) {
        return evaluerDanger(offre.getCarteVisible());
    }

    /**
     * Évalue le "danger" d'une carte (Carreau = danger, Pique/Trèfle = sûr)
     */
    private int evaluerDanger(Carte c) {
        if (c instanceof Joker) {
            return 2; // Danger moyen
        }
        if (c instanceof CarteCouleur) {
            CarteCouleur cc = (CarteCouleur) c;
            int valeur = cc.getValeurNumerique();

            switch (cc.getCouleur()) {
                case CARREAU:
                    return valeur * 2; // Très dangereux (points négatifs)
                case COEUR:
                case SOLEIL:
                    return valeur; // Danger moyen (dépend du Joker ou dépend de pair ou impair)
                case PIQUE:
                case TREFLE:
                case TRIANGLE:
                    return -valeur; // Pas dangereux (valeur négative = bon)
                case ETOILE:
                	return -valeur * 2; // Pas dangereux du tout
            }
        }
        return 0;
    }
}package jest_package1;

import java.util.List;

public class StrategieOffensive implements Strategie {
    private static final long serialVersionUID = 1L;

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        if (offres.isEmpty())
            return null;

        // Choisir l'offre avec la carte visible la plus forte
        Offre meilleureOffre = offres.get(0);
        int valeurMax = evaluerCarte(meilleureOffre.getCarteVisible());

        for (Offre o : offres) {
            int valeur = evaluerCarte(o.getCarteVisible());
            if (valeur > valeurMax) {
                valeurMax = valeur;
                meilleureOffre = o;
            }
        }

        // Prendre la carte visible (stratégie offensive = prendre ce qu'on voit de
        // bien)
        return new ChoixCarte(meilleureOffre, meilleureOffre.getCarteVisible());
    }

    @Override
    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        // Cacher la carte la plus faible, montrer la plus forte
        int val1 = evaluerCarte(c1);
        int val2 = evaluerCarte(c2);

        if (val1 > val2) {
            // c1 est meilleure, on la montre
            return new Offre(c2, c1, null);
        } else {
            // c2 est meilleure, on la montre
            return new Offre(c1, c2, null);
        }
    }

    @Override
    public int evaluerOffre(Offre offre, Jest jest) {
        return evaluerCarte(offre.getCarteVisible());
    }

    /**
     * Évalue la valeur d'une carte (positive pour Pique/Trèfle, négative pour
     * Carreau)
     */
    private int evaluerCarte(Carte c) {
        if (c instanceof Joker) {
            return 4; // Le Joker vaut 4 points sans cœur
        }
        if (c instanceof CarteCouleur) {
            CarteCouleur cc = (CarteCouleur) c;
            int valeur = cc.getValeurNumerique();

            switch (cc.getCouleur()) {
            	case ETOILE:
            		return valeur*2;
            	case PIQUE:
                case TREFLE:
                case TRIANGLE:
                    return valeur; // Positif
                case CARREAU:
                    return -valeur; // Négatif
                case COEUR:
                case SOLEIL:
                    return 0; // Neutre (dépend du Joker)
            }
        }
        return 0;
    }
}package jest_package1;

public class TestDesFonctions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Carte c1 = new CarteCouleur(Couleur.PIQUE, Valeur.TROIS);
		Carte c2 = new CarteCouleur(Couleur.COEUR, Valeur.AS);
		Carte j = new Joker();

		System.out.println(c1); // 3♠
		System.out.println(c2); // 1♥
		System.out.println(j); // Joker
		Pioche p = new Pioche();
		p.initialiser(false);
		// while (!p.estVide()) System.out.println(p.piocher());
		JoueurHumain j1 = new JoueurHumain("Léna");
		System.out.println(j1.getNom());
		System.out.println(j1.getJest().getCartes());
		j1.getJest().getCartes().add(c1);
		j1.getJest().getCartes().add(c2);

		System.out.println("");
		// j1.faireOffre();

		System.out.println("Cachée " + j1.getOffreCourante().getCarteCachee());
		System.out.println("Visible " + j1.getOffreCourante().getCarteVisible());

	}

}
package jest_package1;

public class Trophee {
	@SuppressWarnings("unused")
	private TypeCondition typeCondition;
	@SuppressWarnings("unused")
	private Couleur couleurCible;
	@SuppressWarnings("unused")
	private int valeurCible;
	private int modificateurScore;

	public Trophee(Valeur chiffre, Couleur c) {
		super();
	}

	public boolean verifierCondition(Jest jest) {
		return false;

	}

	public int appliquerEffet(Jest jest) {
		return modificateurScore;

	}

}
package jest_package1;

/**
 * 
 */
public enum TypeCondition {
	JOKER, // condition de qui a le joker
	MEILLEUR_JEST, // qui a le meilleur jest avant l'attribution des trophées
	MEILLEUR_JEST_SANS_JOKER, // meilleur jest sans joker avant l'attribution des trophées
	LE_MOINS_DE_TYPE, // le moins de carte d'un certain type
	LE_PLUS_DE_TYPE, // le plus de carte d'un certain type
	LE_PLUS_DE_NUMERO// le plus de carte d'un certain chiffre
}package jest_package1;

public enum Valeur {
    AS, DEUX, TROIS, QUATRE;

    public int getValeur() {
        switch (this) {
        case AS:
            return 1;
        case DEUX:
            return 2;
        case TROIS:
            return 3;
        case QUATRE:
            return 4;
        }
        return 0;
    }
}package jest_package1;

import java.util.List;

public class VarianteRapide implements RegleJeu {
    private static final long serialVersionUID = 1L;
    private int nombreManchesMax = 3;
    private int manchesJouees = 0;

    
    
    @Override
	public boolean sontOffresVisibles() {
		// TODO Auto-generated method stub
		return false; // dans la variante rapide, pareil que le jeu standard, une carte est cachée et l'autre visible
	}

	@Override
    public int calculerValeurJest(Jest jest) {
        // Utilise le même calcul que les règles standard
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        return calc.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerOrdreJeu(offres);
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        manchesJouees++;
        if (manchesJouees >= nombreManchesMax) {
            System.out.println("⏰ Limite de manches atteinte (" + nombreManchesMax + ")");
        }
    }

    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerGagnantTrophee(joueurs, trophee);
    }

    public boolean partiTerminee() {
        return manchesJouees >= nombreManchesMax;
    }

	@Override
	public Offre creerOffre(Joueur joueur, Carte c1, Carte c2) {
		// TODO Auto-generated method stub
		return null;
	}
}
/*package jest_package1;

import java.util.List;

// Variante Stratégique: Les offres sont visibles
public class VarianteStrategique implements RegleJeu {
    private static final long serialVersionUID = 1L;
    private boolean offresVisibles = true;

    @Override
    public int calculerValeurJest(Jest jest) {
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        return calc.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerOrdreJeu(offres);
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        if (offresVisibles) {
            System.out.println("📋 Mode OFFRES VISIBLES: Toutes les cartes sont révélées!");
        }
    }

    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerGagnantTrophee(joueurs, trophee);
    }

    public boolean sontOffresVisibles() {
        return offresVisibles;
    }

	@Override
	public Offre creerOffre(Joueur joueur, Carte c1, Carte c2) {
		// Variante Stratégique donc les deux cartes sont visibles
		c1.setVisible(true);
		c2.setVisible(true);
		return new Offre(c1, c2, joueur);
	}
	
	
}*/package jest_package1;

public interface VisiteurScore {
    int visiterPique(CarteCouleur carte, Jest jest);
    int visiterTrefle(CarteCouleur carte, Jest jest);
    int visiterCarreau(CarteCouleur carte, Jest jest);
    int visiterCoeur(CarteCouleur carte, Jest jest);
    int visiterEtoile(CarteCouleur carte, Jest jest);
    int visiterTriangle(CarteCouleur carte, Jest jest);
    int visiterSoleil(CarteCouleur carte, Jest jest);
    int visiterJoker(Joker carte, Jest jest);
    //int visiterExtension(CarteExtension carte, Jest jest);
    int calculerScore(Jest jest);
}
