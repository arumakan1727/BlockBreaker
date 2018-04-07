package myGame;

import ydk.game.sprite.Sprite;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ScoreRenderer
{
    private int ballCount;
    private int waveCount;
    private int score;
    private List<OneUP> bonusPoses;

    private static final int WIDTH_DIFF = Block.WIDTH - BonusPanel.WIDTH;

    private static final BufferedImage img = Game.img_glossPanel;
    private static final int WIDTH = (Game.WIDTH - Game.STATUS_PANEL_X) +10; //ステータスパネルの幅

    private static final int HEIGHT = Game.HEIGHT + 40; //"WAVE"の文字列の下の座標
    private static final int BOTTOM_Y_STRING_WAVE = 30;
    private static final int BOTTOM_Y_STRING_BALL = 160;
    private static final int BOTTOM_Y_STRING_SCORE = 290;

    public ScoreRenderer() {
        bonusPoses = new ArrayList<>();
        init();
    }

    public void init()
    {
        ballCount = 0;
        waveCount = 1;
        score = 0;
        System.out.println("init() ScoreRenderer");
    }

    public GameState update(final GameState gameState)
    {
        Sprite.update(this.bonusPoses);

        final Queue<Point> que = gameState.bonusPos;
        while (!que.isEmpty())
        {
            final Point point = que.poll();
            this.bonusPoses.add(new OneUP(
                    Game.img_1up,
                    point.x - WIDTH_DIFF,
                    point.y
                    )
            );
        }
        return gameState;
    }

    public void draw(Graphics2D g2d)
    {
        Sprite.draw(bonusPoses, g2d);

        g2d.drawImage(img, Game.STATUS_PANEL_X, 0, WIDTH, HEIGHT, null);

        // 初期の設定を保存し,他のクラスが安全な描画を出来るようにする
        final RenderingHints  defaultRenderingHints = g2d.getRenderingHints();
        final Font defaultFont = g2d.getFont();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 文字列の色は白
        g2d.setColor(Color.WHITE);

        //WAVE数の描画
        {
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
            g2d.drawString("WAVE:", Game.STATUS_PANEL_X + 20, BOTTOM_Y_STRING_WAVE);

            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            String S = String.valueOf(waveCount);
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle rect = metrics.getStringBounds(S, g2d).getBounds();
            final int x = Game.STATUS_PANEL_X + (WIDTH / 2) - (rect.width / 2);
            final int y = BOTTOM_Y_STRING_WAVE + metrics.getAscent() + 10;
            g2d.drawString(S, x, y);
        }
        //BALLの数の描画
        {
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
            g2d.drawString("BALL  :", Game.STATUS_PANEL_X + 20, BOTTOM_Y_STRING_BALL);
            g2d.drawImage(Game.img_ball, Game.STATUS_PANEL_X + 76, BOTTOM_Y_STRING_BALL - Ball.SIZE, null);

            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            String S = String.valueOf(ballCount);
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle rect = metrics.getStringBounds(S, g2d).getBounds();
            final int x = Game.STATUS_PANEL_X + (WIDTH / 2) - (rect.width / 2);
            final int y = BOTTOM_Y_STRING_BALL + metrics.getAscent() + 10;
            g2d.drawString(S, x, y);
        }
        //SCOREの描画
        {
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
            g2d.drawString("SCORE:", Game.STATUS_PANEL_X + 20, BOTTOM_Y_STRING_SCORE);

            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            String S = String.valueOf(score);
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle rect = metrics.getStringBounds(S, g2d).getBounds();
            final int x = Game.STATUS_PANEL_X + (WIDTH / 2) - (rect.width / 2);
            final int y = BOTTOM_Y_STRING_SCORE + metrics.getAscent() + 10;
            g2d.drawString(S, x, y);
        }
        g2d.setRenderingHints(defaultRenderingHints);
        g2d.setFont(defaultFont);
    }

    public int getBallCount()
    {
        return ballCount;
    }

    public void setBallCount(int ballCount)
    {
        this.ballCount = ballCount;
    }

    public int getWaveCount()
    {
        return waveCount;
    }

    public void setWaveCount(int waveCount)
    {
        this.waveCount = waveCount;
    }


    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    private class OneUP extends Sprite
    {
        private static final double OPACITY_SUBTRACT_VALUE = 0.03; //1フレーム毎に透明化する値
        private static final double Y_MOVE_VALUE        = 0.4; //上へ移動させる値(=vy)
        private final BufferedImage img;
        private double oparity; //不透明度

        OneUP(final BufferedImage img, double x, double y)
        {
            this.img = img;
            this.x = x;
            this.y = y;
            this.oparity = 1;
        }


        @Override
        public void update(double eta)
        {
            this.y -= Y_MOVE_VALUE;
            this.oparity -= OPACITY_SUBTRACT_VALUE;
            if (this.oparity <= 0) {
                this.vanish();
            }
        }

        @Override
        public void draw(Graphics2D g2d)
        {
            Composite defaultComposite = g2d.getComposite();
            AlphaComposite composite
                    = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)this.oparity);

            g2d.setComposite(composite);
            g2d.drawImage(img, (int)x, (int)y, null);

            g2d.setComposite(defaultComposite);
        }
    }
}
