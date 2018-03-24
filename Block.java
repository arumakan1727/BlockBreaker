package myGame;

import ydk.game.sprite.Sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Block extends Sprite
{
    public final int WIDTH, HEIGHT;
    private int life;
    private final BufferedImage img;

    public Block(final BufferedImage img)
    {
        this.img = img;
        this.WIDTH = img.getWidth();
        this.HEIGHT = img.getHeight();
    }

    public RectBounds getBounds()
    {
        return (new RectBounds(x, y, x+WIDTH, y+HEIGHT));
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

    }
}
