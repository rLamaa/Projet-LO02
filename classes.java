package jest_package1;

public class CalculateurScoreStandard implements VisiteurScore {
    public int visiterPique(CarteCouleur c, Jest j) { return 0; }
    public int visiterTrefle(CarteCouleur c, Jest j) { return 0; }
    public int visiterCarreau(CarteCouleur c, Jest j) { return 0; }
    public int visiterCoeur(CarteCouleur c, Jest j) { return 0; }
    public int visiterJoker(Joker c, Jest j) { return 0; }
    public int visiterExtension(CarteExtension c, Jest j) { return 0; }
    public int calculerScore(Jest j) { return 0; }

    private int calculerBonusPairesNoires(Jest j) { return 0; }
    private int calculerValeurAs(CarteCouleur c, Jest j) { return 0; }
    private int compterCoeurs(Jest j) { return 0; }
}
package jest_package1;

import java.io.Serializable;

public abstract class Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Couleur couleur;
    protected Valeur valeur;

    public int accepter(VisiteurScore visiteur, Jest jest) {
        return 0;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Valeur getValeur() {
        return valeur;
    }

    public int getValeurNumerique() {
        return valeur.getValeur();
    }

    // C'est quoi l'utilité de cette methode ?
    /*
     * public boolean estAs() {
     * return this.valeur == 1;
     * }
     */

    public String toString() {
        return "";
    }
}
package jest_package1;

import java.io.Serializable;

public class CarteCouleur extends Carte implements Serializable {
    private static final long serialVersionUID = 1L;

    public CarteCouleur(Couleur couleur, Valeur valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return valeur + couleur.getSymbole();
    }

    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        switch (couleur) {
            case PIQUE:
                return visiteur.visiterPique(this, jest);
            case TREFLE:
                return visiteur.visiterTrefle(this, jest);
            case CARREAU:
                return visiteur.visiterCarreau(this, jest);
            case COEUR:
                return visiteur.visiterCoeur(this, jest);
        }
        return 0;
    }
}
package jest_package1;

import java.io.Serializable;

public class CarteExtension extends Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    private String effetSpecial;
    private String description;

    public int accepter(VisiteurScore visiteur, Jest jest) {
        return 0;
    }

    public void appliquerEffet(Jest jest) {
    }
}
package jest_package1;

import java.io.Serializable;

public class ChoixCarte implements Serializable {
	private static final long serialVersionUID = 1L;
	private Offre offreChoisie;
	private Carte carteChoisie;

	public ChoixCarte(Offre offreChoisie, Carte carteChoisie) {
		this.offreChoisie = offreChoisie;
		this.carteChoisie = carteChoisie;
	}

	public Offre getOffre() {
		return offreChoisie;
	}

	public Carte getCarte() {
		return carteChoisie;
	}
}package jest_package1;

public enum Couleur {
    PIQUE, TREFLE, CARREAU, COEUR;

    public int getForce() {
    	switch(this) {
    	case PIQUE: return 4;
    	case TREFLE: return 3;
    	case CARREAU: return 2;
    	case COEUR: return 1;
    	}
    	return 0;
    	}
    public String getSymbole() { 
        switch (this) {
        case PIQUE: return "♠";
        case TREFLE: return "♣";
        case CARREAU: return "♦";
        case COEUR: return "♥";
    }
    return "";
    }
}
package jest_package1;


public enum EtatPartie{
	CONFIGURATION,
	EN_COURS,
	TERMINEE,
	SUSPENDUE
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public class Extension implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private List<CarteExtension> nouvellesCartes;
    private boolean active;

    public void ajouterCarte(CarteExtension c) {
    }

    public List<CarteExtension> getCartes() {
        return null;
    }

    public boolean estActive() {
        return active;
    }

    public void activer() {
    }

    public void desactiver() {
    }
}package jest_package1;

import java.io.Serializable;
import java.util.*;

