package ui;

import model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameUI extends javax.swing.JFrame
        implements Runnable, KeyEventDispatcher, MouseListener, MouseMotionListener, ActionListener {

    //Game ui.GameUI Logic
    protected static int WSize = 500;
    Boolean running = false; //Checks that the game is running
    private Thread gameThread;
    private Image ib; // Image buffer
    private Graphics ibg; // to set to our image buffer's graphics object
    private int frames = 0;
    private int updates = 0;
    private String fps = "";

    //Keyboard inputs
    boolean key_d = false;
    boolean key_a = false;
    boolean key_space = false;

    //Object Class Variables
    ArrayList<enemy> aliens = new ArrayList<enemy>(); // Aliens array
    ArrayList<laser> laserArrayP = new ArrayList<laser>(); // model.laser Array from model.player
    ArrayList<laser> laserArrayE = new ArrayList<laser>(); // model.laser array from model.enemy
    ArrayList<rect> rectAliens = new ArrayList<rect>(); // Collision Box for Aliens
    ArrayList<rectLaser> rectArrayPL = new ArrayList<rectLaser>(); // Collision Box for Player Lasers
    ArrayList<rectLaser> rectArrayEL = new ArrayList<rectLaser>(); // Collision Box for Enemy Lasers
    public int numEnemiesX = 8; //# Columns
    public int numEnemiesY = 4; //# Rows
    int totalEnemies = numEnemiesX * numEnemiesY; //total enemies in game
    boolean shiftDown = false;
    public boolean enableShoot = true; //Delay for model.player to shoot one shot
    public boolean enableShootE = true; //Delay for enemies to randomly shoot
    int objectRadius = 30;
    int delay = 0; //Start delay counter for when model.player is able to shoot again
    int delayELasers = 0; //Start delay counter for when enemies are able to shoot again
    boolean collision = false;

    //Object Class Imaging
    public BufferedImage playerImage;
    public BufferedImage enemyImage;
    public BufferedImage laserImage;
    public String currentDir = System.getProperty("user.dir");

    //Test model.player
    player user = new player();
    rect userRect = new rect(user.x, user.y);

    public GameUI() {
        initComponents();
        setResizable(false);
        beginImageBuffer();
        fillEnemies();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        setLocationRelativeTo(null);
        addMouseListener(this);  //registers this frame to receive mouse clicks
        addMouseMotionListener(this); //register this frame to receive mouse motions  
        run();
        start();
    }

    public final void beginImageBuffer() {
        ib = drawPanel.createImage(drawPanel.getWidth(), drawPanel.getHeight());
        ibg = ib.getGraphics();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawPanel = new javax.swing.JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                myDrawCode(g);
            }

        };
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        buttonStart = new javax.swing.JMenuItem();
        buttonPause = new javax.swing.JMenuItem();
        buttonStop = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        buttonControls = new javax.swing.JMenuItem();
        buttonManual = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        drawPanel.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout drawPanelLayout = new javax.swing.GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
                drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 550, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
                drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE)
        );

        jMenu1.setText("Game");

        buttonStart.setText("Start");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });
        jMenu1.add(buttonStart);

        buttonPause.setText("Pause");
        buttonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPauseActionPerformed(evt);
            }
        });
        jMenu1.add(buttonPause);

        buttonStop.setText("Stop");
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopActionPerformed(evt);
            }
        });
        jMenu1.add(buttonStop);

        jMenuBar2.add(jMenu1);

        jMenu2.setText("Help");

        buttonControls.setText("Controls");
        buttonControls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonControlsActionPerformed(evt);
            }
        });
        jMenu2.add(buttonControls);

        buttonManual.setText("Manual");
        buttonManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonManualActionPerformed(evt);
            }
        });
        jMenu2.add(buttonManual);

        jMenuBar2.add(jMenu2);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(drawPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(drawPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Custom game loop 
    //(Created watching a video about the concept of gaming looping and the most effective implementations so not
    // entirely my idea but it's a modified version of the original Minecraft version)
    public final void run() {
        this.requestFocus();

        //separates the interval that the game updates and the interval which the game draws frames
        final int MAX_FRAMES_PER_SECOND = 60; // FPS
        final int MAX_UPDATES_PER_SECOND = 60; // UPS

        //1 Second has 1 billion (1 000 000 000) nano seconds
        //Calculates the draw and update intervals based on nanoseconds
        final double uOPTIMAL_TIME = 1000000000 / MAX_UPDATES_PER_SECOND;
        final double fOPTIMAL_TIME = 1000000000 / MAX_FRAMES_PER_SECOND;

        //Instantiates my global time variables
        double uDelta = 0, fDelta = 0;
        long startTime = System.nanoTime();

        long timer = System.currentTimeMillis();

        //Timer logic
        while (running) {
            long currentTime = System.nanoTime();
            uDelta += (currentTime - startTime);
            fDelta += (currentTime - startTime);
            startTime = currentTime;

            if (uDelta >= uOPTIMAL_TIME) {
                update();
                delay++;
                if (delay % 40 == 0) {
                    enableShoot = true;
                }
                updates++;
                uDelta -= uOPTIMAL_TIME;
            }

            if (fDelta >= fOPTIMAL_TIME) {
                drawPanel.repaint();
                frames++;
                fDelta -= fOPTIMAL_TIME;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("UPS: " + updates + ", FPS: " + frames);
                fps = ("UPS: " + updates + ", FPS: " + frames);
                updates = 0;
                frames = 0;
                timer += 1000;
            }
        }


        stop();

    }

    //Starts the thread
    public final synchronized void start() {
        if (running)
            return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //Stops the thread
    public final synchronized void stop() {
        if (!running)
            return;
        try {
            gameThread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Where the graphics game magic happens
    public void myDrawCode(Graphics g) {
        //Draw background

        ibg.setColor(Color.black);
        ibg.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());

        //Draw FPS
        ibg.setColor(Color.white);
        ibg.drawString(fps, 2, 10);

        //Draws each individual alien from the arraylist if they exist
        //Draws in the middle of the sprite
        if (aliens.size() > 0) {
            for (int k = 0; k < aliens.size(); k++) {
                if (aliens.get(k) != null) {
                    ibg.drawImage(enemyImage(), aliens.get(k).x - 15, aliens.get(k).y - 15, null);
                    //comment this line out if you don't want to see the alien hit boxes
                    //ibg.drawRect(aliens.get(k).x-15, aliens.get(k).y-15, 30, 30);
                }
            }
        }
        //Draws the lasers (if they exist) and from the middle of the model.laser
        if (laserArrayP.size() > 0) {
            for (int k = 0; k < laserArrayP.size(); k++) {
                ibg.drawImage(laserImage(), laserArrayP.get(k).x - 1, laserArrayP.get(k).y - 7, null);
            }
        }
        //Draws the lasers from the model.enemy (if they exist) and from the middle of the model.laser
        if (laserArrayE.size() > 0) {
            for (int k = 0; k < laserArrayE.size(); k++) {
                ibg.drawImage(laserImage(), laserArrayE.get(k).x - 1, laserArrayE.get(k).y - 7, null);
            }
        }

        //Draws the model.player wherever his position his
        ibg.drawImage(playerImage(), user.x - 15, user.y - 15, null);


        g.drawImage(ib, 0, 0, this);
    }

    public Image enemyImage() {
        String enemyDir = currentDir + "/src/data/enemy30.png";
        try {
            enemyImage = ImageIO.read(new File(enemyDir));
        } catch (IOException e) {
            System.out.println("Error reading file model.enemy!");
        }
        return enemyImage;
    }

    public Image laserImage() {
        String laserDir = currentDir + "/src/data/laser10.png";
        try {
            laserImage = ImageIO.read(new File(laserDir));
        } catch (IOException e) {
            System.out.println("Error reading file model.laser!");
        }
        return laserImage;
    }

    public Image playerImage() {
        String playerDir = currentDir + "/src/data/ship30.png";
        try {
            playerImage = ImageIO.read(new File(playerDir));
        } catch (IOException e) {
            System.out.println("Error reading file model.player!");
        }
        return playerImage;
    }

    //Updates positions of the sprites and character based on key press etc
    //My game logic code
    //My game loop already updates this section of code
    public void update() {
        collision();
        shootLasersE();
        updateLasersE();
        updateLasersP();
        updateEnemies();
        keyboardCheck();
    }

    public void collision() {
        if (rectArrayPL.size() > 0) {
            for (int k = 0; k < rectArrayPL.size(); k++) {
                if (rectArrayPL.get(k) != null) {
                    Rectangle rectangle1 = rectArrayPL.get(k).bounds();
                    for (int i = 0; i < rectAliens.size(); i++) {
                        if (rectAliens.get(i) != null) {
                            Rectangle rectangle2 = rectAliens.get(i).bounds();
                            if (rectangle1.intersects(rectangle2)) {
                                System.out.println("Collision");
                                laserArrayP.remove(k);
                                rectArrayPL.remove(k);
                                aliens.set(i, null);
                                rectAliens.set(i, null);
                            }
                        }
                    }
                }
            }
        }
    }

    //Checks for collisions and/or updates their position;
    public void updateLasersP() {
        if (laserArrayP.size() > 0) {
            for (int k = 0; k < laserArrayP.size(); k++) {
                laser laserUpdate = laserArrayP.get(k);
                rectLaser laserRectUpdate = rectArrayPL.get(k);
                if (laserUpdate.y <= 0 || laserUpdate.y >= 500) {
                    laserArrayP.remove(k);
                    rectArrayPL.remove(k);
                } else {
                    laserUpdate.shootUp();
                    laserRectUpdate.y = laserUpdate.y;
                }
            }
        }
    }

    public void shootLasersE() {
        if (laserArrayE.size() >= 0 && laserArrayE.size() < 8) {
            int size = aliens.size();
            int rand = (int) (Math.random() * size) + 0;
            if (aliens.get(rand) != null) {
                laser newLaserE = new laser(aliens.get(rand).x, aliens.get(rand).y);
                rectLaser newLaserRectE = new rectLaser(aliens.get(rand).x, aliens.get(rand).y);
                laserArrayE.add(newLaserE);
                rectArrayEL.add(newLaserRectE);
            }
        }
    }

    public void updateLasersE() {
        if (laserArrayE.size() > 0) {
            for (int k = 0; k < laserArrayE.size(); k++) {
                laserArrayE.get(k).shootUp();
                rectArrayEL.get(k).x = laserArrayE.get(k).x;
            }
        }
    }

    //Updates the enemies's positions 60 times a second
    public void updateEnemies() {
        int shifterX = 1;
        int shifterY = 2;

        //Has to check for potential out of bounds objects before actually updating their positions
        for (int k = 0; k < totalEnemies; k++) {
            if (aliens.get(k) != null) {
                enemy checkMe = aliens.get(k);
                rect checkPos = rectAliens.get(k);

                if (checkMe.x >= 535 || checkPos.x >= 535) {
                    for (int i = 0; i < totalEnemies; i++) {
                        if (aliens.get(i) != null) {
                            enemy updateMe = aliens.get(i);
                            rect updatePos = rectAliens.get(i);
                            updateMe.right = false;
                            updateMe.left = true;
                            updateMe.decreaseY(shifterY);
                            updatePos.y = updateMe.y - 15;
                        }
                    }
                } else if (checkMe.x <= 0 || checkPos.x <= 0) {
                    for (int j = 0; j < totalEnemies; j++) {
                        if (aliens.get(j) != null) {
                            enemy updateMe = aliens.get(j);
                            rect updatePos = rectAliens.get(j);
                            updateMe.left = false;
                            updateMe.right = true;
                            updateMe.decreaseY(shifterY);
                            updatePos.y = updateMe.y - 15;
                        }
                    }
                }
            }
        }

        //Finally updates both the Rectangle for collisions as well as the positions of the aliens
        for (int f = 0; f < totalEnemies; f++) {
            if (aliens.get(f) != null) {
                enemy position = aliens.get(f);
                rect pos = rectAliens.get(f);
                if (position.right == true && position.left == false) {
                    position.increaseX(shifterX);
                    pos.x = position.x - 15;
                } else if (position.right == false || position.left == true) {
                    position.decreaseX(shifterX);
                    pos.x = position.x - 15;
                }
            }
        }
    }

    //fills the model.enemy array with objects of the type model.enemy class
    //Should only do this once at the start of the game
    public void fillEnemies() {
        //Initial separation distance between each model.enemy
        int separateX = 55;
        int separateY = 50;
        int initialX = 40;
        int startX = 40;
        int startY = 30;
        int health = 1;

        for (int k = 0; k < totalEnemies; k++) {
            enemy alien = new enemy(startX, startY, health);
            rect enemyRect = new rect(startX, startY);
            rectAliens.add(enemyRect);
            aliens.add(alien);
            startX += separateX;
            int mod = (k + 1) % (numEnemiesX);
            //increases the Y separation every so often.
            //You have to reset X to starting X when moving down
            if (mod == 0) {
                startY += separateY;
                startX = initialX;
            }
        }

        //Collision box
        //Have to use rectangle class for collision detection
        //For each model.enemy, there must be a rectangle at its position to check to see if the model.enemy got hit
        
        /*
        for (int i=0; i<aliens.size(); i++) {
            Rectangle attachMe = new Rectangle(aliens.get(i).x, aliens.get(i).y, 30,30);
            
            rectArrayE.add(attachMe);
        }
        */
    }

    public void shootLaser() {
        laser newLaser = new laser(user.x + 15, user.y - 14);
        rectLaser newLaserRect = new rectLaser(user.x + 15, user.y - 14);
        laserArrayP.add(newLaser);
        rectArrayPL.add(newLaserRect);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

    //Keyboard bindings
    public void keyboardCheck() {
        if (key_d == true) {
            if (user.x >= 535) {
                user.x = 535;
            } else {
                user.moveRight();
            }
        }
        if (key_a == true)
            if (user.x <= 15) {
                user.x = 15;
            } else {
                user.moveLeft();
            }
        if (key_space == true) {
            if (enableShoot == true) {
                shootLaser();
                enableShoot = false;
                key_space = false;
            }
        }
    }

    /* the KeyEvent dispatcher interface requires we have this method.  This method gets called whenever
     * a key is pressed or released.  You can easily check what is happening and which key.
     * Notice what we are doing here.  We remember the state of the key/s with those booleans.
     * This is just one way to do it.  If you only want to do something when a key is actually pressed,
     * then just ask if statements about the keys and you don't have to use the booleans.  */
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == e.VK_D) key_d = true;
            else if (e.getKeyCode() == e.VK_A) key_a = true;
            else if (e.getKeyCode() == e.VK_SPACE) key_space = true;
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == e.VK_D) key_d = false;
            else if (e.getKeyCode() == e.VK_A) key_a = false;
            else if (e.getKeyCode() == e.VK_SPACE) key_space = false;
        }
        return false;
    }

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPauseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonPauseActionPerformed

    private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonStopActionPerformed

    private void buttonControlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonControlsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonControlsActionPerformed

    private void buttonManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonManualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonManualActionPerformed

    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem buttonControls;
    private javax.swing.JMenuItem buttonManual;
    private javax.swing.JMenuItem buttonPause;
    private javax.swing.JMenuItem buttonStart;
    private javax.swing.JMenuItem buttonStop;
    private javax.swing.JPanel drawPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar2;
    // End of variables declaration//GEN-END:variables

}