import controller.AbstractController;
import controller.ControllerSimpleGame;
import model.ObservableGame;
import view.ViewSimpleCommande;
import view.ViewSimpleGame;

public class Test {
    
    public static void main(String[] args) {
        ObservableGame game = new ObservableGame(10) {
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
                            return turn < getMaxTurn();
                        }

            @Override
                        public void gameOver() {
                            System.out.println("Le jeu est terminé !");
                        }
        };

        ViewSimpleGame view = new ViewSimpleGame();
        game.addObserver(view);

        AbstractController controller = new ControllerSimpleGame(game);
        new ViewSimpleCommande(controller);

        
        game.init();
    }
}
