package myGame;

import ydk.image.ImageEffect;
import ydk.image.ImageUtil;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlockManager
{
    public static final int NUM_BLOCK_COLOR = 4;

    private final static BufferedImage img_blocks[] = new BufferedImage[NUM_BLOCK_COLOR];
    private final List<Block> blocks;
    private GameState gameState;

    public BlockManager(BufferedImage src, GameState state)
    {
        this.blocks = new ArrayList<>();
        this.gameState = state;

        for (int i = 0; i < NUM_BLOCK_COLOR; i++) {
            img_blocks[i] = ImageUtil.imageCopy(src);
        }
        ImageEffect.addRGB(img_blocks[0], 150, 0, 0);
        ImageEffect.addRGB(img_blocks[1], 0, 150, 0);
        ImageEffect.addRGB(img_blocks[2], 0, 0, 200);
        ImageEffect.addRGB(img_blocks[3], 90, 90, -20);
    }

    public void draw(Graphics2D g2d)
    {
        for (int i  = 0; i < NUM_BLOCK_COLOR; ++i) {
            g2d.drawImage(img_blocks[i], 20+i*img_blocks[i].getWidth(), 400, null);
        }
    }
}
