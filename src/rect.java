import java.awt.Rectangle;
        
public class rect {
    int x,y;
    
    public rect(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    //increases X by a set distance (10 pixels) every update
    //Boundaries exist so the aliens are never off screen
    public void increaseX(int shift) {
        x+=shift;
    }
    
    public void decreaseX(int shift) {
        x-=shift;
    }
    
    //This method is only called if the aliens change directions
    public void increaseY(int shift) {
        y-=shift;
    }
    
    public void decreaseY(int shift) {
        y+=shift;
    }
    
    public Rectangle bounds() {
        return (new Rectangle(x,y,30,30));
    }
}
