package jest_package1;

public class CalculateurScoreStandard implements VisiteurScore {
    public int visiterPique(CarteCouleur c, Jest j) { return 0; }
    public int visiterTrefle(CarteCouleur c, Jest j) { return 0; }
    public int visiterCarreau(CarteCouleur c, Jest j) { return 0; }
    public int visiterCoeur(CarteCouleur c, Jest j) { return 0; }
    public int visiterJoker(Joker c, Jest j) { return 0; }
    public int visiterExtension(CarteExtension c, Jest j) { return 0; }
    public int calculerScore(Jest j) { return 0; }

    private int calculerBonusPairesNoires(Jest j) { return 0; }
    private int calculerValeurAs(CarteCouleur c, Jest j) { return 0; }
    private int compterCoeurs(Jest j) { return 0; }
}
