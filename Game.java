package myGame;

import ydk.game.engine.BufferingRenderer;
import ydk.game.engine.GameEngine;
import ydk.game.engine.GamePanel;
import ydk.game.engine.GameProcess;
import ydk.image.ImageEffect;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game implements GameProcess
{
    public static final int WIDTH   = 720;
    public static final int HEIGHT  = 540;
    public static BufferedImage img_ball, img_block, img_hexagonBack;

    private GameState gameState;

    private final Component     screen;
    private final BlockManager  blockManager;
    private final BallManager   ballManager;

    static {    //最初に一度だけ実行: 画像読み込み
        System.out.println("Game static init");
        try {
            img_ball = ImageIO.read(Game.class.getResourceAsStream("/resources/ball.png"));
            img_block = ImageIO.read(Game.class.getResourceAsStream("/resources/block-bebel.png"));
            img_hexagonBack = ImageIO.read(Game.class.getResourceAsStream("/resources/hexagon-back.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    private Game(BufferingRenderer renderer)
    {
        this.screen = (Component) renderer;
        this.blockManager = new BlockManager(img_block, this.gameState);
        this.ballManager = new BallManager(img_ball, this.gameState);

        this.eventListenInit(this.screen);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                GameEngine ge = new GameEngine(renderer, Game.this);
                ge.setWindowTitle("Block breaker");
                ge.start();
            }
        });
    }

    private void eventListenInit(Component screen)
    {
        this.screen.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent ev)
            {
                // CLICK_WAITの時にマウスの左ボタンがクリックされたら gameState を変更
                if ( (gameState == GameState.CLICK_WAIT) && (ev.getButton() == MouseEvent.BUTTON1) )
                {
                    gameState = GameState.NOW_CLICKED;
                    gameState.mousePos.x = ev.getX();
                    gameState.mousePos.y = ev.getY();
                }
            }
        });

        this.screen.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent ev)
            {
                switch (ev.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        gameState.keyPressed_space = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent ev)
            {
                switch (ev.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        gameState.keyPressed_space = false;
                        break;
                }
            }
        });
    }


    @Override
    public void initialize()
    {
        ImageEffect.addRGB(img_block, 240, 0, 0);
        gameState = GameState.CLICK_WAIT;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.drawImage(img_hexagonBack, 0, 0,null);
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                int y = 20 + i * (img_block.getHeight() + 5);
                int x = 30 + j * (img_block.getWidth() + 5);
                g2d.drawImage(img_block, x, y, null);
            }
        }
        blockManager.draw(g2d);
    }

    public static void main(String[] args)
    {
        new Game(new GamePanel(WIDTH, HEIGHT));
    }
}
