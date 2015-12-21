package Sprites;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by sagi on 12/18/15.
 */
public class Ship extends Sprite {

    private final int STOP=0, UP=1, DOWN=-1, TURN_SPEED=10;
    private final double MAX_SPEED = 6, SLOWING_FACTOR = 0.1;
    private int direction, turnDirection;
    private BufferedImage bi;
    private double selfAccel;


    public Ship(int x, int y, int w, int h, int size) {
        super(x, y, w, h, 0, "ship2.png", 0, size);
        direction = STOP;
        turnDirection = STOP;
        selfAccel = acceleration;

    }

    @Override
    public void update() {
        setSpeed();
        this.angle+=TURN_SPEED*turnDirection;
        locX += selfAccel * Math.cos(Math.toRadians(angle));
        locY -= selfAccel * (-1 * Math.sin(Math.toRadians(angle)));
        outOfScreeFix();

    }

    private void setSpeed(){
        if (direction == UP && !(selfAccel > MAX_SPEED)){
            selfAccel+=SLOWING_FACTOR*2;
        }
        else if (direction == DOWN && (selfAccel > MAX_SPEED*(-1))){
            selfAccel-=SLOWING_FACTOR*2;
        }
        else { //slowing down
            if (selfAccel > 0) {
                selfAccel -= SLOWING_FACTOR;
                if (selfAccel < 0) {
                    selfAccel = 0;
                }
            }
            if (selfAccel < 0) {
                selfAccel += SLOWING_FACTOR;
                if (selfAccel > 0) {
                    selfAccel = 0;
                }
            }
        }
    }

    public void setDirection(int direction){
        this.direction = direction;
    }


    public void drawSprite(Graphics g, JPanel panel) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.rotate(Math.toRadians(angle), locX + (bImage.getWidth()/2), locY + (bImage.getHeight()/2));
        g2d.drawImage(bImage, (int)locX, (int)locY, panel);
        g2d.rotate(-1*Math.toRadians(angle), locX + (bImage.getWidth()/2), locY + (bImage.getHeight()/2));

    }
    public void turnShip(int direction){
        turnDirection=direction;
    }

}
