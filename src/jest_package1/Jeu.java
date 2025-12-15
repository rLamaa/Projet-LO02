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
		partieCourante.initialiser(joueurs, regleJeu, extension);
		List<Carte> trophees = partieCourante.getTrophees();
		System.out.println("Les trophées sont : ");
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
