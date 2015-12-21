package GamePanels;
import Engine.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by sagi on 12/18/15.
 */
public class GamePanel extends JPanel implements Runnable{
//    private final String BASE_PATH = System.getProperty("user.dir") + File.separator +"src"+ File.separator;

//    private final String bgPath = BASE_PATH + "Images" + File.separator + "bg.gif";
//    private final String gameOverPath = BASE_PATH + "Images" + File.separator + "gameOver.png";
//    private final String startPath = BASE_PATH + "Images" + File.separator + "start.png";

    private final URL bgURL= getClass().getResource("/Images/bg2.gif");
    private final URL gameOverURL= getClass().getResource("/Images/gameOver.png");
    private final URL startURL= getClass().getResource("/Images/start.png");


    private int width, height;
    private GameEngine engine;
    private JLabel lbl_score, lbl_countDown, lbl_gameOver, lbl_bg, lbl_start;

    private ImageIcon img_bg, img_go, img_start;
    private Image bg_image;




    public GamePanel(int width, int height){
        this.setLayout(null);
        this.width = width;
        this.height = height;
        this.engine = new GameEngine(width, height);
        this.addKeyListener(engine);
        this.setFocusable(true);
        this.requestFocus();

        this.lbl_score = new JLabel("");
        lbl_score.setBounds(15,15,width,30);

        this.lbl_countDown = new JLabel("");
        lbl_countDown.setBounds(width/2,0,width,height);
        System.out.println("URL = " +bgURL);

        img_bg = new ImageIcon(bgURL);
        Image tmp_BG = img_bg.getImage();
        bg_image = tmp_BG.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        img_bg = new ImageIcon(tmp_BG);
        lbl_bg = new JLabel(img_bg);
        lbl_bg.setBounds(0,0,width,height);

        lbl_countDown.setFont(new Font("Ariel", Font.BOLD, 100));
        lbl_countDown.setForeground(Color.WHITE);
        this.add(lbl_countDown);

        img_start = new ImageIcon(startURL);
        lbl_start = new JLabel(img_start);
        lbl_start.setVisible(true);
        lbl_start.setBounds(0,0,width,height);
        this.add(lbl_start);

        img_go = new ImageIcon(gameOverURL);
        lbl_gameOver = new JLabel(img_go);
        lbl_gameOver.setVisible(false);
        lbl_gameOver.setBounds(0,0,width,height);
        this.add(lbl_gameOver);


        lbl_score.setFont(new Font("Ariel", Font.BOLD, 24));
        lbl_score.setForeground(Color.GREEN);
        this.add(lbl_score);
        repaint();

    }

    @Override
    public void run() {
        while(engine.gameOn){
            engine.update();

            if(engine.isGameOver()){
                this.lbl_countDown.setVisible(false);
                if(!engine.isFirstGame)
                    this.lbl_gameOver.setVisible(true);

            }else{
                this.lbl_gameOver.setVisible(false);
                this.lbl_countDown.setVisible(true);
                lbl_start.setVisible(false);

            }

            engine.render(this);
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addNotify(){
        super.addNotify();
        (new Thread(this)).start();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        g.drawImage(bg_image, 0, 0, this); //draw the background
        g2d.drawImage(engine.getScene(),0,0,this); //Draw the scene
        //Some labels - Score & countDown
        lbl_score.setText("SCORE : " + engine.getScore());
        if(engine.getCountDown() > 0){
            lbl_countDown.setText(engine.getCountDown()+"");
        }else if(engine.getCountDown() == 0){
            lbl_countDown.setText("GO!");
        }else{
            lbl_countDown.setText("");
        }
    }


}
