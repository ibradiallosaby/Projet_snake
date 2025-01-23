package model;

import agent.Snake;
import factory.SnakeFactory;
import item.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import javax.swing.Timer;
import utils.AgentAction;
import utils.FeaturesItem;
import utils.FeaturesSnake;
import utils.GameFeatures;
import utils.GameState;
import utils.ItemType;
import utils.Position;

public class SnakeGame extends Game {

    public static final int TIME_INVINCIBLE = 20;
    public static final int TIME_SICK = 20;

    double probSpecialItem = 1;
    private static SnakeGame instance;

    private ArrayList<Snake> snakes;
    private ArrayList<Item> items;

    InputMap inputMap;
    private int sizeX;
    private int sizeY;
    private String layout;
    List<String> players; 
    Random rand = new Random();
    private Timer gameTimer ;
    private Map<Integer, AgentAction> playerActions;
    private int difficultyLevel = 1; 
    private int applesEaten = 0;  
    private final int difficultyThreshold = 3;  
    private int gameSpeed = 200;  


// Méthode pour augmenter la difficulté
private void increaseDifficulty() {
    applesEaten++;  

    // Augmenter le niveau de difficulté tous les `difficultyThreshold` pommes mangées
    if (applesEaten % difficultyThreshold == 0) {
        difficultyLevel++;  
       // System.out.println("Niveau de difficulté : " + difficultyLevel);
        

        // Augmenter la vitesse à chaque nouveau niveau
        increaseGameSpeed();
    }
}

// Méthode pour modifier la vitesse du jeu
private void increaseGameSpeed() {
    switch (difficultyLevel) {
        case 2:
            gameSpeed = 150;  
            break;
        case 3:
            gameSpeed = 100;
            break;
        case 4:
            gameSpeed = 80;
            break;
        case 5:
            gameSpeed = 50;
            break;
        default:
            if (difficultyLevel > 5) {
                gameSpeed = 20;  // Très rapide
            }
            break;
    } 

    // Réajuster la vitesse du jeu (en utilisant un Timer, par exemple)
    if (gameTimer != null) {
        gameTimer.setDelay(gameSpeed);
    }
}



    
    private SnakeGame(int maxTurn, InputMap inputMap, List<String> players) {

    super(maxTurn);
    this.inputMap = inputMap;
    this.players = players;
    playerActions = new HashMap<>(); 
    this.state = GameState.STARTING; 

    }

    public static SnakeGame getInstance(int maxTurn, InputMap inputMap, List<String> players) {
        
        if (instance == null) {
            synchronized (SnakeGame.class) {
                if (instance == null) {
                    instance = new SnakeGame(maxTurn,inputMap,players); // Crée l'instance une seule fois
                }
            }
        }
        return instance; // Retourne l'instance unique de SnakeGame
    }

    

    // Une méthode pour enregistrer l'action d'un joueur
    public void setPlayerAction(int playerId, AgentAction action) {
        playerActions.put(playerId, action);
    }

    // Une méthode pour récupérer l'action d'un joueur
    public AgentAction getPlayerAction(int playerId) {
        return playerActions.getOrDefault(playerId, AgentAction.MOVE_DOWN); // Retourne MOVE_DOWN par défaut
    }
    public void initializeGame() {
		this.walls = inputMap.get_walls().clone();

		SnakeFactory snakeFactory = new SnakeFactory();

		ArrayList<FeaturesSnake> startSnakes = inputMap.getStart_snakes();
		ArrayList<FeaturesItem> startItems = inputMap.getStart_items();

		this.sizeX = inputMap.getSizeX();
		this.sizeY = inputMap.getSizeY();

		snakes = new ArrayList<>();
		items = new ArrayList<>();

		int id = 0;

		ArrayList<Integer> usedIds = new ArrayList<>();
		for (int i = 0; i < startSnakes.size(); ++i) {
			FeaturesSnake featuresSnake = startSnakes.get(i);
			if (i < players.size()) {
				int playerId = i ;
				snakes.add(snakeFactory.createSnake(featuresSnake, playerId));
				usedIds.add(playerId);
			} else {
				while (usedIds.contains(id)) {
					id++;
				}
				snakes.add(snakeFactory.createSnake(featuresSnake, id, "IAavancedplus"));
				usedIds.add(id);
			}
		}

		for (FeaturesItem featuresItem : startItems) {
			items.add(new Item(featuresItem.getX(), featuresItem.getY(), featuresItem.getItemType()));
		}
	}
    @Override
    public void takeTurn() {
        ListIterator<Snake> iterSnakes = snakes.listIterator();
    
        while (iterSnakes.hasNext()) {
            Snake snake = iterSnakes.next();
            if (snake.isAlive()) {
                AgentAction agentAction = playSnake(snake);
    
                if (isLegalMove(snake, agentAction)) {
                    moveSnake(agentAction, snake);
                } else {
                    moveSnake(snake.getLastAction(), snake);
                }
                snake.increaseScore(10);
            }
        }
    
        checkSnakeEaten();
        checkWalls();
    
        boolean isAppleEaten = checkItemFound();
    
        if (isAppleEaten) {
            addRandomApple();
            double r = rand.nextDouble();
    
            if (r < probSpecialItem) {
                addRandomItem();
            }
        }
        updateSnakeTimers();
    
        // Vérifie si des serpents doivent être réinitialisés
        handleDeadSnakes();
    }
    
