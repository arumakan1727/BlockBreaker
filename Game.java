package myGame;

import ydk.game.engine.BufferingRenderer;
import ydk.game.engine.GameEngine;
import ydk.game.engine.GamePanel;
import ydk.game.engine.GameProcess;

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
    public static final int FLOOR_Y = HEIGHT - 30;
    public static final int STATUS_PANEL_X = 520;
    public static BufferedImage
            img_ball, img_block, img_bonusPanel, img_hexagonBack, img_floor, img_glossPanel,
            img_gameover;

    private GameState gameState;

    private final Component     screen;
    private final BlockManager  blockManager;
    private final BallManager   ballManager;
    private final StatusRenderer statusRenderer;
    private final SessionRenderer sessionRenderer;

    static {    //最初に一度だけ実行: 画像読み込み
        System.out.println("Game static init");
        try {
            img_ball        = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/ball.png"));
            img_block       = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/block-dark.png"));
            img_bonusPanel  = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/bonusPanel.png"));
            img_hexagonBack = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/hexagon-back.jpeg"));
            img_floor       = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/floor.png"));
            img_glossPanel  = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/gloss-panel.png"));
            img_gameover  = ImageIO.read(Game.class.getResourceAsStream("/myGame/resources/gameover.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    private Game(BufferingRenderer renderer)
    {
        this.gameState = new GameState();
        this.gameState.state = GameState.State.CLICK_WAIT;
        this.screen = (Component) renderer;
        this.blockManager = new BlockManager(img_block);
        this.ballManager = new BallManager(img_ball, blockManager.getBlocks());
        this.statusRenderer = new StatusRenderer();
        this.sessionRenderer = new SessionRenderer();

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

    @Override
    public void initialize()
    {
        gameState.state = GameState.State.CLICK_WAIT;
    }

    @Override
    public void update()
    {
        this.gameState = ballManager.update(this.gameState);
        this.gameState = blockManager.update(this.gameState);

        this.statusRenderer.setWaveCount(gameState.getWaveCount());

        this.gameState.setBallCount(ballManager.getBallCount());
        this.statusRenderer.setBallCount( ballManager.getBallCount() );

        this.sessionRenderer.update(gameState);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        switch (gameState.state) {
            case MAIN_MENU:
                break;
            case GAMEOVER:
            case RETURNABLE_TO_MENU:
                sessionRenderer.draw(g2d, gameState);
                break;

            default:
                g2d.drawImage(img_hexagonBack, 0, 0, null);
                ballManager.draw(g2d);
                blockManager.draw(g2d);

                g2d.drawImage(img_floor, 0, FLOOR_Y, null);
                statusRenderer.draw(g2d);
                break;
        }
    }

    public static void main(String[] args)
    {
        new Game(new GamePanel(WIDTH, HEIGHT));
    }

    private void eventListenInit(Component screen)
    {
        this.screen.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent ev)
            {
                // CLICK_WAITの時にマウスの左ボタンがクリックされたら gameState を変更
                System.out.println("mouse: " + ev.getX() + ", " + ev.getY() + "   state: " + gameState);
                Game.this.screen.requestFocus();

                switch (gameState.state) {
                    case CLICK_WAIT:
                        if ( (ev.getButton() == MouseEvent.BUTTON1)
                                && ev.getY() < FLOOR_Y - (Ball.SIZE + 25)
                                && ev.getX() < STATUS_PANEL_X)
                        {
                            gameState.state = GameState.State.NOW_CLICKED;
                            gameState.mousePos.x = ev.getX();
                            gameState.mousePos.y = ev.getY();
                        }
                        break;

                    case RETURNABLE_TO_MENU:
                        gameState.state = GameState.State.CLICK_WAIT;
                        break;
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
                        if (gameState.state != GameState.State.CLICK_WAIT) {
                            gameState.keyPressed_space = true;
                        }
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

}
