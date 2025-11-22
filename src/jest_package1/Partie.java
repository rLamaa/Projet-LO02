package jest_package1;

import java.util.*;

public class Partie {
	private Pioche pioche=new Pioche();
	private List<Carte> trophees;
	private List<Joueur> joueurs;
	private RegleJeu regleJeu;
	private int numeroManche;
	private List<Offre> offresActuelles;
	private Joueur joueurActif;
	
	public void initialiser(List<Joueur> joueurs,RegleJeu regleJeu,Extension extension) {

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
	    this.joueurs=joueursInitialises;
	    this.regleJeu = regleJeu;
	    this.numeroManche=1;
	    }
	
	public void jouerManche() {
		
	}
	
	public void distribuerCartes() {
		
	}
	
	public void creerOffres() {
		
	}
	
	public void resoudreTour() {
		
	}
	
	public Joueur determinerJoueurActif() {
		return null;
	}
	
	public void prendreCarteOffre(Joueur joueurchoisi, Offre offrechoisie, Carte cartechoisie) {
		
	}
	
	public boolean verifierFinManche() {
		return false;
	}
	
	public boolean verifierFinJeu() {
		return false;
	}
	
	public void attribuerTrophees() {
		Carte trophee1=pioche.pop();
		
		//List<Carte> trophees=
	}
	
	public Joueur calculerGagnant() {
		return null;
	}
}
