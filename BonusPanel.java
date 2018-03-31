package myGame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BonusPanel extends Block
{
    // スーパークラスのフィールドを隠蔽
    public static final int WIDTH = 40;

    public BonusPanel(BufferedImage img, int x, int y, int life)
    {
        super(img, x, y, life);
    }

    @Override
    public void soundPlay()
    {
        new MP3Player(Game.url_coin, false);
    }

    @Override
    public RectBounds getBounds()
    {
        return new RectBounds(this.x, this.y, this.x+WIDTH, this.y + HEIGHT);
    }

    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(this.img, (int)this.x, (int)this.y, null);
    }
}
