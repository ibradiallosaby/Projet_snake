package view;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class ViewSimpleGame implements PropertyChangeListener {
    private JFrame frame;
    private JLabel label;

    public ViewSimpleGame() {
        frame = new JFrame("Simple Game");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label = new JLabel("Jeu en cours", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        frame.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("turn".equals(evt.getPropertyName())) {
            int turn = (int) evt.getNewValue();
            label.setText("Tour : " + turn);
        }
    }
}
