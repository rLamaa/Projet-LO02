# 📑 Index Complet de la Javadoc - Jest

## Toutes les Classes et Interfaces

### A

**`CalculateurScoreStandard`** ⭐ [jest_package1]
- Classe implémentant le pattern Visiteur pour les calculs de score
- Responsabilité : Calculer les points basés sur les couleurs et valeurs
- Implémente : `VisiteurScore`
- Clés : Scoring, Visiteur, Règles

---

**`CarteCouleur`** [jest_package1]
- Représente une carte standard (pas un Joker)
- Responsabilité : Implémenter le comportement des cartes ordinaires
- Étends : `Carte`
- Clés : Cartes, Couleur, Valeur

---

**`Carte`** (abstraite) [jest_package1]
- Classe abstraite de base pour toutes les cartes
- Responsabilité : Définir l'interface commune des cartes
- Méthodes abstraites : `accepter()`, `toString()`
- Clés : Cartes, Pattern Visiteur

---

**`ChoixCarte`** [jest_package1]
- Encapsule le choix d'une carte par un joueur
- Responsabilité : Stocker offre + carte choisie
- Implémente : `Serializable`
- Clés : Choix, Offre, Carte

---

**`Couleur`** (énumération) [jest_package1]
- Énumération des 7 couleurs de cartes
- Valeurs : PIQUE, TREFLE, CARREAU, COEUR, ETOILE, TRIANGLE, SOLEIL
- Méthodes : `getForce()`, `getSymbole()`
- Clés : Couleurs, Symboles Unicode

---

**`ControleurJest`** [Controleur]
- Contrôleur MVC pour l'interface graphique
- Responsabilité : Lier vue (GUI) et modèle (Partie)
- Gère : Événements, Actions utilisateur
- Clés : MVC, Swing, Événements

---

### E

**`EtatPartie`** (énumération) [jest_package1]
- Énumération des états possible d'une partie
- Valeurs : CONFIGURATION, EN_COURS, TERMINEE, SUSPENDUE
- Clés : États, Sauvegarde

---

### G

**`GestionnaireImages`** [Vue]
- Gestionnaire des images de cartes
- Responsabilité : Charger et redimensionner les images
- Supporte : Plusieurs tailles, Dos de cartes, Cache
- Clés : Images, Ressources, GUI

---

### I

**`InterfaceGraphiqueJest`** [Vue]
- Interface graphique principale (Swing)
- Responsabilité : Afficher l'état du jeu et recevoir entrées utilisateur
- Implémente : `Observer`, `Serializable`
- Clés : GUI, Swing, Panels, Logs

---

### J

**`Jest`** [jest_package1]
- Classe représentant l'ensemble de cartes d'un joueur
- Responsabilité : Gérer une collection de cartes
- Implémente : `Serializable`
- Clés : Collection, Cartes, Joueur

---

