import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;

public class Game extends Canvas {

    private final ArrayList<Brick> bricks;
    private final Paddle paddle;
    private final ArrayList<Ball> balls;
    private static int lives;
    private final ArrayList<Bonus> bonuses;
    private final static int frameWidth = 800;
    private final static int frameHeight = 800;
    private final static int ballX = 390;
    private final static int ballY = 678;
    private final static int ballSpeedX = 3;
    private final static int ballSpeedY = 4;
    private final static int paddleX = 350;
    private final static int paddleY = 700;

    public Game(ArrayList<Brick> bricks, Paddle paddle, ArrayList<Ball> balls, int lives) throws InterruptedException {

        this.bricks = bricks;
        this.paddle = paddle;
        this.balls = balls;
        Game.lives = lives;
        this.bonuses = new ArrayList<>();

        JFrame window = new JFrame("Casse-briques");
        JPanel gamePanel = (JPanel) window.getContentPane();
        gamePanel.setPreferredSize(new Dimension(frameWidth, frameHeight));
        setBounds(0, 0, frameWidth, frameHeight);
        gamePanel.add(this);
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.requestFocusInWindow();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.requestFocus();
        createBufferStrategy(2);
        setIgnoreRepaint(true);
        setFocusable(true);
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 37) {
                    if (paddle.getX() + paddle.getWidth() <= 0) {
                        paddle.setX(frameWidth - paddle.getWidth());
                    } else {
                        paddle.setX(paddle.getX() - paddle.getSpeed());
                    }
                } else if (e.getKeyCode() == 39) {
                    if (paddle.getX() >= frameWidth) {
                        paddle.setX(0);
                    } else {
                        paddle.setX(paddle.getX() + paddle.getSpeed());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        start();
    }

    /**
     * Generate the bricks and the ball then launch the game.
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        ArrayList<Ball> balls = new ArrayList<>();
        balls.add(new Ball(ballX, ballY, ballSpeedX, ballSpeedY, getRandomDirection(true)));

        new Game(generateBricks(frameWidth, frameHeight, 100, 40, 4, 6),
                new Paddle(paddleX, paddleY, 100, 10, 20), balls, 3);
    }

    private int update() {

        checkCollisions();
        checkBonus();
        return checkGameOver();
    }

    /**
     * For each ball, manage collisions on the edges of the screen as well as on the paddle to make it bounce correctly.
     * This function also checks if a brick has been hit.
     * If so, change the state of the brick and activate a penalty or a bonus randomly.
     */
    private void checkCollisions() {

        // for each ball:
        for (Ball ball : balls) {

            // manages collisions of the ball on the edges of the frame
            ball.manageCollisionsOnTheEdgesOfFrame(frameWidth);

            // if the ball hits the paddle:
            if (ball.getY() + ball.getHeight() >= paddle.getY() &&
                    ball.getX() + ball.getWidth() >= paddle.getX() &&
                    ball.getX() <= paddle.getX() + paddle.getWidth()) {

                // assign a random upward direction (left or right)
                ball.setDirection(getRandomDirection(true));
            }

            // for each brick:
            for (Brick brick : bricks) {

                // if the brick is not broken:
                if (!brick.isBroken()) {
                    // if one ball hit the brick:
                    if (!ball.isBroken() && ball.getY() <= brick.getY() + brick.getHeight() &&
                            ball.getX() + ball.getWidth() >= brick.getX() &&
                            ball.getX() <= brick.getX() + brick.getWidth()) {

                        // change the direction of the ball from the direction it had before hitting the brick
                        if (ball.getDirection().contains("droite")) {
                            ball.setDirection("bas à droite");
                        } else {
                            ball.setDirection("bas à gauche");
                        }

                        // if the state of the brick is 0:
                        if (brick.getState() == 0) {
                            // change it to 1
                            brick.setState(1);
                        } else {

                            // add bonus or ball speed increase randomly
                            int r = (int) Math.round(Math.random() * 11);
                            if (r > 3) {
                                if (r > 7) {
                                    ball.setSpeedX(ball.getSpeedX() + 1);
                                    if (r == 10) {
                                        bonuses.add(new Bonus(brick.getX(), 0, 1, 3, "slow"));
                                    }
                                } else {
                                    ball.setSpeedY(ball.getSpeedY() + 1);
                                }
                            } else if (r > 1) {
                                bonuses.add(new Bonus(brick.getX(), 0, 1, 3, "ball"));
                            } else {
                                bonuses.add(new Bonus(brick.getX(), 0, 1, 3, "paddle"));
                            }

                            // break the brick
                            brick.setBroken(true);
                        }

                        break;
                    }
                }
            }

            // move the ball according to its direction
            ball.move();
        }
    }

    /**
     * Check the current state of the game.
     *
     * @return state of the game (0 for game over, 1 for lose one life, 2 for win game and 3 for continue normally)
     */
    private int checkGameOver() {

        // count the number of balls in play
        int ballsInGame = 0;

        for (Ball ball : balls) {
            if (!ball.isBroken()) {
                if (ball.getY() > (paddle.getY() + paddle.getHeight() / 2)) {
                    ball.setBroken(true);
                } else {
                    ballsInGame++;
                }
            }
        }

        // if there are no more balls in play:
        if (ballsInGame == 0) {

            // subtracts a life
            lives--;

            // if there is no more life:
            if (lives == 0) {
                System.out.println("Game over");
                return 0;
            } else {

                // reset ball and paddle, and removes the potential bonuses that were falling
                balls.get(0).setX(ballX);
                balls.get(0).setY(ballY);
                balls.get(0).setDirection(getRandomDirection(true));
                balls.get(0).setBroken(false);
                paddle.setX(paddleX);
                paddle.setY(paddleY);

                if (!bonuses.isEmpty()) {
                    for (Bonus bonus : bonuses) {
                        if (!bonus.isTaken()) {
                            bonus.setTaken(true);
                        }
                    }
                }

                return 1;
            }
        }

        // count the number of bricks in play
        boolean allBricksBroken = true;
        for (Brick brick : bricks) {
            if (!brick.isBroken()) {
                allBricksBroken = false;
                break;
            }
        }

        // if all the bricks are broken, the player wins, otherwise the game continues
        if (allBricksBroken) {
            System.out.println("GG You win!");
            return 2;
        } else {
            return 3;
        }
    }

    public static int getLives() {
        return lives;
    }

    private void start() throws InterruptedException {

        while (true) {

            // initialize the game graphics
            Graphics2D g = (Graphics2D) getBufferStrategy().getDrawGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, frameWidth, frameHeight);

            // for each brick:
            for (Brick brick : bricks) {

                // if the brick is not broken:
                if (!brick.isBroken()) {

                    // draw the brick according to its state
                    if (brick.getState() == 0) {
                        g.setColor(new Color(123, 46, 27));
                    } else {
                        g.setColor(new Color(220, 85, 57));
                    }
                    brick.draw(g);
                }
            }

            // for each ball:
            for (Ball ball : balls) {

                // draw the ball if it is still in play
                if (!ball.isBroken()) {
                    g.setColor(ball.getColor());
                    ball.draw(g);
                }
            }

            // for each bonus:
            for (Bonus bonus : bonuses) {

                // if the bonus is not taken yet:
                if (!bonus.isTaken()) {

                    // draw the bonus according to its type
                    if (Objects.equals(bonus.getType(), "ball")) {
                        g.setColor(Color.MAGENTA);
                    } else if (Objects.equals(bonus.getType(), "paddle")) {
                        g.setColor(Color.RED);
                    } else if (Objects.equals(bonus.getType(), "slow")) {
                        g.setColor(Color.BLUE);
                    }
                    bonus.draw(g);
                }
            }

            // draw the paddle
            g.setColor(Color.BLACK);
            paddle.draw(g);

            // draw the life counter
            g.drawString("Vies restantes : " + Game.getLives(), 660, 760);

            // update the game
            int gameState = update();

            // draw an end message if the game is over:
            if (gameState == 0) {

                g.setColor(Color.RED);
                g.fillRect(0, 0, frameWidth, frameHeight);
                g.setColor(Color.BLACK);
                g.drawString("Game over :( Try again!", (frameWidth / 2) - 50, frameHeight / 2);
                g.dispose();
                getBufferStrategy().show();
                break;
            }
            // draw an end message if the game is won:
            else if (gameState == 2) {
                g.setColor(Color.GREEN);
                g.fillRect(0, 0, frameWidth, frameHeight);
                g.setColor(Color.BLACK);
                g.drawString("GG You win!", (frameWidth / 2) - 50, frameHeight / 2);
                g.dispose();
                getBufferStrategy().show();
                break;
            } else {
                g.dispose();
                getBufferStrategy().show();

                // stop the program for 3 seconds if a life is lost
                if (gameState == 1) {
                    Thread.sleep(2980);
                }
            }

            Thread.sleep(1000 / 60);
        }
    }

