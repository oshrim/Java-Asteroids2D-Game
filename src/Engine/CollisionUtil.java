package Engine;

import Sprites.Sprite;

import java.awt.*;
import java.awt.image.PixelGrabber;

/**
 * Created by sagi on 12/19/15.
 */
public  class CollisionUtil {

    /**
     * Checks if a Sprite is colliding with another Sprite.
     * @param otherSprite The Sprite to check a collission with.
     * @param pixelPerfect If true, it will use a pixel-perfect algorithm. If
     * false, it only checks its bounding box.
     * @return true if the Sprites collide, otherwise false.
     */
    public static boolean collidesWith(Sprite thisSprite, Sprite otherSprite) {
        boolean isColliding=false;

        Rectangle r1 = thisSprite.getBounds();
        Rectangle r2 = otherSprite.getBounds();

        r1.intersection(r2);

        if(intersection(r1, r2)) {
                isColliding = pixelPerfectCollision(thisSprite, otherSprite, r1, r2);
        }
        return isColliding;
    }

    private static boolean intersection(Rectangle r, Rectangle d) {
        int rect1x = r.x;
        int rect1y = r.y;
        int rect1w = r.width;
        int rect1h = r.height;

        int rect2x = d.x;
        int rect2y = d.y;
        int rect2w = d.width;
        int rect2h = d.height;

        return (rect1x + rect1w >= rect2x &&
                rect1y + rect1h >= rect2y &&
                rect1x <= rect2x + rect2w &&
                rect1y <= rect2y + rect2h);
    }


    /*
     *  pixelPerfectCollision(); first determines the area where the sprites collides
     *  AKA the collision-rectangle. It then grabs the pixels from both sprites
     *  which are inside the rectangle. It then checks every pixel from the arrays
     *  given by grabPixels();, and if 2 pixels at the same position are opaque,
     *  (alpha value over 0) it will return true. Otherwise it will return false.
     */
    private static boolean pixelPerfectCollision(Sprite sprite1,Sprite sprite2, Rectangle r1, Rectangle r2) {
        /*
         * Get the X-values and Y-values for the two coordinates where the sprites collide
         */

        int cornerTopX = (r1.x>r2.x)?r1.x:r2.x;
        int cornerBottomX = ((r1.x+r1.width) < (r2.x+r2.width))?(r1.x+r1.width):(r2.x+r2.width);

        int cornerTopY = (r1.y>r2.y)?r1.y:r2.y;
        int cornerBottomY = ((r1.y+r1.height) < (r2.y+r2.height))?(r1.y+r1.height):(r2.y+r2.height);

        //Determine the width and height of the collision rectangle
        int width=cornerBottomX-cornerTopX;
        int height=cornerBottomY-cornerTopY;

        //Create arrays to hold the pixels
        int[] pixels1 = new int[width*height];
        int[] pixels2 = new int[width*height];

        //Create the pixelgrabber and fill the arrays
        PixelGrabber pg1 = new PixelGrabber(sprite1.getbImage(), cornerTopX-(int)sprite1.getLocX(), cornerTopY-(int)sprite1.getLocY(), width, height, pixels1, 0, width);
        PixelGrabber pg2 = new PixelGrabber(sprite2.getbImage(), cornerTopX-(int)sprite2.getLocX(), cornerTopY-(int)sprite2.getLocY(), width, height, pixels2, 0, width);

        //Grab the pixels
        try {
            pg1.grabPixels();
            pg2.grabPixels();
        } catch (InterruptedException ex) {
            //Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Check if pixels at the same spot from both arrays are not transparent.
        for(int i=0;i<pixels1.length;i++) {
            int a = (pixels1[i] >>> 24) & 0xff;
            int a2 = (pixels2[i] >>> 24) & 0xff;

            /* Awesome, we found two pixels in the same spot that aren't
             * completely transparent! Thus the sprites are colliding!
             */
            if(a > 0 && a2 > 0) return true;

        }

        return false;
    }



}
