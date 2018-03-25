package myGame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BonusPanel extends Block
{
    // スーパークラスのフィールドとは異なる
    public static final int WIDTH = 40;
    public static final int DIFF_WIDTH = Block.WIDTH - BonusPanel.WIDTH;

    public BonusPanel(BufferedImage img, int x, int y, int life)
    {
        super(img, x, y, life);
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
