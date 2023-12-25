package Controls;

import java.awt.*;
import javax.swing.JPanel;
import Entity.Player;
import Tile.TileManager;
import Object.SuperObject;
import Entity.Entity;

public class GamePanel extends JPanel implements Runnable {

    //Screen settings
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 4;
    public final int tileSize = originalTileSize * scale; // 64x64 tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 1024 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 704 pixels

    //WORLD SETTING
    public final int maxWorldCol = 20;
    public final int maxWorldRow = 20;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound sound = new Sound();

    public CollisionDetection cDetection = new CollisionDetection(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // ENTITY & OBJECTS
    public Player player = new Player (this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[10];

    // GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    public GamePanel(){
        this.setPreferredSize(new Dimension (screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        playMusic(0);
        gameState = playState;
    }
    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;

            }
            if (timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update(){
        if(gameState == playState) {
            player.update();

            for (int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
        }
        if(gameState == pauseState){
            //nothing
        }
//        for (int i = 0; i < npc.length; i++){
//            if(npc[i] != null){
//                npc[i].update();
//            }
//        }
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void stopMusic(){
        sound.stop();
    }
    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        int playerY = player.worldY;

        //Debug
        long drawStart = 0;
        if(keyH.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }

        //TILE
        tileM.draw(g2);
        //OBJECT
        for(int i = 0; i < obj.length; i++){
            if(obj[i] != null){
                obj[i].draw(g2,this);
            }
        }

        //Debug
        if(keyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.black);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }

        //PLAYER (add)
        player.draw(g2);

        //NPC
        for(int i = 0; i < npc.length; i++){
            if(npc[i] != null){
                int npcY = npc[i].worldY;
                if(playerY < npcY){
                    player.draw(g2);
                    npc[i].draw(g2);
                }
                else{
                    npc[i].draw(g2);
                    player.draw(g2);
                }
            }
        }

        //UI
        ui.draw(g2);
        g2.dispose();
    }
}