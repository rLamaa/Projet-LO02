package jest_package1;

import java.util.*;

/**
 * Classe représentant un joueur humain du jeu de Jest.
 * 
 * Permet à un joueur humain de prendre des décisions via l'entrée console.
 * Gère l'interactivité complète du jeu en mode console.
 * 
 * Responsabilités:
 * - Créer des offres: Affiche les cartes et demande au joueur de choisir
 * quelle carte cacher/afficher
 * - Choisir des cartes: Affiche les offres disponibles et demande
 * au joueur de sélectionner une offre puis une carte
 * 
 * Comportement:
 * - Affichage formaté avec symboles Unicode et clarté
 * - Gestion des entrées invalides avec reprise de saisie
 * - Cas spéciaux gérés (dernier joueur, aucune offre disponible)
 * - Messages informatifs pour guider le joueur
 * 
 * @author David et Léna
 */
public class JoueurHumain extends Joueur {
	/**
	 * Identifiant de version pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur d'un joueur humain.
	 * 
	 * @param nom le nom du joueur humain
	 */
	public JoueurHumain(String nom) {
		super(nom);
	}

	/**
	 * Permet au joueur humain de choisir une carte parmi les offres disponibles.
	 * Gère les cas particuliers (dernier joueur, aucune offre disponible).
	 * 
	 * @param offres la liste des offres proposées
	 * @return le choix de carte effectué par le joueur
	 */
	@Override
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
					if (o.getCarteCachee().estVisible()) {
						// Variante stratégique, toutes les cartes sont visibles
						offresVisibles = true;
					}

					if (offresVisibles) {
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
			if (o.getCarteCachee().estVisible()) {
				// Variante stratégique, toutes les cartes sont visibles
				offresVisibles = true;
				System.out.println("  " + (i + 1) + ". [" + o.getProprietaire().getNom() + "] Carte 1 : "
						+ o.getCarteVisible() + " | Carte 2 : " + o.getCarteCachee());
			} else {
				// Jeu standard donc une carte cachée, une carte visible
				System.out.println("  " + (i + 1) + ". [" + o.getProprietaire().getNom() + "] Visible : "
						+ o.getCarteVisible() + " | Cachée : [?]");

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

		// Demander le choix de la carte (visible ou cachée (standard) ou 1 ou 2
		// (variante stratégique))
		if (offresVisibles) {
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
			// Variante stratégique, pas besoin de choisir quelle carte est cachée et
			// l'autre visible : les deux visibles
			System.out.println("\n[" + this.nom
					+ "] Mode Stratégique : Les cartes sont toutes visibles donc vous n'avez pas à créer d'offre");
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
}