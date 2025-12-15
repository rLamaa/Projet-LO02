package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Classe Partie implémentant le patron Singleton.
 * Gère le déroulement d'une partie de Jest.
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
	private Joueur joueurActif;
	private transient Jeu jeuReference;

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

	public void initialiser(List<Joueur> joueurs, RegleJeu regleJeu, Extension extension) {
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
		pioche.initialiser(extension != null);
		if (extension != null && extension.estActive()) {
			pioche.ajouterCartes(new ArrayList<>(extension.getCartes()));
		}

		System.out.println("Pioche initialisée avec " + pioche.getTaille() + " cartes.");
		pioche.melanger();
		System.out.println("Pioche mélangée.");

		initialiserTrophees();

		this.joueurs = joueursInitialises;
		this.regleJeu = regleJeu;
		this.numeroManche = 1;
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
		distribuerCartes();

		if (jeuReference != null && jeuReference.proposerSauvegardeOuQuitter()) {
			return;
		}

		creerOffres();

		while (!verifierFinManche() && !verifierFinJeu()) {
			resoudreTour();
		}

		numeroManche++;
	}

	public void distribuerCartes() {
		if (numeroManche == 1) {
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

			// Ajouter des cartes de la pioche
			int nbCartesAPiocher = joueurs.size();
			cartesRestantes.addAll(pioche.piocher(nbCartesAPiocher));

			Collections.shuffle(cartesRestantes);

			// Distribuer 2 cartes à chaque joueur
			for (int i = 0; i < joueurs.size(); i++) {
				joueurs.get(i).ajouterCarteJest(cartesRestantes.get(i * 2));
				joueurs.get(i).ajouterCarteJest(cartesRestantes.get(i * 2 + 1));
			}
		}
		System.out.println("Les cartes sont distribuées");
	}

	public void creerOffres() {
		offresActuelles = new ArrayList<>();
		System.out.println("\n=== Création des offres ===");

		for (Joueur j : joueurs) {
			Offre offre = j.faireOffre();
			offresActuelles.add(offre);

			System.out.println("[" + j.getNom() + "] Offre créée - Visible: " +
					offre.getCarteVisible() + " | Cachée: [?]");
		}
	}

	public void resoudreTour() {
		// Trier les joueurs par ordre décroissant de leur carte visible
		List<Joueur> ordreJoueurs = new ArrayList<>(joueurs);
		ordreJoueurs.sort((j1, j2) -> {
			int val1 = 0, val2 = 0;
			if (!(j1.getOffreCourante().getCarteVisible() instanceof Joker)) {
				val1 = j1.getOffreCourante().getCarteVisible().getValeurNumerique();
			}
			if (!(j2.getOffreCourante().getCarteVisible() instanceof Joker)) {
				val2 = j2.getOffreCourante().getCarteVisible().getValeurNumerique();
			}
			return Integer.compare(val2, val1); // Ordre décroissant
		});

		// Chaque joueur prend son tour une fois
		for (Joueur actif : ordreJoueurs) {
			if (verifierFinManche()) {
				break; // La manche est finie
			}

			System.out.println("\n--- Tour de " + actif.getNom() + " ---");

			ChoixCarte choix = actif.choisirCarte(offresActuelles);

			if (choix != null) {
				Carte carteChoisie = choix.getCarte();
				Offre offreChoisie = choix.getOffre();

				actif.ajouterCarteJest(carteChoisie);
				offreChoisie.retirerCarte(carteChoisie);

				System.out.println("[" + actif.getNom() + "] a pris: " + carteChoisie);
			}
		}
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
		return pioche.estVide();
	}

	public void terminerPartie() {
		System.out.println("\n=== FIN DE LA PARTIE ===");

		// Chaque joueur prend sa dernière carte
		for (int i = 0; i < joueurs.size(); i++) {
			Offre offre = offresActuelles.get(i);
			Carte derniereCarte = offre.getCarteVisible() != null ? offre.getCarteVisible() : offre.getCarteCachee();
			joueurs.get(i).ajouterCarteJest(derniereCarte);
		}

		attribuerTrophees();
		calculerGagnant();
	}

	public void attribuerTrophees() {
		System.out.println("\n=== Attribution des trophées ===");

		for (Carte trophee : trophees) {
			Joueur gagnant = regleJeu.determinerGagnantTrophee(joueurs, trophee);
			if (gagnant != null) {
				gagnant.getJest().ajouterTrophee(trophee);
				System.out.println("Trophée " + trophee + " attribué à " + gagnant.getNom());
			}
		}
	}

	public Joueur calculerGagnant() {
		System.out.println("\n=== Calcul des scores ===");

		VisiteurScore calculateur = new CalculateurScoreStandard();
		int scoreMax = Integer.MIN_VALUE;
		Joueur gagnant = null;

		for (Joueur j : joueurs) {
			int score = calculateur.calculerScore(j.getJest());
			System.out.println("[" + j.getNom() + "] Score: " + score);

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
}