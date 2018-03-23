package myGame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BallManager
{
    public static final Point DEFAULT_START_POS
            = new Point(Game.WIDTH / 2, Game.HEIGHT - Ball.SIZE);    //ボールの左上の座標

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
        /*マウスに向けて飛ぶように速度(向き)を算出 */
        //ボールの左上の座標
        final double nextX = gameState.mousePos.x - Ball.SIZE;
        final double nextY = gameState.mousePos.y - Ball.SIZE;

        // 角度計算
        final double rad = Math.atan2(nextY - preLaunchPos.y,
                nextX - preLaunchPos.x);
        System.out.println("Degree: " + Math.toDegrees(rad));

        final double vx = Ball.SPEED_FLY * Math.cos(rad); // x 方向の速度
        final double vy = Ball.SPEED_FLY * Math.sin(rad);

        for (int i = 0; i < balls.size(); i++) {
            Ball b = balls.get(i);
            b.setDelay(i * 20);
            b.setLanded(false);
            b.setVx(vx); //速度設定
            b.setVy(vy);
        }
    }

    private void ballMove()
    {
        // 初発のボールが地面についたか判定
        Ball b = balls.get(0);
        if (b.isLanded())
        {
            // 以降のボールの着地した後の定位置
            preLaunchPos.x = (int)b.getX();
            preLaunchPos.y = (int)b.getY();
        }

        // 更新
        for (Ball v : balls)
        {
            if (v.isLanded()) //地面についているとき
            {
                v.setVy(0); //地面に付いているので上下運動なし

                if ((int) v.getX() == preLaunchPos.x && (int) v.getY() == preLaunchPos.y) {
                    // 止める
                    v.setVx(0);
                } else {
                    // TODO: 発射場所へ水平移動
                    if ((int)v.getX() < preLaunchPos.x) { //定位置よりも左にある
                        v.setVx(Ball.SPEED_ARRANGEMENT);
                    } else {
                        v.setVx(-(Ball.SPEED_ARRANGEMENT));
                    }
                }
            }
            else    //まだ飛んでいる時
            {
                v.update(gameState.keyPressed_space ? 1.0 : 2.0);
                // TODO: ブロックとの当たり判定
            }
        }
    }
}
