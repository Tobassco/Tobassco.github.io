package game;

import bagel.util.Colour;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Parses gameData.properties once on construction and exposes every value
 */
public class GameData {

    /** Immutable value object for a single enemy entry from the properties file. */
    public record EnemyData(int arrivalTime, double movementSpeed, double posX) {}

    private final Properties props;

    public GameData(Properties props) {
        this.props = props;
    }



    public int getWindowWidth()  { return Integer.parseInt(props.getProperty("window.width")); }
    public int getWindowHeight() { return Integer.parseInt(props.getProperty("window.height")); }


    public String getTextFont() { return props.getProperty("text.font"); }
    public int    getTextSize() { return Integer.parseInt(props.getProperty("text.size")); }

    public Colour getTextColour() {
        return parseColour(props.getProperty("text.colour"));
    }

    public double[] getBackgroundColour() {
        String[] p = props.getProperty("background.colour").split(",");
        return new double[]{
                Double.parseDouble(p[0].trim()),
                Double.parseDouble(p[1].trim()),
                Double.parseDouble(p[2].trim())
        };
    }


    public String   getWaveText()  { return props.getProperty("wave.text"); }
    public double[] getWavePos()   { return parseDoubles(props.getProperty("wave.pos")); }

    public String   getScoreText() { return props.getProperty("score.text"); }
    public double[] getScorePos()  { return parseDoubles(props.getProperty("score.pos")); }


    public String   getLivesImage() { return props.getProperty("playerLives.image"); }
    public double[] getLivesStart() { return parseDoubles(props.getProperty("playerLives.startPosition")); }
    public double   getLivesGap()   { return Double.parseDouble(props.getProperty("playerLives.gap").trim()); }


    public String getPlayerImage()        { return props.getProperty("player.image"); }
    public int    getPlayerInitialLives() { return Integer.parseInt(props.getProperty("player.initialLives")); }
    public double getPlayerPosY()         { return Double.parseDouble(props.getProperty("player.posY").trim()); }
    public int    getPlayerSpeed()        { return Integer.parseInt(props.getProperty("player.speed")); }
    public int    getPlayerShootCooldown(){ return Integer.parseInt(props.getProperty("player.shootCooldown")); }


    public String getProjectileImage() { return props.getProperty("projectile.image"); }
    public int    getProjectileSpeed() { return Integer.parseInt(props.getProperty("projectile.movementSpeed")); }


    public String getEnemyImage() { return props.getProperty("enemy.image"); }

    public List<EnemyData> getEnemies() {
        List<EnemyData> list = new ArrayList<>();
        int i = 0;
        while (props.containsKey("enemy." + i + ".arrivalTime")) {
            int    arrival = Integer.parseInt(props.getProperty("enemy." + i + ".arrivalTime").trim());
            double speed   = Double.parseDouble(props.getProperty("enemy." + i + ".movementSpeed").trim());
            double posX    = Double.parseDouble(props.getProperty("enemy." + i + ".posX").trim());
            list.add(new EnemyData(arrival, speed, posX));
            i++;
        }
        return list;
    }


    public String getExplosionImage()    { return props.getProperty("explosion.image"); }
    public int    getExplosionDuration() { return Integer.parseInt(props.getProperty("explosion.duration")); }


    public String getPausedTitleText()  { return props.getProperty("pausedTitle.text"); }
    public double getPausedTitlePosY()  { return Double.parseDouble(props.getProperty("pausedTitle.posY").trim()); }
    public int    getPausedTitleSize()  { return Integer.parseInt(props.getProperty("pausedTitle.size")); }

    public String[] getControlsList()     { return props.getProperty("controlsList.text").split(","); }
    public double   getControlsStartPosY(){ return Double.parseDouble(props.getProperty("controlsList.startPosY").trim()); }
    public double   getControlsRowGap()   { return Double.parseDouble(props.getProperty("controlsList.rowGap").trim()); }

    public String   getTimescaleText() { return props.getProperty("timescale.text"); }
    public double[] getTimescalePos()  { return parseDoubles(props.getProperty("timescale.pos")); }


    private Colour parseColour(String csv) {
        String[] p = csv.split(",");
        return new Colour(
                Double.parseDouble(p[0].trim()),
                Double.parseDouble(p[1].trim()),
                Double.parseDouble(p[2].trim())
        );
    }

    private double[] parseDoubles(String csv) {
        String[] p = csv.split(",");
        return new double[]{ Double.parseDouble(p[0].trim()), Double.parseDouble(p[1].trim()) };
    }
}