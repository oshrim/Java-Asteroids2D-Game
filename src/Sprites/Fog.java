package Sprites;



import javax.swing.*;
import java.awt.*;
import java.util.Random;



public class Fog extends Sprite {

    private double selfAngle, rotationSpeed;
    private float alphaChannel;
    private Random r;

    public Fog(int x, int y, int w, int h, int acc, String imgName, double angle, int size) {
        super(x, y, w, h, acc, imgName, angle, size);
        selfAngle = 0;
        r= new Random();
        rotationSpeed = 0;
        alphaChannel = r.nextFloat()-0.04f;
    }

    @Override
    public void update() {
        locX += acceleration * Math.cos(Math.toRadians(angle));
        locY -= acceleration * (-1 * Math.sin(Math.toRadians(angle)));
        outOfScreeFix();
        selfAngle+=rotationSpeed;
    }

    @Override
    public void drawSprite(Graphics g, JPanel p) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.rotate(Math.toRadians(selfAngle), locX + (bImage.getWidth()/2), locY + (bImage.getHeight()/2));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaChannel));
        g2d.drawImage(bImage, (int)locX, (int)locY, p);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        g2d.rotate(-1 * Math.toRadians(selfAngle), locX + (bImage.getWidth()/2), locY + (bImage.getHeight()/2));

    }
}
