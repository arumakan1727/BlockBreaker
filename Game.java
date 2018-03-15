package myGame;

import ydk.game.engine.GameEngine;
import ydk.game.engine.GameProcess;
import ydk.image.ImageEffect;

import javax.imageio.ImageIO;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game implements GameProcess
{
    public static final int WIDTH   = 720;
    public static final int HEIGHT  = 540;
    public static GameState gameState = GameState.MAIN_MENU;
    private final BlockManager blockManager;
    private BufferedImage img_ball, img_block, img_hexagonBack;
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
        //g2d.drawImage(img_hexagonBack, 0, 0, WIDTH, WIDTH, null);
        //g2d.drawImage(img_hexagonBack, 0, 0, HEIGHT, HEIGHT, null);
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

    private Game()
    {
        try {
            img_ball = ImageIO.read(getClass().getResourceAsStream("/resources/ball.png"));
            img_block = ImageIO.read(getClass().getResourceAsStream("/resources/block-bebel.png"));
            img_hexagonBack = ImageIO.read(getClass().getResourceAsStream("/resources/hexagon-back.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
        blockManager = new BlockManager(img_block);
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                GameEngine ge = new GameEngine(WIDTH, HEIGHT, new Game());
                ge.setWindowTitle("Block breaker");
                ge.start();
            }
        });
    }
}
