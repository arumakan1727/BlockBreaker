package myGame;

public class RectBounds
{
    private int ax, ay, bx, by;

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

    //自分の矩形と引数の矩形が衝突してるか
    public boolean collision(RectBounds r)
    {
        return ((this.ax < r.bx) && (r.ax < this.bx)
                && (this.ay < r.by) && (r.ay < this.by));
    }

    // 矩形内に引数の座標が入るか
    public boolean containts(int x, int y)
    {
        return ((x >= ax) && (y >= ay) && (x <= bx) && (y <= by));
    }

    public boolean containts(double x, double y)
    {
        return (containts((int)x, (int)y));
    }

    public static CornerCollisionState getCornerCollisionState(final RectBounds me, final RectBounds target)
    {
        return new CornerCollisionState(
                target.containts(me.ax, me.ay),
                target.containts(me.bx, me.ay),
                target.containts(me.ax, me.by),
                target.containts(me.bx, me.by)
        );
    }

    public static EightPointsCollisionState getEightPointsCollisionState(final RectBounds me, final RectBounds target)
    {
        final int centerX = (me.ax + me.bx) / 2;
        final int centerY = (me.ay + me.by) / 2;

        return new EightPointsCollisionState(
                target.containts(me.ax, me.ay),
                target.containts(me.bx, me.ay),
                target.containts(me.ax, me.by),
                target.containts(me.bx, me.by),
                target.containts(centerX, me.ay),   //top
                target.containts(centerX, me.by),   //bottom
                target.containts(me.ax, centerY),   //left
                target.containts(me.by, centerY)    //right
        );
    }

    public static RectBounds.Location whereCollisionAt(RectBounds me, RectBounds target)
    {
        final CornerCollisionState corner
                = RectBounds.getCornerCollisionState(me, target);
        return corner.whereCollisionAt();
    }

    //=================================================================================================================

    //どこが衝突しているかの列挙体
    public enum Location
    {
        NIL, TOP, BOTTOM, LEFT, RIGHT,
        RIGHT_TOP, RIGHT_BOTTOM, LEFT_TOP, LEFT_BOTTOM,
    }
}
