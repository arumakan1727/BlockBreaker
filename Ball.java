package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Ball extends Sprite
{
    public static final int SIZE = 18;
    public static final double SPEED_FLY = 8;
    public static final double SPEED_ARRANGEMENT = 5;

    private final BufferedImage img;
    private double vx, vy;  //speed
    private int delay;
    private boolean isPrepareLaunchPos;

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
        return (new RectBounds(x+1, y, x+SIZE-1, y+SIZE));
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
            if ((x < 0) || (x + SIZE > Game.STATUS_PANEL_X)) {
                vx = -vx;
            }
            if (y < 0) {
                vy = -vy;
                y = 1;
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
