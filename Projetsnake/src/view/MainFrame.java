package view;

import agent.Snake;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import model.SnakeGame;
import utils.AgentAction;
import utils.FeaturesItem;
import utils.FeaturesSnake;

public class MainFrame extends JFrame implements KeyListener {
    private final SnakeGame snakeGame;
    private final PanelSnakeGame panel;

    public MainFrame(SnakeGame snakeGame, PanelSnakeGame panel) {
        this.snakeGame = snakeGame;
        this.panel = panel;
      

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Réagir aux touches directionnelles pour déplacer le premier serpent
        if (snakeGame.getSnakes().isEmpty()) return; // Vérifier qu'il y a des serpents

        Snake currentSnake = snakeGame.getSnakes().get(0);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> currentSnake.getStrategy().setCurrentAction(AgentAction.MOVE_RIGHT);
            case KeyEvent.VK_LEFT -> currentSnake.getStrategy().setCurrentAction(AgentAction.MOVE_LEFT);
            case KeyEvent.VK_UP -> currentSnake.getStrategy().setCurrentAction(AgentAction.MOVE_UP);
            case KeyEvent.VK_DOWN -> currentSnake.getStrategy().setCurrentAction(AgentAction.MOVE_DOWN);
        }
    }

    
    @Override
    public void keyReleased(KeyEvent e) {
        // Ne fait rien ici
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Ne fait rien ici
    }

    // Méthode pour mettre à jour l'affichage
    public void updateDisplay(ArrayList<FeaturesSnake> snakesfList, ArrayList<FeaturesItem> itemsfList) {
        panel.updateInfoGame(snakesfList, itemsfList);
        panel.repaint();
    }
}
