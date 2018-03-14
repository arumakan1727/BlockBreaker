package myGame;

public class RectBounds
{
    private int ax, ay, bx, by;

    public enum Location
    {
        NIL, TOP, BOTTOM, LEFT, RIGHT,
        R_TOP, R_BOTTOM, L_TOP, L_BOTTOM;
    }

    /**
     * 左上の座標を(ax,ay), 右下の座標を(bx,by)とする矩形を生成します。<br>
     * 右上と左下の座標位置関係を満たす必要があり, (ax < bx)かつ(ay < by)でなければなりません。
     * @param ax 左上の x座標
     * @param ay 左上の y座標
     * @param bx 右下の x座標
     * @param by 左下の y座標
     * @throws IllegalArgumentException 右上・左下の位置関係が満たされない, すなわち(ax >= bx)または(ay >= by)となった時
     */
    public RectBounds(int ax, int ay, int bx, int by) throws IllegalArgumentException
    {
        if (ax >= bx || ay >= by)
            throw new IllegalArgumentException();
        this.ax = ax;
        this.ay = ay;
        this.bx = bx;
        this.by = by;
    }

    public RectBounds(double ax, double ay, double bx, double by) throws IllegalArgumentException
    {
        this((int)ax, (int)ay, (int)bx, (int)by);
    }

    public boolean collision(RectBounds r)
    {
        return ((this.ax < r.bx) && (r.ax < this.bx)
                && (this.ay < r.by) && (r.ay < this.by));
    }

    public boolean containts(int x, int y)
    {
        return ((x > ax) && (y > ay) && (x < bx) && (y < by));
    }

    public boolean containts(double x, double y)
    {
        return ((x > ax) && (y > ay) && (x < bx) && (y < by));
    }
}
