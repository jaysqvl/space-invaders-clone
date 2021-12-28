package model;

public class player {
    

    
    public int x=260;
    public int y=460;
    public int health=5;
    public static int radius=20;
    
    public void moveLeft() {
        x=x-5;
    }
    
    public void moveRight() {
        x=x+5;
    }
    
}
