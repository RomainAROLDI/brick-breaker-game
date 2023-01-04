import java.awt.*;

public class Brick extends Sprite {

    private boolean broken;
    private int state;

    public Brick(int x, int y, int width, int height, int state) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.broken = false;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    @Override
    public void draw(Graphics2D drawing) {
        drawing.fillRect(x, y, width, height);
    }
}