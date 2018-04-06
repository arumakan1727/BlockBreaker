package myGame;

public class CornerCollisionState
{
    protected boolean leftTop, rightTop, leftBtm, rightBtm;

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
    public void orAll(final CornerCollisionState rhs)
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
