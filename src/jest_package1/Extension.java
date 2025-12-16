
package jest_package1;

import java.io.Serializable;
import java.util.*;

/**
 * Système d'extension avec nouvelles cartes
 */
public class Extension implements Serializable {
    /**
     * Identifiant de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;
    private String nom;
    private List<CarteExtension> nouvellesCartes;
    private boolean active;

    /**
     * Constructeur de la classe Extension
     * 
     * @param nom
     */
    public Extension(String nom) {
        this.nom = nom;
        this.nouvellesCartes = new ArrayList<>();
        this.active = false;
    }

    /**
     * Fonction permettant d'ajouter une carte Extension
     * 
     * @param c
     */
    public void ajouterCarte(CarteExtension c) {
        nouvellesCartes.add(c);
    }

    /**
     * Getter de la liste de carte d'extension
     * 
     * @return
     */
    public List<CarteExtension> getCartes() {
        return nouvellesCartes;
    }

    /**
     * Fonction permettant de voir si les extensions sont activées
     * 
     * @return
     */
    public boolean estActive() {
        return active;
    }

    /**
     * Fonction permettant d'activer les extensions
     */
    public void activer() {
        this.active = true;
    }

    /**
     * Fonction permettant desactiver les extensions
     */
    public void desactiver() {
        this.active = false;
    }

    /**
     * Getter du nom de l'Extension
     * 
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Crée une extension avec des effets prédéfinies
     * 
     * @return
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