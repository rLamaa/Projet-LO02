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
		//todo :expliquer le code
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
	
    private void initialiserTrophees() {
        trophees.add(pioche.piocher());
        trophees.add(pioche.piocher());
    }
	public void jouerManche() {
		
	}
	
	public void distribuerCartes() {
		
	}
	
    public void creerOffres() {
        offresActuelles = new ArrayList<>();

        for (Joueur j : joueurs) { // boucle qui permet d'aller voir tout les joueurs
            j.faireOffre(); // le joueur fait son offre
            offresActuelles.add(j.getOffreCourante()); // on l'ajoute aux offres courantes
        }
    }
	
	public void resoudreTour() {
		//todo: apres l'implementation des méthodes d'autres classes mais avant le score (agit comme un main)
	}
	
	public Joueur determinerJoueurActif() {
		return joueurActif; // attribut présent dans la classe, on peut juste l'appeler
	}
	
	public void prendreCarteOffre(Joueur joueurchoisi, Offre offrechoisie, Carte cartechoisie) {
		
	}
	
	public boolean verifierFinManche() {
		   for (Offre o : offresActuelles) { // boucle qui permet de tourner dans toutes les offres
		        if (o.estComplete()) {
		            return false;  // il reste une offre complète, la manche continue
		        }
		    }
		    return true; // toutes les offres ont exactement 1 carte
	}
	
	public boolean verifierFinJeu() {
		return pioche.estVide(); // regarde si la pioche est vide
	}
	
	public void attribuerTrophees() {
		//todo : attribution finale des trophées, faire à la fin
	}
	
	public Joueur calculerGagnant() {
		//todo : attribution des points a implementer avant
		return null;
	}
}
