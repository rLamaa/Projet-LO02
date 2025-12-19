package jest_package1;

import java.io.Serializable;
import java.util.*;

public class Jest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Carte> cartes;


    /**
     * Constructeur de la fonction
     */
    public Jest() {
        this.cartes = new ArrayList<>();
    }

    public void ajouterCarte(Carte carte) {
        cartes.add(carte); // permet d'ajouter une carte au jest
    }

    /**
     * Fonction permettant de vider la liste de cartes
     * NON UTILISE
     */
    public void vider() {
        cartes.clear();
    }

    public void enleverCarte(Carte carte) {
        cartes.remove(carte);// permet d'enlever une carte au jest
    }

  
    /**
     * Getter de la liste de cartes du jest
     * 
     * @return
     */
    public List<Carte> getCartes() {
        return cartes;
    }

    /**
     * Getter de la liste de trophées du jest
     * 
     * @return
     */
 
    
    // Permet de faire un seul jest final : en ajoutant les trophées au jestPerso pour pouvoir compter le score final
    public List<Carte> getToutesLesCartes() {
		List<Carte> toutes = new ArrayList<>();
		toutes.addAll(this.cartes);
		
		return toutes;
	}
}
