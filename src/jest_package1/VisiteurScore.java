package jest_package1;

public interface VisiteurScore {
    int visiterPique(CarteCouleur carte, Jest jest);
    int visiterTrefle(CarteCouleur carte, Jest jest);
    int visiterCarreau(CarteCouleur carte, Jest jest);
    int visiterCoeur(CarteCouleur carte, Jest jest);
    int visiterJoker(Joker carte, Jest jest);
    int visiterExtension(CarteExtension carte, Jest jest);
    int calculerScore(Jest jest);
}
