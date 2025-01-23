package view;

import controller.AbstractController;
import java.awt.*;
import javax.swing.*;

public class ViewSimpleCommande {
    private JFrame frame;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stepButton;

    public ViewSimpleCommande(AbstractController controller) {
        frame = new JFrame("Commandes");
        frame.setSize(400, 100);
        frame.setLayout(new GridLayout(1, 3));

        startButton = new JButton("Lancer");
        pauseButton = new JButton("Pause");
        stepButton = new JButton("Étape");

        startButton.addActionListener(e -> controller.play());
        pauseButton.addActionListener(e -> controller.pause());
        stepButton.addActionListener(e -> controller.step());

        frame.add(startButton);
        frame.add(pauseButton);
        frame.add(stepButton);

        frame.setVisible(true);
    }
}