public class Jest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Carte> cartes;
    private List<Carte> trophees;

    public Jest() {
        this.cartes = new ArrayList<>();
        this.trophees = new ArrayList<>();
    }

    public void ajouterCarte(Carte carte) {
        cartes.add(carte);
    }

    public void ajouterTrophee(Carte carte) {
        trophees.add(carte);
    }

    public List<Carte> getCartes() {
        return cartes;
    }

    public List<Carte> getTrophees() {
        return trophees;
    }
}
package jest_package1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
		// configuration par défaut
		this.regleJeu = new RegleStandard();
		this.extension = null;
		// configurer les joueurs en ligne de commandes
		System.out.println("Configuration du jeu Jest");
		int nbJoueurs = 0;
		while (nbJoueurs < 1 || nbJoueurs > 4) {
			System.out.print(
					"Entrez le nombre de joueurs physique (3-4), si il n'y en a pas assez, des bots seront ajouté automatiquement : ");
			nbJoueurs = scanner.nextInt();
			scanner.nextLine(); // Consume the leftover newline
		}
		System.out.println("[DEBUG] " + nbJoueurs + " joueur(s) humain(s) configuré(s).");
		if (nbJoueurs < 3) {
			System.out.println(
					"Le nombre de joueurs physique est inférieur à 3, des joueurs virtuels seront ajoutés automatiquement.");
		}
		for (int i = 1; i <= nbJoueurs; i++) {
			System.out.print("Entrez le nom du joueur " + i + " : ");
			String nom = scanner.next();
			scanner.nextLine(); // Consume the leftover newline
			Joueur joueur = new JoueurHumain(nom);
			ajouterJoueur(joueur);
		}
		for (int i = joueurs.size() + 1; i <= 3; i++) {
			String nomVirtuel = "Bot_" + i;
			System.out.println("[DEBUG] Ajout du joueur virtuel : " + nomVirtuel);
			JoueurVirtuel joueurVirtuel = new JoueurVirtuel(nomVirtuel);
			ajouterJoueur(joueurVirtuel);
		}
		System.out.println("Configuration terminée.");
		System.out.println(
				"[DEBUG] Joueurs en jeu: " + java.util.Arrays.toString(joueurs.stream().map(Joueur::getNom).toArray()));
	}

	public void ajouterJoueur(Joueur joueurs) {
		if (etat != EtatPartie.CONFIGURATION) { // verification si le jeu est en config
			System.out.println("Impossible d'ajouter des joueurs : jeu déjà démarré.");
			return;
		}
		this.joueurs.add(joueurs);
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
		this.partieCourante = new Partie();
		partieCourante.setJeuReference(this);
		partieCourante.initialiser(joueurs, regleJeu, extension);
		List<Carte> trophees = partieCourante.getTrophees();
		System.out.println("Les trophées sont : ");
		System.out.println("♠");
		for (Carte c : trophees) {
			if (!(c instanceof Joker)) {
				System.out.println(c.getValeur() + c.getCouleur().getSymbole());
			} else {
				System.out.println("Joker");
			}

		}
		// debug
		// System.out.println(partieCourante.verifierFinJeu());
		while (!partieCourante.verifierFinJeu()) {
			System.out.println("\n     Début de la manche " + partieCourante.getNumeroManche());
			partieCourante.jouerManche();

		}
	}

	public boolean proposerSauvegardeOuQuitter() {
		Scanner sc = new Scanner(System.in);

		System.out.print("Voulez-vous sauvegarder la partie ? (o/n) : ");
		String rep = sc.nextLine().trim().toLowerCase();

		if (rep.equals("o") || rep.equals("oui")) {
			sauvegarder();

			System.out.print("Voulez-vous quitter la partie ? (o/n) : ");
			String quitter = sc.nextLine().trim().toLowerCase();

			if (quitter.equals("o") || quitter.equals("oui")) {
				etat = EtatPartie.SUSPENDUE;
				System.out.println("✔ Partie sauvegardée et arrêtée.");
				sc.close();
				return true; // ← signal d'arrêt
			}
		}
		sc.close();
		return false; // on continue à jouer

	}

	public void sauvegarder() {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sauvegarde_jeu.dat"))) {

			oos.writeObject(this);

			System.out.println("✔ Partie sauvegardée dans sauvegarde_jeu.dat");

		} catch (IOException e) {
			System.err.println("❌ Erreur lors de la sauvegarde");
			e.printStackTrace();
		}
	}

	public static Jeu charger(String fichier) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {

			Jeu jeu = (Jeu) ois.readObject();
			System.out.println("✔ Partie chargée depuis " + fichier);
			return jeu;

		} catch (IOException | ClassNotFoundException e) {
			System.err.println("❌ Erreur lors du chargement");
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		Jeu jeu = new Jeu();
		jeu.configurerJeu();
		jeu.demarrer();
	}
}
package jest_package1;

