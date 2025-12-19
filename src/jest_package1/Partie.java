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
	 * Distribue 2 cartes à chaque joueur.
	 * Première manche: cartes piochées directement
	 * Autres manches: récupère les cartes non choisies + nouvelles cartes de la
	 * pioche
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
			System.out.println("  └─ jestPerso (définitif) : " + j.getJestPerso().getCartes()); // TODO: remplacer par
			// jestPerso
			
		}
	}

	/**
	 * Crée les offres de chaque joueur (1 carte visible + 1 carte cachée)
	 * Chaque joueur choisit 2 cartes de sa main pour faire une offre
	 */
	public void creerOffres() {
		offresActuelles = new ArrayList<>();
		System.out.println("\n=== Création des offres ===");

		// Boucle: chaque joueur propose ses 2 cartes (1 visible, 1 cachée)
		for (Joueur j : joueurs) {
			Offre offre = j.faireOffre();
			offresActuelles.add(offre);

			System.out.println("[" + j.getNom() + "] Offre créée - Visible: " +
					offre.getCarteVisible() + " | Cachée: [?]");

			// DEBUG: Afficher l'état des deux Jest après création offre
			System.out.println("[DEBUG] " + j.getNom() + " APRÈS offre:");
			System.out.println("  ├─ jest (temporaire)     : " + j.getJest().getCartes() + " (cartes pour l'offre)");
			System.out.println("  ├─ Carte visible : " + offre.getCarteVisible());
			System.out.println("  ├─ Carte cachée  : " + offre.getCarteCachee());
			System.out.println("  └─ jestPerso (définitif) : " + j.getJestPerso().getCartes());

		}
	}

	/**
	 * Gère un tour complet: chaque joueur choisit une carte jusqu'à fin de la
	 * manche
	 * L'ordre des joueurs dépend de la valeur de leurs cartes visibles
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
				System.out.println("  └─ jestPerso (définitif) : " + joueurActif.getJestPerso().getCartes()); // TODO:
																												// remplacer
																												// par
																												// jestPerso
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
	 * Détermine le premier joueur selon les règles :
	 * - Celui avec la carte visible de plus grande valeur
	 * - En cas d'égalité, celui avec la couleur la plus forte
	 * - Joker = valeur 0
	 * Boucle: compare chaque joueur pour trouver celui avec la meilleure carte
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
	 * Détermine le prochain joueur selon les règles :
	 * - C'est le propriétaire de l'offre choisie (s'il n'a pas encore joué)
	 * - Sinon, c'est le joueur restant avec la plus grande carte visible
	 * Boucle: parcourt les joueurs restants pour trouver celui avec la meilleure
	 * carte
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
	 * Retourne la valeur de la carte visible d'un joueur (Joker = 0)
	 * Condition: si c'est un Joker, retourne 0; sinon retourne sa valeur numérique
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
	 * Trouve l'offre d'un joueur donné
	 * Boucle: parcourt toutes les offres pour trouver celle appartenant au joueur
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
	 * Attribue les trophées aux joueurs selon les règles de jeu
	 * Boucle: pour chaque trophée, détermine le gagnant et l'ajoute à son jest
	 */
	/**
	 * Attribue les trophées aux joueurs selon les règles de jeu
	 * Boucle: pour chaque trophée, détermine le gagnant et l'ajoute à son jest
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
	 * Calcule le score final de chaque joueur et détermine le gagnant
	 * Boucle: parcourt chaque joueur et calcule son score, puis détermine le
	 * meilleur
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
}