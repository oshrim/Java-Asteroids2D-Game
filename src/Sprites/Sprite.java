package  Sprites;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public abstract class Sprite {
    protected BufferedImage bImage;
    protected int imageWidth, imageHeight; // image dimensions

    protected double locX, locY;
    protected int acceleration;
    protected int pWidth, pHeight, size;  // panel's dimensions

    protected double angle;
    private final String BASE_PATH = System.getProperty("user.dir") + File.separator +"src"+ File.separator;


    public Sprite(int x, int y, int w, int h, int acc, String imgName, double angle, int size)
    {
        this.size = size;
        locX = x;
        locY = y;
        acceleration = acc;
        pWidth = w;
        pHeight = h;
        try {///home/sagi/Development/JCE/Astroids2D/src/Images/ship.gif
            bImage = ImageIO.read(getClass().getResource("/Images/"+imgName));
        }catch (IOException pin){
            pin.printStackTrace();
            bImage = null;
        }
        this.angle = angle;

        setImageDimensions();
    }


    public void setImageDimensions()
    {
        Image tmp = bImage.getScaledInstance(size,size, Image.SCALE_SMOOTH);
        BufferedImage bi = new BufferedImage(size,size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(tmp,0,0,null);
        g2d.dispose();
        bImage = bi;
    }

    public abstract void update();

//    public Rectangle getBoundingBox()
//    {
//        return new Rectangle(getLocX(), getLocY(), imageWidth, imageHeight);
//    }

    public double getLocX() {
        return locX;
    }

    public double getLocY() {
        return locY;
    }

    public int getAcceleration(){
        return acceleration;
    }

    public BufferedImage getbImage()
    {
        return bImage;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight()
    {
        return imageHeight;
    }

    public double getAngle(){
        return angle;
    }

    protected void outOfScreeFix(){ //its not a bug it's  a feature
        if(locX < 0 - size)
            locX = pWidth;
        else if (locX > pWidth+size)
            locX = 0-size;

        if(locY < 0 - size)
            locY = pHeight;
        else if(locY > pHeight+size)
            locY = 0-size ;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle((int)locX, (int)locY, size, size);
        return r;
    }

    public int getSize(){
        return size;
    }


    public abstract void drawSprite(Graphics g, JPanel p);


}
