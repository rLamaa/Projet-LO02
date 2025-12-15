package jest_package1;

import java.io.Serializable;

public class CarteExtension extends Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    private String effetSpecial;
    private String description;

    public int accepter(VisiteurScore visiteur, Jest jest) {
        return 0;
    }

    public void appliquerEffet(Jest jest) {
    }
}
