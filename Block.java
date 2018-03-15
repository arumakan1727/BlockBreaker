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
    @Override
    public void update()
    {

    }

    @Override
    public void draw(Graphics2D g2d)
    {

    }
}
