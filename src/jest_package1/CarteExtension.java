package jest_package1;

import java.util.HashSet;
import java.util.Set;

public class CarteExtension extends Carte {
    private static final long serialVersionUID = 1L;

    private EffetExtension effet;
    private String description;

    public CarteExtension(EffetExtension effet, String description) {
        this.effet = effet;
        this.description = description;
    }

    public EffetExtension getEffet() {
        return effet;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int accepter(VisiteurScore visiteur, Jest jest) {
        switch (effet) {

            case DOUBLEMENT:
                return effetDoublement(jest);

            case INVERSION:
                return effetInversion(jest);

            case MIROIR:
                return effetMiroir(jest);

            default:
                return 0;
        }
    }

    /* ================= EFFETS ================= */

    private int effetDoublement(Jest jest) {
        int bonus = 0;
        for (Carte c : jest.getCartes()) {
            if (c instanceof CarteCouleur cc &&
                    cc.getCouleur() == Couleur.PIQUE) {
                bonus += cc.getValeurNumerique();
            }
        }
        return bonus;
    }

    private int effetInversion(Jest jest) {
        int bonus = 0;
        for (Carte c : jest.getCartes()) {
            if (c instanceof CarteCouleur cc &&
                    cc.getCouleur() == Couleur.CARREAU) {
                bonus += 2 * cc.getValeurNumerique();
            }
        }
        return bonus;
    }

    private int effetMiroir(Jest jest) {
        Set<Couleur> couleurs = new HashSet<>();

        for (Carte c : jest.getCartes()) {
            if (c instanceof CarteCouleur cc) {
                couleurs.add(cc.getCouleur());
            }
        }

        return couleurs.size() == 4 ? 3 : 0;
    }

    @Override
    public String toString() {
        return "⭐ " + effet;
    }
}
