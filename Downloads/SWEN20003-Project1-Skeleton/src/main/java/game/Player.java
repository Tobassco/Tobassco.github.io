package game;

import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Rectangle;

/**
 * The player's spaceship. Moves along the x-axis only, shoots projectiles
 * upward, and tracks remaining lives.
 */
public class Player {

    private final Image  image;
    private final int    speed;
    private final int    maxCooldown;
    private final double screenWidth;

    private double x;
    private final double y;
    private int    lives;

    // Counts down from maxCooldown in "scaled" frames; shooting is blocked while > 0.
    private double cooldownRemaining = 0;

    public Player(GameData data) {
        image       = new Image(data.getPlayerImage());
        speed       = data.getPlayerSpeed();
        maxCooldown = data.getPlayerShootCooldown();
        lives       = data.getPlayerInitialLives();
        screenWidth = ShadowAliens.screenWidth;
        // Horizontally centred at start
        x = screenWidth / 2.0;
        y = data.getPlayerPosY();
    }


    public Projectile update(Input input, double tm, GameData data) {
        handleMovement(input, tm);

        if (cooldownRemaining > 0) {
            cooldownRemaining -= tm;
        }

        if (input.wasPressed(Keys.SPACE) && cooldownRemaining <= 0) {
            cooldownRemaining = maxCooldown;
            return new Projectile(data, x, y);
        }
        return null;
    }

    private void handleMovement(Input input, double tm) {
        boolean left  = input.isDown(Keys.A);
        boolean right = input.isDown(Keys.D);

        if (left && !right) {
            x -= speed * tm;
        } else if (right && !left) {
            x += speed * tm;
        }

        // Clamp so no pixel of the ship goes off-screen
        double half = image.getWidth() / 2.0;
        x = Math.max(half, Math.min(screenWidth - half, x));
    }

    public void draw() {
        image.draw(x, y);
    }

    public void loseLife() { lives--; }
    public boolean isDead() { return lives <= 0; }
    public int     getLives() { return lives; }

    public boolean collidesWith(Enemy enemy) {
        return getBounds().intersects(enemy.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(
                x - image.getWidth()  / 2.0,
                y - image.getHeight() / 2.0,
                image.getWidth(),
                image.getHeight()
        );
    }
}