package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public abstract class ObservableGame extends Game {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ObservableGame(int maxTurn) {
        super(maxTurn);
    }

    public void addObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void notifyObservers(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void step() {
        int oldTurn = this.turn;
        super.step();
        notifyObservers("turn", oldTurn, this.turn);
    }

    @Override
    public void init() {
        super.init();
        notifyObservers("status", null, "started");
    }
}
