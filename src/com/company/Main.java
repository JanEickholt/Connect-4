package com.company;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private Tehc logic;
    public Main() {
        logic = new Tehc();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFont(new Font("Roboto", Font.PLAIN, 20));
        setTitle("4row");
        setResizable(false);
        setBounds(300, 50, 750, 870);
        setContentPane(logic.contentPane);
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
