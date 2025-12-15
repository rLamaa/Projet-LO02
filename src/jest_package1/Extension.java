package jest_package1;

import java.io.Serializable;
import java.util.List;

public class Extension implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private List<CarteExtension> nouvellesCartes;
    private boolean active;

    public void ajouterCarte(CarteExtension c) {
    }

    public List<CarteExtension> getCartes() {
        return null;
    }

    public boolean estActive() {
        return active;
    }

    public void activer() {
    }

    public void desactiver() {
    }
}