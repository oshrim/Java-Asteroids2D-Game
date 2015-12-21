package Engine;

import Sprites.*;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;
import java.util.Vector;

/**
 * Created by sagi on 12/18/15.
 */



public class GameEngine implements KeyListener {
    private final int LARGE=100, MEDIUM=50, SMALL=25, NUM_OF_LIVES = 3, LASER_SPEED = 10, ASTROID_SPEED = 3;
    private final int STOP=0, UP=1, DOWN=-1;
    public boolean gameOn,fireLock, gameOver, isFirstGame;
    private Ship player;
    private Vector<Sprite> astroidsSprites,laserSprites;
    private int pWidth, pHeight;
    private Timer astroidTimer, fireTimer, startTime;
    private Random r;
    private int score, countDown;
    private Vector<Sprite> lives, fogs;
    private AudioClip laserAudioClip;
    private BufferedImage sceneImage;

    private AudioClip themeAudioClip;
    private final URL laserURL= getClass().getResource("/Sounds/laser.wav");
    private final URL themeURL= getClass().getResource("/Sounds/theme.wav");

    public GameEngine(int width, int height){
        this.isFirstGame = true;
        this.gameOver = true;
        this.pWidth = width;
        this.pHeight = height;
        try {
            laserAudioClip = Applet.newAudioClip(laserURL);
            themeAudioClip = Applet.newAudioClip(themeURL);
        }catch (Exception e){
            laserAudioClip = null;
            themeAudioClip = null;
        }
        if(themeAudioClip != null)
            themeAudioClip.loop();
        fogs = new Vector<>();
        r = new Random();
        sceneImage = new BufferedImage(width, height, Image.SCALE_SMOOTH);
        initFogs();
        startNewGame();
    }

    private void initFogs(){
        for(int i = 0 ; i < 4 ; i++){
            fogs.add(new Fog((-1)*r.nextInt(),(-1)*r.nextInt(),pWidth,pHeight,1,"Fog.png",r.nextInt(360),pWidth));
        }
    }

    private void startNewGame(){
        score = 0;
        lives = new Vector<>();
        setupLives(NUM_OF_LIVES);
        astroidTimer = new Timer(5000, new astroidTimerListener()) ;
        fireTimer = new Timer(500, new fireTimerListener()) ;
        startTime = new Timer(1000, new countDownListener());
        fogs.remove(fogs.size()-1);
        initGame();
    }

    private void initGame(){
        this.countDown = 3;
        astroidsSprites = new Vector<>();
        laserSprites = new Vector<>();
        this.player = new Ship(pWidth/2, pHeight/2,pWidth,pHeight, MEDIUM);
        gameOn = true;
        fireTimer.start();
        astroidTimer.start();

        for (int i =0 ; i<4 ; i++)
            createAstroid();
    }

    private void setupLives(int num){
        int size = 30;
        for(int i = 0 ; i < num ; i++){
            lives.add(new Ship(pWidth - ((i+1)*size), 15, pWidth, pHeight, size));
        }
    }

    public int getScore(){
        return score;
    }

    public boolean isGameOver(){
        return this.gameOver;
    }

    public int getCountDown(){
        return countDown;
    }

