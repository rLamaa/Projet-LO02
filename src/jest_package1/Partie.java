package jest_package1;

import java.io.Serializable;
import java.util.*;

public class Partie implements Serializable {
	private static final long serialVersionUID = 1L;
	private Pioche pioche = new Pioche();
	private List<Carte> trophees;
	private List<Joueur> joueurs;
	private RegleJeu regleJeu;
	private int numeroManche;
	private List<Offre> offresActuelles;
	private Joueur joueurActif;

	public void initialiser(List<Joueur> joueurs, RegleJeu regleJeu, Extension extension) {
		// Créer de nouveaux joueurs pour éviter les références partagées
		List<Joueur> joueursInitialises = new ArrayList<>();
		// Boucle pour chaque joueur dans la liste fournie
		for (Joueur p : joueurs) {
			Joueur joueur = null;

			if (p instanceof JoueurHumain) {
				joueur = new JoueurHumain(p.getNom());
			} else if (p instanceof JoueurVirtuel) { // Vérifie si le joueur est un JoueurVirtuel
				joueur = new JoueurVirtuel(p.getNom()); // pour le nom on pourra faire une enumeration de noms
														// predefinis
			}
			// on ajoute le joueur à la liste
			joueursInitialises.add(joueur);
		}
		// initialisation de la pioche
		pioche.initialiser(false);
		System.out.println("Pioche initialisée avec " + pioche.getTaille() + " cartes.");
		// on melange la pioche
		pioche.melanger();
		System.out.println("Pioche mélangée.");
		// pioche.afficherPioche();
		// on initialise les trophées
		initialiserTrophees();
		// on assigne les attributs
		this.joueurs = joueursInitialises;
		this.regleJeu = regleJeu;
		this.numeroManche = 1;
	}

	private void initialiserTrophees() {
		trophees = new ArrayList<>();
		trophees.add(pioche.piocher());
		trophees.add(pioche.piocher());
	}

	public List<Carte> getTrophees() {
		return trophees;
	}

	public void setTrophees(List<Carte> trophees) {
		this.trophees = trophees;
	}

	public void jouerManche() {
		// apres avoir initialisé la partie, on peut commencer la manche
		// distribution des cartes
		distribuerCartes();
		// creations des offres
		creerOffres();
		// boucle de la manche
		while (!verifierFinManche() && !verifierFinJeu()) {
			// a faire
			resoudreTour();

		}
	}

	public void distribuerCartes() {
		if (numeroManche == 1) {
			// Distribution initiale
			for (Joueur j : joueurs) {
				// On pioche 2 cartes à mettre dans le Jest (face-down)
				List<Carte> cartesInitiales = pioche.piocher(2);
				for (Carte c : cartesInitiales) {
					j.ajouterCarteJest(c);
				}
			}
		} else {
			// Pour les manches suivantes
			for (Joueur j : joueurs) {
				// On pioche 2 cartes par joueur (à ajouter à Jest)
				List<Carte> cartesManche = pioche.piocher(2);
				for (Carte c : cartesManche) {
					j.ajouterCarteJest(c);
				}
			}
		}
		System.out.println("Les cartes sont distribuées");
	}

	public void creerOffres() {
		offresActuelles = new ArrayList<>();
		System.out.println("\n=== Création des offres ===");
		for (Joueur j : joueurs) { // boucle qui permet d'aller voir tout les joueurs
			j.faireOffre(); // le joueur fait son offre
			offresActuelles.add(j.getOffreCourante()); // on l'ajoute aux offres courantes
		}
		System.out.println("[DEBUG] " + offresActuelles.size() + " offres créées.");
	}

	public void resoudreTour() {
		List<Joueur> listJoueursTemp = joueurs;
		for (Joueur i : joueurs) {
			Joueur joueurActif = determinerJoueurActif();
			listJoueursTemp.remove(joueurActif);
			for (int k = 0; k < joueurs.size(); k++) {
				System.out.println("Les offres sont : ");
				int numOffre = 1;
				for (Joueur j : listJoueursTemp) {
					if (j.getOffreCourante().estComplete()) {
						System.out.println("\nOffre " + numOffre + " : " + j.getOffreCourante().getCarteVisible());
					} else {
						System.out.println("L'offre " + numOffre + " n'est plus complète. Pas possible de la choisir");
					}
					numOffre++;
				}

				int choixOffre = 0;
				System.out.println("\n[" + joueurActif.nom + "] Choisissez l'offre qui vous intéresse (1, 2 ou 3) :");
				choixOffre = Integer.parseInt(Jeu.scanner.nextLine().trim()); // Read user input and trim whitespace
				String choixCarte = "0";
				System.out.println("\n[" + joueurActif.nom + "] Quelle carte voulez-vous ?");
				System.out.println(" Choix 1 : " + joueurs.get(choixOffre).getOffreCourante().getCarteVisible());
				System.out.println(" Choix 2 : Carte cachée");
				System.out.print("[" + joueurActif.nom + "] La 1 ou la 2? ");
				choixCarte = Jeu.scanner.nextLine().trim(); // Read user input and trim whitespace
				while (choixCarte != "1" && choixCarte != "2") {
					if (choixCarte.equals("1")) {
						joueurActif.ajouterCarteJest(joueurs.get(choixOffre).getOffreCourante().getCarteVisible());
					} else if (choixCarte.equals("2")) {
						joueurActif.ajouterCarteJest(joueurs.get(choixOffre).getOffreCourante().getCarteCachee());
					}
				}
			}

		}
	}

	public Joueur determinerJoueurActif() {
		int valeurMax = -1;
		int valeur;
		for (Joueur j : joueurs) {
			if (!(j.getOffreCourante().getCarteVisible() instanceof Joker)) {
				valeur = j.getOffreCourante().getCarteVisible().getValeurNumerique();
			} else {
				valeur = 0;
			}
			if (valeur > valeurMax) {
				valeurMax = valeur;
				joueurActif = j;
			}
		}
		return joueurActif; // attribut présent dans la classe, on peut juste l'appeler
	}

	public void prendreCarteOffre(Joueur joueurchoisi, Offre offrechoisie, Carte cartechoisie) {

	}

	public boolean verifierFinManche() {
		for (Offre o : offresActuelles) { // boucle qui permet de tourner dans toutes les offres
			if (o.estComplete()) {
				return false; // il reste une offre complète, la manche continue
			}
		}
		return true; // toutes les offres ont exactement 1 carte
	}

	public boolean verifierFinJeu() {
		return pioche.estVide(); // regarde si la pioche est vide
	}

	public void attribuerTrophees() {
		// todo : attribution finale des trophées, faire à la fin
	}

	public Joueur calculerGagnant() {
		// todo : attribution des points a implementer avant
		return null;
	}

	public int getNumeroManche() {
		return numeroManche;
	}
}
