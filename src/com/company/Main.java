package com.company;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() throws Exception {
        Tehc logic = new Tehc();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFont(new Font("Roboto", Font.PLAIN, 20));
        setTitle("Connect 4");
        setIconImage(new ImageIcon(Tehc.image_folder + "\\AppIcon.png").getImage());
        setResizable(false);
        setBounds(300, 50, 750, 830);
        setContentPane(logic.content_pane);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
