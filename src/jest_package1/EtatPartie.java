package jest_package1;

/**
 * Énumération représentant les différents états possibles d'une partie de Jest.
 * 
 * Utilise une machine à états pour gérer le cycle de vie complet de la partie:
 * 
 * - CONFIGURATION: État initial où l'utilisateur configure les paramètres
 * (nombre de joueurs, règles, extensions, etc.)
 * 
 * - EN_COURS: État actif pendant le déroulement du jeu
 * (manches, tours, choix de cartes)
 * 
 * - TERMINEE: État final quand toutes les conditions de victoire
 * sont remplies (manches terminées)
 * 
 * - SUSPENDUE: État temporaire quand la partie est sauvegardée et mise en pause
 * (permet de reprendre plus tard)
 * 
 * @author David et Léna
 */
public enum EtatPartie {
	/** État de configuration  */
	CONFIGURATION,
	/** État de jeu en cours */
	EN_COURS,
	/** État de fin  */
	TERMINEE,
	/** État suspendu  */
	SUSPENDUE
}