package jest_package1;

/**
 * Énumération représentant les différents types de conditions pour les
 * trophées.
 * 
 * Définit les critères d'attribution des trophées qui récompensent
 * les joueurs pour différents exploits:
 * 
 * - JOKER: Qui possède le Joker (carte très importante)
 * - MEILLEUR_JEST: Qui a le Jest le plus fort (tous les Joker compris)
 * - MEILLEUR_JEST_SANS_JOKER: Qui a le Jest le plus fort sans le Joker
 * - LE_MOINS_DE_TYPE: Qui a le moins de cartes d'une couleur donnée
 * - LE_PLUS_DE_TYPE: Qui a le plus de cartes d'une couleur donnée
 * - LE_PLUS_DE_NUMERO: Qui a le plus de cartes d'une valeur donnée
 * 
 * Chaque trophée est associé à une ou plusieurs conditions qui déterminent
 * quel joueur le remporte.
 * 
 * @author David et Léna
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