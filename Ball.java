package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Ball extends Sprite
{
    public static final int SIZE = 20;

    private final BufferedImage img;
    private double vx, vy;  //speed
    private int delay;

    public Ball(final BufferedImage img)
    {
        this.img = img;
    }

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
        if (leftBtm && rightBtm) {  // 左下と右下がブロック内
            return RectBounds.Location.BOTTOM;
        }
        if (leftTop && leftBtm) {   // 左上と左下がブロック内
            return RectBounds.Location.LEFT;
        }
        if (rightTop && rightBtm) { // 右上と右下がブロック内
            return RectBounds.Location.RIGHT;
        }
        if (leftTop) {  // 左上
            return RectBounds.Location.L_TOP;
        }
        if (rightTop) { // 右上
            return RectBounds.Location.R_TOP;
        }
        if (leftBtm) {  // 左下
            return RectBounds.Location.L_BOTTOM;
        }
        if (rightBtm) { // 右下
            return RectBounds.Location.R_BOTTOM;
        }
        return RectBounds.Location.NIL;
    }

    public int getDelay() {
        return this.delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void update()
    {
        if (this.delay > 0) {
            this.delay--;
            return ;
        }

        x += vx;
        y += vy;
        // 画面の縁に触れたなら向きを反転
        if ((x < 0) || (x+SIZE > Game.WIDTH)) {
            vx = -vx;
        }
        if ((y < 0) || (y+SIZE > Game.HEIGHT)) {
            vy = -vy;
        }

    }

    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, (int)x, (int)y, null);
    }
}
