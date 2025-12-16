package jest_package1;

import java.util.*;

public class JoueurHumain extends Joueur {
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

					System.out.println("  1. Visible: " + o.getCarteVisible());
					System.out.println("  2. Cachée: [?]");
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
		for (int i = 0; i < offresDisponibles.size(); i++) {
			Offre o = offresDisponibles.get(i);
			System.out.println("  " + (i + 1) + ". [" + o.getProprietaire().getNom() +
					"] Visible: " + o.getCarteVisible() + " | Cachée: [?]");
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

		// Demander le choix de la carte (visible ou cachée)
		System.out.println("\n[" + this.nom + "] Quelle carte voulez-vous ?");
		System.out.println("  1. Visible: " + offreChoisie.getCarteVisible());
		System.out.println("  2. Cachée: [?]");
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

	@Override
	public Offre faireOffre() {
		String choix = "0";
		System.out.println("\n[" + this.nom + "] Quelle carte doit être cachée?");

		// CORRECTION IMPORTANTE : Utiliser jest (temporaire), PAS getJest() (définitif)
		List<Carte> cartes = this.jest.getCartes(); // <-- CHANGÉ ICI

		for (int i = 0; i < cartes.size(); i++) {
			if (!(cartes.get(i) instanceof Joker)) {
				System.out.println(
						"  Choix " + (i + 1) + ": " + cartes.get(i).getValeur() + " de "
								+ cartes.get(i).getCouleur().getSymbole());
			} else {
				System.out.println("  Choix " + (i + 1) + ": Joker");
			}
		}

		System.out.print("[" + this.nom + "] La 1 ou la 2? ");
		choix = Jeu.scanner.nextLine().trim();
		Carte c1;
		Carte c2;
		if (choix.equals("1")) {
			c1 = cartes.get(0);
			c2 = cartes.get(1);
		} else if (choix.equals("2")) {
			c2 = cartes.get(0);
			c1 = cartes.get(1);
		} else {
			System.out.println("Mauvais choix, première carte mise par défaut");
			c1 = cartes.get(0);
			c2 = cartes.get(1);
		}

		// On enlève les cartes du Jest TEMPORAIRE
		this.jest.enleverCarte(c1);
		this.jest.enleverCarte(c2);

		this.offreCourante = new Offre(c1, c2, this);
		return this.offreCourante;
	}
}