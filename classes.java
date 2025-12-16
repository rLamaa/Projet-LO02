package jest_package1;

import java.util.*;

//A REVOIR IMPERATIVEMENT
/**
 * Implémentation du patron Visitor pour calculer les scores selon les règles
 * standard.
 */
public class CalculateurScoreStandard implements VisiteurScore {

    @Override
    public int calculerScore(Jest j) {
        int scoreTotal = 0;

        // Calculer le score de chaque carte
        for (Carte c : j.getCartes()) {
            scoreTotal += c.accepter(this, j);
        }

        // Ajouter les bonus des paires noires
        scoreTotal += calculerBonusPairesNoires(j);

        // Ajouter les trophées
        for (Carte trophee : j.getTrophees()) {
            scoreTotal += trophee.accepter(this, j);
        }

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

    @Override
    public int visiterCoeur(CarteCouleur c, Jest j) {
        boolean aJoker = false;
        int nbCoeurs = compterCoeurs(j);

        // Vérifier si le joueur a le Joker
        for (Carte carte : j.getCartes()) {
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

    @Override
    public int visiterExtension(CarteExtension c, Jest j) {
        // À implémenter selon les cartes d'extension
        return 0;
    }

    /**
     * Calcule le bonus pour les paires noires (Pique + Trèfle de même valeur)
     */
    private int calculerBonusPairesNoires(Jest j) {
        int bonus = 0;
        Map<Integer, Boolean> piques = new HashMap<>();
        Map<Integer, Boolean> trefles = new HashMap<>();

        for (Carte carte : j.getCartes()) {
            if (carte instanceof CarteCouleur) {
                CarteCouleur cc = (CarteCouleur) carte;
                int valeur = calculerValeurAs(cc, j);

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
        for (Carte carte : j.getCartes()) {
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
        for (Carte carte : j.getCartes()) {
            if (carte instanceof CarteCouleur) {
                CarteCouleur cc = (CarteCouleur) carte;
                if (cc.getCouleur() == Couleur.COEUR) {
                    compte++;
                }
            }
        }
        return compte;
    }
}package jest_package1;

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

import java.util.HashSet;
import java.util.Set;

public class CarteExtension extends Carte {
    private static final long serialVersionUID = 1L;

    private EffetExtension effet;
    private String description;

    public CarteExtension(EffetExtension effet, String description) {
        this.effet = effet;
        this.description = description;
    }

    public EffetExtension getEffet() {
        return effet;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        switch (effet) {

            case DOUBLEMENT:
                return effetDoublement(jest);

            case INVERSION:
                return effetInversion(jest);

            case MIROIR:
                return effetMiroir(jest);

            default:
                return 0;
        }
    }

    /* ================= EFFETS ================= */

    private int effetDoublement(Jest jest) {
        int bonus = 0;
        for (Carte c : jest.getCartes()) {
            if (c instanceof CarteCouleur cc &&
                    cc.getCouleur() == Couleur.PIQUE) {
                bonus += cc.getValeurNumerique();
            }
        }
        return bonus;
    }

    private int effetInversion(Jest jest) {
        int bonus = 0;
        for (Carte c : jest.getCartes()) {
            if (c instanceof CarteCouleur cc &&
                    cc.getCouleur() == Couleur.CARREAU) {
                bonus += 2 * cc.getValeurNumerique();
            }
        }
        return bonus;
    }

    private int effetMiroir(Jest jest) {
        Set<Couleur> couleurs = new HashSet<>();

        for (Carte c : jest.getCartes()) {
            if (c instanceof CarteCouleur cc) {
                couleurs.add(cc.getCouleur());
            }
        }

        return couleurs.size() == 4 ? 3 : 0;
    }

    @Override
    public String toString() {
        return "⭐ " + effet;
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

public enum EffetExtension {
    DOUBLEMENT,
    INVERSION,
    MIROIR
}
package jest_package1;


public enum EtatPartie{
	CONFIGURATION,
	EN_COURS,
	TERMINEE,
	SUSPENDUE
}
package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Système d'extension avec nouvelles cartes
 */
public class Extension implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private List<CarteExtension> nouvellesCartes;
    private boolean active;

    public Extension(String nom) {
        this.nom = nom;
        this.nouvellesCartes = new ArrayList<>();
        this.active = false;
    }

    public void ajouterCarte(CarteExtension c) {
        nouvellesCartes.add(c);
    }

    public List<CarteExtension> getCartes() {
        return nouvellesCartes;
    }

    public boolean estActive() {
        return active;
    }

    public void activer() {
        this.active = true;
    }

    public void desactiver() {
        this.active = false;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Crée une extension avec des cartes prédéfinies
     */
    public static Extension creerExtensionStandard() {
        Extension ext = new Extension("Extension Magique");

        ext.ajouterCarte(new CarteExtension(
                EffetExtension.DOUBLEMENT,
                "Double la valeur de tous vos Piques"));

        ext.ajouterCarte(new CarteExtension(
                EffetExtension.INVERSION,
                "Vos Carreaux deviennent positifs"));

        ext.ajouterCarte(new CarteExtension(
                EffetExtension.MIROIR,
                "+3 points si vous avez les 4 couleurs"));

        return ext;
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
    
    public void enleverCarte(Carte carte) {
        cartes.remove(carte);
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
}package jest_package1;

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
        this.jestPerso = new Jest();
    }

    protected String nom;
    protected Jest jest;
    protected Jest jestPerso;
    protected Offre offreCourante;

    public abstract Offre faireOffre();

    public abstract ChoixCarte choisirCarte(List<Offre> offres);

    public void ajouterCarteJest(Carte carte) {
        jest.ajouterCarte(carte);
    }
    
    public void ajouterCarteJestPerso(Carte carte) {
        jestPerso.ajouterCarte(carte);
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
		// Filtrer les offres disponibles (complètes et pas du joueur lui-même)
		List<Offre> offresDisponibles = new ArrayList<>();
		for (Offre o : offres) {
			if (o.estComplete() && o.getProprietaire() != this) {
				offresDisponibles.add(o);
			}
		}

		if (offresDisponibles.isEmpty()) {
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
		// On enlève les cartes du Jest du joueur pour qu'au prochain tour, le jest soit vide et une fois les cartes distribuées, 
		// le jest ne contienne que 2 cartes, comme il se doit
		this.jest.enleverCarte(c1);
		this.jest.enleverCarte(c2);
		
		this.offreCourante = new Offre(c1, c2, this);
		return this.offreCourante;
	}
}package jest_package1;

import java.util.List;

/**
 * Joueur virtuel utilisant une stratégie pour prendre ses décisions
 */
public class JoueurVirtuel extends Joueur {
    private static final long serialVersionUID = 1L;
    private Strategie strategie;

    public JoueurVirtuel(String nom) {
        super(nom);
        // Stratégie par défaut: offensive
        this.strategie = new StrategieOffensive();
    }

    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public Offre faireOffre() {
        List<Carte> cartes = jest.getCartes();
        if (cartes.size() < 2) {
            throw new IllegalStateException("Pas assez de cartes pour faire une offre");
        }

        Carte c1 = cartes.remove(0);
        Carte c2 = cartes.remove(0);

        // Utiliser la stratégie pour choisir quelle carte cacher
        Offre offre = strategie.choisirCartesOffre(c1, c2);
        this.offreCourante = new Offre(offre.getCarteCachee(), offre.getCarteVisible(), this);

        System.out.println("[" + nom + "] (Bot) Offre créée - Visible: " +
                this.offreCourante.getCarteVisible());

        return this.offreCourante;
    }

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres) {
        // Filtrer les offres complètes qui ne sont pas les nôtres
        List<Offre> offresDisponibles = new java.util.ArrayList<>();
        for (Offre o : offres) {
            if (o.estComplete() && o.getProprietaire() != this) {
                offresDisponibles.add(o);
            }
        }

        if (offresDisponibles.isEmpty()) {
            return null;
        }

        // Utiliser la stratégie pour choisir
        ChoixCarte choix = strategie.choisirCarte(offresDisponibles, jest);

        if (choix != null) {
            System.out.println("[" + nom + "] (Bot) choisit la carte " +
                    choix.getCarte() + " de l'offre de " +
                    choix.getOffre().getProprietaire().getNom());
        }

        return choix;
    }

    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

    public Strategie getStrategie() {
        return strategie;
    }
}package jest_package1;

import java.io.Serializable;

/**
 * Représente une offre de deux cartes (une visible, une cachée)
 */
public class Offre implements Serializable {
	private static final long serialVersionUID = 1L;
	private Carte carteVisible;
	private Carte carteCachee;
	private Joueur proprietaire;

	public Offre(Carte carteCachee, Carte carteVisible, Joueur proprietaire) {
		this.carteVisible = carteVisible;
		this.carteCachee = carteCachee;
		this.proprietaire = proprietaire;
	}

	public Carte getCarteVisible() {
		return carteVisible;
	}

	public Carte getCarteCachee() {
		return carteCachee;
	}

	/**
	 * Retire une carte de l'offre
	 * 
	 * @param carte La carte à retirer
	 * @return La carte retirée
	 */
	public Carte retirerCarte(Carte carte) {
		if (carte == carteVisible) {
			Carte temp = carteVisible;
			carteVisible = null;
			return temp;
		} else if (carte == carteCachee) {
			Carte temp = carteCachee;
			carteCachee = null;
			return temp;
		}
		return null;
	}

	/**
	 * Retire une carte selon si elle est visible ou cachée
	 * 
	 * @param visible true pour retirer la carte visible, false pour la cachée
	 * @return La carte retirée
	 */
	public Carte retirerCarte(boolean visible) {
		if (visible) {
			Carte temp = carteVisible;
			carteVisible = null;
			return temp;
		} else {
			Carte temp = carteCachee;
			carteCachee = null;
			return temp;
		}
	}

	/**
	 * Vérifie si l'offre contient encore 2 cartes
	 */
	public boolean estComplete() {
		return this.carteCachee != null && this.carteVisible != null;
	}

	/**
	 * Compte le nombre de cartes restantes dans l'offre
	 */
	public int compterCartes() {
		int count = 0;
		if (carteVisible != null)
			count++;
		if (carteCachee != null)
			count++;
		return count;
	}

	public Joueur getProprietaire() {
		return proprietaire;
	}
}package jest_package1;

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

				actif.ajouterCarteJestPerso(carteChoisie);
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
			joueurs.get(i).ajouterCarteJestPerso(derniereCarte);
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
}package jest_package1;

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

    Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee);

    void appliquerReglesSpeciales(Jeu jeu);
}
package jest_package1;

import java.util.*;

/**
 * Implémentation des règles standard du jeu Jest
 */
public class RegleStandard implements RegleJeu {
    private static final long serialVersionUID = 1L;

    @Override
    public int calculerValeurJest(Jest jest) {
        CalculateurScoreStandard calculateur = new CalculateurScoreStandard();
        return calculateur.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte trophee) {
        // Cette méthode pourrait être utilisée pour des vérifications spécifiques
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        // Trier les joueurs par valeur de carte visible (ordre décroissant)
        List<Joueur> ordre = new ArrayList<>();
        List<Offre> offresTriees = new ArrayList<>(offres);

        offresTriees.sort((o1, o2) -> {
            int val1 = o1.getCarteVisible() instanceof Joker ? 0 : o1.getCarteVisible().getValeurNumerique();
            int val2 = o2.getCarteVisible() instanceof Joker ? 0 : o2.getCarteVisible().getValeurNumerique();

            if (val1 != val2) {
                return Integer.compare(val2, val1); // Ordre décroissant
            }

            // En cas d'égalité, comparer les couleurs
            if (o1.getCarteVisible() instanceof Joker)
                return 1;
            if (o2.getCarteVisible() instanceof Joker)
                return -1;

            return Integer.compare(
                    o2.getCarteVisible().getCouleur().getForce(),
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
     * Détermine le gagnant d'un trophée selon les règles standard
     */
    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        if (trophee instanceof Joker) {
            // Le Joker va au joueur avec le PIRE Jest
            return determinerPireJest(joueurs);
        }

        if (trophee instanceof CarteCouleur) {
            CarteCouleur ct = (CarteCouleur) trophee;

            // Trophée va au joueur avec la majorité de cette couleur
            return determinerMajoriteCouleur(joueurs, ct.getCouleur());
        }

        return null;
    }

    /**
     * Trouve le joueur avec le pire Jest
     */
    private Joueur determinerPireJest(List<Joueur> joueurs) {
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        int scoreMin = Integer.MAX_VALUE;
        Joueur pire = null;

        for (Joueur j : joueurs) {
            int score = calc.calculerScore(j.getJest());
            if (score < scoreMin) {
                scoreMin = score;
                pire = j;
            }
        }

        return pire;
    }

    /**
     * Trouve le joueur avec la majorité d'une couleur donnée
     */
    private Joueur determinerMajoriteCouleur(List<Joueur> joueurs, Couleur couleur) {
        int maxCount = 0;
        Joueur gagnant = null;
        int maxValeur = 0;

        for (Joueur j : joueurs) {
            int count = 0;
            int valeurMax = 0;

            for (Carte c : j.getJest().getCartes()) {
                if (c instanceof CarteCouleur) {
                    CarteCouleur cc = (CarteCouleur) c;
                    if (cc.getCouleur() == couleur) {
                        count++;
                        if (cc.getValeurNumerique() > valeurMax) {
                            valeurMax = cc.getValeurNumerique();
                        }
                    }
                }
            }

            // En cas d'égalité, comparer les valeurs les plus élevées
            if (count > maxCount || (count == maxCount && valeurMax > maxValeur)) {
                maxCount = count;
                maxValeur = valeurMax;
                gagnant = j;
            }
        }

        return gagnant;
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

public class VarianteRapide implements RegleJeu {
    private static final long serialVersionUID = 1L;
    private int nombreManchesMax = 5;
    private int manchesJouees = 0;

    @Override
    public int calculerValeurJest(Jest jest) {
        // Utilise le même calcul que les règles standard
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        return calc.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerOrdreJeu(offres);
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        manchesJouees++;
        if (manchesJouees >= nombreManchesMax) {
            System.out.println("⏰ Limite de manches atteinte (" + nombreManchesMax + ")");
        }
    }

    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerGagnantTrophee(joueurs, trophee);
    }

    public boolean partiTerminee() {
        return manchesJouees >= nombreManchesMax;
    }
}
package jest_package1;

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

    @Override
    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'determinerGagnantTrophee'");
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
