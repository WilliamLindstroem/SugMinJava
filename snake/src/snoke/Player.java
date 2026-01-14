package snoke;

import java.util.List;

public class Player extends MovingObject{

    private int aimX = 1;
    private int aimY = 0;



    private long lastShotNanos = 0;
    private static final long COOLDOWN_NANOS = 120_000_000L;



    public Player(
            int width,
            int height,
            int x,
            int y){

        super(width, height, x, y);


    }


    public void setAim(int ax, int ay){


        if (ax == 0 && ay == 0) return;
        aimX = Integer.compare(ax, 0);
        aimY = Integer.compare(ay, 0);
    }

    public Bullet shoot(){


        long now = System.nanoTime();
        if (now - lastShotNanos < COOLDOWN_NANOS) return null;

        lastShotNanos = now;

        final int bulletSize = 6;
        final int bulletSpeed = 900;

        int spawnX = x + w / 2 - bulletSize / 2;
        int spawnY = y + h / 2 - bulletSize / 2;



        double len = Math.sqrt(aimX * aimX + aimY * aimY);
        double vx = (aimX / len) * bulletSpeed;
        double vy = (aimY / len) * bulletSpeed;


        return new Bullet(bulletSize, bulletSize, spawnX, spawnY, vx, vy);


    }

}
