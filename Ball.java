package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Ball extends Sprite
{
    public static final int SIZE = 20;
    public static final double SPEED_FLY = 5;
    public static final double SPEED_ARRANGEMENT = 5;

    private final BufferedImage img;
    private double vx, vy;  //speed
    private int delay;
    private boolean isPrepareLaunchPos;

    public Ball(final BufferedImage img)
    {
        this(img, 0, 0);
    }

    public Ball(final BufferedImage img, Point p)
    {
        this(img, p.x, p.y);
    }

    public Ball(final BufferedImage img, int x, int y)
    {
        this.img = img;
        this.x = x;
        this.y = y;
        this.delay = 0;
        this.landed = true;
    }

    public boolean isLanded()
    {
        return landed;
    }

    public void setLanded(boolean landed)
    {
        this.landed = landed;
    }

    private boolean landed;

    public RectBounds getBounds()
    {
        return (new RectBounds(x, y, x+SIZE, y+SIZE));
    }

    /**
     * 引数の{@link Block}インスタンスとどの部位で衝突しているかを返します。<br>
     * なお, 衝突していない場合は RectBounds.Location#NIL を返り値とします。
     * @param b Block
     * @return <u><strong>ボールにおける</strong></u>衝突部位
     * @see RectBounds.Location
     */
    public RectBounds.Location collision(Block b)
    {
        final RectBounds rect = b.getBounds();
        final boolean leftTop   = rect.containts(x,y);
        final boolean rightTop  = rect.containts(x+SIZE, y);
        final boolean leftBtm   = rect.containts(x, y+SIZE);
        final boolean rightBtm  = rect.containts(x+SIZE, y+SIZE);

        if (leftTop && rightTop) {  // 左上と右上がブロック内
            return RectBounds.Location.TOP;
        }
        else if (leftBtm && rightBtm) {  // 左下と右下がブロック内
            return RectBounds.Location.BOTTOM;
        }
        else if (leftTop && leftBtm) {   // 左上と左下がブロック内
            return RectBounds.Location.LEFT;
        }
        else if (rightTop && rightBtm) { // 右上と右下がブロック内
            return RectBounds.Location.RIGHT;
        }
        else if (leftTop) {  // 左上
            return RectBounds.Location.LEFT_TOP;
        }
        else if (rightTop) { // 右上
            return RectBounds.Location.RIGHT_TOP;
        }
        else if (leftBtm) {  // 左下
            return RectBounds.Location.LEFT_BOTTOM;
        }
        else if (rightBtm) { // 右下
            return RectBounds.Location.RIGHT_BOTTOM;
        }
        else
            return RectBounds.Location.NIL;
    }

    public void setVx(double vx)
    {
        this.vx = vx;
    }

    public void setVy(double vy)
    {
        this.vy = vy;
    }

    public double getVx()
    {
        return vx;
    }

    public double getVy()
    {
        return vy;
    }

    public void invertVx()
    {
        vx = -vx;
    }

    public void invertVy()
    {
        vy = -vy;
    }

    public int getDelay() {
        return this.delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void update(double eta)
    {
        if (this.delay > 0) {
            this.delay--;
            return ;
        }

        x += vx * eta;
        y += vy * eta;

        // 飛んでいる場合はhogehoge処理
        if (!landed) {
            // 画面の縁に触れたなら向きを反転
            if ((x < 0) || (x + SIZE > Game.WIDTH)) {
                vx = -vx;
            }
            if (y < 0) {
                vy = -vy;
            }

            //地面についたらフラグを立てる
            if ((int)(y + SIZE+1) > Game.FLOOR_Y) {
                this.landed = true;
            }
        }

    }

    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, (int)x, (int)y, null);
    }

    public boolean isPrepareLaunchPos()
    {
        return isPrepareLaunchPos;
    }

    public void setisPrepareLaunchPos(boolean prepareLaunchPos)
    {
        isPrepareLaunchPos = prepareLaunchPos;
    }
}
