import javax.swing.*;
import GamePanels.*;

/**
 * Created by sagi on 12/18/15.
 */
public class Main {

    public static void main(String args[]){

        JFrame frame = new JFrame("Asteroids 2D - Star Wars Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.add(new GamePanel(800,600));
        frame.setVisible(true);
    }
}
