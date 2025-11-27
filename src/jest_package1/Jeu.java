package jest_package1;

import java.util.*;

public class Jeu {
    private List<Joueur> joueurs;
    private RegleJeu regleJeu;
    private Extension extension;
    private Partie partieCourante;
    private EtatPartie etat;

    public Jeu() {
        this.joueurs = new ArrayList<>();
        this.etat = EtatPartie.CONFIGURATION;
    }

	public void configurerJeu() {
		
	}
	
	public void ajouterJoueur(Joueur joueurs) {
		 if (etat != EtatPartie.CONFIGURATION) { //verification si le jeu est en config
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
		this.regleJeu=regleJeu;
	}
	
	   public void activerExtension(Extension extension) {
	        if (etat != EtatPartie.CONFIGURATION) {
	            System.out.println("Impossible d'activer une extension : jeu déjà démarré.");
	            return;
	        }
	        this.extension = extension;
	    }
	
	public void demarrer() {
		this.etat=EtatPartie.EN_COURS;
	}
	
	public void sauvegarder(String fichier) {
		
	}
	
	public void charger(String fichier) {
		
	}


}

