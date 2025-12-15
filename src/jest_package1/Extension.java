
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
}