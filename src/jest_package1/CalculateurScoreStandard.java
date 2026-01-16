package jest_package1;

import java.util.*;

/**
 * Classe implémentant le patron Visitor pour calculer les scores selon les
 * règles du jeu standard de Jest.
 * 
 * @author David et Léna
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

	/*
	 * @Override
	 * public int visiterExtension(CarteExtension c, Jest j) {
	 * // À implémenter selon les cartes d'extension
	 * return 0;
	 * }
	 */

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
}