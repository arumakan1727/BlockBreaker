package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

public class BallManager
{
    // static
    private static BufferedImage img_ball;
    public static final int     DEFAULT_BALL_COUNT = 3;     //ボールの数の初期値
    private static final int    DEFAULT_START_POS_X = Game.STATUS_PANEL_X / 2;     //初期の発射位置
    private static final int    DEFAULT_START_POS_Y = Game.FLOOR_Y - Ball.SIZE;
    private static final double SCALE_KEY_PRESSED_SPEED = 1.6;  //スペースキーが押された時に何倍速にするか
    private static final int    MOVE_VALUE_ON_HIT = 3;

    private final List<Ball> balls;

    private Point preLaunchPos; //次の発射位置
    private boolean anyLanded;  //ボールが1つでも着地したらtrue
    private List<Block> blocks; //当たり判定するblockのリスト
    private int num_newBall;    //ターン終了時にリストに追加するボールの数
    private Ball visibleBall;

    public BallManager(BufferedImage src, List<Block> list)
    {
        img_ball = src;
        this.blocks = list;
        this.balls = new ArrayList<>();
        this.preLaunchPos = new Point(DEFAULT_START_POS_X, DEFAULT_START_POS_Y);
    }

    public void init()
    {
        //ボール配列を初期化
        balls.clear();
        num_newBall = 0;
        //初期発射位置
        this.preLaunchPos.x = DEFAULT_START_POS_X;
        this.preLaunchPos.y = DEFAULT_START_POS_Y;
        // リストにボールを追加
        addNewBall(DEFAULT_BALL_COUNT);
        visibleBall = balls.get(0);
        System.out.println("init() BallManager");
    }

    public int getBallCount()
    {
        return balls.size() + num_newBall;
    }

    public void draw(Graphics2D g2d)
    {
        visibleBall.draw(g2d);
        Sprite.draw(balls, g2d);
    }


    public GameState update(GameState gameState)
    {
        switch (gameState.state) {
            case NOW_CLICKED:
                prepareLaunch(gameState); // ボールの発射準備(向きの設定等)
                gameState.state = GameState.State.BALL_FLYING;
                break;
            case BALL_FLYING:
                gameState =  ballMove(gameState);
                break;
        }
        return gameState;
    }

    //=========================================================================================================
    private void addNewBall(int n)
    {
        for (int i=0; i<n; ++i) {
            balls.add(new Ball(img_ball, preLaunchPos.x, preLaunchPos.y));
        }
    }

    private void prepareLaunch(GameState gameState) //発射準備
    {
        System.out.println("prepareLaunch");
        this.anyLanded = false;
        /*マウスに向けて飛ぶように速度(向き)を算出 */
        //ボールの左上の座標
        final double nextX = gameState.mousePos.x - Ball.SIZE / 2;
        final double nextY = gameState.mousePos.y - Ball.SIZE / 2;

        // 角度計算
        final double rad = Math.atan2(nextY - preLaunchPos.y,
                nextX - preLaunchPos.x);

        // x方向の速度
        final double vx = Ball.SPEED_FLY * Math.cos(rad);
        // y方向の速度
        final double vy = Ball.SPEED_FLY * Math.sin(rad);

        for (int i = 0; i < balls.size(); i++) {
            Ball b = balls.get(i);
            b.setDelay(i * 8);
            b.setLanded(false);
            b.setVisible(true);
            //速度設定
            b.setVx(vx);
            b.setVy(vy);
            b.setisPrepareLaunchPos(false);
        }
    }

    // ボールが発射位置についているか
    private boolean isPrepareLaunchPosision(Ball b)
    {
        int xdiff = abs((int)b.getX() - preLaunchPos.x); //x方向のズレの距離
        if (xdiff <= Ball.SPEED_ARRANGEMENT) {
            b.setX(preLaunchPos.x); // 距離が近いなら強制的に移動
        }
        return (int)b.getX() == preLaunchPos.x && (int)b.getY() == preLaunchPos.y;
    }


