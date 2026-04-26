package game;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Entry point and top-level state machine.
 * Manages the two Project-1 screens: Battle and Pause.
 */
public class ShadowAliens extends AbstractGame {

    public static double screenWidth;
    public static double screenHeight;

    private static Properties gameProps;

    private GameData    gameData;
    private BattleScreen battleScreen;
    private PauseScreen  pauseScreen;
    private boolean      paused = false;

    public ShadowAliens(Properties gameProps) {
        super(
                Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens"
        );

        ShadowAliens.gameProps = gameProps;
        screenWidth  = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        initialise();
    }

    /** Creates fresh game objects – also used by the R-key reset. */
    private void initialise() {
        gameData     = new GameData(gameProps);
        battleScreen = new BattleScreen(gameData);
        pauseScreen  = new PauseScreen(gameData);
        paused       = false;
    }

    @Override
    protected void update(Input input) {
        // R always resets, regardless of current screen
        if (input.wasPressed(Keys.R)) {
            initialise();
            return;
        }

        // ESC toggles between Battle and Pause
        if (input.wasPressed(Keys.ESCAPE)) {
            paused = !paused;
            return;
        }

        if (paused) {
            pauseScreen.update(input, battleScreen);
        } else {
            battleScreen.update(input);
        }
    }

    public static void main(String[] args) {
        // Path from JVM arg, falls back to "gameData.properties" in working directory
        String path = System.getProperty("gameData", "gameData.properties");
        Properties props = IOUtils.readPropertiesFile(path);
        ShadowAliens game = new ShadowAliens(props);
        game.run();
    }
}