package model;

import java.awt.Rectangle;

public class rectLaser {
        public int x,y;
    
    public rectLaser(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public void shootUp() {
        y-=5;
    }
    
    public void shootDown() {
        y+=10;
    }
    
    public Rectangle bounds() {
        return (new Rectangle(x,y,3,14));
    }
}
