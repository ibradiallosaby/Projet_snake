package model;

public class SimpleGame extends Game {

    public SimpleGame(int maxTurn) {
        super(maxTurn);
    }

    @Override
        public void initializeGame() {
            System.out.println("Jeu initialisé.");
        }

    @Override
        public void takeTurn() {
            System.out.println("Tour " + turn + " du jeu en cours.");
        }

    @Override
        public boolean gameContinue() {
            return true; // Jeu continue par défaut
        }

    @Override
        public void gameOver() {
            System.out.println("Le jeu est terminé !");
        }
}

