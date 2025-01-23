package view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import utils.FeaturesItem;
import utils.FeaturesSnake;
import utils.GameFeatures;

public class ViewSnakeGame {

	private JFrame jFrame;
	private PanelSnakeGame panelSnake;

	public ViewSnakeGame(PanelSnakeGame panelSnake) {

		// Création de la fenêtre principale
		jFrame = new JFrame("Game");
		jFrame.setSize(new Dimension(panelSnake.getSizeX() * 45, panelSnake.getSizeY() * 45 + 100));

		
		// Positionnement de la fenêtre au centre de l'écran
		Dimension windowSize = jFrame.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2 - 350;
		jFrame.setLocation(dx, dy);
	    // Configurer la fermeture manuelle pour jFrame (fenêtre principale du jeu)
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Ajouter un écouteur pour gérer la confirmation de fermeture
        jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
       @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        int response = JOptionPane.showConfirmDialog(jFrame,
                "Êtes-vous sûr de vouloir fermer le jeu ?", 
                "Fermer le jeu ?", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                 System.exit(0); // Quitter l'application
         }
        // Sinon, la fenêtre reste ouverte
    }
});

		
		this.panelSnake = panelSnake;

		// Ajout du panneau central
		jFrame.add("Center", panelSnake);

		// Rendre la fenêtre visible
		jFrame.setVisible(true);
	}

	// Mise à jour des informations affichées
	public void update(GameFeatures game) {
		panelSnake.updateInfoGame((ArrayList<FeaturesSnake>) game.getFeaturesSnakes(),
				(ArrayList<FeaturesItem>) game.getFeaturesItems());
		panelSnake.repaint();
	}

	// Getters et setters
	public JFrame getjFrame() {
		return jFrame;
	}

	public void setjFrame(JFrame jFrame) {
		this.jFrame = jFrame;
	}
}
