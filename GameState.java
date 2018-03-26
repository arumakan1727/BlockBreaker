package myGame;

import java.awt.Point;

public class GameState
{
    public enum State
    {
        MAIN_MENU,
        CLICK_WAIT,
        NOW_CLICKED,
        BALL_FLYING,
        BLOCK_DOWN,
        GAMEOVER;
    }

    public State state;
    public Point mousePos = new Point();
    public boolean keyPressed_space = false;

    private int waveCount = 1;
    private int ballCount = BallManager.DEFAULT_BALL_COUNT;

    public void countUpWave()
    {
        waveCount++;
    }
    public int getWaveCount()
    {
        return this.waveCount;
    }
    public int getBallCount()
    {
        return ballCount;
    }
    public void setBallCount(int n)
    {
        this.ballCount = n;
    }


    @Override
    public String toString()
    {
        return super.toString() + " / " + mousePos.toString();
    }
}
