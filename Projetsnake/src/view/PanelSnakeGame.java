package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import utils.*;

/**
 * Classe qui permet de charger d'afficher le panneau du jeu à partir d'une
 * carte et de listes d'agents avec leurs positions.
 * 
 */

public class PanelSnakeGame extends JPanel {

	private static final long serialVersionUID = 1L;

	protected Color ground_Color = new Color(0, 0, 0);

	private int sizeX;
	private int sizeY;

	private int fen_x;
	private int fen_y;

	private double stepx;
	private double stepy;

	float[] contraste = { 0, 0, 0, 1.0f };

	protected ArrayList<FeaturesSnake> featuresSnakes = new ArrayList<FeaturesSnake>();
	protected ArrayList<FeaturesItem> featuresItems = new ArrayList<FeaturesItem>();

	private boolean[][] walls;
	
	// Dictionnaire des images chargées
	private Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	// Variable pour les cycles d'animation
	int cpt;

	// Constructeur
	public PanelSnakeGame(int sizeX, int sizeY, boolean[][] walls, ArrayList<FeaturesSnake> featuresSnakes,
			ArrayList<FeaturesItem> featuresItems) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.walls = walls;
		this.featuresSnakes = featuresSnakes;
		this.featuresItems = featuresItems;
		loadImages(); // Charger les images à la création du panneau
	}

	// Chargement des images
	private void loadImages() {
		try {
			for (int i = 0; i <= 4; i++) {
				images.put("snake_green_" + i, ImageIO.read(new File("Projetsnake/src/images/snake_green_" + i + ".png")));
				images.put("snake_red_" + i, ImageIO.read(new File("Projetsnake/src/images/snake_red_" + i + ".png")));
			}
			images.put("apple", ImageIO.read(new File("Projetsnake/src/images/apple.png")));
			images.put("mysteryBox", ImageIO.read(new File("Projetsnake/src/images/mysteryBox.png")));
			images.put("sickBall", ImageIO.read(new File("Projetsnake/src/images/sickBall.png")));
			images.put("invicibleBall", ImageIO.read(new File("Projetsnake/src/images/invicibleBall.png")));
			images.put("wall", ImageIO.read(new File("Projetsnake/src/images/wall.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		fen_x = getSize().width;
		fen_y = getSize().height;

		this.stepx = fen_x / (double) sizeX;
		this.stepy = fen_y / (double) sizeY;

		g.setColor(ground_Color);
		g.fillRect(0, 0, fen_x, fen_y);

		double position_x = 0;

		for (int x = 0; x < sizeX; x++) {
			double position_y = 0;

			for (int y = 0; y < sizeY; y++) {
				// Vérification des dimensions
				if (x < walls.length && y < walls[x].length && walls[x][y]) {
					g.drawImage(images.get("wall"), (int) position_x, (int) position_y, (int) stepx, (int) stepy, this);
				}
				position_y += stepy;
			}
			position_x += stepx;
		}

		for (FeaturesSnake snake : featuresSnakes) {
			if (snake.isAlive()) {
				paint_Snake(g, snake);
			}
		}

		for (FeaturesItem item : featuresItems) {
			paint_Item(g, item);
		}

		// Affichage du score de chaque serpent
		for (FeaturesSnake snake : featuresSnakes) {
			if (snake.isAlive()) {
				g.setColor(Color.WHITE);
				g.drawString("Score: " + snake.getScore(), 10, 10); // Positionnement du score
			}
		}

		cpt++;
	}

	// Méthode pour dessiner le serpent
	void paint_Snake(Graphics g, FeaturesSnake featuresSnake) {
		List<Position> positions = featuresSnake.getPositions();

        AgentAction lastAction = featuresSnake.getLastAction();

        BufferedImage img = null;

        double pos_x;
        double pos_y;

        int cpt_img = -1;

        for (int i = 0; i < positions.size(); i++) {
            pos_x = positions.get(i).getX() * stepx;
            pos_y = positions.get(i).getY() * stepy;

            if (i == 0) {
                switch (lastAction) {
                    case MOVE_UP:
                        cpt_img = 0;
                        break;
                    case MOVE_DOWN:
                        cpt_img = 1;
                        break;
                    case MOVE_RIGHT:
                        cpt_img = 2;
                        break;
                    case MOVE_LEFT:
                        cpt_img = 3;
                        break;
                    default:
                        break;
                }
            } else {
                cpt_img = 4;
            }

            try {
                if (featuresSnake.getColorSnake() == ColorSnake.Green) {
                    img = images.get("snake_green_" + cpt_img);
                } else if (featuresSnake.getColorSnake() == ColorSnake.Red) {
                    img = images.get("snake_red_" + cpt_img);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            float[] scales = new float[] { 1, 1, 1, 1.0f };

            if (featuresSnake.isInvincible())
                scales = new float[] { 3, 0.75f, 3, 1.0f };

            if (featuresSnake.isSick())
                scales = new float[] { 1.5f, 1.5f, 0.75f, 1.0f };

            RescaleOp op = new RescaleOp(scales, contraste, null);
            img = op.filter(img, null);

            if (img != null) {
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            }
        }
    }

    // Méthode pour dessiner un item
    void paint_Item(Graphics g, FeaturesItem featuresItem) {
        int x = featuresItem.getX();
        int y = featuresItem.getY();

        double pos_x = x * stepx;
        double pos_y = y * stepy;

        String itemKey = "";
        if (featuresItem.getItemType() == ItemType.APPLE) {
            itemKey = "apple";
        } else if (featuresItem.getItemType() == ItemType.BOX) {
            itemKey = "mysteryBox";
        } else if (featuresItem.getItemType() == ItemType.SICK_BALL) {
            itemKey = "sickBall";
        } else if (featuresItem.getItemType() == ItemType.INVINCIBILITY_BALL) {
            itemKey = "invicibleBall";
        }

        if (!itemKey.isEmpty()) {
            g.drawImage(images.get(itemKey), (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
        }
    }

    // Mise à jour de la liste des serpents et des items
    public void updateInfoGame(ArrayList<FeaturesSnake> featuresSnakes, ArrayList<FeaturesItem> featuresItems) {
        this.featuresSnakes = featuresSnakes;
        this.featuresItems = featuresItems;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean[][] getWalls() {
        return walls;
    }

    public void setWalls(boolean[][] walls) {
        this.walls = walls;
    }
}
