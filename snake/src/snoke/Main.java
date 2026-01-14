package snoke;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main{

    public static void main(String[] args) {

        SwingUtilities.invokeLater(()->{

            JFrame frame = new JFrame("Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            MovementPanel panel = new MovementPanel();

            System.out.println(panel.ENEMY_SPAWN_INTERVAL);

            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.start();


        });


    }

}