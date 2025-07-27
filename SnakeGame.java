// Java program to implement Snake Game using JFrame

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        add(new GamePanel());
        setTitle("Snake Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SnakeGame();
            frame.setVisible(true);
        });
    }
}

class GamePanel extends JPanel implements ActionListener {

    private final int TILE_SIZE = 10;
    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 400;

    private LinkedList<Point> snake;
    private Point food;
    private int directionX, directionY;
    private boolean gameOver;
    private Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(new Color(250, 235, 215));
        setFocusable(true);

        // Initialize game variables
        snake = new LinkedList<>();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        directionX = TILE_SIZE;
        directionY = 0;
        spawnFood();
        gameOver = false;

        // Key listener for snake movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (directionX == 0) {
                            directionX = -TILE_SIZE;
                            directionY = 0;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (directionX == 0) {
                            directionX = TILE_SIZE;
                            directionY = 0;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (directionY == 0) {
                            directionY = -TILE_SIZE;
                            directionX = 0;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (directionY == 0) {
                            directionY = TILE_SIZE;
                            directionX = 0;
                        }
                        break;
                }
            }
        });

        // Timer to handle game updates every 100 ms
        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        int foodX = rand.nextInt(BOARD_WIDTH / TILE_SIZE) * TILE_SIZE;
        int foodY = rand.nextInt(BOARD_HEIGHT / TILE_SIZE) * TILE_SIZE;
        food = new Point(foodX, foodY);
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                return;
            }
        }

        // Wall collision
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver = true;
        }
    }

    private void updateSnake() {
        Point head = new Point(snake.getFirst());
        head.translate(directionX, directionY);
        snake.addFirst(head);

        if (head.equals(food)) {
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            repaint();
            return;
        }

        updateSnake();
        checkCollision();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Helvetica", Font.BOLD, 20));
            String message = "Game Over! Press R to Restart or Q to Quit";
            FontMetrics metrics = g.getFontMetrics();
            g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_HEIGHT / 2);
        } else {
            // Draw food
            g.setColor(new Color(139, 69, 19));
            g.fillRect(food.x, food.y, TILE_SIZE, TILE_SIZE);

            // Draw snake
            g.setColor(Color.BLACK);
            for (Point p : snake) {
                g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        if (gameOver) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    System.exit(0);
                }
            }
        }
        super.processKeyEvent(e);
    }

    private void restartGame() {
        snake.clear();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        directionX = TILE_SIZE;
        directionY = 0;
        spawnFood();
        gameOver = false;
        timer.start();
    }
}