    private GameState ballMove(GameState gameState)
    {
        boolean allisPreLaunchPos = true;
        final double speedEta = gameState.keyPressed_space ? SCALE_KEY_PRESSED_SPEED : 1.0;

        for (int i=0; i < balls.size(); ++i)
        {
            final Ball v = balls.get(i);
            if (v.isLanded()) //地面についているとき
            {
                if (!anyLanded) { //まだ誰も着地していない時
                    anyLanded = true;
                    visibleBall = v; //地面に最初についたボールをvisibleBallにする
                    preLaunchPos.x = (int)v.getX();
                    preLaunchPos.y = Game.FLOOR_Y - Ball.SIZE;
                }
                v.setVy(0);
                v.setY(preLaunchPos.y);
                moveToPreLaunchPos(v);
            }
            else {
                int cnt = colideJudge(v, gameState);
                gameState.addScore(cnt * 100);
            }
            allisPreLaunchPos &= v.isPrepareLaunchPos();
            v.update(speedEta);
        }

        // すべてのボールが発射位置についたか
        if (allisPreLaunchPos) {
            System.out.println("all is PreLaunchPos.");
            gameState.state = GameState.State.BLOCK_DOWN;
            addNewBall(num_newBall);
            num_newBall = 0;
        }

        return gameState;
    }

    private void moveToPreLaunchPos(Ball v)
    {
        if (isPrepareLaunchPosision(v)) {
            v.setVx(0); // 止める
            v.setisPrepareLaunchPos(true);
            v.setVisible(false);
        } else {
            if ((int)v.getX() < preLaunchPos.x) { //定位置よりも左にある
                v.setVx(Ball.SPEED_ARRANGEMENT);
            } else {
                v.setVx(-(Ball.SPEED_ARRANGEMENT));
            }
        }
    }

    // 壊したブロックの数を返す
    private int colideJudge(final Ball v, final GameState gameState)
    {
        //壊した数(スター:+2,  ブロック:+1)
        int breakCount = 0;

        final EightPointsCollisionState eightPoints = new EightPointsCollisionState();
        final Iterator<Block> it = blocks.iterator();
        final RectBounds ballBounds = v.getBounds();

        while (it.hasNext()) {
            Block b = it.next();
            //block の当たり判定矩形を取得
            final RectBounds blockBounds = b.getBounds();

            if (ballBounds.collision(blockBounds)) { //触れた時
                if (b instanceof BonusPanel) { //スターパネルか?
                    num_newBall++;
                    b.vanish();
                    breakCount += 2;
                    gameState.bonusPos.add(new Point((int)b.getX(), (int)b.getY())); //1UP表示のQueue
                }
                else {
                    eightPoints.orAll(RectBounds.getEightPointsCollisionState(ballBounds, blockBounds));
                    if (b.addDamage()) { //もし破壊したら
                        breakCount++;
                    }
                }
            }
        }
        //ボールの反射方向を計算,設定
        checkHitBlock(v, eightPoints);
        return breakCount;
    }

    private void checkHitBlock(final Ball v, final EightPointsCollisionState eightPoints)
    {
        RectBounds.Location location
                = eightPoints.whereCollisionAt();
        if (location != RectBounds.Location.NIL) {
            onHitBlock(v, location);
        }
    }

    private void onHitBlock(Ball v, RectBounds.Location location)
    {
        switch (location) {
            case RIGHT:
            case LEFT:
                v.invertVx();
                break;
            case TOP:
            case BOTTOM:
                v.invertVy();
                break;

            case RIGHT_BOTTOM:  //右下が当たったので左へ向ける,上下の方向は反転
                if (v.getVx() > 0) v.invertVx();
                v.setVy(-1 * abs(v.getVy()));
                break;
            case LEFT_BOTTOM:
                if (v.getVx() < 0) v.invertVx();
                v.setVy(-1 * abs(v.getVy()));
                break;

            case RIGHT_TOP:
                if (v.getVx() > 0) v.invertVx();
                v.setVy(abs(v.getVy()));
                break;
            case LEFT_TOP:
                if (v.getVx() < 0) v.invertVx();
                v.setVy(abs(v.getVy()));
                break;
        }
        // ボールが貫通しないように余分に移動させる
        v.update(1);
    }

}
