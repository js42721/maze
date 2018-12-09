package maze;

/** Represents a cardinal direction. */
public enum Direction {
    NORTH(1 << 0,  0, -1),
    EAST (1 << 1,  1,  0),
    SOUTH(1 << 2,  0,  1),
    WEST (1 << 3, -1,  0);

    public final int mask;
    public final int dx;
    public final int dy;

    private Direction reverse;

    static {
        NORTH.reverse = SOUTH;
        EAST.reverse  = WEST;
        SOUTH.reverse = NORTH;
        WEST.reverse  = EAST;
    }

    Direction(int mask, int dx, int dy) {
        this.mask = mask;
        this.dx = dx;
        this.dy = dy;
    }

    public Direction getReverse() {
        return reverse;
    }
}
