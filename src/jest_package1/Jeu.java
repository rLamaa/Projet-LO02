package jest_package1;

import java.io.*;
import java.util.*;

public class Jeu implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Joueur> joueurs;
	private RegleJeu regleJeu;
	private Extension extension;
	private Partie partieCourante;
	private EtatPartie etat;
	public static Scanner scanner = new Scanner(System.in);

	public Jeu() {
		this.joueurs = new ArrayList<>();
		this.etat = EtatPartie.CONFIGURATION;
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
		while (nbJoueurs < 1 || nbJoueurs > 4) {
			System.out.print("Nombre de joueurs humains (1-4): ");
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
			if (nom.isEmpty())
				nom = "Joueur" + i;
			ajouterJoueur(new JoueurHumain(nom));
		}

		// Compléter avec des bots jusqu'à 3 joueurs minimum
		int nbBots = Math.max(0, 3 - nbJoueurs);
		String[] nomsBots = { "Alpha", "Beta", "Gamma", "Delta" };
		Strategie[] strategies = {
				new StrategieOffensive(),
				new StrategieDefensive(),
				new StrategieAleatoire()
		};

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
		System.out.println("1. Règles Standard");
		System.out.println("2. Variante Rapide (5 manches max)");
		System.out.println("3. Variante Stratégique (offres visibles, scores modifiés)");
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
				this.regleJeu = new VarianteStrategique();
				System.out.println("✓ Variante Stratégique sélectionnée");
				break;
			default:
				this.regleJeu = new RegleStandard();
				System.out.println("✓ Règles Standard sélectionnées");
		}
	}

	private void choisirExtension() {
		System.out.println("\n=== Extension ===");
		System.out.print("Activer l'extension 'Cartes Magiques' ? (o/n): ");
		String reponse = scanner.nextLine().trim().toLowerCase();

		if (reponse.equals("o") || reponse.equals("oui")) {
			this.extension = Extension.creerExtensionStandard();
			this.extension.activer();
			System.out.println("✓ Extension activée!");
			System.out.println("  Cartes ajoutées: Doublement, Inversion, Miroir");
		} else {
			this.extension = null;
			System.out.println("✓ Pas d'extension");
		}
	}

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
		System.out.println("  Extension: " + (extension != null ? "Oui" : "Non"));
		System.out.println("╚════════════════════════════════════╝\n");
	}

	public void ajouterJoueur(Joueur joueur) {
		if (etat != EtatPartie.CONFIGURATION) { // verification si le jeu est en config
			System.out.println("Impossible d'ajouter des joueurs : jeu déjà démarré.");
			return;
		}
		this.joueurs.add(joueur);
	}

	public void choisirRegle(RegleJeu regleJeu) {
		if (etat != EtatPartie.CONFIGURATION) {
			System.out.println("Impossible de changer les règles : jeu déjà démarré.");
			return;
		}
		this.regleJeu = regleJeu;
	}

	public void activerExtension(Extension extension) {
		if (etat != EtatPartie.CONFIGURATION) {
			System.out.println("Impossible d'activer une extension : jeu déjà démarré.");
			return;
		}
		this.extension = extension;
	}

	public void demarrer() {
		this.etat = EtatPartie.EN_COURS;

		// Utiliser le Singleton Partie
		Partie.reinitialiser();
		this.partieCourante = Partie.getInstance();
		partieCourante.setJeuReference(this);
		partieCourante.initialiser(joueurs, regleJeu, extension);

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
		}

		// Fin de partie
		partieCourante.terminerPartie();
		this.etat = EtatPartie.TERMINEE;
	}

	private void afficherTrophees() {
		System.out.println("\n🏆 === TROPHÉES DE LA PARTIE ===");
		List<Carte> trophees = partieCourante.getTrophees();
		for (Carte c : trophees) {
			System.out.println("  • " + c);
		}
		System.out.println();
	}

	public boolean proposerSauvegardeOuQuitter() {
		System.out.print("\n💾 Sauvegarder la partie ? (o/n): ");
		String rep = scanner.nextLine().trim().toLowerCase();

		if (rep.equals("o") || rep.equals("oui")) {
			sauvegarder();

			System.out.print("Quitter la partie ? (o/n): ");
			String quitter = scanner.nextLine().trim().toLowerCase();

			if (quitter.equals("o") || quitter.equals("oui")) {
				etat = EtatPartie.SUSPENDUE;
				System.out.println("✓ Partie sauvegardée et arrêtée");
				return true;
			}
		}

		return false;
	}

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

	public static Jeu charger(String fichier) {
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(fichier))) {
			Jeu jeu = (Jeu) ois.readObject();
			Partie.reinitialiser();
			jeu.partieCourante = Partie.getInstance();
			jeu.partieCourante.setJeuReference(jeu);
			System.out.println("✓ Partie chargée depuis '" + fichier + "'");
			return jeu;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("❌ Erreur lors du chargement");
			e.printStackTrace();
			return null;
		}
	}

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
}