    public static String getRandomDirection(boolean toTheTop) {

        if ((int) Math.round(Math.random()) == 0) {
            return toTheTop ? "haut à gauche" : "bas à gauche";
        } else {
            return toTheTop ? "haut à droite" : "bas à droite";
        }
    }

    private void checkBonus() {

        // if there is at least one bonus:
        if (!bonuses.isEmpty()) {

            // for each bonus:
            for (Bonus bonus : bonuses) {

                // if the bonus is not taken yet:
                if (!bonus.isTaken()) {

                    // move it forward
                    bonus.setY(bonus.getY() + bonus.getSpeedY());

                    // if the bonus hit the paddle:
                    if (bonus.getY() + bonus.getHeight() >= paddle.getY() &&
                            bonus.getX() + bonus.getWidth() >= paddle.getX() &&
                            bonus.getX() <= paddle.getX() + paddle.getWidth()) {

                        // set the bonus "taken"
                        bonus.setTaken(true);

                        // activates the bonus in question according to its type
                        if (Objects.equals(bonus.getType(), "ball")) {
                            balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2), ballY, ballSpeedX, ballSpeedY, getRandomDirection(true)));
                        } else if (Objects.equals(bonus.getType(), "paddle")) {
                            paddle.setWidth((int) (paddle.getWidth() * 1.25));
                        } else if (Objects.equals(bonus.getType(), "slow")) {
                            for (Ball ball : balls) {
                                ball.setSpeedX(ballSpeedX);
                                ball.setSpeedY(ballSpeedY);
                            }
                        }

                    }

                    // otherwise if the player does not manage to recover the bonus, set the bonus "taken"
                    else if (bonus.getY() > (paddle.getY() + paddle.getHeight() / 2)) {
                        bonus.setTaken(true);
                    }
                }
            }
        }
    }

    public static ArrayList<Brick> generateBricks(int frameWidth, int frameHeight, int brickWidth, int brickHeight, int rowCount, int columnCount) {

        ArrayList<Brick> bricks = new ArrayList<>();
        int maxBricksPerColumn = (int) (frameWidth / brickWidth) - 2;
        int maxBricksPerRow = (int) ((frameHeight / 2) / brickHeight) - 2;
        rowCount = Math.min(maxBricksPerRow, rowCount);
        columnCount = Math.min(maxBricksPerColumn, columnCount);

        int gap = (int) (frameWidth - brickWidth * columnCount) / columnCount;
        int brickX = gap / 2;
        for (int x = 0; x < columnCount; x++) {

            if (x != 0) {
                brickX = brickX + gap + brickWidth;
            }

            int brickY = 0;
            for (int y = 0; y < rowCount; y++) {

                int state = (int) Math.round(Math.random() - 0.1);

                if (y != 0) {
                    brickY = brickY + brickHeight + (int) (brickHeight / rowCount);
                }

                bricks.add(new Brick(brickX, brickY, brickWidth, brickHeight, state));
            }
        }

        return bricks;
    }
}
