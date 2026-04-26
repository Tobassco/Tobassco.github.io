package game;

import bagel.Image;
import bagel.util.Rectangle;

/**
 * A projectile fired by the player. Moves straight up and is destroyed when
 * it leaves the top of the screen or hits an enemy.
 */
public class Projectile {

    private final Image image;
    private final int   speed;

    private double  x;
    private double  y;
    private boolean destroyed = false;

    public Projectile(GameData data, double startX, double startY) {
        image = new Image(data.getProjectileImage());
        speed = data.getProjectileSpeed();
        x     = startX;
        y     = startY;
    }

    /** Moves the projectile upward by {@code tm} scaled frames worth of distance. */
    public void update(double tm) {
        y -= speed * tm;
        // Destroy once completely above the top edge
        if (y + image.getHeight() / 2.0 < 0) {
            destroyed = true;
        }
    }

    public void draw() {
        image.draw(x, y);
    }

    public boolean collidesWith(Enemy enemy) {
        return getBounds().intersects(enemy.getBounds());
    }

    public void    destroy()     { destroyed = true; }
    public boolean isDestroyed() { return destroyed; }

    private Rectangle getBounds() {
        return new Rectangle(
                x - image.getWidth()  / 2.0,
                y - image.getHeight() / 2.0,
                image.getWidth(),
                image.getHeight()
        );
    }
}