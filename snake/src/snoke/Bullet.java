package snoke;

import java.awt.Graphics2D;

public class Bullet extends MovingObject{


    private double vx, vy;

    public Bullet(
            int width,
            int height,
            int x,
            int y,
            double vx,
            double vy)

    {

        super(width, height, x, y);

        this.vx = vx;
        this.vy = vy;
    }

    public void update(double dtSeconds){

        x += (int) Math.round(vx * dtSeconds);
        y += (int) Math.round(vy * dtSeconds);

    }


    public boolean isOffScreen(int panelW, int panelH){
        return (x+w < 0) || (x > panelW) || (y+h < 0) || (y > panelH);

    }






}
