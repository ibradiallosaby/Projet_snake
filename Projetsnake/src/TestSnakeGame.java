import agent.Snake;
import controller.ControllerSnakeGame;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.SnakeGame;
import strategy.StrategyAdvanced;
import strategy.StrategyAdvancedplus;
import strategy.StrategyHuman;
import strategy.StrategyRandom;
import utils.*;
import view.MainFrame;
import view.PanelSnakeGame;
import view.ViewCommand;
import view.ViewSnakeGame;

public class TestSnakeGame {
    public static void main(String[] args) {
        
         UIManager.put("MenuItem.background", Color.DARK_GRAY);  // Arrière-plan sombre pour les éléments du menu
         UIManager.put("MenuItem.foreground", Color.WHITE);  // Texte blanc pour les éléments du menu
         UIManager.put("MenuBar.background", Color.BLACK);  // Fond de la barre de menu en noir
         UIManager.put("MenuBar.foreground", Color.BLUE);  // Texte de la barre de menu en vert

 
        // Création du contrôleur et initialisation du jeu
        ControllerSnakeGame controller = new ControllerSnakeGame("alone", List.of("joueur 1"));
        controller.initGame();
        SnakeGame snakeGame = controller.getGame();

        // Panneau principal pour afficher le jeu
        PanelSnakeGame panel = new PanelSnakeGame(
                22, // Taille initiale
                11,
                controller.getInputMap().get_walls(),
                new ArrayList<>(), // Liste des serpents
                new ArrayList<>()
        );

        MainFrame mainFrame = new MainFrame(snakeGame, panel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(800, 300);

        // Barre d'état pour les informations de jeu
        JLabel statusBar = new JLabel("Score : 0 | Vies : 3 | Niveau : 1");
        mainFrame.add(statusBar, BorderLayout.NORTH);

        // Création du menu
        JMenu layoutMenu = new JMenu("Choisir un Layout");
        layoutMenu.setForeground(Color.GREEN);
        JMenuItem loadLayoutItem = new JMenuItem("Charger un layout");
        loadLayoutItem.addActionListener(e -> handleLayoutSelection(mainFrame, snakeGame, panel, statusBar));
        layoutMenu.add(loadLayoutItem);
        
        JMenu configMenu = new JMenu("Configurer Stratégies");
        configMenu.setForeground(Color.GREEN);
        JMenuItem changeStrategyItem = new JMenuItem("Changer la stratégie");
        changeStrategyItem.addActionListener(e -> changeStrategyOption(snakeGame));
        configMenu.add(changeStrategyItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(layoutMenu);
        menuBar.add(configMenu);
        mainFrame.setJMenuBar(menuBar);

        // Configuration du reste du jeu
        mainFrame.add(panel, BorderLayout.CENTER);

        // Vue de commandes utilisateur
        ViewCommand viewCommand = new ViewCommand(mainFrame, snakeGame);
        mainFrame.add(viewCommand.panelMain, BorderLayout.SOUTH);

        // Initialisation de la vue Snake Game pour le rendu graphique
        ViewSnakeGame viewGame = new ViewSnakeGame(panel);

        mainFrame.setVisible(true);
        // Empêcher la fermeture par défaut
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

       // Ajouter un écouteur pour gérer la confirmation de fermeture
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
       @Override
       public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        int response = JOptionPane.showConfirmDialog(mainFrame,
                "Êtes-vous sûr de vouloir fermer le jeu ?", 
                "Fermer le jeu ?", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            System.exit(0); // Quitter l'application
        }
        // Si l'utilisateur sélectionne "Non", ne rien faire (la fenêtre reste ouverte)
    }
});


        final Timer gameTimer = new Timer(300, null); // Déclarez le Timer avec un ActionListener null

        gameTimer.addActionListener(e -> {
            if (snakeGame.getState() == GameState.PLAYING) {
                snakeGame.step();
                updateUI(snakeGame, viewGame, viewCommand, statusBar);
            } else if (snakeGame.getState() == GameState.OVER) {
                JOptionPane.showMessageDialog(mainFrame, "Le jeu est terminé !", "Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
                gameTimer.stop(); 
            }
        });

        // Lien entre le Timer et les commandes utilisateur
        viewCommand.setGameTimer(gameTimer);

        // Gestion des interactions utilisateur
        addListeners(snakeGame, viewGame, viewCommand, gameTimer, statusBar);

        gameTimer.start();
    }

