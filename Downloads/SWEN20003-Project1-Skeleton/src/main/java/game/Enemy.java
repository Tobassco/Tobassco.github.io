package game;

import bagel.Image;
import bagel.util.Rectangle;
import bagel.DrawOptions;

/**
 * A single enemy ship. Waits above the screen until its arrival frame is
 * reached, then moves straight down at a fixed speed.
 */
public class Enemy {

    private final Image  image;
    private final double movementSpeed;
    private final double screenHeight;

    private final double x;
    private double y;

    private boolean active    = false;
    private boolean destroyed = false;

    public Enemy(GameData data, GameData.EnemyData ed) {
        image         = new Image(data.getEnemyImage());
        movementSpeed = ed.movementSpeed();
        screenHeight  = ShadowAliens.screenHeight;
        x             = ed.posX();
        // Positioned entirely above the screen before first activation
        y = -image.getHeight() / 2.0;
    }

    public void update(double tm, double frameCounter, int arrivalTime) {
        if (destroyed) return;

        if (!active && frameCounter >= arrivalTime) {
            active = true;
        }

        if (active) {
            y += movementSpeed * tm;
            if (y - image.getHeight() / 2.0 > screenHeight) {
                destroyed = true;
            }
        }
    }

    public void draw() {
        image.draw(x, y, new DrawOptions().setRotation(Math.PI / 2));
    }

    public void    destroy()     { destroyed = true; }
    public boolean isDestroyed() { return destroyed; }

    /** True only while visible and alive on screen. */
    public boolean isActive()    { return active && !destroyed; }

    public double getX() { return x; }
    public double getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(
                x - image.getWidth()  / 2.0,
                y - image.getHeight() / 2.0,
                image.getWidth(),
                image.getHeight()
        );
    }
}