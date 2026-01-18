import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {
    int frameWidth = 1000;
    int frameHeight = 800;
    int x = 100;
    int y = 325;
    int playerWidth = 15;
    int playerHeight = 70;
    int ballX = 500;
    int ballY = 400;
    int ballSpeedX = 5;

    // Initialize the game
    public Game() {
        JFrame frame = new JFrame("Really awesome megacool game");
        frame.add(this);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);

        Timer timer = new Timer(16, e -> {
            ballX -= ballSpeedX;
            repaint();
        });

        timer.start();
    }

    // Draws the "graphics"
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, frameWidth, frameHeight);

        g.setColor(Color.white);
        g.fillRect(x, y, playerWidth, playerHeight); // draws the player line

        g.setColor(Color.white);
        g.fillOval(ballX, ballY, 20, 20);
    }

    // Changes the "players" location based on button presses
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (y > 0) {
                y -= 10;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (y < this.getHeight() - playerHeight) {
                y += 10;
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new Game();
    }
}