    // Permet de changer la stratégie au niveau du menu
    private static void changeStrategyOption(SnakeGame snakeGame) {
        // Fenêtre de choix de stratégie
        String[] strategies = {"Humain", "Random" , "IAavanced", "IAavancedplus"};
        String selectedStrategy = (String) JOptionPane.showInputDialog(null,
                "Sélectionner la stratégie",
                "Changer Stratégie",
                JOptionPane.QUESTION_MESSAGE,
                null,
                strategies,
                strategies[0]);

        if (selectedStrategy != null && !selectedStrategy.isEmpty()) {
           for(Snake snake : snakeGame.getSnakes())
           { 
            switch (selectedStrategy) {
            case "Humain":
                snake.setStrategy(new StrategyHuman());
                break;
            case "Random" :
                 snake.setStrategy(new StrategyRandom());
                 break ;
            case "IAavanced" :
                 snake.setStrategy(new StrategyAdvanced());
                 break;
            case "IAavancedplus" :
                 snake.setStrategy(new StrategyAdvancedplus());
                 break;
        
            default:
                // Si aucune des options ne correspond, définir une stratégie par défaut ou gérer l'erreur
                System.out.println("Stratégie inconnue");
                break;
        }

           }
           
        }
    }

    // Gestion du changement de Layout
    private static void handleLayoutSelection(JFrame mainFrame, SnakeGame snakeGame, PanelSnakeGame panel, JLabel statusBar) {
        JFileChooser fileChooser = new JFileChooser("Projetsnake/src/layouts");
        fileChooser.setDialogTitle("Sélectionnez un layout");

        int result = fileChooser.showOpenDialog(mainFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filename = selectedFile.getName().replace(".lay", "");

            try {
                // Chargement du nouveau layout
                model.InputMap input = new model.InputMap(filename);
                snakeGame.setInputMap(input);
                panel.setWalls(input.get_walls());
                snakeGame.resetGame();
                statusBar.setText("Score : 0 | Vies : 3 | Niveau : 1");
                panel.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Erreur lors du chargement du layout", "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }



    private static void updateUI(SnakeGame snakeGame, ViewSnakeGame viewGame, ViewCommand viewCommand, JLabel statusBar) {
        // Mettre à jour l'affichage des informations de jeu
        viewGame.update(snakeGame.toGameFeatures());
        viewCommand.update(snakeGame.toGameFeatures(), 0);
        
        // Vérifier si le jeu continue ou est terminé
        if (!snakeGame.gameContinue()) {
            snakeGame.setState(GameState.OVER);  // Assurez-vous que l'état du jeu est bien "OVER" quand tous les serpents sont morts
            statusBar.setText("Fin de jeu !"); // Afficher "Fin de jeu" si tous les serpents sont morts
             
    

        } else {
            // Sinon, afficher le statut des serpents vivants
            StringBuilder statusText = new StringBuilder("Status : ");
            boolean allSnakesDead = true;
          // Afficher le niveau de difficulté
           statusText.append("Niveau de difficulté : ").append(snakeGame.getDifficultyLevel()).append(" | ");
    
           
            for (Snake snake : snakeGame.getSnakes()) {
                if (snake.isAlive()) {
                    allSnakesDead = false;
                    int nbserpent = snake.getId()+1;
                    statusText.append("Serpent " + nbserpent + " - Score : " + snake.getScore() +
                            " | Vies : " + snake.getLives() + " | ");
                } else {
                    statusText.append("Serpent " + snake.getId() + " est mort | ");
                  
                }
            }
            statusBar.setText(statusText.toString());
    
            if (allSnakesDead) {
                snakeGame.setState(GameState.OVER); // Terminer la partie si tous les serpents sont morts
                viewCommand.getPauseChoice().setEnabled(false); // Désactiver la pause au démarrage
                viewCommand.getInitChoice().setEnabled(true);
            } 
        }
    }
    
    // Gestion des actions utilisateur
    private static void addListeners(SnakeGame snakeGame, ViewSnakeGame viewGame, ViewCommand viewCommand, Timer gameTimer, JLabel statusBar) {
        // Redémarrer le jeu
        viewCommand.getInitChoice().addActionListener(e -> {
            snakeGame.resetGame();  // Réinitialise l'état du jeu
            gameTimer.restart();  // Redémarre le timer du jeu
            statusBar.setText("Score : 0 | Vies : 3 | Niveau : 1");
        
            // Mise à jour des états des boutons
            viewCommand.getPlayChoice().setEnabled(true);
            viewCommand.getPauseChoice().setEnabled(false); // Désactiver la pause au démarrage
            viewCommand.getInitChoice().setEnabled(false);  // Désactiver l'initialisation pendant que le jeu est en cours
        });

        // Lancer le jeu
        viewCommand.getPlayChoice().addActionListener(e -> snakeGame.launch());

        // Pause du jeu
        viewCommand.getPauseChoice().addActionListener(e -> snakeGame.pause());

        // Un seul pas en pause
        viewCommand.getStepChoice().addActionListener(e -> {
            if (snakeGame.getState() == GameState.PAUSED) {
                snakeGame.step();
                updateUI(snakeGame, viewGame, viewCommand, statusBar);
            }
        });
    }
}
