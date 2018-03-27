package myGame;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

public class SessionRenderer
{
    private static final double DEFAULT_OPACITY = 0.2;
    private static final int FIRST_IMG_Y = -50;
    private static final int LAST_IMG_Y = -40;
    private static final double IMG_VY = 0.08;
    private static final double FADE_IN_SPEED = 0.02;

    private double opacity;
    private double img_y;
    private int delay = 90;

    public SessionRenderer()
    {
        init();
    }

    public void init()
    {
        this.opacity = DEFAULT_OPACITY;
        this.img_y = FIRST_IMG_Y;
    }

    public GameState update(GameState gameState)
    {
        switch (gameState.state) {
            case GAMEOVER:
                if (img_y < LAST_IMG_Y) {
                    img_y += IMG_VY;
//                    opacity += FADE_IN_SPEED;
                    if (opacity > 1.0) opacity = 1.0;

                    System.out.println("opacity: " + (float)opacity + "\timg_y: " + img_y);
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
                break;
            case GAMEOVER:
                drawGameOver(g2d);
                break;
            case RETURNABLE_TO_MENU:
                drawGameOver(g2d);
                g2d.drawString("Click to MAIN-MENU...", 40, 460);
                break;
        }
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
