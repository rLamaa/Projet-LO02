package jest_package1;

/**
 * Énumération représentant les différents états possibles d'une partie de Jest.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public enum EtatPartie {
	/** État de configuration : choix des paramètres, joueurs et extensions */
	CONFIGURATION,
	/** État de jeu en cours : la partie est active */
	EN_COURS,
	/** État de fin : la partie est terminée */
	TERMINEE,
	/** État suspendu : la partie a été sauvegardée et arrêtée */
	SUSPENDUE
}