**`Jeu`** ⭐ [jest_package1]
- Classe principale du jeu (point d'entrée)
- Responsabilité : Orchestrer le jeu complet
- Gère : Configuration, GUI/Console, Sauvegarde
- Clés : Main, Orchestration, Sauvegarde

---

**`Joker`** [jest_package1]
- Représente la carte Joker spéciale
- Responsabilité : Comportement spécial du Joker
- Étends : `Carte`
- Clés : Joker, Spécial, Scoring

---

**`Joueur`** (abstraite) [jest_package1]
- Classe abstraite de base pour tous les joueurs
- Responsabilité : Définir l'interface du joueur
- Méthodes abstraites : `faireOffre()`, `choisirCarte()`
- Clés : Joueur, Interface, Abstraction

---

**`JoueurHumain`** [jest_package1]
- Joueur contrôlé par utilisateur (console)
- Responsabilité : Prendre décisions via console
- Étends : `Joueur`
- Clés : Humain, Interactif, Console

---

**`JoueurHumainGUI`** [jest_package1]
- Joueur humain avec support interface graphique
- Responsabilité : Supporter console ET GUI seamlessly
- Étends : `JoueurHumain`
- Utilise : `CountDownLatch` (synchronisation)
- Clés : GUI, Thread-safe, Dual-mode

---

**`JoueurVirtuel`** [jest_package1]
- Bot (joueur virtuel) utilisant une stratégie
- Responsabilité : Jouer automatiquement selon une stratégie
- Étends : `Joueur`
- Utilise : `Strategie`
- Clés : Bot, IA, Stratégie

---

### O

**`Offre`** [jest_package1]
- Représente une offre de deux cartes
- Responsabilité : Encapsuler une offre (visible + cachée)
- Propriétaire : Le joueur qui propose l'offre
- Clés : Offre, Cartes, Propriétaire

---

### P

**`Partie`** ⭐ (Singleton) [jest_package1]
- Gère le déroulement d'une partie de Jest
- Responsabilité : Orchestrer les manches et tours
- Implémente : `Observable`, `Serializable`
- Patterns : Singleton, Observer
- Clés : Partie, Manche, Observable

---

**`Pioche`** [jest_package1]
- Gère le deck de cartes
- Responsabilité : Initialiser, mélanger, distribuer cartes
- Implémente : `Serializable`
- Structure : `Stack<Carte>`
- Clés : Pioche, Deck, Cartes

---

### R

**`RegleJeu`** (interface) [jest_package1]
- Interface définissant les règles du jeu
- Méthodes : `calculerValeurJest()`, `verifierConditionTrophee()`, etc.
- Implémente : `Serializable`
- Clés : Règles, Interface, Calcul

---

**`RegleStandard`** [jest_package1]
- Implémentation des règles standards
- Responsabilité : Appliquer les règles classiques du Jest
- Implémente : `RegleJeu`
- Clés : Règles, Standard, Scoring

---

**`RegleStrategique`** [jest_package1]
- Variante où toutes les cartes sont visibles
- Responsabilité : Changer la dynamique du jeu
- Implémente : `RegleJeu`
- Clés : Stratégique, Visible, Variante

---

### S

**`Strategie`** (interface) [jest_package1]
- Interface définissant une stratégie de bot
- Méthodes : `choisirCarte()`, `choisirCartesOffre()`, `evaluerOffre()`
- Implémente : `Serializable`
- Clés : Stratégie, Pattern Strategy, Bot

---

**`StrategieAleatoire`** [jest_package1]
- Stratégie avec décisions aléatoires
- Responsabilité : Choisir aléatoirement pour tester
- Implémente : `Strategie`
- Clés : Aléatoire, Test, Imprévisible

---

**`StrategieDefensive`** [jest_package1]
- Stratégie défensive (évite les pièges)
- Responsabilité : Minimiser les risques
- Implémente : `Strategie`
- Clés : Défensive, Sûre, Risque

---

**`StrategieOffensive`** [jest_package1]
- Stratégie offensive (prend cartes fortes)
- Responsabilité : Maximiser les gains
- Implémente : `Strategie`
- Clés : Offensive, Agressif, Gain

---

### T

**`TestDesFonctions`** [jest_package1]
- Classe de test pour vérifier les composants
- Responsabilité : Tests unitaires et de fonctionnalité
- Clés : Tests, Débogage

---

**`Trophee`** [jest_package1]
- Représente les bonus spéciaux
- Responsabilité : Gérer les conditions et effets du trophée
- Clés : Trophée, Bonus, Conditions

---

**`TypeCondition`** (énumération) [jest_package1]
- Énumération des types de conditions pour trophées
- Valeurs : JOKER, MEILLEUR_JEST, LE_PLUS_DE_TYPE, etc.
- Clés : Conditions, Trophée, Types

---

### V

**`Valeur`** (énumération) [jest_package1]
- Énumération des valeurs de cartes
- Valeurs : AS(1), DEUX(2), TROIS(3), QUATRE(4)
- Clés : Valeurs, Numérique

---

**`VarianteRapide`** [jest_package1]
- Variante avec 3 manches maximum
- Responsabilité : Accélérer le jeu
- Implémente : `RegleJeu`
- Clés : Rapide, Manches, Variante

---

**`VisiteurScore`** (interface) [jest_package1]
- Interface du pattern Visiteur pour scoring
- Méthodes : `visiterPique()`, `visiterTrefle()`, etc.
- Implémente : `Serializable`
- Clés : Visiteur, Scoring, Pattern

---

**`VueConsoleJest`** [Vue]
- Vue console pour mode mixte
- Responsabilité : Afficher les mises à jour en console
- Implémente : `Observer`
- Clés : Console, Observer, Logs

---

## 📊 Classification par Domaine

### 🃏 Cartes (5)
- `Carte` (abstraite)
- `CarteCouleur`
- `Joker`
- `Couleur` (enum)
- `Valeur` (enum)

### 👥 Joueurs (4)
- `Joueur` (abstraite)
- `JoueurHumain`
- `JoueurHumainGUI`
- `JoueurVirtuel`

### 🎯 Stratégies (4)
- `Strategie` (interface)
- `StrategieOffensive`
- `StrategieDefensive`
- `StrategieAleatoire`

### ⚙️ Règles (4)
- `RegleJeu` (interface)
- `RegleStandard`
- `VarianteRapide`
- `RegleStrategique`

### 🎲 Scoring (2)
- `VisiteurScore` (interface)
- `CalculateurScoreStandard`

### 🕹️ Contrôle (2)
- `Partie` (Singleton)
- `Jeu` (Main)

### 💾 Collections (4)
- `Jest`
- `Pioche`
- `Offre`
- `ChoixCarte`

### 📺 Interface Utilisateur (3)
- `InterfaceGraphiqueJest`
- `VueConsoleJest`
- `GestionnaireImages`

### 🎮 Contrôleur (1)
- `ControleurJest`

### 📝 Utilitaires (3)
- `EtatPartie` (enum)
- `TypeCondition` (enum)
- `Trophee`

### 🧪 Tests (1)
- `TestDesFonctions`

---

## 🔗 Dépendances Principales

```
Jeu
├── Partie (Singleton)
│   ├── Joueur[]
│   │   ├── JoueurHumain
│   │   ├── JoueurHumainGUI
│   │   └── JoueurVirtuel
│   │       └── Strategie
│   ├── Pioche
│   │   └── Carte
│   │       ├── CarteCouleur
│   │       └── Joker
│   ├── RegleJeu
│   │   ├── RegleStandard
│   │   ├── VarianteRapide
│   │   └── RegleStrategique
│   └── Offre[]
│       └── Carte
├── InterfaceGraphiqueJest (Observer)
├── VueConsoleJest (Observer)
└── ControleurJest
    ├── InterfaceGraphiqueJest
    └── JoueurHumainGUI
```

---

## 🎓 Ordre de Lecture Recommandé

### Pour Comprendre l'Architecture
1. `Jeu` - Point d'entrée
2. `Partie` - Logique main
3. `Joueur` - Interface joueur
4. `RegleJeu` - Système de règles

### Pour Comprendre le Gameplay
1. `Jest` - Collection de cartes
2. `Pioche` - Distribution
3. `Offre` - Échange
4. `ChoixCarte` - Sélection

### Pour Comprendre les Bots
1. `Strategie` - Interface
2. `StrategieOffensive` - Exemple 1
3. `StrategieDefensive` - Exemple 2
4. `JoueurVirtuel` - Utilisation

### Pour Comprendre le Scoring
1. `VisiteurScore` - Interface
2. `CalculateurScoreStandard` - Implémentation
3. `RegleStandard` - Application

---

## ✅ Légende

- ⭐ = Classe majeure/Point d'entrée
- (abstraite) = Classe abstraite
- (interface) = Interface
- (enum) = Énumération
- [package] = Package Java

---

**Index généré le** : 16 Janvier 2026
**Version** : 1.0
**Total Classes/Interfaces** : 33

