package jest_package1;

import java.util.*;

/**
 * Classe implémentant les règles standard du jeu Jest.
 * 
 * C'est l'implémentation de référence des règles du jeu avec:
 * 
 * Calcul des scores:
 * - Piques et Trèfles: Ajoutent leurs valeurs
 * - Carreaux: Retranchent leurs valeurs
 * - Cœurs: Valeur nulle (sauf avec Joker)
 * - Étoiles: Doublent les points
 * - Triangles: Valeur nulle (sauf avec Joker)
 * - Soleils: Points selon pair/impair
 * - Joker: 4 points seul, modifie le scoring des autres couleurs
 * 
 * Système de trophées:
 * - Gère les trophées de manière standard
 * - Détermine les gagnants basé sur les conditions
 * 
 * Ordre de jeu:
 * - Basé sur la force des cartes visibles des offres
 * 
 * @author David et Léna
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
}