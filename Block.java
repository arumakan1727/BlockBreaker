package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Block extends Sprite
{
    public static final int WIDTH = 70;
    public static final int HEIGHT = 36;
    public static final int FONT_SIZE = 28;

    private int life;
    final BufferedImage img;

    public Block(final BufferedImage img, int x, int y, int life)
    {
        this.img = img;
        this.x = x;
        this.y = y;
        this.life = life;
    }

    //当たり判定矩形を返す
    public RectBounds getBounds()
    {
        return (new RectBounds(x-1, y-1, x+WIDTH+1, y+HEIGHT+1));
    }

    //死んだらtrueを返す
    public boolean addDamage()
    {
        this.life--;
        if (life <= 0) {
            this.vanish();
            return true;
        }

        return false;
    }

    // 音を鳴らす(現在は鳴らさない仕様)
    public void soundPlay()
    { }

    @Override
    public void vanish()
    {
        super.vanish();
        this.soundPlay();
    }

    @Override
    public void update(double eta)
    {
    }

    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, (int)x, (int)y, null);
        drawHP(g2d);
    }

    private void drawHP(Graphics2D g2d)
    {
        RenderingHints defaultHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        {
            String HP = String.valueOf(this.life);

            Font font = new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);

            // 文字列を中心に描画する
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle rect = metrics.getStringBounds(HP, g2d).getBounds();
            final int HPx = ((int)this.x + Block.WIDTH / 2) - (rect.width / 2);
            final int HPy = ((int)this.y + Block.HEIGHT / 2) + metrics.getAscent() / 2;
            g2d.drawString(HP, HPx, HPy);
        }

        g2d.setRenderingHints(defaultHints);
    }

}
