import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Ball extends Sprite {

    private int speedX;
    private int speedY;
    private String direction;
    private boolean broken;
    private Color color;

    public Ball(int x, int y, int speedX, int speedY, String direction) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 20;
        this.speedX = speedX;
        this.speedY = speedY;
        this.direction = direction;
        this.broken = false;
        this.color = getRandomColor();
    }

    @Override
    public void draw(Graphics2D drawing) {
        drawing.fillOval(x, y, width, height);
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color getRandomColor() {

        Random random = new Random();

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return new Color(r, g, b);
    }

    /**
     * Move the ball according to its direction.
     */
    public void move() {

        if (Objects.equals(this.getDirection(), "haut à droite")) {
            this.setX(this.getX() + this.getSpeedX());
            this.setY(this.getY() - this.getSpeedY());
        } else if (Objects.equals(this.getDirection(), "haut à gauche")) {
            this.setX(this.getX() - this.getSpeedX());
            this.setY(this.getY() - this.getSpeedY());
        } else if (Objects.equals(this.getDirection(), "bas à droite")) {
            this.setX(this.getX() + this.getSpeedX());
            this.setY(this.getY() + this.getSpeedY());
        } else if (Objects.equals(this.getDirection(), "bas à gauche")) {
            this.setX(this.getX() - this.getSpeedX());
            this.setY(this.getY() + this.getSpeedY());
        }
    }

    /**
     * Manages collisions of the ball on the edges of the frame (assuming there is no need to handle collisions
     * at the bottom of the screen.
     *
     * @param frameWidth game frame width
     */
    public void manageCollisionsOnTheEdgesOfFrame(int frameWidth) {

        if ((this.getX() + this.getWidth()) >= frameWidth) {
            if (this.getDirection().contains("haut")) {
                this.setDirection("haut à gauche");
            } else {
                this.setDirection("bas à gauche");
            }
        } else if ((this.getX() + this.getWidth()) <= 0) {
            if (this.getDirection().contains("haut")) {
                this.setDirection("haut à droite");
            } else {
                this.setDirection("bas à droite");
            }
        }
        if ((this.getY() + this.getHeight()) <= 0) {
            if (this.getDirection().contains("gauche")) {
                this.setDirection("bas à gauche");
            } else {
                this.setDirection("bas à droite");
            }
        }
    }
}
