# Documentation Javadoc - Projet Jest

## Vue d'ensemble

Ce document récapitule la documentation Javadoc complète du projet Jest, un jeu de cartes implémenté en Java avec interface graphique et console.

---

## Structure du Projet

### 📦 Package `jest_package1` - Modèle du jeu

#### Classes de Cartes

- **`Carte`** (abstraite)
  - Classe abstraite représentant une carte du jeu
  - Propriétés : couleur, valeur, visibilité
  - Implémente le pattern Visiteur pour le calcul des scores

- **`CarteCouleur`**
  - Représente une carte standard (pas un Joker)
  - Étend `Carte`
  - Accepte les visiteurs de score basés sur la couleur

- **`Joker`**
  - Représente le Joker spécial
  - Calcul de score particulier selon le context du Jest

- **`Couleur`** (énumération)
  - PIQUE ♠, TREFLE ♣, CARREAU ♦, COEUR ♥
  - ETOILE ☆, TRIANGLE ▲, SOLEIL ☼ (extension)
  - Chaque couleur a une force et un symbole

- **`Valeur`** (énumération)
  - AS(1), DEUX(2), TROIS(3), QUATRE(4)

#### Classes de Jeu

- **`Jest`**
  - Représente l'ensemble des cartes d'un joueur
  - Gère l'ajout et la suppression de cartes

- **`Pioche`**
  - Gère le deck de cartes
  - Initialise les cartes standards et d'extension
  - Fournit les méthodes de piocharge et mélange

- **`Offre`**
  - Représente une offre de deux cartes (visible et cachée)
  - Propriétaire : le joueur qui propose l'offre

- **`ChoixCarte`**
  - Encapsule le choix d'une carte par un joueur
  - Contient l'offre choisie et la carte sélectionnée

#### Joueurs

- **`Joueur`** (abstraite)
  - Classe abstraite de base pour tous les joueurs
  - Propriétés : nom, jest courant, jest personnel, offre courante
  - Méthodes abstraites : `faireOffre()`, `choisirCarte()`

- **`JoueurHumain`** (concrète)
  - Joueur contrôlé par un utilisateur via console
  - Interactif avec affichage et saisie

- **`JoueurHumainGUI`** (concrète)
  - Extension de `JoueurHumain` pour interface graphique
  - Utilise `CountDownLatch` pour synchroniser les threads
  - Supporte mode console et GUI

- **`JoueurVirtuel`** (concrète)
  - Bot utilisant une stratégie de jeu
  - Implémente `faireOffre()` et `choisirCarte()` selon la stratégie

#### Stratégies de Jeu

- **`Strategie`** (interface)
  - Définit le pattern Strategy pour les bots
  - Méthodes : `choisirCarte()`, `choisirCartesOffre()`, `evaluerOffre()`

- **`StrategieOffensive`**
  - Prend les cartes visibles les plus fortes
  - Montre ses meilleures cartes

- **`StrategieDefensive`**
  - Évite les cartes dangereuses
  - Choisit les cartes cachées
  - Cache ses meilleures cartes

- **`StrategieAleatoire`**
  - Décisions complètement aléatoires
  - Utile pour les tests

#### Règles du Jeu

- **`RegleJeu`** (interface)
  - Définit les méthodes pour les règles du jeu
  - Méthodes : `calculerValeurJest()`, `verifierConditionTrophee()`, etc.

- **`RegleStandard`**
  - Implémente les règles standards du Jest
  - Une carte cachée, une carte visible
  - Paires noires, Joker, conditions spéciales

- **`VarianteRapide`**
  - Limite le jeu à 3 manches
  - Utilise les mêmes règles que Standard

- **`RegleStrategique`**
  - Les deux cartes de l'offre sont visibles
  - Change complètement la stratégie du jeu

#### Calcul des Scores

- **`VisiteurScore`** (interface)
  - Pattern Visiteur pour calculer les scores
  - Méthodes pour chaque couleur : `visiterPique()`, `visiterTrefle()`, etc.

- **`CalculateurScoreStandard`**
  - Implémente le calcul des scores
  - Applique les bonus et pénalités selon les règles

#### Autres Classes

- **`Partie`** (Singleton)
  - Gère le déroulement d'une partie
  - Implémente `Observable` pour le pattern MVC
  - Méthodes : `jouerManche()`, `distribuerCartes()`, `determinerGagnants()`

- **`Jeu`** (principale)
  - Classe principale du jeu
  - Gère la configuration, l'initialisation et exécution
  - Supporte console, GUI et mode hybride
  - Sauvegarde/chargement de parties

- **`EtatPartie`** (énumération)
  - CONFIGURATION, EN_COURS, TERMINEE, SUSPENDUE

