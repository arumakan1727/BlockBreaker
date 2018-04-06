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
    private static final int NUM_BLOCK_COLOR = 4;   //ブロックの色の種類
    private static final int MARGIN_X = 1;          //ブロックの周りの空間
    private static final int MARGIN_Y = 1;          //下の空間の幅
    private static final int OFFSET_X = 40;         //ブロックのオフセット
    private static final int OFFSET_Y = 0;
    private static final int NUM_BLOCK_HORIZONTAL = 6;  //横一行のブロック数
    private static final int NUM_BLOCK_VERTICAL = 5;    //縦一列のブロック数
    private static final double DEFAULT_BLOCK_DOWN_SPEED = 4.2;//ブロックが降りてくる初期のスピード
    private static final double VALUE_DOWN_SPEED_SLOW = 0.22; //降りるスピードの減速定数
    private static final int BONUS_PROBABILITY = 70;    //スターが1行の中に出る確率
    private static final int NUM_VOID = 3;          //空白の数

    private final static BufferedImage img_blocks[] = new BufferedImage[NUM_BLOCK_COLOR];
    private final List<Block> blocks;

    private double blockDownSpeed;
    private int delay;
    private double movedDist;

    public BlockManager(BufferedImage src)
    {
        this.blocks = new ArrayList<>();

        for (int i = 0; i < NUM_BLOCK_COLOR; i++) {
            img_blocks[i] = ImageUtil.imageCopy(src);
        }
        // カラフルなブロックの生成
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
            blocks.addAll(this.createHorizontalBlockArray(
                    y,
                    calcNUM_VOID(),
                    BONUS_PROBABILITY,
                    BallManager.DEFAULT_BALL_COUNT));
        }
        // 上部の見えない部分のブロックを生成, フィールド初期化
        initDown(BallManager.DEFAULT_BALL_COUNT);
        System.out.println("init() BlockManager : num_blocks_count = " + blocks.size());
    }

    public GameState update(GameState gameState)
    {
        // BLOCK_DOWNの時のみ処理
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
        }
        //もしブロックの高さ分下へ移動したら
        else if ((int)movedDist >= Block.HEIGHT + MARGIN_Y) {
            // 次のblockDown() に向けて初期化
            initDown(gameState.getBallCount());
            //gamestate更新
            gameState.state = GameState.State.CLICK_WAIT;
            gameState.countUpWave();
            gameState.addScore( (gameState.getWaveCount() % 10  == 0) ?
                    300 : (gameState.getWaveCount() % 5 == 0) ?
                    100 : 50);
        }
        else {  //まだスピードがあるならすべてのブロックに対しY座標を更新
            for (int i = 0; i < blocks.size(); i++)
            {
                final Block e = blocks.get(i);
                e.addY(blockDownSpeed);
                // 床に触れたらゲームオーバー
                if (!(e instanceof BonusPanel) && e.getY() + Block.HEIGHT > Game.FLOOR_Y) {
                    gameState.state = GameState.State.GAMEOVER;
                    new MP3Player(Game.url_explosion, false);
                    return gameState;
                }
            }
            movedDist += blockDownSpeed; //ブロックの下がった距離に加算
            blockDownSpeed -= VALUE_DOWN_SPEED_SLOW;
            // もしスピードが0未満になったら
            if (blockDownSpeed < 0) blockDownSpeed = 0.25;
        }
        return gameState;
    }

    private void initDown(final int ballCount)
    {
        blockDownSpeed = DEFAULT_BLOCK_DOWN_SPEED;
        delay = 15;
        movedDist = 0;
        blocks.addAll(createHorizontalHideArray(calcNUM_VOID(), ballCount));
    }


    // 空白のブロックの数
    private int calcNUM_VOID()
    {
        return NUM_VOID + ((new Random().nextInt(10) < 2) ? 1 : 0);
    }

    // 上部の目に見えないところのブロックを生成
    private List<Block> createHorizontalHideArray(int num_void, int ballCount)
    {
        return createHorizontalBlockArray(-1 * (Block.HEIGHT + MARGIN_Y) , calcNUM_VOID(), BONUS_PROBABILITY, ballCount);
    }

    private List<Block> createHorizontalBlockArray(int y, int num_void, int bonusProbab, int ballCount)
    {
        if (num_void > NUM_BLOCK_HORIZONTAL)
            throw new IllegalArgumentException("num_void is larger than NUM_BLOCK_HORIZONTAL");

        // return用のブロックリスト
        List<Block> list = new ArrayList<>();
        Random rand = new Random();
        boolean is_void[] = new boolean[NUM_BLOCK_HORIZONTAL];
        int bonusPos = -1; //スターの位置(左から何番目の配列か)

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
                if (i == bonusPos) {    // 空白で,bonusPosならスターパネルを入れる
                    int x = OFFSET_X + i * (Block.WIDTH + MARGIN_X) + (Block.WIDTH - BonusPanel.WIDTH) / 2;
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
