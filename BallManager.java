package myGame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BallManager
{
    public static final int SIZE = 20;
    public static final Point DEFAULT_START_POS
            = new Point(Game.WIDTH / 2, Game.HEIGHT - SIZE);    //ボールの左上の座標

    private static BufferedImage img_ball;

    private final List<Ball> balls;
    private GameState gameState;
    private Point preLaunchPos;
    private Point mouseClickedPos;

    public BallManager(BufferedImage src, GameState state)
    {
        img_ball = src;
        this.gameState = state;
        this.balls = new ArrayList<>();
        this.preLaunchPos = DEFAULT_START_POS;
        this.mouseClickedPos = new Point();

        this.init();
    }

    public void init()
    {
        balls.clear();
        for (int i = 0; i < 3; i++) {
            balls.add(new Ball(img_ball, DEFAULT_START_POS));
        }
    }

    public void update()
    {
        switch (this.gameState)
        {
            case MAIN_MENU:
                break;
            case NOW_CLICKED:
                prepareLaunch();
                gameState = GameState.BALL_FLYING;
                break;
            case BALL_FLYING:
                ballMove();
                break;
            case CLICK_WAIT:
            case BLOCK_DOWN:
                break;
        }
    }

    private void prepareLaunch() //発射準備
    {
        for (int i = 0; i < balls.size(); i++) {
            Ball b = balls.get(i);
            b.setDelay(i * 20);
            b.setLanded(false);
        }
    }

    private void ballMove()
    {
        // 初発のボールが地面についたか判定
        Ball b = balls.get(0);
        if (b.isLanded())
        {
            preLaunchPos.x = (int)b.getX();
            preLaunchPos.y = (int)b.getY();
        }

        // 更新
        for (Ball v : balls)
        {
            if (v.isLanded()) //地面についているとき
            {
                if ((int) v.getX() == preLaunchPos.x && (int) v.getY() == preLaunchPos.y) {
                    continue;
                }
                // TODO: 発射場所へ水平移動
            }
            else    //まだ飛んでいる時
            {
                v.update(gameState.keyPressed_space ? 1.0 : 2.0);
                // TODO: ブロックとの当たり判定
            }
        }
    }
}
