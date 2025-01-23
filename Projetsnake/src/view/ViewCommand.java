package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.SnakeGame;
import utils.GameFeatures;

public class ViewCommand {

    public JPanel panelMain;
    JLabel textTurn;
    JLabel textScore;

    StateViewCommand state;

    private JButton initChoice;
    private JButton pauseChoice;
    private JButton playChoice;
    private JButton stepChoice;

    JSlider j;
    private Timer gameTimer;

    public void setGameTimer(Timer gameTimer) {
     this.gameTimer = gameTimer;
     }

    private boolean userChangeValue = true;
    private SnakeGame snakeGame; // Ajout de snakeGame

    public ViewCommand(JFrame mainFrame, SnakeGame snakeGame) {
        this.snakeGame = snakeGame; // Passer l'instance de SnakeGame

        panelMain = new JPanel(new GridLayout(2, 1));
        panelMain.setMaximumSize(new Dimension(1000, 100));

        Icon iconRestart = new ImageIcon("Projetsnake/src/icons/icon_restart.png");
        setInitChoice(new JButton(iconRestart));

        Icon iconPlay = new ImageIcon("Projetsnake/src/icons/icon_play.png");
        setPlayChoice(new JButton(iconPlay));

        Icon iconStep = new ImageIcon("Projetsnake/src/icons/icon_step.png");
        setStepChoice(new JButton(iconStep));

        Icon iconPause = new ImageIcon("Projetsnake/src/icons/icon_pause.png");
        setPauseChoice(new JButton(iconPause));

        getInitChoice().setPreferredSize(new Dimension(100, 50));
        getPauseChoice().setPreferredSize(new Dimension(100, 50));
        getPlayChoice().setPreferredSize(new Dimension(100, 50));
        getStepChoice().setPreferredSize(new Dimension(100, 50));

        // Actions des boutons
        getInitChoice().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evenement) {
                state.clickRestart(); // Appel direct à l'état local
				snakeGame.init();
				update(snakeGame.toGameFeatures(), 0);

            }
        });

		getPauseChoice().addActionListener(e -> {
			state.clickPause();  // Met à jour l'état de ViewCommand
			snakeGame.pause();   // Passe l'état du jeu à PAUSED
			update(snakeGame.toGameFeatures(), 0);  // Met à jour l'interface
		});
		
		getPlayChoice().addActionListener(e -> {
			state.clickPlay();   // Met à jour l'état de ViewCommand
			snakeGame.launch();  // Lance le jeu
			update(snakeGame.toGameFeatures(), 0);  // Met à jour l'interface
		});
		

		getStepChoice().addActionListener(e -> {
			snakeGame.step();    // Exécute un seul tour
			update(snakeGame.toGameFeatures(), 0);  // Met à jour l'interface
		});
		

        this.j = new JSlider(1, 10);

        // Initialisation avec une valeur par défaut (exemple : 5)
        j.setValue(5);
        j.setMajorTickSpacing(1);
        j.setPaintTicks(true);
        j.setPaintLabels(true);
        j.setFocusable(false);

        j.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evenement) {
                JSlider source = (JSlider) evenement.getSource();
                if (!source.getValueIsAdjusting() && userChangeValue) {
                    int newSpeed = source.getValue();
                    System.out.println("Slider value changed to: " + newSpeed);
                    if (gameTimer != null) {
                        gameTimer.setDelay(1100 - newSpeed * 100); // Exemple : vitesse inversement proportionnelle
                    }
                }
            }
        });
        
        panelMain.setLayout(new GridLayout(2, 1));

        JPanel haut = new JPanel();
        haut.setLayout(new GridLayout(1, 4));
        haut.add(getInitChoice());
        haut.add(getPauseChoice());
        haut.add(getPlayChoice());
        haut.add(getStepChoice());

        panelMain.add(haut);

        JPanel bas = new JPanel();
        bas.setLayout(new GridLayout(1, 2));

        bas.add(j);

        textTurn = new JLabel("Tour : ", SwingConstants.CENTER);
        textScore = new JLabel("", SwingConstants.CENTER);

        JPanel infos = new JPanel();
        infos.setLayout(new GridLayout(2, 1));

        infos.add(textTurn);
        infos.add(textScore);

        bas.add(infos);
        panelMain.add(bas);

        mainFrame.add("South", panelMain);

        panelMain.setVisible(true);

        state = new StateStarting(this);
    }

    public JButton getPlayChoice() {
        return playChoice;
        
    }

    public void setPlayChoice(JButton playChoice) {
        this.playChoice = playChoice;
        
    }

    public JButton getStepChoice() {
        return stepChoice;
        
    }

    public void setStepChoice(JButton stepChoice) {
        this.stepChoice = stepChoice;
        
    }

    public JButton getPauseChoice() {
        return pauseChoice;
        
    }

    public void setPauseChoice(JButton pauseChoice) {
        this.pauseChoice = pauseChoice;
        
    }

    public JButton getInitChoice() {
		return initChoice;
		
	}

	public void setInitChoice(JButton initChoice) {
		this.initChoice = initChoice;
		
	}

	public void setState(StateViewCommand state) {
        this.state = state;
    }
	public void update(GameFeatures game, int id) {
		textTurn.setText("Turn: " + game.getTurn());
		if (game.getPlayerScore(id) != -1) {
			textScore.setText("Score: " + game.getPlayerScore(id));
		} else {
			textScore.setText("Spectator");
		}
	
		// Synchronisez l'état des boutons avec le jeu
		switch (game.getState()) {
			case PLAYING -> {
				getInitChoice().setEnabled(false);
				getPlayChoice().setEnabled(false);
				getPauseChoice().setEnabled(true);
				getStepChoice().setEnabled(false);
			}
			case PAUSED -> {
				getPlayChoice().setEnabled(true);
				getPauseChoice().setEnabled(false);
				getStepChoice().setEnabled(true);
				getInitChoice().setEnabled(true);
			}
			case OVER -> {
				getPlayChoice().setEnabled(false);
				getPauseChoice().setEnabled(false);
				getStepChoice().setEnabled(false);
				getInitChoice().setEnabled(true);
			}
			case STARTING -> {
				getInitChoice().setEnabled(false);
				getPlayChoice().setEnabled(true);
				getPauseChoice().setEnabled(false);
				getStepChoice().setEnabled(true);
			}
		}
	}
	
    public void updateSlider(Double speed) {
        if (this.j.getValue() != speed.intValue()) {
            userChangeValue = false;
            this.j.setValue(speed.intValue());
            userChangeValue = true;
        }
    }
}
