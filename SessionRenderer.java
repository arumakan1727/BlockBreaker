package myGame;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class SessionRenderer
{
    private static final double DEFAULT_OPACITY = 0.2;
    private static final int FIRST_IMG_Y = -80;
    private static final int LAST_IMG_Y = -55;
    private static final double IMG_VY = 0.2;
    private static final double FADE_IN_SPEED = 0.008;
    private static final int DELAY = 70;
    private static final int TEXT_Y = 460;
    private static final int MIN_TOP_TXET_Y = 455;
    private static final int MAX_BOTTOM_TEXT_Y = TEXT_Y;
    private static final double VAULE_TEXT_Y_ADD = 0.2;
    private static final String TEXT_MENU = "Click to start";

    private double opacity;
    private double img_y;
    private int delay = DELAY;
    private double text_y;
    private boolean isText_y_up;

    public SessionRenderer()
    {
        init();
    }

    public void init()
    {
        this.opacity = DEFAULT_OPACITY;
        this.img_y = FIRST_IMG_Y;
        this.delay = DELAY;
        this.text_y = TEXT_Y;
        this.isText_y_up = false;
    }

    public GameState update(GameState gameState)
    {
        switch (gameState.state) {
            case MAIN_MENU:
                textMove();
                break;

            case GAMEOVER:
                if (img_y < LAST_IMG_Y) {
                    img_y += IMG_VY;
                    opacity += FADE_IN_SPEED;
                    if (opacity > 1.0) opacity = 1.0;

                } else {
                    if (delay > 0) {
                        --delay;
                    } else {
                        System.out.println("---RETURNABLE");
                        gameState.state = GameState.State.RETURNABLE_TO_MENU;
                    }
                }
                break;
        }
        return gameState;
    }

    public void draw(Graphics2D g2d, GameState gameState)
    {
        switch (gameState.state) {
            case MAIN_MENU:
                drawMainMenu(g2d);
                break;
            case GAMEOVER:
                drawGameOver(g2d);
                break;
            case RETURNABLE_TO_MENU:
                drawGameOver(g2d);
                drawScore(g2d, gameState.getScore());
                break;
        }
    }

    private void textMove() {
        if (isText_y_up) {
            if (text_y > MIN_TOP_TXET_Y)
                text_y -= VAULE_TEXT_Y_ADD;
            else
                isText_y_up = false;
        }
        else {
            if (text_y < MAX_BOTTOM_TEXT_Y)
                text_y += VAULE_TEXT_Y_ADD;
            else
                isText_y_up = true;
        }
    }

    private void drawScore(Graphics2D g2d, int score)
    {
        final Font defaultFont = g2d.getFont();
        final RenderingHints defaultRenderingHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        {
            g2d.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 24));
            g2d.drawString("Click to MAIN-MENU...", 370, 510);

            {
                final String S = "Your SCORE";
                g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
                Rectangle rect = g2d.getFontMetrics().getStringBounds(S, g2d).getBounds();
                g2d.drawString(S, Game.WIDTH / 2 - rect.width / 2, 380);
            }
            {
                final String S = String.valueOf(score);
                g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 86));
                Rectangle rect = g2d.getFontMetrics().getStringBounds(S, g2d).getBounds();
                g2d.drawString(S, Game.WIDTH / 2 - rect.width / 2, 460);
            }
        }

        g2d.setRenderingHints(defaultRenderingHints);
        g2d.setFont(defaultFont);
    }

    private void drawMainMenu(Graphics2D g2d)
    {
        g2d.drawImage(Game.img_logo, 0, 0, null);

        final Font defaultFont = g2d.getFont();
        final RenderingHints defaultRenderingHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        {
            g2d.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 24));
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle rect = metrics.getStringBounds(TEXT_MENU, g2d).getBounds();

            final int x = Game.WIDTH / 2 - rect.width / 2; //中心
            g2d.drawString(TEXT_MENU, x, (int)text_y);
        }

        g2d.setRenderingHints(defaultRenderingHints);
        g2d.setFont(defaultFont);
    }


    private void drawGameOver(Graphics2D g2d)
    {
        final AlphaComposite alphaComposite
                = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float)this.opacity);

        final Composite defaultComposit =  g2d.getComposite();

        g2d.setComposite(alphaComposite);
        g2d.drawImage(Game.img_gameover, 0, (int)img_y, null);

        g2d.setComposite(defaultComposit);
    }
}
