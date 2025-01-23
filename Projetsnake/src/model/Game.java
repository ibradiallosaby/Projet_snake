package model;

import java.io.IOException;
import utils.GameState;

public abstract class Game implements Runnable {

    protected int turn;
    private int maxTurn;
    protected GameState state;

    
    private Thread gameThread; 
    private long frameTime = 1000; 
    public boolean running; 

    protected Game(int maxTurn) {
        this.setMaxTurn(maxTurn);
        this.turn = 0;
        this.state = GameState.STARTING;
    }

    public int getMaxTurn() {
        return maxTurn;
        
    }

    public void setMaxTurn(int maxTurn) {
        this.maxTurn = maxTurn;
        
    }

    public void init() {
        this.turn = 0;
        this.state = GameState.STARTING;
        initializeGame();
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                if (state == GameState.PLAYING) {
                    step();
                }
            }
            try {
                Thread.sleep(frameTime); // Pause entre chaque étape
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Game thread interrupted.");
            }
        }
    }
    

    public synchronized void step() {
        if (gameContinue() && turn < getMaxTurn()) {
            turn++;
           // System.out.println("Turn : " + turn);
            try {
                takeTurn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            state = GameState.OVER;
            stopGameThread();
            gameOver();
        }
    }

    public synchronized void launch() {
        if (state == GameState.STARTING || state == GameState.PAUSED) {
            state = GameState.PLAYING;
            if (gameThread == null || !gameThread.isAlive()) {
                running = true;
                gameThread = new Thread(this);
                gameThread.start();
            }
            //System.out.println("Game launched.");
        }
    }

    public synchronized void pause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
           // System.out.println("Game paused.");
        }
    }

    public synchronized void stop() {
        state = GameState.OVER;
        stopGameThread();
       // System.out.println("Game stopped.");
    }

    private synchronized void stopGameThread() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    public abstract void initializeGame();

    public abstract void takeTurn() throws IOException;

    public abstract boolean gameContinue();

    public abstract void gameOver();

    public long getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }

    public int getTurn() {
        return turn;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
