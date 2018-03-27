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
        GAMEOVER,
        RETURNABLE_TO_MENU;
    }

    public State state;
    public Point mousePos = new Point();
    public boolean keyPressed_space = false;

    private int waveCount;
    private int ballCount;
    private int score;

    public void init()
    {
        this.state = State.MAIN_MENU;
        this.keyPressed_space = false;
        this.waveCount = 1;
        this.ballCount = BallManager.DEFAULT_BALL_COUNT;
        this.score = 0;
    }

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

    public int getScore()
    {
        return this.score;
    }
    public void addScore(int n)
    {
        this.score += n;
    }


    @Override
    public String toString()
    {
        return state.toString() + " / " + mousePos.toString();
    }
}
