package strategy;

import java.util.ArrayList;
import java.util.Random;

import agent.Snake;
import model.SnakeGame;
import utils.AgentAction;
import utils.Position;

public class StrategyAdvanced implements Strategy {

	@Override
	public AgentAction chooseAction(Snake snake, SnakeGame snakeGame) {

		AgentAction[] listActions = AgentAction.values();

		ArrayList<AgentAction> nonLetalActions = new ArrayList<AgentAction>();

		for (int i = 0; i < listActions.length; i++) {

			if (!isbadMove(snake, snakeGame, listActions[i])) {

				nonLetalActions.add(listActions[i]);
			}

		}

		Random rand = new Random();

		if (nonLetalActions.size() > 0) {

			int randomIndex = rand.nextInt(nonLetalActions.size());

			return nonLetalActions.get(randomIndex);
		} else {

			int randomIndex = rand.nextInt(listActions.length);
			return listActions[randomIndex];

		}

	}
	

	public boolean isbadMove(Snake snake, SnakeGame snakeGame, AgentAction action) {

		Position head = snake.getPositions().get(0);
		int x = head.getX();
		int y = head.getY();

		switch (action) {
			case MOVE_DOWN:
				if (isME(x, (y + 1) % snakeGame.getSizeY(), snake)
						|| snakeGame.getWalls()[x][(y + 1) % snakeGame.getSizeY()])
					return true;
				break;
			case MOVE_UP:

				int newy = -1;

				if (y > 0) {
					newy = y - 1;
				} else {
					newy = snakeGame.getSizeY() - 1;
				}
				if (isME(x, newy, snake) || snakeGame.getWalls()[x][newy])
					return true;
				break;
			case MOVE_RIGHT:
				if (isME((x + 1) % snakeGame.getSizeX(), y, snake)
						|| snakeGame.getWalls()[(x + 1) % snakeGame.getSizeX()][y])
					return true;
				break;
			case MOVE_LEFT:

				int newx = -1;

				if (x > 0) {
					newx = x - 1;
				} else {
					newx = snakeGame.getSizeX() - 1;
				}

				if (isME(newx, y, snake) || snakeGame.getWalls()[newx][y])
					return true;
				break;
			default:
				break;
		}
		return false;
	}

	public boolean isME(int x, int y, Snake snake) {

		for (int i = 1; i < snake.getPositions().size(); i++) {

			if (snake.getPositions().get(i).getX() == x & snake.getPositions().get(i).getY() == y) {
				return true;
			}

		}

		return false;
	}

	@Override
	public void setCurrentAction(AgentAction action) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setCurrentAction'");
	}


	
}
