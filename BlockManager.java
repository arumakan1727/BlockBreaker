package myGame;

import ydk.game.sprite.Sprite;
import ydk.image.ImageEffect;
import ydk.image.ImageUtil;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockManager
{
    private static final int NUM_BLOCK_COLOR = 4;
    private static final int MARGIN_X = 3;
    private static final int MARGIN_Y = 3;
    private static final int OFFSET_X = 40;
    private static final int OFFSET_Y = 3;
    private static final int NUM_BLOCK_HORIZONTAL = 6;
    private static final int NUM_BLOCK_VERTICAL = 5;
    private static final int DEFAULT_BLOCK_DOWN_SPEED = 5;
    private static final double VALUE_DOWN_SPEED_SLOW = 0.33;
    private static final int BONUS_PROBABILITY = 64;
    private static final int NUM_VOID = 3;

    private final static BufferedImage img_blocks[] = new BufferedImage[NUM_BLOCK_COLOR];
    private final List<Block> blocks;

    private double blockDownSpeed;
    private int delay;

    public BlockManager(BufferedImage src)
    {
        this.blocks = new ArrayList<>();

        for (int i = 0; i < NUM_BLOCK_COLOR; i++) {
            img_blocks[i] = ImageUtil.imageCopy(src);
        }
        ImageEffect.addRGB(img_blocks[0], 180, 0, 0);
        ImageEffect.addRGB(img_blocks[1], 0, 180, 0);
        ImageEffect.addRGB(img_blocks[2], 0, 0, 220);
        ImageEffect.addRGB(img_blocks[3], 150, 150, -20);

    }

    public void init()
    {
        blocks.clear();

        for (int i = 0; i < NUM_BLOCK_VERTICAL; ++i) {
            int y = OFFSET_Y + i * (Block.HEIGHT + MARGIN_Y);
            blocks.addAll(createHorizontalBlockArray(y, calcNUM_VOID(), BONUS_PROBABILITY, BallManager.DEFAULT_BALL_COUNT));
        }
        blocks.addAll(createHorizontalHideArray(calcNUM_VOID(), BallManager.DEFAULT_BALL_COUNT));
        blockDownSpeed = DEFAULT_BLOCK_DOWN_SPEED;
        delay = 15;
        System.out.println("init() BlockManager : num_blocks_count = " + blocks.size());
    }

    public GameState update(GameState gameState)
    {
        switch (gameState.state)
        {
            case BLOCK_DOWN:
                gameState = blockDown(gameState);
        }
        return gameState;
    }

    public void draw(Graphics2D g2d)
    {
        Sprite.draw(blocks, g2d);
    }

    private GameState blockDown(GameState gameState)
    {
        if (delay > 0) {
            delay--;
            return gameState;
        }
        if (blockDownSpeed < 0) {
            blockDownSpeed = DEFAULT_BLOCK_DOWN_SPEED;
            delay = 20;
            gameState.state = GameState.State.CLICK_WAIT;
            blocks.addAll(createHorizontalHideArray(calcNUM_VOID(), gameState.getBallCount()));
            gameState.countUpWave();
            gameState.addScore( (gameState.getWaveCount() % 10  == 0) ?
                    300 : (gameState.getWaveCount() % 5 == 0) ?
                    100 : 50);
        }
        else {
            for (int i = 0; i < blocks.size(); i++) {
                final Block e = blocks.get(i);
                e.addY(blockDownSpeed);
                // 床に触れたらゲームオーバー
                if (!(e instanceof BonusPanel) && e.getY() + Block.HEIGHT > Game.FLOOR_Y) {
                    gameState.state = GameState.State.GAMEOVER;
                    new MP3Player(Game.url_explosion, false);
                    return gameState;
                }
            }
            blockDownSpeed -= VALUE_DOWN_SPEED_SLOW;
        }
        return gameState;
    }

    // 空白のブロックの数を生成
    private int calcNUM_VOID()
    {
        return NUM_VOID + ((new Random().nextInt(10) < 2) ? 1 : 0);
    }

    // 上部の目に見えないところのブロックを生成
    private List<Block> createHorizontalHideArray(int num_void, int ballCount)
    {
        return createHorizontalBlockArray(-1*Block.HEIGHT - OFFSET_Y+2, calcNUM_VOID(), BONUS_PROBABILITY, ballCount);
    }

    private List<Block> createHorizontalBlockArray(int y, int num_void, int bonusProbab, int ballCount)
    {
        if (num_void > NUM_BLOCK_HORIZONTAL)
            throw new IllegalArgumentException("num_void is larger than NUM_BLOCK_HORIZONTAL");

        List<Block> list = new ArrayList<>();
        Random rand = new Random();
        boolean is_void[] = new boolean[NUM_BLOCK_HORIZONTAL];
        int bonusPos = -1;

        {
            List<Integer> voidPoslist = new ArrayList<>();
            int r;
            // 空にする場所を決める
            for (int i = 0; i < num_void; ++i) {
                do {
                    r = rand.nextInt(NUM_BLOCK_HORIZONTAL);
                } while (is_void[r] == true);
                is_void[r] = true;
                voidPoslist.add(r);
            }
            //ボーナスパネルを置くか,　置くならどこの場所に置くかを決める
            if (rand.nextInt(100) < bonusProbab) {
                Collections.shuffle(voidPoslist);
                bonusPos = voidPoslist.get(0);
            }
        }
        for (int i=0; i<NUM_BLOCK_HORIZONTAL; ++i) {
            if (is_void[i]) {
                if (i == bonusPos) {
                    int x = OFFSET_X + i * (Block.WIDTH + MARGIN_X) + BonusPanel.DIFF_WIDTH / 2;
                    list.add(new BonusPanel(Game.img_bonusPanel, x, y, 1));
                }
            }
            else {
                int x = OFFSET_X + i * (Block.WIDTH + MARGIN_X);
                int HP = 1 + ballCount + rand.nextInt(ballCount);
                list.add(new Block(img_blocks[rand.nextInt(NUM_BLOCK_COLOR)], x, y, HP));
            }
        }

        return list;
    }

    public List<Block> getBlocks()
    {
        return this.blocks;
    }

}
