package myGame;

import ydk.game.sprite.Sprite;
import ydk.image.ImageEffect;
import ydk.image.ImageUtil;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockManager
{
    public static final int NUM_BLOCK_COLOR = 4;
    public static final int MARGIN_X = 3;
    public static final int MARGIN_Y = 3;
    public static final int OFFSET_X = 40;
    public static final int OFFSET_Y = 5;
    public static final int DEFAULT_HP = 10;
    private static final int NUM_BLOCK_HORIZONTAL = 6;
    private static final int NUM_BLOCK_VERTICAL = 5;

    private final static BufferedImage img_blocks[] = new BufferedImage[NUM_BLOCK_COLOR];
    private final List<Block> blocks;

    public BlockManager(BufferedImage src)
    {
        this.blocks = new ArrayList<>();

        for (int i = 0; i < NUM_BLOCK_COLOR; i++) {
            img_blocks[i] = ImageUtil.imageCopy(src);
        }
        ImageEffect.addRGB(img_blocks[0], 180, 0, 0);
        ImageEffect.addRGB(img_blocks[1], 0, 180, 0);
        ImageEffect.addRGB(img_blocks[2], 0, 0, 180);
        ImageEffect.addRGB(img_blocks[3], 150, 150, -20);

        init();
    }

    public void init()
    {
        blocks.clear();

        for (int i = 0; i < NUM_BLOCK_VERTICAL; ++i) {
            int y = OFFSET_Y + i * (Block.HEIGHT + MARGIN_Y);
            blocks.addAll(createHorizontalBlockArray(y, 2));
        }
        System.out.println("BlockManager#init : blocks_size = " + blocks.size());
    }

    public GameState update(GameState gameState)
    {
        switch (gameState)
        {
            case BLOCK_DOWN:
                gameState = GameState.CLICK_WAIT;
        }
        return gameState;
    }

    public void draw(Graphics2D g2d)
    {
        Sprite.draw(blocks, g2d);
    }

    private List<Block> createHorizontalBlockArray(int y, int num_void)
    {
        if (num_void > NUM_BLOCK_HORIZONTAL)
            throw new IllegalArgumentException("num_void is larger than NUM_BLOCK_HORIZONTAL");

        List<Block> list = new ArrayList<>();
        Random rand = new Random();
        boolean is_void[] = new boolean[NUM_BLOCK_HORIZONTAL];

        // 空にする場所を決める
        for (int r, i=0; i < num_void; ++i) {
            do {
                r = rand.nextInt(NUM_BLOCK_HORIZONTAL);
            } while (is_void[r] == true);
            is_void[r] = true;
        }

        for (int i=0; i<NUM_BLOCK_HORIZONTAL; ++i) {
            if (!is_void[i]) {
                int x = OFFSET_X + i * (Block.WIDTH + MARGIN_X);
                list.add(new Block(img_blocks[rand.nextInt(NUM_BLOCK_COLOR)], x, y, DEFAULT_HP));
            }
        }

        return list;
    }

    public List<Block> getBlocks()
    {
        return this.blocks;
    }

}
