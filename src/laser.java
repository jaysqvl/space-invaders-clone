public class laser {
    int x;
    int y;
    int width=3;
    int height=14;
    
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