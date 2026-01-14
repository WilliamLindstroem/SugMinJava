package infiniteStairs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class GamePanel extends JPanel {

    public static final int W = 800;
    public static final int H = 1000;
    private static final int FPS = 60;
    private static final int FRAME_MS = 1000 / FPS;


    private final Player player = new Player(W/2, 635);
    private final StairCase stairs = new StairCase(player);

    private final Timer timer;

    public GamePanel() {

        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        setBackground(Color.BLACK);

        setupKeyBindings();


        timer = new Timer(FRAME_MS, e -> {
            updateGame();
            repaint();
        });
    }

    public void start(){
        requestFocusInWindow();
        timer.start();
    }

    public void setupKeyBindings(){
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("pressed D"), "step");
        am.put("step", new AbstractAction(){
            @Override public void actionPerformed(ActionEvent e){

                stairs.onStep();
            }

        });

        im.put(KeyStroke.getKeyStroke("pressed A"), "turnAndStep");
        am.put("turnAndStep", new AbstractAction(){
            @Override public void actionPerformed(ActionEvent e) {

                player.toggleDirection();
                stairs.onStep();
            }
        });
    }

    private void updateGame() {
        stairs.update(FRAME_MS / 1000.0);
    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        stairs.draw(g2);
        player.draw(g2);
        stairs.drawHud(g2);

        g2.dispose();

    }
}
