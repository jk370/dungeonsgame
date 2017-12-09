package uk.ac.bath.se;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class DungeonView implements KeyListener {
    private final JFrame gameWindow;
    private JPanel gamePanel;
    private JTextArea gameArea;
    private JTextArea scoreArea;
    private DungeonController dungeonController;
    Dungeon dungeon;

    public DungeonView() {
        gameWindow = new JFrame();
        gamePanel = new JPanel();

        gameArea = new JTextArea();
        gameArea.setForeground(Color.white);
        gameArea.setFont(new Font("monospaced", Font.PLAIN, 16)); //size depicts the size of the game view.
        gameArea.setVisible(true);
        gameArea.setEditable(false);

        scoreArea = new JTextArea();

        gameWindow.add(gamePanel);
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setExtendedState(Frame.MAXIMIZED_BOTH);

        gamePanel.add(gameArea);
        gamePanel.setBackground(Color.BLACK);

        gamePanel.add(scoreArea);

        gameArea.addKeyListener(this);
        gameArea.setBackground(Color.BLACK);

        gameWindow.setVisible(true);

        dungeon = Dungeon.getInstance();
        dungeonController = new DungeonController();
        dungeonController.initialiseDungeonGame();

        scoreArea.setText("Player Name: "
                +dungeonController.player.getPlayerName()+
                "\n" +
                "Lives: " +
                Player.lives +
                "\n" +
                "Score: " +
                dungeonController.player.getScore() +
                "\n" +
                "Gold: " +
                dungeonController.player.getGold() +
                "\n" +
                "Total gold required: " +
                dungeonController.gameWinAmount
        );

        drawDungeon();
    }

    //Draws dungeon with coordinates (y, x)
    private void drawDungeon() {
        gameArea.setText("");
        for (int i = 0; i < dungeon.dungeonMatrix.length; i++) {
            for (int j = 0; j < dungeon.dungeonMatrix[i].length; j++) {
                gameArea.append(dungeon.dungeonMatrix[i][j] + " ");
            }
            gameArea.append("\n");
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                //if player hits boundary.
                if (!dungeonController.checkCollision(dungeonController.player.getyCoord() - 1, dungeonController.player.getxCoord())) {
                    dungeonController.movePlayer(PlayerMovement.UP);
                    drawDungeon();
                }
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                //if player hits boundary
                if (!dungeonController.checkCollision(dungeonController.player.getyCoord() + 1, dungeonController.player.getxCoord())) {
                    dungeonController.movePlayer(PlayerMovement.DOWN);
                    drawDungeon();
                }
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                //if player hits boundary
                if (!dungeonController.checkCollision(dungeonController.player.getyCoord(), dungeonController.player.getxCoord() - 1)) {
                    dungeonController.movePlayer(PlayerMovement.LEFT);
                    drawDungeon();
                }
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                //if player hits boundary
                if (!dungeonController.checkCollision(dungeonController.player.getyCoord(), dungeonController.player.getxCoord() + 1)) {
                    dungeonController.movePlayer(PlayerMovement.RIGHT);
                    drawDungeon();
                }
                break;

            case KeyEvent.VK_SPACE:
                if (dungeonController.nearChest) {
                    dungeonController.giveRandomItem();
                }
        }

        if (dungeonController.checkLoss()) {
            Player.lives -= 1;
            if (Player.lives == 0) {
                gameWindow.dispose();
                SplashScreen.loseScreen();
            } else {
                dungeonController.initialiseDungeonGame();
                SplashScreen.loseOneLifeScreen();
                drawDungeon();
            }
        }

        if (dungeonController.checkWin()) {
            //TODO input username to use
            dungeonController.saveGoldAmount(dungeonController.player.getPlayerName(),
                    dungeonController.player.getGold(),
                    dungeonController.player.getScore()
            );
            gameWindow.dispose();
            SplashScreen.winScreen();
        }
        scoreArea.setText("Player Name: "
                +dungeonController.player.getPlayerName()+
                "\n" +
                "Lives: " +
                Player.lives +
                "\n" +
                "Score: " +
                dungeonController.player.getScore() +
                "\n" +
                "Gold: " +
                dungeonController.player.getGold() +
                "\n" +
                "Total gold required: " +
                dungeonController.gameWinAmount
        );
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
