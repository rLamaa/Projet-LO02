package jest_package1;

public class TestDesFonctions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Carte c1 = new CarteCouleur(Couleur.PIQUE, Valeur.TROIS);
		Carte c2 = new CarteCouleur(Couleur.COEUR, Valeur.AS);
		Carte j = new Joker();

		System.out.println(c1); // 3♠
		System.out.println(c2); // 1♥
		System.out.println(j); // Joker
		Pioche p = new Pioche();
		p.initialiser(false);
		// while (!p.estVide()) System.out.println(p.piocher());
		JoueurHumain j1 = new JoueurHumain("Léna");
		System.out.println(j1.getNom());
		System.out.println(j1.getJest().getCartes());
		j1.getJest().getCartes().add(c1);
		j1.getJest().getCartes().add(c2);

		System.out.println("");
		j1.faireOffre();

		System.out.println("Cachée " + j1.getOffreCourante().getCarteCachee());
		System.out.println("Visible " + j1.getOffreCourante().getCarteVisible());

	}

}
