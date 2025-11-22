package jest_package1;

import java.util.*;

public class Jeu {
	private List<Joueur> joueurs;
	private RegleJeu regleJeu;
	private Extension extension;
	private Jeu jeuCourant;
	private EtatPartie etat;
	
	public void configurerJeu() {
		
	}
	
	public void ajouterJoueur(Joueur joueurs) {
	    if (this.joueurs == null) {
	        this.joueurs = new ArrayList<>();
	    }
		this.joueurs.add(joueurs);
	}
	
	public void choisirRegle(RegleJeu regleJeu) {
		this.regleJeu=regleJeu;
	}
	
	public void activerExtension(Extension extension) {
		
	}
	
	public void demarrer() {
		
	}
	
	public void sauvegarder(String fichier) {
		
	}
	
	public void charger(String fichier) {
		
	}
	
	public Jeu getJeu() {
		return jeuCourant;
	}


}