import java.io.Serializable;

public class Joker extends Carte implements Serializable {
    private static final long serialVersionUID = 1L;

    public Joker() {
        this.couleur = null; // Joker n’a pas de couleur
    }

    @Override
    public String toString() {
        return "Joker";
    }

    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        return visiteur.visiterJoker(this, jest);
    }
}
package jest_package1;

import java.io.Serializable;
import java.util.List;

public abstract class Joueur implements Serializable {
    private static final long serialVersionUID = 1L;

    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
    }

    protected String nom;
    protected Jest jest;
    protected Offre offreCourante;

    public abstract Offre faireOffre();

    public abstract ChoixCarte choisirCarte(List<Offre> offres);

    public void ajouterCarteJest(Carte carte) {
        jest.ajouterCarte(carte);
    }

    public Jest getJest() {
        return jest;
    }

    public String getNom() {
        return nom;
    }

    public Offre getOffreCourante() {
        return offreCourante;
    }
}
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
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public class JoueurVirtuel extends Joueur implements Serializable {
    private static final long serialVersionUID = 1L;

    public JoueurVirtuel(String nom) {
        super(nom);
    }

    private Strategie strategie;

    @Override
    public Offre faireOffre() {
        List<Carte> cartes = jest.getCartes();
        if (cartes.size() < 2) {
            throw new IllegalStateException("Pas assez de cartes pour faire une offre");
        }

        // On choisit la carte de plus faible valeur pour la cacher
        Carte c1 = cartes.get(0);
        Carte c2 = cartes.get(1);

        if (c1.getValeurNumerique() > c2.getValeurNumerique()) {
            Carte temp = c1;
            c1 = c2;
            c2 = temp;
        }

        this.offreCourante = new Offre(c1, c2, this);
        return this.offreCourante;
    }

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres) {
        // Stratégie simple: choisir la carte face-up avec la plus grande valeur
        Carte meilleureCarte = null;
        Offre offreChoisie = null;

        for (Offre o : offres) {
            Carte c = o.getCarteVisible();
            if (meilleureCarte == null || c.getValeurNumerique() > meilleureCarte.getValeurNumerique()) {
                meilleureCarte = c;
                offreChoisie = o;
            }
        }

        return new ChoixCarte(offreChoisie, meilleureCarte);
    }

    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

}
package jest_package1;

import java.io.Serializable;

public class Offre implements Serializable {
	private static final long serialVersionUID = 1L;
	private Carte carteVisible;
	private Carte carteCachee;
	private Joueur proprietaire;

	public Offre(Carte carteCachee, Carte carteVisible, Joueur proprietaire) {
		super();
		this.carteVisible = carteVisible;
		this.carteCachee = carteCachee;
		this.proprietaire = proprietaire;
	} // fini

	public Carte getCarteVisible() {
		return carteVisible;
	} // fini

	public Carte getCarteCachee() {
		return carteCachee;
	} // fini

	public Carte retirerCarte(boolean visible) {
		Carte carteChoisie = null; // null à remplacer par la carte choisie
		return carteChoisie;
	}

	public boolean estComplete() {
		return this.carteCachee != null && this.carteVisible != null;
	} // fini

