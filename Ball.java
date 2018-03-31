package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Ball extends Sprite
{
    public static final int SIZE = 16; //ボールの大きさ(縦,横同じサイズ)
    public static final double SPEED_FLY = 8; //飛ぶ時の速さ
    public static final double SPEED_ARRANGEMENT = 5;//発射場所へ戻る速さ

    private final BufferedImage img;
    private double vx, vy;  //speed
    private int delay; //クリックされてから発射するまでの時間
    private boolean isPrepareLaunchPos; //発射位置についたか
    private boolean landed;//地面についたか

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
    public void update(double eta) //etaはスペースキーが押されているかで変わる早送りの定数
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
            if ((x < 0)) {
                vx = -vx;
                x = 1;
            }
            else if (x + SIZE > Game.STATUS_PANEL_X) {
                vx = -vx;
                x = Game.STATUS_PANEL_X - SIZE - 1;
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

    public RectBounds getBounds()
    {
        return (new RectBounds(x, y-1, x+SIZE, y+SIZE+1));
    }

    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, (int)x, (int)y, null);
        g2d.setColor(java.awt.Color.RED);
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
