package strategy;

import utils.AgentAction;

public class StrategyHuman implements Strategy {

    private AgentAction currentAction = AgentAction.MOVE_RIGHT; // Action par défaut

    @Override
    public AgentAction chooseAction(agent.Snake snake, model.SnakeGame snakeGame) {
        return currentAction;
    }

    public void setCurrentAction(AgentAction action) {
        this.currentAction = action;
    }
}

