package maze.coordinates;

import java.io.Serializable;

/** An immutable pair of coordinates. */
public final class ImmutablePoint extends Point implements Serializable {
    private static final long serialVersionUID = 3157903951803175800L;

    public final int x;
    public final int y;

    /**
     * Creates a point with the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a point from another point.
     *
     * @param  other the other point
     * @throws NullPointerException if other is null
     */
    public ImmutablePoint(ImmutablePoint other) {
        this(other.x, other.y);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
