package jest_package1;

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
