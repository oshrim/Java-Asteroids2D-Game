package Sprites;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sagi on 12/18/15.
 */
public class LaserBlast extends Sprite {


    public LaserBlast(int x, int y, int w, int h, int acc, String imgName, double angle, int size) {
        super(x, y, w, h, acc, imgName, angle, size);

    }

    @Override
    public void update() {
        locX += acceleration * Math.cos(Math.toRadians(angle));
        locY -= acceleration * (-1 * Math.sin(Math.toRadians(angle)));
        outOfScreeFix();
    }

    @Override
    public void drawSprite(Graphics g, JPanel p) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.rotate(Math.toRadians(angle), locX + (bImage.getWidth()/2), locY + (bImage.getHeight()/2));
        g2d.drawImage(bImage, (int)locX, (int)locY, p);
        g2d.rotate(-1 * Math.toRadians(angle), locX + (bImage.getWidth()/2), locY + (bImage.getHeight()/2));

    }
}