    private void createAstroid(){
        astroidsSprites.add(new Astroid((-1)*r.nextInt(),(-1)*r.nextInt(),pWidth,pHeight,ASTROID_SPEED,"astroid.png",r.nextInt(360),LARGE));
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(gameOn) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_UP:
                    player.setDirection(UP);
                    break;
                case KeyEvent.VK_DOWN:
                    player.setDirection(DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    player.turnShip(DOWN);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.turnShip(UP);
                    break;
                case KeyEvent.VK_SPACE:
                    if (!fireLock && countDown <= 0) {
                        if(laserAudioClip != null)
                            laserAudioClip.loop();
                        laserSprites.add(new LaserBlast((int) player.getLocX() + (player.getImageWidth() / 2),
                                (int) player.getLocY() + (player.getImageHeight() / 2),
                                pWidth,
                                pHeight,
                                LASER_SPEED,
                                "laser2.gif",
                                player.getAngle(),
                                SMALL));
                        fireLock = true;
                    }

                    break;
                case KeyEvent.VK_F2:
                    this.isFirstGame = false;
                    gameOver = false;
                    startNewGame();
                    startTime.start();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(gameOn){
            switch (keyEvent.getKeyCode()){
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    player.setDirection(STOP);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_LEFT:
                    player.turnShip(STOP);
                    break;
                case KeyEvent.VK_SPACE:
                    laserAudioClip.stop();
                    break;
                default:
                    break;

            }
        }


    }

    public void update(){
        for (Sprite sprite : fogs) {
            sprite.update();
        }
        if(!gameOver) {
            if (countDown > 0)
                return;
        }
        else
            return;

        player.update();
        for (Sprite sprite : astroidsSprites) {
            sprite.update();
        }
        for (Sprite sprite : laserSprites) {
            sprite.update();
        }


        collisionHandler();


    }

    private void collisionHandler(){

        Vector<Sprite> newAstroids = new Vector<>();
        Vector<Sprite> removeLasers = new Vector<>();
        Vector<Sprite> removeAstroids = new Vector<>();

        for (int i = 0; i<laserSprites.size(); i++){
            for (int j = 0; j<astroidsSprites.size(); j++){

                if(CollisionUtil.collidesWith(laserSprites.get(i), astroidsSprites.get(j))) {
                    score+=100;
                    if (astroidsSprites.get(j).getSize() == LARGE) {
                        for (int k = 0; k < 2; k++) {
                            newAstroids.add(new Astroid((int) astroidsSprites.get(j).getLocX(), (int) astroidsSprites.get(j).getLocY(), pWidth, pHeight, astroidsSprites.get(j).getAcceleration(), "astroid.png", r.nextInt(360), MEDIUM));
                        }
                    } else if (astroidsSprites.get(j).getSize() == MEDIUM) {
                        for (int h = 0; h < 2; h++) {
                            newAstroids.add(new Astroid((int) astroidsSprites.get(j).getLocX(), (int) astroidsSprites.get(j).getLocY(), pWidth, pHeight, astroidsSprites.get(j).getAcceleration(), "astroid.png", r.nextInt(360), SMALL));
                        }
                        score+=100;
                    }else{
                        score+=200;
                    }

                    removeLasers.add(laserSprites.get(i));
                    removeAstroids.add(astroidsSprites.get(j));
                }
            }
        }

        for(int i = 0 ; i < astroidsSprites.size() ; i++){
            if(CollisionUtil.collidesWith(astroidsSprites.get(i), player)){
                if(lives.size() == 1){
                    gameOver = true;
                    fogs.add(new Fog(pWidth,0,pWidth,pHeight,1,"Fog.png",180,pWidth));
                }
                if(lives.size() > 0)
                    lives.remove(lives.size()-1);
                initGame();
                System.out.println("==============GAME OVER==============");
            }
        }



        astroidsSprites.addAll(newAstroids);

        laserSprites.removeAll(removeLasers);
        astroidsSprites.removeAll(removeAstroids);
    }

    public void render(JPanel panel){
        sceneImage = new BufferedImage(this.pWidth, this.pHeight, Image.SCALE_FAST);
        renderScene(sceneImage.getGraphics(), panel);
    }

    public void renderScene(Graphics g, JPanel panel){
        if(!gameOver)
            player.drawSprite(g, panel);
        try {
            for (Sprite sprite : astroidsSprites) {
                sprite.drawSprite(g, panel);
            }
            for (Sprite sprite : lives) {
                sprite.drawSprite(g, panel);
            }
            for (Sprite sprite : laserSprites) {
                sprite.drawSprite(g, panel);
            }
            for (Sprite sprite : fogs) {
                sprite.drawSprite(g, panel);
            }
        }catch (Exception e)
        {

        }
    }


private class astroidTimerListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        createAstroid();
    }
}
private class fireTimerListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        fireLock = false;
    }
}

    private class countDownListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            countDown--;

        }
    }

    public BufferedImage getScene(){
        return this.sceneImage;
    }


}
