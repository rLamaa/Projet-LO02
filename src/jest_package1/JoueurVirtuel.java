package jest_package1;

import java.util.List;

/**
 * Joueur virtuel utilisant une stratégie pour prendre ses décisions
 */
public class JoueurVirtuel extends Joueur {
    private static final long serialVersionUID = 1L;
    private Strategie strategie;

    public JoueurVirtuel(String nom) {
        super(nom);
        // Stratégie par défaut: offensive
        this.strategie = new StrategieOffensive();
    }

    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public Offre faireOffre(boolean offresVisibles) {
        List<Carte> cartes = jest.getCartes();
        if (cartes.size() < 2) {
            throw new IllegalStateException("Pas assez de cartes pour faire une offre");
        }

        Carte c1 = cartes.remove(0);
        Carte c2 = cartes.remove(0);
        
        if(offresVisibles) {
        	// Variante stratégique : pas de stratégie pour les bot car les deux cartes sont visibles
        	this.offreCourante = new Offre(c1, c2, this);
        	System.out.println("[" + nom + "] (Bot) Offre créée - Carte 1 : " + c1 + " | Carte 2 : " + c2);
        } else {
        	// Jeu standard : Utiliser la stratégie pour choisir quelle carte cacher
            Offre offre = strategie.choisirCartesOffre(c1, c2);
            this.offreCourante = new Offre(offre.getCarteCachee(), offre.getCarteVisible(), this);

            System.out.println("[" + nom + "] (Bot) Offre créée - Visible: " +
                    this.offreCourante.getCarteVisible());

        }
        
        return this.offreCourante;
    }

    @Override
    public ChoixCarte choisirCarte(List<Offre> offres) {
        // Filtrer les offres complètes qui ne sont pas les nôtres
        List<Offre> offresDisponibles = new java.util.ArrayList<>();
        for (Offre o : offres) {
            if (o.estComplete() && o.getProprietaire() != this) {
                offresDisponibles.add(o);
            }
        }

        if (offresDisponibles.isEmpty()) {
            return null;
        }

        // Utiliser la stratégie pour choisir
        ChoixCarte choix = strategie.choisirCarte(offresDisponibles, jest);

        if (choix != null) {
            System.out.println("[" + nom + "] (Bot) choisit la carte " +
                    choix.getCarte() + " de l'offre de " +
                    choix.getOffre().getProprietaire().getNom());
        }

        return choix;
    }

    /**
     * Setter de la stratégie du robot
     * 
     * @param strategie la stratégie que l'on souhaite qu'il adopte
     */
    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

    /**
     * Getter de la stratégie du robot
     * 
     * @return
     */
    public Strategie getStrategie() {
        return strategie;
    }
}