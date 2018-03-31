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
        RIGHT_TOP, RIGHT_BOTTOM, LEFT_TOP, LEFT_BOTTOM;
    }

    //4すみの衝突状態を保持するクラス
    public static class CornerCollisionState
    {
        public boolean leftTop, rightTop, leftBtm, rightBtm;

        public CornerCollisionState(boolean a, boolean b, boolean c, boolean d)
        {
            leftTop     = a;
            rightTop    = b;
            leftBtm     = c;
            rightBtm    = d;
        }

        public CornerCollisionState()
        {
            leftTop = rightTop = leftBtm = rightBtm = false;
        }

        @Override
        public String toString()
        {
            return ("LT: "+leftTop + " RT: " + rightTop + " LB: "+leftBtm + " RB: "+rightBtm );
        }

        //引数のフィールドとORをとる
        public void orAll(CornerCollisionState rhs)
        {
            this.leftTop    |= rhs.leftTop;
            this.rightTop   |= rhs.rightTop;
            this.leftBtm    |= rhs.leftBtm;
            this.rightBtm   |= rhs.rightBtm;
        }

        // 4隅の衝突状態によってどこが衝突しているかを返す
        public RectBounds.Location whereCollisionAt()
        {
            if (leftTop && rightTop) {  // 左上と右上がブロック内
                return RectBounds.Location.TOP;
            }
            else if (leftBtm && rightBtm) {  // 左下と右下がブロック内
                return RectBounds.Location.BOTTOM;
            }
            else if (leftTop && leftBtm) {   // 左上と左下がブロック内
                return RectBounds.Location.LEFT;
            }
            else if (rightTop && rightBtm) { // 右上と右下がブロック内
                return RectBounds.Location.RIGHT;
            }
            else if (leftTop) {  // 左上
                return RectBounds.Location.LEFT_TOP;
            }
            else if (rightTop) { // 右上
                return RectBounds.Location.RIGHT_TOP;
            }
            else if (leftBtm) {  // 左下
                return RectBounds.Location.LEFT_BOTTOM;
            }
            else if (rightBtm) { // 右下
                return RectBounds.Location.RIGHT_BOTTOM;
            }
            else
                return RectBounds.Location.NIL;
        }
    }
}
