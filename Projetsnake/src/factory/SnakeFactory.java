package factory;

import agent.Snake;
import strategy.StrategyAdvanced;
import strategy.StrategyAdvancedplus;
import strategy.StrategyHuman;
import strategy.StrategyRandom;
import utils.FeaturesSnake;
import utils.Position;

public class SnakeFactory {

	public Snake createSnake(FeaturesSnake featuresSnake, int id, String IA) {

	
		int x = featuresSnake.getPositions().get(0).getX();
		int y = featuresSnake.getPositions().get(0).getY();

		Snake snake = new Snake(new Position(x, y), featuresSnake.getLastAction(), id, featuresSnake.getColorSnake());
        if ("IAavanced".equals(IA)) {
            snake.setStrategy(new StrategyAdvanced());  
        }
		else if ("IAavancedplus".equals(IA)) {
            snake.setStrategy(new StrategyAdvancedplus()); 
        }
		else snake.setStrategy(new StrategyRandom() ) ;
		
		return snake;

	}

	public Snake createSnake(FeaturesSnake featuresSnake, int id) {

		int x = featuresSnake.getPositions().get(0).getX();
		int y = featuresSnake.getPositions().get(0).getY();

		Snake snake = new Snake(new Position(x, y), featuresSnake.getLastAction(), id, featuresSnake.getColorSnake());

		snake.setStrategy(new StrategyHuman());

		return snake;

	}

}