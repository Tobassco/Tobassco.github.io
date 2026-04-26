package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Colour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the entire Battle Screen state: entity updates, collision detection,
 * HUD rendering, and dev-mode controls (speed, invincible).
 * Reset is handled externally by re-creating this object.
 */
public class BattleScreen {

    private final GameData data;

    private Player player;
    private final List<Enemy>      enemies     = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private final List<Explosion>  explosions  = new ArrayList<>();

    // Arrival frames for each enemy, kept in sync with the enemies list
    private final List<Integer> arrivalTimes = new ArrayList<>();

    private final Font   hudFont;
    private final Colour hudColour;

    private final Image  livesIcon;
    private final double livesStartX;
    private final double livesStartY;
    private final double livesGap;

    // Dev-mode state; exposed via getters/setters for PauseScreen
    private int     timescale  = 1;
    private boolean invincible = false;

    // Accumulates "normal" frames scaled by the speed multiplier
    private double frameCounter = 0;

    private int score = 0;

    public BattleScreen(GameData data) {
        this.data = data;

        player = new Player(data);

        for (GameData.EnemyData ed : data.getEnemies()) {
            enemies.add(new Enemy(data, ed));
            arrivalTimes.add(ed.arrivalTime());
        }

        hudFont   = new Font(data.getTextFont(), data.getTextSize());
        hudColour = data.getTextColour();

        double[] livesPos = data.getLivesStart();
        livesIcon   = new Image(data.getLivesImage());
        livesStartX = livesPos[0];
        livesStartY = livesPos[1];
        livesGap    = data.getLivesGap();

        double[] bg = data.getBackgroundColour();
        Window.setClearColour(bg[0], bg[1], bg[2]);
    }

    /**
     * Effective speed multiplier derived from the integer timescale index.
     *   timescale ≥ 1  →  multiplier = timescale        (1x, 2x, 3x …)
     *   timescale ≤ 0  →  multiplier = 1/(2 – timescale) (0.5x, 0.33x …)
     */
    public double getSpeedMultiplier() {
        if (timescale >= 1) return timescale;
        return 1.0 / (2.0 - timescale);
    }

    /** Called every frame while in the BATTLE state. */
    public void update(Input input) {
        handleDevControls(input);

        double tm = getSpeedMultiplier();
        frameCounter += tm;

        Projectile shot = player.update(input, tm, data);
        if (shot != null) projectiles.add(shot);

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(tm, frameCounter, arrivalTimes.get(i));
        }

        projectiles.forEach(p -> p.update(tm));
        explosions.forEach(e -> e.update(tm));

        handleCollisions();
        removeDestroyed();

        if (player.isDead()) {
            Window.close();
        }

        draw();
    }

    private void handleDevControls(Input input) {
        if (input.wasPressed(Keys.G)) timescale++;
        if (input.wasPressed(Keys.F)) timescale--;
        if (input.wasPressed(Keys.I)) invincible = !invincible;
    }

    private void handleCollisions() {
        // Projectile vs enemy
        Iterator<Projectile> pIter = projectiles.iterator();
        while (pIter.hasNext()) {
            Projectile proj = pIter.next();
            for (Enemy enemy : enemies) {
                if (!enemy.isDestroyed() && enemy.isActive() && proj.collidesWith(enemy)) {
                    proj.destroy();
                    enemy.destroy();
                    score++;
                    explosions.add(new Explosion(data, enemy.getX(), enemy.getY()));
                    break;
                }
            }
        }

        // Player vs enemy
        for (Enemy enemy : enemies) {
            if (!enemy.isDestroyed() && enemy.isActive() && player.collidesWith(enemy)) {
                enemy.destroy();
                if (!invincible) {
                    player.loseLife();
                }
            }
        }
    }

    private void removeDestroyed() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).isDestroyed()) {
                enemies.remove(i);
                arrivalTimes.remove(i);
            }
        }
        projectiles.removeIf(Projectile::isDestroyed);
        explosions.removeIf(Explosion::isFinished);
    }

    // ── Rendering (Z-order: effects → ships → projectiles → info) ────────────

    /** Also called by PauseScreen to render the frozen game state. */
    public void draw() {
        explosions.forEach(Explosion::draw);

        enemies.stream().filter(Enemy::isActive).forEach(Enemy::draw);
        player.draw();

        projectiles.forEach(Projectile::draw);

        drawHud();
    }

    private void drawHud() {
        DrawOptions textOpts = new DrawOptions().setBlendColour(hudColour);

        // Lives icons keep their original image colours (no blend option)
        double iconX = livesStartX;
        for (int i = 0; i < player.getLives(); i++) {
            livesIcon.draw(iconX, livesStartY);
            iconX += livesGap;
        }

        double[] wavePos = data.getWavePos();
        hudFont.drawString(data.getWaveText() + " 1", wavePos[0], wavePos[1], textOpts);

        double[] scorePos = data.getScorePos();
        hudFont.drawString(data.getScoreText() + " " + score, scorePos[0], scorePos[1], textOpts);
    }

    // ── Accessors for PauseScreen ─────────────────────────────────────────────

    public int     getTimescale()           { return timescale; }
    public void    setTimescale(int t)      { timescale = t; }
    public boolean isInvincible()           { return invincible; }
    public void    setInvincible(boolean v) { invincible = v; }
}