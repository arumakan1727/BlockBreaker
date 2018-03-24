package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Block extends Sprite
{
    public static final int WIDTH = 70;
    public static final int HEIGHT = 36;

    private int life;
    private final BufferedImage img;

    public Block(final BufferedImage img, int x, int y, int life)
    {
        this.img = img;
        this.x = x;
        this.y = y;
        this.life = life;
    }

    public RectBounds getBounds()
    {
        return (new RectBounds(x-2, y-2, x+WIDTH+2, y+HEIGHT+2));
    }

    public void addDamage()
    {
        this.life--;
        if (life == 0) {
            this.vanish();
        }
    }

    @Override
    public void vanish()
    {
        super.vanish();
    }

    @Override
    public void update(double eta)
    {

    }

    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, (int)x, (int)y, null);
    }
}
