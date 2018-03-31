package myGame;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class SessionRenderer
{
    //GameOverの画像についてのhogehoge
    private static final double DEFAULT_OPACITY = 0.2; //フェードインの最初の透明度(0に近いほど透明)
    private static final double FADE_IN_SPEED = 0.008; //不透明担っていく定数
    private static final int FIRST_IMG_Y = -80; //フェードインの最初のy座標
    private static final int END_IMG_Y = -55;  //フェードインの最後のy座標
    private static final double IMG_VY = 0.2;   //フェードインの下へ降りてくる速さ
    private static final int DELAY = 70;    //フェードインが終わった後の間
    private static final int TEXT_Y = 460;  //"YOUR SCORE"の下のy座標
    //タイトル画面の揺れる文字についてのhogehoge
    private static final int MIN_TOP_TXET_Y = 455;  //上
    private static final int MAX_BOTTOM_TEXT_Y = TEXT_Y; //下
    private static final double VAULE_TEXT_Y_ADD = 0.2; //揺れる速さ
    private static final String TEXT_MENU = "Click to start";

    private double opacity;
    private double img_y;
    private int delay = DELAY;
    private double text_y;
    private boolean isText_y_up;

    public SessionRenderer()
    {
        init();
    }

    public void init()
    {
        this.opacity = DEFAULT_OPACITY;
        this.img_y = FIRST_IMG_Y;
        this.delay = DELAY;
        this.text_y = TEXT_Y;
        this.isText_y_up = false;
        System.out.println("init() SessionRenderer");
    }

    public GameState update(GameState gameState)
    {
        switch (gameState.state) {
            case MAIN_MENU:
                textMove(); //文字を揺らす
                break;
            case GAMEOVER:
                if (img_y < END_IMG_Y) { //画像を下へ移動しながらフェードイン
                    img_y += IMG_VY;
                    opacity += FADE_IN_SPEED;
                    if (opacity > 1.0) opacity = 1.0;
                }
                else { //フェードインが終わったなら
                    if (delay > 0) {
                        --delay;    //間を空ける
                    } else {
                        System.out.println("---RETURNABLE");
                        gameState.state = GameState.State.RETURNABLE_TO_MENU;
                    }
                }
                break;
        }
        return gameState;
    }

    public void draw(Graphics2D g2d, GameState gameState)
    {
        switch (gameState.state) {
            case MAIN_MENU:
                drawMainMenu(g2d);
                break;
            case GAMEOVER:
                drawGameOver(g2d);
                break;
            case RETURNABLE_TO_MENU:
                drawGameOver(g2d);
                drawScore(g2d, gameState.getScore());
                break;
        }
    }

    // タイトル画面の文字列を揺らす
    private void textMove() {
        if (isText_y_up) {
            if (text_y > MIN_TOP_TXET_Y)
                text_y -= VAULE_TEXT_Y_ADD;
            else
                isText_y_up = false;
        }
        else {
            if (text_y < MAX_BOTTOM_TEXT_Y)
                text_y += VAULE_TEXT_Y_ADD;
            else
                isText_y_up = true;
        }
    }

    // フェードインが終わった後に描画する。最終的な結果を表示
    private void drawScore(Graphics2D g2d, int score)
    {
        // 初期の設定を保存して他のクラスが描画を安全に描画できるようにする
        final Font defaultFont = g2d.getFont();
        final RenderingHints defaultRenderingHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 22));
        g2d.drawString("Click to MAIN-MENU...", 420, 520);

        // "YOUR SCORE"の描画
        {
            final String S = "YOUR SCORE";
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
            Rectangle rect = g2d.getFontMetrics().getStringBounds(S, g2d).getBounds();
            g2d.drawString(S, Game.WIDTH / 2 - rect.width / 2, 370);
        }
        //実際のスコアの数値を描画
        {
            final String S = String.valueOf(score);
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 86));
            Rectangle rect = g2d.getFontMetrics().getStringBounds(S, g2d).getBounds();
            g2d.drawString(S, Game.WIDTH / 2 - rect.width / 2, 460);
        }

        //保存しておいた初期の設定を再設定
        g2d.setRenderingHints(defaultRenderingHints);
        g2d.setFont(defaultFont);
    }

    private void drawMainMenu(Graphics2D g2d)
    {
        g2d.drawImage(Game.img_logo, 0, 0, null);

        // 初期の設定を保存
        final Font defaultFont = g2d.getFont();
        final RenderingHints defaultRenderingHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 揺れる文字を描画
        {
            g2d.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 24));
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle rect = metrics.getStringBounds(TEXT_MENU, g2d).getBounds();

            final int x = Game.WIDTH / 2 - rect.width / 2; //中心
            g2d.drawString(TEXT_MENU, x, (int)text_y);
        }

        //保存しておいた設定を再設定
        g2d.setRenderingHints(defaultRenderingHints);
        g2d.setFont(defaultFont);
    }


    // GameOverの画像をフェードインして描画
    private void drawGameOver(Graphics2D g2d)
    {
        // 初期の設定を保存(透明度)
        final Composite defaultComposit =  g2d.getComposite();

        // 透明にするためのインスタンスを取得
        final AlphaComposite alphaComposite
                = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float)this.opacity);

        g2d.setComposite(alphaComposite);
        g2d.drawImage(Game.img_gameover, 0, (int)img_y, null);

        g2d.setComposite(defaultComposit);
    }
}