    public void handleDeadSnakes() {
        for (Snake snake : snakes) {
            if (!snake.isAlive()) {
                if (snake.getLives() > 1) {
                    snake.setLives(snake.getLives() - 1);
                    resetSnakePosition(snake);
                    //System.out.println("Le serpent " + snake.getId() + " a perdu une vie. Vies restantes : " + snake.getLives());
                } else {
                  //  System.out.println("Le serpent " + snake.getId() + " est définitivement mort.");
                }
            }
        }
    }
    

    public boolean isLegalMove(Snake snake, AgentAction action) {

        
        return !(snake.getSize() > 1
                && ((snake.getLastAction() == AgentAction.MOVE_DOWN && action == AgentAction.MOVE_UP) ||
                    (snake.getLastAction() == AgentAction.MOVE_UP && action == AgentAction.MOVE_DOWN) ||
                    (snake.getLastAction() == AgentAction.MOVE_LEFT && action == AgentAction.MOVE_RIGHT) ||
                    (snake.getLastAction() == AgentAction.MOVE_RIGHT && action == AgentAction.MOVE_LEFT)));
    }

    @Override
    public boolean gameContinue() {
        for (Snake snake : snakes) {
            if (snake.isAlive() || snake.getLives() > 0) {
                return true;
            }
        }
        return false;
    }
    

    @Override
public void gameOver() {
    System.out.println("Le jeu est terminé!");
}
public void resetGame() {
    // Réinitialisation des serpents
    this.state = GameState.STARTING ;
    this.snakes.clear();
    this.items.clear();
    setDifficultyLevel(1);


    // Réinitialiser l'état du jeu
    this.state = GameState.STARTING;

    // Réinitialiser les serpents et les items
    initializeGame();
}


    public void addRandomApple() {
        boolean notPlaced = true;
    
        while (notPlaced) {
            int x = rand.nextInt(this.inputMap.getSizeX()); // Taille X du plateau
            int y = rand.nextInt(this.inputMap.getSizeY()); // Taille Y du plateau
    
            // Vérifie que la position n'est pas un mur, un serpent ou un autre item
            if (!this.walls[x][y] && !isSnake(x, y) && !isItem(x, y)) {
                this.items.add(new Item(x, y, ItemType.APPLE));
                notPlaced = false; // Position trouvée
            }
        }
    }
    

    public void addRandomItem() {
        int r = rand.nextInt(3); // Génère un type d'item aléatoire
        ItemType itemType = switch (r) {
            case 0 -> ItemType.BOX;
            case 1 -> ItemType.SICK_BALL;
            case 2 -> ItemType.INVINCIBILITY_BALL;
            default -> null;
        };
    
        boolean notPlaced = true;
    
        while (notPlaced) {
            int x = rand.nextInt(this.inputMap.getSizeX());
            int y = rand.nextInt(this.inputMap.getSizeY());
    
            // Vérifie la validité de la position
            if (!this.walls[x][y] && !isSnake(x, y) && !isItem(x, y)) {
                this.items.add(new Item(x, y, itemType));
                notPlaced = false; // Position trouvée
            }
        }
    }
    