- **`TypeCondition`** (énumération)
  - Types de conditions pour les trophées
  - JOKER, MEILLEUR_JEST, LE_PLUS_DE_TYPE, etc.

- **`Trophee`**
  - Représente les bonus spéciaux
  - Vérifie les conditions et applique les effets

---

### 📺 Package `Vue` - Interface Utilisateur

- **`InterfaceGraphiqueJest`**
  - Interface graphique principale (Swing)
  - Implémente `Observer` pour le pattern MVC
  - Affichage des cartes, joueurs, offres et logs

- **`VueConsoleJest`**
  - Vue console pour affichage parallèle
  - Implémente `Observer`
  - Fonctionne en mode mixte avec la GUI

- **`GestionnaireImages`**
  - Charge et redimensionne les images des cartes
  - Gère plusieurs tailles de cartes
  - Support des images du dos pour cartes cachées

---

### 🎮 Package `Controleur`

- **`ControleurJest`**
  - Contrôleur MVC pour la GUI
  - Gère les interactions utilisateur
  - Lie la vue et le modèle

---

## Patterns de Conception Utilisés

### 1. **Singleton** (`Partie`)
- Une seule instance de Partie à la fois
- Méthodes `getInstance()` et `reinitialiser()`

### 2. **Observer** (Partie, Vue)
- Notification automatique des changements
- Pattern MVC pour la synchronisation

### 3. **Visiteur** (VisiteurScore, CalculateurScoreStandard)
- Calcul des scores découpé par couleur
- Flexibilité pour ajouter nouvelles règles

### 4. **Stratégie** (Strategie, Strategie*)
- Différentes stratégies pour les bots
- Facile d'ajouter de nouvelles stratégies

### 5. **Factory** (Jeu, Partie)
- Création d'objets dynamique selon options

### 6. **Modèle-Vue-Contrôleur** (MVC)
- Séparation claire entre logique, interface et contrôle
- Support console et GUI

---

## Flux Principal du Jeu

```
1. Jeu.main()
   ├─ Configuration (joueurs, règles, extension)
   ├─ Initialisation InterfaceGraphique (optionnel)
   └─ Jeu.demarrer()
      ├─ Partie.getInstance().initialiser()
      │  ├─ Pioche.initialiser()
      │  └─ Initialiser trophées
      └─ Boucle jeu:
         ├─ Partie.jouerManche()
         │  ├─ Distribuer cartes
         │  ├─ Créer offres
         │  └─ Jouer tours:
         │     ├─ Joueur.faireOffre()
         │     ├─ Joueur.choisirCarte()
         │     └─ Attribuer cartes
         ├─ Appliquer règles spéciales
         └─ Vérifier fin de partie
      ├─ Terminer partie
      └─ Afficher résultats
```

---

## Fichiers Documentés

### Jest Package (28 fichiers)
✅ Carte.java
✅ CarteCouleur.java
✅ Joker.java
✅ Couleur.java
✅ Valeur.java
✅ Jest.java
✅ Pioche.java
✅ Offre.java
✅ ChoixCarte.java
✅ Joueur.java
✅ JoueurHumain.java
✅ JoueurHumainGUI.java
✅ JoueurVirtuel.java
✅ Partie.java
✅ Jeu.java
✅ EtatPartie.java
✅ TypeCondition.java
✅ Trophee.java
✅ Strategie.java
✅ StrategieOffensive.java
✅ StrategieDefensive.java
✅ StrategieAleatoire.java
✅ RegleJeu.java
✅ RegleStandard.java
✅ VarianteRapide.java
✅ RegleStrategique.java
✅ VisiteurScore.java
✅ CalculateurScoreStandard.java
✅ TestDesFonctions.java

### Vue Package
✅ InterfaceGraphiqueJest.java
✅ VueConsoleJest.java
✅ GestionnaireImages.java

### Controleur Package
✅ ControleurJest.java

---

## Génération de la Javadoc

Pour générer la documentation Javadoc en HTML:

```bash
cd c:\Users\Utilisateur\Desktop\LO02\projet
javadoc -d docs -sourcepath src -subpackages jest_package1:Vue:Controleur
```

Les fichiers HTML seront générés dans le dossier `docs/`.

---

## Notes de Documentation

- Toutes les classes sont documentées avec descriptions claire
- Les méthodes publiques incluent `@param` et `@return`
- Les énumérations sont documentées avec les valeurs possibles
- Les patterns de conception sont explicitement mentionnés
- Les dépendances entre classes sont claires

---

## Auteurs et Version

- **Auteurs** : LO02 Project Team
- **Version** : 1.0
- **Date** : Janvier 2026

---
