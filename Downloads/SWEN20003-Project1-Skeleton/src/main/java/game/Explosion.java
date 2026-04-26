package game;

import bagel.Image;

/**
 * A static explosion image shown at the position of a destroyed enemy.
 * It disappears automatically after a fixed number of scaled frames.
 */
public class Explosion {

    private final Image  image;
    private final double x;
    private final double y;

    private double framesRemaining;

    public Explosion(GameData data, double x, double y) {
        image             = new Image(data.getExplosionImage());
        this.x            = x;
        this.y            = y;
        framesRemaining   = data.getExplosionDuration();
    }

    public void update(double tm) {
        framesRemaining -= tm;
    }

    public void draw() {
        image.draw(x, y);
    }

    public boolean isFinished() { return framesRemaining <= 0; }
}