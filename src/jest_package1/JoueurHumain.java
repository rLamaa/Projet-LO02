package jest_package1;

import java.io.Serializable;
import java.util.*;

public class JoueurHumain extends Joueur implements Serializable {
	private static final long serialVersionUID = 1L;

	public JoueurHumain(String nom) {
		super(nom);
	}

	public ChoixCarte choisirCarte(List<Offre> offres) {

		return null;
	}

	@Override
	public Offre faireOffre() {
		String choix = "0";
		System.out.println("\n[" + this.nom + "] Quelle carte doit être cachée?");
		List<Carte> cartes = this.getJest().getCartes();
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
		choix = Jeu.scanner.nextLine().trim(); // Read user input and trim whitespace
		Carte c1;
		Carte c2;
		if (choix.equals("1")) {
			c1 = cartes.get(0);
			c2 = cartes.get(1);
		} else if (choix.equals("2")) {
			c2 = cartes.get(0);
			c1 = cartes.get(1);
		}

		else {
			System.out.println("Mauvais choix, première carte mise par défaut");
			c1 = cartes.get(0);
			c2 = cartes.get(1);
		}
		this.offreCourante = new Offre(c1, c2, this);
		return this.offreCourante;
	}
}