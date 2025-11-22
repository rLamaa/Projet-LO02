package jest_package1;

import java.util.List;

public class JoueurHumain extends Joueur {
    public JoueurHumain(String nom) {
        super(nom);
    }
    public ChoixCarte choisirCarte(List<Offre> offres) { 
    	return null;
    	}
    @Override
    public void faireOffre(Carte c1, Carte c2) { 
    	this.offreCourante=new Offre(c1,c2,this);
    	}
}