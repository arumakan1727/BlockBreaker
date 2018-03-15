package myGame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BallManager
{
    private final List<Ball> balls;
    private final BufferedImage img_ball;
    private final int SIZE;

    public BallManager(BufferedImage src)
    {
        balls = new ArrayList<>();
        img_ball = src;
        SIZE = img_ball.getWidth();
    }

    public void update()
    {
        switch (Game.gameState)
        {
            case MAIN_MENU:
                break;
            case CLICK_WAIT:
            case BLOCK_DOWN:
                break;
            case BALL_FLYING:
                break;
        }
    }
}
