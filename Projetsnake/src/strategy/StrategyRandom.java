package strategy;

import agent.Snake;
import java.util.Random;
import model.SnakeGame;
import utils.AgentAction;

public class StrategyRandom implements Strategy {

	@Override
	public AgentAction chooseAction(Snake snake, SnakeGame snakeGame) {

		AgentAction[] listActions = AgentAction.values();

		Random rand = new Random();

		int randomIndex = rand.nextInt(listActions.length);

		return listActions[randomIndex];

	}

	@Override
	public void setCurrentAction(AgentAction action) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setCurrentAction'");
	}

}
