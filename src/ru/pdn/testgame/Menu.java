package ru.pdn.testgame;

import java.awt.*;

public class Menu {
    //Fields
    private int buttonWidth, buttonHeight;
    private Color color1;
    private String s;

    //Constructor
    public Menu() {
         buttonWidth = 120;
         buttonHeight = 60;

         color1 = Color.white;
         s = "Play!";
    }
    //Functions
    public void draw(Graphics2D g) {
        g.setColor(color1);
        g.setStroke(new BasicStroke(3));
        g.drawRect(GamePanel.WIDTH/2 - buttonWidth/2,
                GamePanel.HEIGHT/2 - buttonHeight/2, buttonWidth, buttonHeight);
        g.setStroke(new BasicStroke(1));
        g.setColor(color1);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        long lenght = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.drawString(s, (int) ((GamePanel.WIDTH / 2) - (lenght / 2)), (int) ((GamePanel.HEIGHT / 2) + buttonHeight/4));
    }
}
