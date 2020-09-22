import java.awt.image.BufferedImage;

public class player {
    

    
    int x=260;
    int y=460;
    int health=5;
    int radius=20;
    
    public void moveLeft() {
        x=x-5;
    }
    
    public void moveRight() {
        x=x+5;
    }
    
}
