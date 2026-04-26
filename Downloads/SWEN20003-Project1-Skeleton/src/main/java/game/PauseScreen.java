package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.util.Colour;

/**
 * Renders the pause overlay on top of a frozen copy of the Battle Screen.
 * Dev-mode controls (G, F, I) still work here and are forwarded to the
 * BattleScreen so their state is preserved when the game resumes.
 */
public class PauseScreen {

    private final GameData data;
    private final Font     titleFont;
    private final Font     defaultFont;
    private final Colour   textColour;
    private final double   screenWidth;

    public PauseScreen(GameData data) {
        this.data        = data;
        titleFont        = new Font(data.getTextFont(), data.getPausedTitleSize());
        defaultFont      = new Font(data.getTextFont(), data.getTextSize());
        textColour       = data.getTextColour();
        screenWidth      = ShadowAliens.screenWidth;
    }

    /**
     * Called every frame while paused. Draws the frozen battle scene first,
     * then the pause UI on top. Dev controls are forwarded to battleScreen.
     */
    public void update(Input input, BattleScreen battleScreen) {
        handleDevControls(input, battleScreen);
        battleScreen.draw();
        drawOverlay(battleScreen.getTimescale());
    }

    private void handleDevControls(Input input, BattleScreen battleScreen) {
        if (input.wasPressed(Keys.G)) battleScreen.setTimescale(battleScreen.getTimescale() + 1);
        if (input.wasPressed(Keys.F)) battleScreen.setTimescale(battleScreen.getTimescale() - 1);
        if (input.wasPressed(Keys.I)) battleScreen.setInvincible(!battleScreen.isInvincible());
    }

    private void drawOverlay(int timescale) {
        DrawOptions opts = new DrawOptions().setBlendColour(textColour);

        // Paused title – centred horizontally
        String title      = data.getPausedTitleText();
        double titleWidth = titleFont.getWidth(title);
        titleFont.drawString(title, (screenWidth - titleWidth) / 2.0, data.getPausedTitlePosY(), opts);

        // Controls list – each line centred
        String[] controls = data.getControlsList();
        double   lineY    = data.getControlsStartPosY();
        for (String line : controls) {
            double lineWidth = defaultFont.getWidth(line);
            defaultFont.drawString(line, (screenWidth - lineWidth) / 2.0, lineY, opts);
            lineY += data.getControlsRowGap();
        }

        // Timescale indicator (bottom-right area per properties)
        double[] tsPos = data.getTimescalePos();
        defaultFont.drawString(data.getTimescaleText() + " " + timescale, tsPos[0], tsPos[1], opts);
    }
}