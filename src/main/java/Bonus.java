import java.awt.*;

public class Bonus extends Sprite {

    private int speedX;
    private int speedY;
    private boolean taken;
    private String type;

    public Bonus(int x, int y, int speedX, int speedY, String type) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 20;
        this.speedX = speedX;
        this.speedY = speedY;
        this.taken = false;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    @Override
    public void draw(Graphics2D drawing) {
        drawing.fillRect(x, y, width, height);
    }
}
