package jest_package1;

import java.util.List;

// Variante Stratégique: Les offres sont visibles
public class VarianteStrategique implements RegleJeu {
    private static final long serialVersionUID = 1L;
    private boolean offresVisibles = true;

    @Override
    public int calculerValeurJest(Jest jest) {
        CalculateurScoreStandard calc = new CalculateurScoreStandard();
        return calc.calculerScore(jest);
    }

    @Override
    public boolean verifierConditionTrophee(Jest jest, Carte carte) {
        return true;
    }

    @Override
    public List<Joueur> determinerOrdreJeu(List<Offre> offres) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerOrdreJeu(offres);
    }

    @Override
    public void appliquerReglesSpeciales(Jeu jeu) {
        if (offresVisibles) {
            System.out.println("📋 Mode OFFRES VISIBLES: Toutes les cartes sont révélées!");
        }
    }

    public Joueur determinerGagnantTrophee(List<Joueur> joueurs, Carte trophee) {
        RegleStandard standard = new RegleStandard();
        return standard.determinerGagnantTrophee(joueurs, trophee);
    }

    public boolean sontOffresVisibles() {
        return offresVisibles;
    }

	@Override
	public Offre creerOffre(Joueur joueur, Carte c1, Carte c2) {
		// TODO Auto-generated method stub
		return null;
	}
}