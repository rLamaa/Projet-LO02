package jest_package1;

import java.io.*;

import java.util.*;

import javax.swing.*;

import Vue.InterfaceGraphiqueJest;
import Vue.VueConsoleJest;
import Controleur.ControleurJest;

/**
 * Classe principale du jeu de Jest.
 * 
 * Orchestrateur central du jeu qui gère l'ensemble du cycle de vie d'une
 * partie.
 * Responsable de:
 * - La configuration initiale du jeu (choix des joueurs, variantes, extensions)
 * - L'initialisation de l'interface utilisateur (console, GUI ou hybride)
 * - L'exécution de la boucle principale du jeu
 * - La gestion des sauvegardes et chargements de parties
 * 
 * Supporte trois modes de jeu:
 * - Mode Console: Interface textuelle simple
 * - Mode GUI: Interface graphique Swing complète
 * - Mode Hybride: Console et GUI simultanément
 * 
 * Implémente Serializable pour permettre la persistance des parties en cours.
 * 
 * @author David et Léna
 */
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

	// Attributs pour l'interface graphique (GUI)
	private transient InterfaceGraphiqueJest interfaceGraphique;
	private transient VueConsoleJest vueConsole;
	private transient ControleurJest controleurGUI;
	private boolean avecGUI = false;
	private boolean modeConsoleEtGUI = false;

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

	// utile pour que le joueur verifie qu'il a bien tout fait comme il voulait et
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
	 * Initialisation de l'interface graphique
	 */
	private void initialiserInterfaceGraphique() {
		// Trouver le joueur humain
		JoueurHumainGUI joueurHumain = null;
		for (Joueur j : joueurs) {
			if (j instanceof JoueurHumain) {
				// Remplacer le JoueurHumain par un JoueurHumainGUI
				int index = joueurs.indexOf(j);
				joueurHumain = new JoueurHumainGUI(j.getNom());
				joueurHumain.setUtiliseGUI(true);

				// Copier l'état du joueur
				for (Carte c : j.getJest().getCartes()) {
					joueurHumain.ajouterCarteJest(c);
				}
				for (Carte c : j.getJestPerso().getCartes()) {
					joueurHumain.ajouterCarteJestPerso(c);
				}

				joueurs.set(index, joueurHumain);
				break;
			}
		}

		if (joueurHumain == null) {
			System.out.println("⚠ Pas de joueur humain détecté, GUI désactivée.");
			avecGUI = false;
			return;
		}

		final JoueurHumainGUI joueurFinal = joueurHumain;

		// Réinitialiser la partie avec les nouveaux joueurs
		if (partieCourante != null) {
			partieCourante.setJeuReference(this);
			partieCourante.setModeGUI(!modeConsoleEtGUI);
		}

		// Créer l'interface dans le thread EDT
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						interfaceGraphique = new InterfaceGraphiqueJest(partieCourante, joueurFinal);
						controleurGUI = new ControleurJest(partieCourante, joueurFinal, interfaceGraphique);

						// Enregistrer l'interface comme observateur
						partieCourante.addObserver(interfaceGraphique);

						interfaceGraphique.afficher();
						interfaceGraphique.ajouterLog("=== Jeu de Jest ===");
						interfaceGraphique.ajouterLog("Interface graphique initialisée");

						System.out.println("✓ Interface graphique initialisée");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			System.err.println("Erreur lors de l'initialisation de la GUI");
			e.printStackTrace();
		}

		// Si mode mixte, créer aussi la vue console
		if (modeConsoleEtGUI) {
			vueConsole = new VueConsoleJest(partieCourante);
			System.out.println("✓ Vue console initialisée (mode mixte)");
		}
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

		if (avecGUI) {
			initialiserInterfaceGraphique();
		}

		this.etat = EtatPartie.EN_COURS;
		afficherTrophees();

		// Boucle principale du jeu
		while (!partieCourante.verifierFinJeu()) {
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║   MANCHE " + partieCourante.getNumeroManche() + "                         ║");
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

		// Message final dans la GUI
		if (avecGUI && interfaceGraphique != null) {
			interfaceGraphique.ajouterLog("\n=== PARTIE TERMINÉE ===");
			JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
					"Partie terminée ! Consultez les scores dans la console.",
					"Fin de partie", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Permet d'afficher les throphées de maniere clair
	 */
	/**
	 * Permet d'afficher les trophées de manière claire avec leurs conditions
	 */
	private void afficherTrophees() {
		System.out.println("\n╔════════════════════════════════════════╗");
		System.out.println("║  🏆 TROPHÉES DE LA PARTIE 🏆            ║");
		System.out.println("╚════════════════════════════════════════╝");

		List<Carte> trophees = partieCourante.getTrophees();

		for (int i = 0; i < trophees.size(); i++) {
			Carte c = trophees.get(i);
			String description = RegleStandard.getDescriptionTrophee(c);

			System.out.println("\n  Trophée " + (i + 1) + ": " + c);
			System.out.println("  ┗━━ " + description);
		}

		// On affiche les règles
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
		// Si il y a extension, il y a des règles en plus (concernant les cartes de
		// l'extension)
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

	// Sans Joker, les Triangles valent 0

	// Avec Joker et 4 Triangles, les Triangles perdent leur valeur
	// Avec Joker et 1-3 Triangles, les Triangles augmentent le score

	/**
	 * Affiche un dialogue sauvegarde/quitter qui fonctionne en console et GUI
	 * L'utilisateur peut répondre dans l'un ou l'autre mode
	 * 
	 * @return true si l'utilisateur a choisi de quitter
	 */
	public boolean proposerSauvegardeOuQuitter() {
		if (avecGUI && interfaceGraphique != null) {
			// Mode GUI + Console ou GUI seul
			return afficherDialogueSauvegardeGUI();
		} else {
			// Mode console uniquement
			return afficherDialogueSauvegardeConsole();
		}
	}

	/**
	 * Dialogue de sauvegarde en mode GUI
	 */
	private boolean afficherDialogueSauvegardeGUI() {
		int result = JOptionPane.showConfirmDialog(
				interfaceGraphique.getFrame(),
				"💾 Sauvegarder la partie avant de quitter ?",
				"Sauvegarde",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (result == JOptionPane.YES_OPTION) {
			sauvegarder();
			interfaceGraphique.ajouterLog("✓ Partie sauvegardée");

			int quitResult = JOptionPane.showConfirmDialog(
					interfaceGraphique.getFrame(),
					"Quitter la partie ?",
					"Quitter",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (quitResult == JOptionPane.YES_OPTION) {
				this.etat = EtatPartie.SUSPENDUE;
				interfaceGraphique.ajouterLog("✓ Partie arrêtée");
				return true;
			} else {
				this.etat = EtatPartie.EN_COURS;
				return false;
			}
		} else if (result == JOptionPane.NO_OPTION) {
			interfaceGraphique.ajouterLog("Partie non sauvegardée");

			int quitResult = JOptionPane.showConfirmDialog(
					interfaceGraphique.getFrame(),
					"Quitter sans sauvegarder ?",
					"Confirmation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);

			if (quitResult == JOptionPane.YES_OPTION) {
				this.etat = EtatPartie.SUSPENDUE;
				return true;
			} else {
				this.etat = EtatPartie.EN_COURS;
				return false;
			}
		}
		return false;
	}

	/**
	 * Dialogue de sauvegarde en mode console
	 */
	private boolean afficherDialogueSauvegardeConsole() {
		System.out.print("\n💾 Sauvegarder la partie ? (o/n): ");
		String rep = scanner.nextLine().trim().toLowerCase();

		if (rep.equals("o") || rep.equals("oui")) {
			this.etat = EtatPartie.SUSPENDUE;
			sauvegarder();

			System.out.print("Quitter la partie ? (o/n): ");
			String quitter = scanner.nextLine().trim().toLowerCase();

			if (quitter.equals("o") || quitter.equals("oui")) {
				this.etat = EtatPartie.SUSPENDUE;
				System.out.println("✓ Partie sauvegardée et arrêtée");
				return true;
			} else {
				this.etat = EtatPartie.EN_COURS;
			}
		}

		return false;
	}

	/**
	 * Crée une offre pour un joueur humain (fonctionne en console ou GUI)
	 * 
	 * @param joueur Le joueur qui crée l'offre
	 * @return L'offre créée ou null
	 */
	public Offre creerOffre(JoueurHumain joueur) {
		List<Carte> cartes = joueur.getJest().getCartes();

		if (cartes.size() < 2) {
			String message = "Vous n'avez pas assez de cartes pour faire une offre.";
			if (avecGUI && interfaceGraphique != null) {
				JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
						message, "Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				System.out.println("⚠ " + message);
			}
			return null;
		}

		// Déterminer si c'est la variante stratégique
		boolean offresVisibles = false;
		if (partieCourante != null && partieCourante.getRegleJeu() != null) {
			offresVisibles = partieCourante.getRegleJeu().sontOffresVisibles();
		}

		Carte carteCachee, carteVisible;

		if (offresVisibles) {
			// Mode stratégique : afficher les deux cartes
			if (avecGUI && interfaceGraphique != null) {
				JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
						"Mode Stratégique : Vos deux cartes sont visibles \n" +
								"Carte 1 : " + cartes.get(0) + "\nCarte 2 : " + cartes.get(1),
						"Offre Créée pour " + joueur.getNom(),
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				System.out.println("Mode Stratégique : Vos deux cartes sont visibles :");
				System.out.println("  1. " + cartes.get(0));
				System.out.println("  2. " + cartes.get(1));
			}
			carteCachee = cartes.get(0);
			carteVisible = cartes.get(1);
			// Les deux cartes sont visibles en mode stratégique
			carteCachee.setVisible(true);
			carteVisible.setVisible(true);
		} else {
			// Mode standard : choisir la carte à cacher
			int indexCachee = choisirIndiceCarte(cartes, "Quelle carte voulez-vous cacher " + joueur.getNom() + " ?");
			if (indexCachee < 0)
				return null;

			carteCachee = cartes.get(indexCachee);
			carteVisible = cartes.get(1 - indexCachee);
			// Définir la visibilité
			carteCachee.setVisible(false);
			carteVisible.setVisible(true);
		}

		// Retirer les cartes du jest
		joueur.getJest().enleverCarte(carteCachee);
		joueur.getJest().enleverCarte(carteVisible);

		// Créer et retourner l'offre
		Offre offre = new Offre(carteCachee, carteVisible, joueur);

		String message = "[" + joueur.getNom() + "] Offre créée - ";
		if (offresVisibles) {
			message += "Carte 1: " + carteVisible + " | Carte 2: " + carteCachee + " (toutes visibles)";
		} else {
			message += "Visible: " + carteVisible + " | Cachée: [?]";
		}

		if (avecGUI && interfaceGraphique != null) {
			interfaceGraphique.ajouterLog(message);
		} else {
			System.out.println("✓ " + message);
		}

		return offre;
	}

	/**
	 * Permet au joueur de choisir une offre et une carte (fonctionne en console ou
	 * GUI)
	 * 
	 * @param joueur Le joueur qui choisit
	 * @return Le choix (offre + carte) ou null
	 */
	public ChoixCarte choisirCarte(JoueurHumain joueur) {
		if (partieCourante == null) {
			String message = "Aucune partie en cours.";
			if (avecGUI && interfaceGraphique != null) {
				JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
						message, "Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				System.out.println("⚠ " + message);
			}
			return null;
		}

		List<Offre> offresActuelles = partieCourante.getOffresActuelles();
		if (offresActuelles == null || offresActuelles.isEmpty()) {
			String message = "Aucune offre disponible.";
			if (avecGUI && interfaceGraphique != null) {
				JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
						message, "Information", JOptionPane.INFORMATION_MESSAGE);
			} else {
				System.out.println("ℹ " + message);
			}
			return null;
		}

		// Filtrer les offres disponibles
		List<Offre> offresDisponibles = new ArrayList<>();
		Offre offreJoueur = null;

		for (Offre o : offresActuelles) {
			if (o.getProprietaire() == joueur) {
				offreJoueur = o;
			} else if (o.estComplete()) {
				offresDisponibles.add(o);
			}
		}

		// Cas où le joueur est dernier et doit prendre sa propre offre
		if (offresDisponibles.isEmpty() && offreJoueur != null && offreJoueur.estComplete()) {
			return choisirDansSaPropreOffre(offreJoueur, joueur);
		}

		if (offresDisponibles.isEmpty()) {
			String message = "Aucune offre disponible pour vous.";
			if (avecGUI && interfaceGraphique != null) {
				JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
						message, "Information", JOptionPane.INFORMATION_MESSAGE);
			} else {
				System.out.println("ℹ " + message);
			}
			return null;
		}

		// Déterminer si les offres sont visibles
		boolean offresVisibles = offresDisponibles.get(0).getCarteCachee().estVisible();

		// Choisir une offre
		Offre offreChoisie = choisirOffre(offresDisponibles, offresVisibles, joueur);
		if (offreChoisie == null)
			return null;

		// Choisir une carte dans l'offre
		Carte carteChoisie = choisirCarteFromOffre(offreChoisie, offresVisibles, joueur);
		if (carteChoisie == null)
			return null;

		// Retourner le choix
		String message = "[" + joueur.getNom() + "] a choisi: " + carteChoisie +
				" de l'offre de " + offreChoisie.getProprietaire().getNom();

		if (avecGUI && interfaceGraphique != null) {
			interfaceGraphique.ajouterLog(message);
		} else {
			System.out.println("✓ " + message);
		}

		return new ChoixCarte(offreChoisie, carteChoisie);
	}

	/**
	 * Affiche les offres et permet au joueur d'en choisir une
	 */
	private Offre choisirOffre(List<Offre> offres, boolean offresVisibles, JoueurHumain joueur) {
		String[] options = new String[offres.size()];

		for (int i = 0; i < offres.size(); i++) {
			Offre o = offres.get(i);
			if (offresVisibles) {
				options[i] = (i + 1) + ". [" + o.getProprietaire().getNom() +
						"] Carte 1: " + o.getCarteVisible() + " | Carte 2: " + o.getCarteCachee();
			} else {
				options[i] = (i + 1) + ". [" + o.getProprietaire().getNom() +
						"] Visible: " + o.getCarteVisible() + " | Cachée: [?]";
			}
		}

		if (avecGUI && interfaceGraphique != null) {
			String choix = (String) JOptionPane.showInputDialog(
					interfaceGraphique.getFrame(),
					"Choisissez une offre :",
					"Sélection d'offre - " + joueur.getNom(),
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if (choix == null)
				return null;
			int index = Integer.parseInt(choix.substring(0, 1)) - 1;
			return offres.get(index);
		} else {
			// Mode console
			System.out.println("\n=== Offres disponibles ===");
			for (String opt : options) {
				System.out.println("  " + opt);
			}

			System.out.print("Votre choix (numéro) : ");
			try {
				int choix = scanner.nextInt();
				scanner.nextLine();
				if (choix >= 1 && choix <= offres.size()) {
					return offres.get(choix - 1);
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
			}
			System.out.println("⚠ Choix invalide");
			return null;
		}
	}

	/**
	 * Permet au joueur de choisir une carte dans une offre
	 */
	private Carte choisirCarteFromOffre(Offre offre, boolean offresVisibles, JoueurHumain joueur) {
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

		if (avecGUI && interfaceGraphique != null) {
			String choix = (String) JOptionPane.showInputDialog(
					interfaceGraphique.getFrame(),
					"Quelle carte voulez-vous prendre ?",
					"Choix de carte - " + joueur.getNom(),
					JOptionPane.QUESTION_MESSAGE,
					null,
					choixCartes,
					choixCartes[0]);
			if (choix == null)
				return null;
			return choix.startsWith("2") ? offre.getCarteCachee() : offre.getCarteVisible();
		} else {
			// Mode console
			System.out.println("\nCartes disponibles :");
			for (String c : choixCartes) {
				System.out.println("  " + c);
			}
			System.out.print("Votre choix (numéro) : ");
			try {
				int choix = scanner.nextInt();
				scanner.nextLine();
				if (choix == 2) {
					return offre.getCarteCachee();
				} else if (choix == 1) {
					return offre.getCarteVisible();
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
			}
			System.out.println("⚠ Choix invalide");
			return null;
		}
	}

	/**
	 * Gère le cas où le joueur doit choisir dans sa propre offre
	 */
	private ChoixCarte choisirDansSaPropreOffre(Offre offre, JoueurHumain joueur) {
		String message = "Vous êtes le dernier joueur.\nVous devez choisir dans votre propre offre.";

		if (avecGUI && interfaceGraphique != null) {
			JOptionPane.showMessageDialog(interfaceGraphique.getFrame(),
					message, "Dernier joueur", JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.out.println("\n⚠ " + message);
		}

		boolean offresVisibles = offre.getCarteCachee().estVisible();
		Carte carteChoisie = choisirCarteFromOffre(offre, offresVisibles, joueur);
		if (carteChoisie == null)
			return null;

		String logMessage = "[" + joueur.getNom() + "] a gardé: " + carteChoisie;
		if (avecGUI && interfaceGraphique != null) {
			interfaceGraphique.ajouterLog(logMessage);
		} else {
			System.out.println("✓ " + logMessage);
		}

		return new ChoixCarte(offre, carteChoisie);
	}

	/**
	 * Utilitaire pour choisir l'indice d'une carte dans une liste
	 */
	private int choisirIndiceCarte(List<Carte> cartes, String titre) {
		String[] options = new String[cartes.size()];
		for (int i = 0; i < cartes.size(); i++) {
			options[i] = (i + 1) + ". " + cartes.get(i).toString();
		}

		if (avecGUI && interfaceGraphique != null) {
			String choix = (String) JOptionPane.showInputDialog(
					interfaceGraphique.getFrame(),
					titre,
					"Sélection de carte",
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if (choix == null)
				return -1;
			return Integer.parseInt(choix.substring(0, 1)) - 1;
		} else {
			// Mode console
			System.out.println("\n" + titre);
			for (String opt : options) {
				System.out.println("  " + opt);
			}
			System.out.print("Votre choix (numéro) : ");
			try {
				int choix = scanner.nextInt();
				scanner.nextLine();
				if (choix >= 1 && choix <= cartes.size()) {
					return choix - 1;
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
			}
			System.out.println("⚠ Choix invalide");
			return -1;
		}
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
	 * Fonction principale du jeu, point d'accès
	 * On propose le choix du mode d'affichage
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("\n╔════════════════════════════════════╗");
		System.out.println("║          JEU DE JEST               ║");
		System.out.println("╚════════════════════════════════════╝\n");

		// Choix du mode d'affichage
		System.out.println("Mode d'affichage :");
		System.out.println("1. Console uniquement");
		System.out.println("2. Interface graphique uniquement");
		System.out.println("3. Console + Interface graphique (mode mixte)");
		System.out.print("Votre choix (1-3): ");

		int choixMode = 1;
		try {
			choixMode = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			scanner.nextLine();
		}

		// Choix création ou chargement de partie
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

		// Configuration du mode d'affichage
		switch (choixMode) {
			case 2:
				jeu.avecGUI = true;
				jeu.modeConsoleEtGUI = false;
				System.out.println("✓ Mode Interface Graphique activé");
				break;
			case 3:
				jeu.avecGUI = true;
				jeu.modeConsoleEtGUI = true;
				System.out.println("✓ Mode Mixte (Console + GUI) activé");
				break;
			default:
				jeu.avecGUI = false;
				jeu.modeConsoleEtGUI = false;
				System.out.println("✓ Mode Console uniquement");
		}

		jeu.demarrer();
	}
}