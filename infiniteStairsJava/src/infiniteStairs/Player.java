package infiniteStairs;

import java.awt.*;

public class Player {

    private int dir = 0;

    private final int x;
    private final int y;

    private final int h = 45;

    public Player(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public void toggleDirection() {

        dir = (dir +1) % 2;
    }

    public int getMoveAmount() {
        int stepX = 70;
        return (dir == 0) ? stepX : -stepX;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getDir() {return dir;}
    public int getHeight(){return h;}
    public int getWidth(){
        return 20;
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.WHITE);
        int w = 35;
        g2.fillRect(x,y, w,h);

        g2.setColor(Color.BLACK);
        if (dir == 0) {
            g2.fillRect(x,y,8,h);
        } else {
            g2.fillRect(x + w - 8, y, 8, h);
        }
    }
}
