package game;

import bagel.AbstractGame;
import bagel.Input;

import java.util.Properties;


/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {
    private static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));
    }

    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {

    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(System.getProperty("gameData"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
