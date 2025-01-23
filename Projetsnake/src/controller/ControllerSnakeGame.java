package controller;

import java.util.List;
import model.InputMap;
import model.SnakeGame;
import utils.GameFeatures;

public class ControllerSnakeGame extends AbstractController {

    SnakeGame snakeGame;
    List<String> players;
    InputMap inputMap = null;



    public ControllerSnakeGame(String layoutName, List<String> players) {
        this.players = players;
        try {
            this.inputMap = new InputMap(layoutName);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    
    public void initGame() {
        this.snakeGame = SnakeGame.getInstance(10000, inputMap, players);
        this.snakeGame.init();

        
        this.game = snakeGame;
    }

    public GameFeatures getGameFeatures() {
        return this.snakeGame.toGameFeatures();
    }

    public SnakeGame getGame() {
        return this.snakeGame;
    }

    public List<String> getPlayers() {
        return players;
    }

    public InputMap getInputMap() {
        return inputMap;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }


    public int getNumberOfPlayers() {
        if (inputMap != null) {
            return inputMap.getStart_snakes().size(); 
        } else {
            return -1;
        }
    }

    public double getSpeed() {
        return this.game.getFrameTime();
    }

}
