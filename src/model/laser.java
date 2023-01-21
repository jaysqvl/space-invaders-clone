package model;

public class laser {
    public int x;
    public int y;
    public int width=3;
    public int height=14;
    
    public laser(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public void shootUp() {
        y-=5;
    }
    
    public void shootDown() {
        y+=10;
    }
}