	public Joueur getProprietaire() {
		return proprietaire;
	} // fini
}package jest_package1;

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
	private transient Jeu jeuReference;

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
		// proposition de sauvegarde avant de créer les offres
		if (jeuReference.proposerSauvegardeOuQuitter()) {
			return; // on quitte le tour immédiatement
		}
		// creations des offres
		creerOffres();

		// boucle de la manche
		while (!verifierFinManche() && !verifierFinJeu()) {
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

	public void setJeuReference(Jeu jeu) {
		this.jeuReference = jeu;
	}

	public void proposerSauvegarde() {
		System.out.println("\n[SAUVEGARDE] Voulez-vous sauvegarder la partie avant de créer les offres? (oui/non)");
		String reponse = Jeu.scanner.nextLine().trim().toLowerCase();
		if (reponse.equals("oui") || reponse.equals("o")) {
			if (jeuReference != null) {
				jeuReference.sauvegarder();
			} else {
				System.out.println("[SAUVEGARDE] Impossible de sauvegarder: référence au jeu non disponible");
			}
		}
	}
}
package jest_package1;

import java.io.Serializable;
import java.util.*;

//finie sauf problèmes d'intégration
public class Pioche implements Serializable {
    private static final long serialVersionUID = 1L;
    // création de la pioche
    private Stack<Carte> pioche = new Stack<>();

    // ajouter les nouvelles cartes si on ajoute une extension
    public void initialiser(boolean avecExtension) {
        for (Couleur c : Couleur.values()) {
            for (Valeur v : Valeur.values()) {
                pioche.add(new CarteCouleur(c, v));
            }
        }
        pioche.add(new Joker());
        melanger();
    }

    public void afficherPioche() {
        for (Carte c : pioche) {
            System.out.println(c);
        }
    }

    // on mélange la pioche
    public void melanger() {
        Collections.shuffle(pioche);
    }

    // on pioche *une* carte
    public Carte piocher() {
        // retourne la carte du dessus de la pioche
        if (this.pioche.isEmpty()) { // verification si la pioche est vide
            return null;
        }
        return this.pioche.pop();
    }

    // on pioche *n* cartes, utile pour la distribution initiale
    public List<Carte> piocher(int n) {
        // initialisation de la liste des cartes piochées
        List<Carte> piochees = new ArrayList<>();
        // boucle pour piocher n cartes
        for (int i = 0; i < n && !this.pioche.isEmpty(); i++) {
            piochees.add(this.pioche.pop());
        }
        return piochees;
    }

    public boolean estVide() {
        // retourne vrai si la pioche est vide
        return this.pioche.isEmpty();
    }

    // utile pour les extensions
    public void ajouterCartes(List<Carte> nouvelleCartes) {
        // ajoute une liste de cartes à la pioche
        this.pioche.addAll(nouvelleCartes);
    }

    public int getTaille() {
        // retourne la taille de la pioche
        return this.pioche.size();
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public interface RegleJeu extends Serializable {
    int calculerValeurJest(Jest jest);

    boolean verifierConditionTrophee(Jest jest, Carte carte);

    List<Joueur> determinerOrdreJeu(List<Offre> offres);

    void appliquerReglesSpeciales(Jeu jeu);
}
package jest_package1;

import java.io.Serializable;
import java.util.List;

public class RegleStandard implements RegleJeu, Serializable {
    private static final long serialVersionUID = 1L;

    public int calculerValeurJest(Jest jest) {
        return 0;
    }

    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return false;
    }

    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        return null;
    }

    public void appliquerReglesSpeciales(Jeu jeu) {
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public interface Strategie extends Serializable {
    ChoixCarte choisirCarte(List<Offre> offres, Jest jest);

    Offre choisirCartesOffre(Carte c1, Carte c2);

    int evaluerOffre(Offre offre, Jest jest);
}
package jest_package1;

import java.io.Serializable;
import java.util.List;

public class StrategieAleatoire implements Strategie, Serializable {
    private static final long serialVersionUID = 1L;

    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        return null;
    }

    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        return null;
    }

