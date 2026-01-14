package snoke;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public final class MovementPanel extends JPanel {

    private static final int W = 900;
    private static final int H = 600;

    private static final int playerW = 15;
    private static final int playerH = 15;

    private static final int startX = W / 2- playerW / 2;
    private static final int startY = H / 2- playerH / 2;

    private final Player player = new Player(playerW, playerH, startX, startY);

    // Speed in pixels per second (frame-rate independent)
    private static final int SPEED_PX_PER_SEC = 360;

    private boolean up, down, left, right;

    private final Timer timer;
    private long lastTimeNanos;

    private int lastH = 0; // -1 = left, +1 = right
    private int lastV = 0; // -1 = up,   +1 = down

    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();

    private static double enemySpawnCooldown = 0.0;
    /*
    * Change to private, just wanted to try printing it
    * Thus public
    */
    public final double ENEMY_SPAWN_INTERVAL = 5*Math.pow(10, 9);

    final int enemySpeed = 200;


    public MovementPanel() {
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);

        setupKeyBindings();

        lastTimeNanos = System.nanoTime();

        // 120 updates per second target (Swing Timer is not perfect, but ok for learning)
        timer = new Timer(1000 / 120, e -> {
            update();
            repaint();
        });
    }

    public void start() {
        requestFocusInWindow();
        timer.start();
    }

    private void setupKeyBindings() {

        bind("pressed SPACE", e -> {
            Bullet b = player.shoot();
            if (b != null) bullets.add(b);
        });

        // Press
        bind("pressed A", e -> { left = true;  lastH = -1; });
        bind("pressed LEFT", e -> { left = true; lastH = -1; });

        bind("pressed D", e -> { right = true; lastH = +1; });
        bind("pressed RIGHT", e -> { right = true; lastH = +1; });

        bind("pressed W", e -> { up = true;    lastV = -1; });
        bind("pressed UP", e -> { up = true;   lastV = -1; });

        bind("pressed S", e -> { down = true;  lastV = +1; });
        bind("pressed DOWN", e -> { down = true; lastV = +1; });

        // Release (do not change lastH/lastV here; it is “last pressed”, not “currently held”)
        bind("released A", e -> left = false);
        bind("released LEFT", e -> left = false);

        bind("released D", e -> right = false);
        bind("released RIGHT", e -> right = false);

        bind("released W", e -> up = false);
        bind("released UP", e -> up = false);

        bind("released S", e -> down = false);
        bind("released DOWN", e -> down = false);
    }


    private void bind(String stroke, java.util.function.Consumer<ActionEvent> action) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), stroke);
        getActionMap().put(stroke, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(e);
            }
        });
    }

    private void update() {



        long now = System.nanoTime();
        double dt = (now - lastTimeNanos) / 1_000_000_000.0; // seconds
        lastTimeNanos = now;

        int dx;
        if (left && !right) dx = -1;
        else if (right && !left) dx = +1;
        else if (left && right) dx = lastH; // last pressed wins
        else dx = 0;

        int dy;
        if (up && !down) dy = -1;
        else if (down && !up) dy = +1;
        else if (up && down) dy = lastV; // last pressed wins
        else dy = 0;

        player.setAim(dx, dy);

        player.x += (int) Math.round(dx * SPEED_PX_PER_SEC * dt);
        player.y += (int) Math.round(dy * SPEED_PX_PER_SEC * dt);

        if (player.x < 0) player.x = 0;
        if (player.y < 0) player.y = 0;
        if (player.x > W - player.w) player.x = W - player.w;
        if (player.y > H - player.h) player.y = H - player.h;

        var it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update(dt);
            if (b.isOffScreen(W, H)) {
                it.remove();
            }
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(player.x, player.y, playerW, playerH);

        g2.setColor(Color.BLUE);
        for (Bullet b : bullets) {
            b.draw(g2);
        }


        g2.setColor(Color.RED);
        for (Enemy e : enemies) {
            e.draw(g2);
        }
    }
}
