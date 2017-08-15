package ru.pdn.testgame;

import com.sun.deploy.panel.JavaPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JavaPanel implements Runnable{

    //Field
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    private Thread thread = new Thread(this);

    private BufferedImage image; //холст
    private Graphics2D g; //кисточка

    private int FPS;
    private double millisToFPS;
    private long timerFPS;
    private int sleepTime;

    private enum STATES {MENU, PLAY}
    private STATES state = STATES.MENU;

    public static GameBack background; //background
    public static Player player; //player
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;
    public static Wave wave;
    public static Menu menu;



    //Constructor
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        addKeyListener(new Listeners());
        addMouseMotionListener(new Listeners());
        addMouseListener(new Listeners());
    }

    //Functions
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        FPS = 30;
        millisToFPS = 1000 / FPS;
        sleepTime = 0;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //сглаживание

        background = new GameBack();
        player = new Player();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        wave = new Wave();
        menu = new Menu();

        ////////////////////////////////////// Убрать
        //enemies.add(new Enemy(1,1));
        //enemies.add(new Enemy(1,1));
        ///////////////////////////////////////

        while (true){
            timerFPS = System.nanoTime();

            if(state.equals(STATES.MENU)) {
                background.update();
                background.draw(g);
                menu.draw(g);
                gameDraw();
            }
            if(state.equals(STATES.PLAY)) {
                gameUpdate();
                gameRender();
                gameDraw();
            }



            timerFPS = (System.nanoTime() - timerFPS) / 1000000;
            if(millisToFPS > timerFPS) {
                sleepTime = (int) (millisToFPS - timerFPS);
            }else sleepTime = 1;

            try {
                thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timerFPS = 0;
            sleepTime = 1;
        }
    }

    public void gameUpdate() {
        //Background update
        background.update();
        //Player update
        player.update();
        //Bullet update
        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).update();
            boolean remove = bullets.get(i).remove();
            if(remove) {
                bullets.remove(i);
                i--;
            }
        }
        //Enemies update
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }
        //Bullets-enemies collide
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();
            for(int j = 0; j < bullets.size();j++) {
                Bullet b = bullets.get(j);
                double bx = b.getX();
                double by = b.getY();
                double dx = ex - bx;
                double dy = ey - by;
                double dist = Math.sqrt(dx * dx + dy * dy );
                if((int)dist <= e.getR() + b.getR()) {
                    e.hit();
                    bullets.remove(j);
                    j--;
                    boolean remove = e.remove();
                    if(remove) {
                        enemies.remove(i);
                        i--;
                    }
                    break;
                }
            }
        }
        //Player-enemy collide
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();

            double px = player.getX();
            double py = player.getY();

            double dx = ex - px;
            double dy = ey - py;

            double dist = Math.sqrt(dx *dx + dy * dy);
            if((int) dist <= e.getR() + player.getR()) {
                e.hit();
                player.hit();
                boolean remove = e.remove();
                if(remove) {
                    enemies.remove(i);
                    i--;
                }
            }
        }
        //Wave update
        wave.update();
    }

    public void gameRender() {
        //Background render
        background.draw(g);
        //Player render
        player.draw(g);
        //Bullet render
        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }
        //Enemies draw
        for (int i = 0; i < enemies.size();i++) {
            enemies.get(i).draw(g);
        }
        //Wave draw
        if(wave.showWave()) {
            wave.draw(g);
        }
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose(); //чистим сборщиком
    }
}