    public int evaluerOffre(Offre offre, Jest jest) {
        return 0;
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public class StrategieDefensive implements Strategie, Serializable {
    private static final long serialVersionUID = 1L;

    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        return null;
    }

    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        return null;
    }

    public int evaluerOffre(Offre offre, Jest jest) {
        return 0;
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public class StrategieOffensive implements Strategie, Serializable {
    private static final long serialVersionUID = 1L;

    public ChoixCarte choisirCarte(List<Offre> offres, Jest jest) {
        return null;
    }

    public Offre choisirCartesOffre(Carte c1, Carte c2) {
        return null;
    }

    public int evaluerOffre(Offre offre, Jest jest) {
        return 0;
    }
}package jest_package1;

import java.util.*;

public class TestDesFonctions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 	Carte c1 = new CarteCouleur(Couleur.PIQUE, Valeur.TROIS);
	        Carte c2 = new CarteCouleur(Couleur.COEUR, Valeur.AS);
	        Carte j = new Joker();
	        
	        System.out.println(c1); // 3♠
	        System.out.println(c2); // 1♥
	        System.out.println(j);  // Joker
	        Pioche p = new Pioche();
	        p.initialiser(false);
	        //while (!p.estVide()) System.out.println(p.piocher());
	        JoueurHumain j1 = new JoueurHumain("Léna");
	        System.out.println(j1.getNom());
	        System.out.println(j1.getJest().getCartes());
	        j1.getJest().getCartes().add(c1);
	        j1.getJest().getCartes().add(c2);

	        System.out.println("");
	        j1.faireOffre();
	        
	        System.out.println("Cachée "+j1.getOffreCourante().getCarteCachee());
	        System.out.println("Visible "+j1.getOffreCourante().getCarteVisible());

	        
	        
	        
	        
	}

}
package jest_package1;

public class Trophee {
	private TypeCondition typeCondition;
	private Couleur couleurCible;
	private int valeurCible;
	private int modificateurScore;
	
	public boolean verifierCondition(Jest jest) {
		return false;
		
	}
	
	public int appliquerEffet(Jest jest) {
		return modificateurScore;
		
	}
	
}

package jest_package1;

public enum TypeCondition{
	MAJORITE_COULEUR,
	MEILLEUR_JEST,
	PIRE_JEST,
	PLUS_DE_PAIRES
}package jest_package1;

public enum Valeur {
    AS, DEUX, TROIS, QUATRE;

    public int getValeur() {
        switch (this) {
        case AS:
            return 1;
        case DEUX:
            return 2;
        case TROIS:
            return 3;
        case QUATRE:
            return 4;
        }
        return 0;
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

class VarianteRapide implements RegleJeu, Serializable {
    private static final long serialVersionUID = 1L;
    private int nombreManchesMax;

    public int calculerValeurJest(Jest jest) {
        return 0;
    }

    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return false;
    }

    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        return null;
    }

    public void appliquerReglesSpeciales(Jeu jeu) {
    }
}package jest_package1;

import java.io.Serializable;
import java.util.List;

public class VarianteStrategique implements RegleJeu, Serializable {
    private static final long serialVersionUID = 1L;
    private boolean offresVisibles;

    public int calculerValeurJest(Jest jest) {
        return 0;
    }

    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return false;
    }

    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        return null;
    }

    public void appliquerReglesSpeciales(Jeu jeu) {
    }
}
package jest_package1;

public interface VisiteurScore {
    int visiterPique(CarteCouleur carte, Jest jest);
    int visiterTrefle(CarteCouleur carte, Jest jest);
    int visiterCarreau(CarteCouleur carte, Jest jest);
    int visiterCoeur(CarteCouleur carte, Jest jest);
    int visiterJoker(Joker carte, Jest jest);
    int visiterExtension(CarteExtension carte, Jest jest);
    int calculerScore(Jest jest);
}
