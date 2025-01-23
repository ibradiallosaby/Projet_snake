Projet Snake - Design Patterns (UE Design Pattern)

1) Design Patterns utilisés :

Singleton : Utilisé pour gérer l'instance du jeu Snake (pour s'assurer qu'il n'y a qu'une seule instance de SnakeGame).


Factory : Utilisé pour la création des serpents. En utilisant une SnakeFactory, on peut facilement changer le type de serpent ou ajouter de nouveaux types sans modifier la logique de création dans la classe principale.


Strategy : Appliqué aux comportements des serpents, permettant de changer la stratégie de jeu à tout moment, comme entre une IA ou un joueur humain, sans modifier directement les classes de serpent.


Observer : Utilisé pour mettre à jour l'interface graphique en fonction des changements dans l'état du jeu (ex. : changement de score, de vie, ou de niveau).


MVC (Modèle-Vue-Contrôleur) : Utilisé pour séparer la logique de l'application (modèle), l'interface graphique (vue) et les actions de l'utilisateur (contrôleur). Cela améliore la modularité du code et facilite les tests ainsi que les modifications futures. Le modèle représente l'état du jeu (les serpents, le score, etc.), la vue affiche ces informations à l'utilisateur et le contrôleur gère les entrées de l'utilisateur (commandes du jeu, changement de stratégie, etc.).

État : Appliqué pour gérer les différentes phases du jeu (comme le mode "jouer", "pause", "fin de jeu"). Chaque état modifie le comportement du jeu, permettant au programme de s'adapter aux actions de l'utilisateur et de changer de mode tout au long de la partie. Par exemple, pendant le jeu, les actions de serpent sont autorisées, mais en pause, elles sont désactivées.

2) Fonctionnalités nouvelles implémentées :


Système de niveaux de difficulté : La difficulté du jeu augmente automatiquement après chaque pomme mangée, augmentant la vitesse du serpent avec chaque niveau.

Interface graphique améliorée : Création d'un menu interactif pour choisir la disposition du jeu et configurer la stratégie de jeu (IA vs Humain) pour chaque serpent.

Changement dynamique de stratégie : À tout moment, un serpent peut passer d'une stratégie humaine à une IA et vice-versa, permettant une plus grande flexibilité dans le gameplay.

Gestion de la vitesse et du score : Les serpents peuvent manger des pommes pour augmenter leur taille et leur score, et la vitesse du jeu est ajustée à chaque niveau de difficulté.

Interface utilisateur avec des couleurs personnalisées : Les options de menu sont personnalisées, et les éléments de menu tels que les choix de stratégie peuvent changer la couleur et le comportement graphique pour améliorer l'expérience utilisateur.

Stratégie AIRandom : Cette stratégie de l'IA choisit de manière aléatoire une direction parmi celles possibles (haut, bas, gauche, droite) à chaque tour, sans tenir compte de l'état actuel du jeu ou des éléments présents.

Stratégie IAAdvanced : Contrairement à l'IA ARandom, cette stratégie utilise une logique avancée pour déplacer le serpent en fonction de son environnement. Elle cherche à éviter les obstacles, lier l'objectif (par exemple, manger la pomme) et optimiser ses déplacements. Cependant, elle ne prend pas en compte la position des items spécifiques comme les pommes ou autres éléments interactifs du jeu.

Stratégie IAAdvancedPlus : Cette stratégie est une évolution de IAAdvanced, où l'IA connaît également la position des items tels que les pommes. Grâce à cette connaissance, l'IA ajuste ses déplacements pour maximiser ses chances d'atteindre des objectifs précis, comme la collecte des pommes.