package strategy;

import agent.Snake;
import item.Item;
import java.util.ArrayList;
import java.util.Random;
import model.SnakeGame;
import utils.AgentAction;
import utils.Position;
public class StrategyAdvancedplus implements Strategy {
    
    @Override
    public AgentAction chooseAction(Snake snake, SnakeGame snakeGame) {
        // Prendre les actions sûres
        ArrayList<AgentAction> safeActions = getSafeActions(snake, snakeGame);

        if (safeActions.isEmpty()) {
            return getRandomAction();  // Si aucune action sûre, on choisit une action aléatoire
        }

        // Choisir une action qui fait avancer vers la nourriture si elle est proche
        AgentAction bestAction = getBestActionTowardsFood(snake, snakeGame);

        // Si aucune action spécifique vers la nourriture n'est sûre, choisir une action sûre aléatoire
        if (bestAction != null) {
            return bestAction;
        } else {
            return safeActions.get(new Random().nextInt(safeActions.size()));
        }
    }

    private ArrayList<AgentAction> getSafeActions(Snake snake, SnakeGame snakeGame) {
        ArrayList<AgentAction> safeActions = new ArrayList<>();

        // Vérifier toutes les actions
        for (AgentAction action : AgentAction.values()) {
            if (!isbadMove(snake, snakeGame, action)) {
                safeActions.add(action);
            }
        }

        return safeActions;
    }
    private AgentAction getBestActionTowardsFood(Snake snake, SnakeGame snakeGame) {
        Position head = snake.getPositions().get(0);
        Position foodPosition = getNearestFood(snakeGame, snake);
    
        if (foodPosition == null) {
            return null;  // Pas de nourriture disponible
        }
    
        // Application d'une recherche heuristique simple (Direction vers la nourriture)
        int dx = foodPosition.getX() - head.getX();
        int dy = foodPosition.getY() - head.getY();
    
        ArrayList<AgentAction> safeActions = getSafeActions(snake, snakeGame);  // Vérifier les mouvements sûrs
    
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && safeActions.contains(AgentAction.MOVE_RIGHT)) {
                return AgentAction.MOVE_RIGHT;
            } else if (dx < 0 && safeActions.contains(AgentAction.MOVE_LEFT)) {
                return AgentAction.MOVE_LEFT;
            }
        } else {
            if (dy > 0 && safeActions.contains(AgentAction.MOVE_DOWN)) {
                return AgentAction.MOVE_DOWN;
            } else if (dy < 0 && safeActions.contains(AgentAction.MOVE_UP)) {
                return AgentAction.MOVE_UP;
            }
        }
    
        // Si aucune direction vers la nourriture n'est sûre, choisir parmi les actions sûres disponibles
        return safeActions.get(new Random().nextInt(safeActions.size()));
    }
    

    private Position getNearestFood(SnakeGame snakeGame, Snake snake) {
        Position nearestFood = null;
        int minDistance = Integer.MAX_VALUE;

        for (Item item : snakeGame.getItems()) {
            
            int dist = calculateManhattanDistance(snake.getPositions().get(0), item.getPosition());
            if (dist < minDistance) {
                nearestFood = item.getPosition();
                minDistance = dist;
            }
        }
        return nearestFood;
    }

    private int calculateManhattanDistance(Position start, Position target) {
        return Math.abs(start.getX() - target.getX()) + Math.abs(start.getY() - target.getY());
    }

    private boolean isbadMove(Snake snake, SnakeGame snakeGame, AgentAction action) {
        Position head = snake.getPositions().get(0);
        int x = head.getX();
        int y = head.getY();

        switch (action) {
            case MOVE_DOWN:
                return isCollision(x, (y + 1) % snakeGame.getSizeY(), snake, snakeGame);
            case MOVE_UP:
                return isCollision(x, (y - 1 + snakeGame.getSizeY()) % snakeGame.getSizeY(), snake, snakeGame);
            case MOVE_LEFT:
                return isCollision((x - 1 + snakeGame.getSizeX()) % snakeGame.getSizeX(), y, snake, snakeGame);
            case MOVE_RIGHT:
                return isCollision((x + 1) % snakeGame.getSizeX(), y, snake, snakeGame);
            default:
                return false;
        }
    }

    private boolean isCollision(int x, int y, Snake snake, SnakeGame snakeGame) {
        // Collision avec le corps du serpent ou avec un mur
        return isMe(x, y, snake) || snakeGame.getWalls()[x][y];
    }

    private boolean isMe(int x, int y, Snake snake) {
        // Collision avec le propre corps du serpent
        for (int i = 1; i < snake.getPositions().size(); i++) {
            if (snake.getPositions().get(i).getX() == x && snake.getPositions().get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }

    private AgentAction getRandomAction() {
        return AgentAction.values()[new Random().nextInt(AgentAction.values().length)];
    }

    @Override
    public void setCurrentAction(AgentAction action) {
        // Méthode laissée vide ou à implémenter selon le contexte
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentAction'");
    }
}
