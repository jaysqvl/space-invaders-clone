public class enemy {
    
    public int x, y, health;
    public boolean right=true;
    public boolean left=false;
    
    /*
    private Image img;
    private BufferedImage ibi;
    private File f;
    */
    
    public enemy(int x, int y, int health) {
        this.x=x;
        this.y=y;
        this.health=health;
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
}