    public boolean isSnake(int x, int y) {
        for (Snake snake : snakes) {

            for (Position pos : snake.getPositions()) {

                if (pos.getX() == x && pos.getY() == y) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isItem(int x, int y) {

        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                return true;
            }
        }
        return false;
    }

    

    public boolean checkItemFound() {
        ListIterator<Item> iterItem = items.listIterator();
        boolean isAppleEaten = false;
    
        // Itérer sur chaque item (pomme, boîte, etc.)
        while (iterItem.hasNext()) {
            Item item = iterItem.next();
            // Vérifier chaque serpent pour voir s'il a mangé un item
            for (Snake snake : snakes) {
                if (snake.getSickTimer() < 1) {  // S'assurer que le serpent n'est pas malade
                    int x = snake.getPositions().get(0).getX();
                    int y = snake.getPositions().get(0).getY();
    
                    // Vérifier si l'item est à la même position que la tête du serpent
                    if (item.getX() == x && item.getY() == y) {
                        iterItem.remove();  // Supprimer l'item de la liste
    
                        if (item.getItemType() == ItemType.APPLE) {
                            // Le serpent mange une pomme
                            increaseSizeSnake(snake);  // Augmenter la taille du serpent
                            snake.increaseScore(100);  // Augmenter le score
                            isAppleEaten = true;
    
                            // Augmenter la difficulté à chaque pomme mangée
                            increaseDifficulty();  // Mettre à jour la difficulté
                        }
    
                        if (item.getItemType() == ItemType.BOX) {
                            // Boîte avec un effet aléatoire (invincibilité ou maladie)
                            double r = rand.nextDouble();
                            if (r < 0.5) {
                                snake.setInvincibleTimer(TIME_INVINCIBLE);
                            } else {
                                snake.setSickTimer(TIME_SICK);
                            }
                        }
    
                        if (item.getItemType() == ItemType.SICK_BALL) {
                            // Ballon de maladie
                            snake.setSickTimer(SnakeGame.TIME_SICK);
                        }
    
                        if (item.getItemType() == ItemType.INVINCIBILITY_BALL) {
                            // Ballon d'invincibilité
                            snake.setInvincibleTimer(SnakeGame.TIME_INVINCIBLE);
                        }
                    }
                }
            }
        }
    
        return isAppleEaten;
    }
    public void checkSnakeEaten() {
        for (Snake snake1 : snakes) {
            if (snake1.getInvincibleTimer() < 1 && snake1.isAlive()) { // Ignorer les snakes morts ou invincibles
                for (Snake snake2 : snakes) {
                    if (snake1.getId() == snake2.getId() || !snake2.isAlive()) {
                        continue; // Éviter de comparer un snake à lui-même ou un snake mort
                    }
    
                    // Coordonnées de la tête de snake2
                    int x2 = snake2.getPositions().get(0).getX();
                    int y2 = snake2.getPositions().get(0).getY();
    
                    // Vérification tête à tête
                    if (x2 == snake1.getPositions().get(0).getX() && y2 == snake1.getPositions().get(0).getY()) {
                        if (snake1.getSize() == snake2.getSize()) {
                            // Les deux meurent si leurs tailles sont égales
                            snake1.setAlive(false);
                            snake2.setAlive(false);
                        } else if (snake1.getSize() < snake2.getSize()) {
                            // Snake1 est plus petit, il meurt
                            snake1.setAlive(false);
                        } else {
                            // Snake2 est plus petit, il meurt
                            snake2.setAlive(false);
                        }
                        continue;
                    }
    
                    // Vérification tête-corps (Snake2 entre en collision avec le corps de Snake1)
                    for (int i = 1; i < snake1.getPositions().size(); i++) {
                        if (x2 == snake1.getPositions().get(i).getX() && y2 == snake1.getPositions().get(i).getY()) {
                            if (snake1.getSize() < snake2.getSize()) {
                                // Snake1 est plus petit, il meurt
                                snake1.setAlive(false);
                            } else {
                                // Sinon, Snake2 meurt
                                snake2.setAlive(false);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    

    public void checkWalls() {
        for (Snake snake : snakes) {
            if (snake.getInvincibleTimer() < 1) {
                int x = snake.getPositions().get(0).getX() % this.sizeX;
                int y = snake.getPositions().get(0).getY() % this.sizeY;
    
                if (walls[x][y]) {
                    snake.setAlive(false);
                }
            }
        }
    }
    
    
    public AgentAction playSnake(Snake snake) {
        // Vérifier que la stratégie du serpent est bien définie
        if (snake.getStrategy() == null) {
            System.err.println("Erreur : la stratégie du serpent est non définie!");
            return null;
        }
    
        // Si la stratégie est définie, obtenez l'action choisie
        return snake.getStrategy().chooseAction(snake, this);
    }
    
   public void moveSnake(AgentAction action, Snake snake) {

		List<Position> positions = snake.getPositions();
		Position head = positions.get(0);

	
		snake.setOldTailX(positions.get(positions.size() - 1).getX());
		snake.setOldTailY(positions.get(positions.size() - 1).getY());

	
		if (positions.size() > 1) {
			for (int i = 1; i < positions.size(); i++) {

				positions.get(positions.size() - i).setX(positions.get(positions.size() - i - 1).getX());
				positions.get(positions.size() - i).setY(positions.get(positions.size() - i - 1).getY());

			}
		}

	
		switch (action) {
			case MOVE_UP:
				int y = positions.get(0).getY();
				if (y > 0) {
					head.setY(positions.get(0).getY() - 1);
				} else {
					head.setY(this.getSizeY() - 1);
				}
				break;
			case MOVE_DOWN:
				head.setY((positions.get(0).getY() + 1) % this.getSizeY());
				break;
			case MOVE_RIGHT:
				head.setX((positions.get(0).getX() + 1) % this.getSizeX());
				break;
			case MOVE_LEFT:
				int x = positions.get(0).getX();

				if (x > 0) {
					head.setX(positions.get(0).getX() - 1);
				} else {
					head.setX(this.getSizeX() - 1);
				}
				break;

			default:
				break;
		}
		snake.setLastAction(action);
	}
    
    
    
    
    
    

    public void increaseSizeSnake(Snake snake) {
        snake.getPositions().add(new Position(snake.getOldTailX(), snake.getOldTailY()));
    }

    public void updateSnakeTimers() {

        ListIterator<Snake> iter = snakes.listIterator();

        while (iter.hasNext()) {
            Snake snake = iter.next();

            if (snake.getInvincibleTimer() > 0) {
                snake.setInvincibleTimer(snake.getInvincibleTimer() - 1);
            }

            if (snake.getSickTimer() > 0) {
                snake.setSickTimer(snake.getSickTimer() - 1);
            }
        }
    }

    public GameFeatures toGameFeatures() {
        ArrayList<FeaturesSnake> snakesFeature = new ArrayList<>();
        ArrayList<FeaturesItem> itemsFeature = new ArrayList<>();
        for (Snake snake : this.snakes) {
            snakesFeature.add(snake.toFeaturesSnake());
        }
        for (Item item : this.items) {
            itemsFeature.add(item.toFeaturesItem());
        }
        return new GameFeatures(walls, sizeX, sizeY, snakesFeature, itemsFeature, getState(), getTurn(), getFrameTime());
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public List<Item> getItems() {
        return items;
    }

    private boolean[][] walls;
    protected InputMap setinputMap;

    public boolean[][] getWalls() {
        return walls;
    }

    public ArrayList<Snake> getSnakes() {
        return snakes;
    }

    public void setSnakes(List<Snake> snakes) {
        this.snakes = new ArrayList<>(snakes);
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
    private void resetSnakePosition(Snake snake) {
        // Vérifiez si le serpent est mort
        if (!snake.isAlive()) {
            Position initialPosition = new Position(5, 5);
            snake.getPositions().clear();
            snake.getPositions().add(initialPosition); 
            snake.setAlive(true);
            
            // Réinitialisation des autres attributs de l'état du serpent
            snake.setInvincibleTimer(0);
            snake.setSickTimer(0);
            snake.setLastAction(AgentAction.MOVE_RIGHT);
        } else {
            // Le serpent est encore vivant
          //  System.out.println("Snake " + snake.getId() + " est encore en vie.");
        }
    }
    
    

    public void setInputMap(InputMap inputMap) {
        this.inputMap = inputMap;
    }

    public void setWalls(boolean[][] walls) {
        this.walls = walls;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    
}
