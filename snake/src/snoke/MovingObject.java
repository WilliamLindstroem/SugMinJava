package snoke;

import java.awt.*;

public class MovingObject{

    public int w, h, x, y;

    public MovingObject(int width,
                        int height,
                        int x,
                        int y){
        this.w = width;
        this.h = height;
        this.x = x;
        this.y = y;
    }


    public void draw(Graphics2D g2){

        g2.fillRect(x,y,w,h);

    }


}
