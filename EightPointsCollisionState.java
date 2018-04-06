package myGame;

import static myGame.RectBounds.Location;

public class EightPointsCollisionState extends CornerCollisionState
{
    protected boolean top, bottom, left, right;

    public EightPointsCollisionState(
            boolean a, boolean b, boolean c, boolean d,
            boolean top, boolean bottom, boolean left, boolean right)
    {
        super(a, b, c, d);
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public EightPointsCollisionState()
    {
    }

    public void orAll(final EightPointsCollisionState rhs)
    {
        super.orAll(rhs);
        this.top    |= rhs.top;
        this.bottom |= rhs.bottom;
        this.left   |= rhs.left;
        this.right  |= rhs.right;
    }

    @Override
    public Location whereCollisionAt()
    {
        Location location
                = super.whereCollisionAt();

        switch (location)
        {
            case LEFT_BOTTOM:
                if (!(left && bottom)) {
                    if (left) location = Location.LEFT;
                    else if (bottom) location = Location.BOTTOM;
                }
                break;

            case RIGHT_BOTTOM:
                if (!(right && bottom)) {
                    if (right) location = Location.RIGHT;
                    else if (bottom) location = Location.BOTTOM;
                }
                break;

            case LEFT_TOP:
                if (!(left && top)) {
                    if (left) location = Location.LEFT;
                    else if (top) location = Location.TOP;
                }
                break;

            case RIGHT_TOP:
                if (!(right && top)) {
                    if (right) location = Location.RIGHT;
                    else if (top) location = Location.TOP;
                }
                break;
        }
        return (location);
    }
}
