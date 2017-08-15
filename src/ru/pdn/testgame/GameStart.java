package ru.pdn.testgame;

import javax.swing.*;

public class GameStart {
    public static void main (String[] args) {
        GamePanel panel = new GamePanel();
        JFrame startFrame = new JFrame("BubbleShooter2D");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startFrame.setContentPane(panel);
        startFrame.pack();
        startFrame.setLocationRelativeTo(null); //позиция
        startFrame.setVisible(true); //видимость
        panel.start();
    }
}
