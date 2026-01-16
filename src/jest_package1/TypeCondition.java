package jest_package1;

/**
 * Énumération représentant les différents types de conditions pour les
 * trophées.
 * Ces conditions déterminent les critères pour attribuer les trophées aux
 * joueurs.
 * 
 * @author LO02 Project Team
 * @version 1.0
 */
public enum TypeCondition {
	/** Trophée: Qui a le Joker */
	JOKER,
	/** Trophée: Meilleur Jest avant l'attribution des trophées */
	MEILLEUR_JEST,
	/** Trophée: Meilleur Jest sans Joker */
	MEILLEUR_JEST_SANS_JOKER,
	/** Trophée: Qui a le moins de cartes d'un type donné */
	LE_MOINS_DE_TYPE,
	/** Trophée: Qui a le plus de cartes d'un type donné */
	LE_PLUS_DE_TYPE,
	/** Trophée: Qui a le plus de cartes d'une valeur donnée */
	LE_PLUS_DE_NUMERO
}