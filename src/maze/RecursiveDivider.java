package maze;

import java.io.Serializable;
import java.util.Random;

/**
 * Implements the recursive division algorithm. The algorithm starts with an
 * empty space and recursively divides it into two with a randomly placed
 * horizontal or vertical wall.
 */
public class RecursiveDivider extends Maze implements Serializable {
    private static final long serialVersionUID = -6219935045687115436L;

    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL   = false;

    private final Random rnd;

    /**
     * Sets the dimensions of the maze.
     *
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecursiveDivider(int width, int height) {
        super(width, height);
        rnd = new Random();
    }

    @Override
    public void generate() {
        clear();
        addBorder();
        recursiveDivision(0, 0, getWidth(), getHeight());
    }

    private void recursiveDivision(int x, int y, int width, int height) {
        if (width <= 1 || height <= 1) {
            return;
        }

        int aw, ah;
        int bx, by, bw, bh;

        if (getOrientation(width, height) == HORIZONTAL) {
            int tx = x + width, ty = y + height;
            int wy = rnd.nextInt((ty - 1) - y) + y; // Picks a random location.
            for (int wx = x; wx < tx; ++wx) { // Places the wall.
                addWall(wx, wy, Direction.SOUTH);
            }
            removeWall(rnd.nextInt(tx - x) + x, wy, Direction.SOUTH); // Makes an opening.
            bx = x;
            by = wy + 1;
            bw = width;
            bh = ty - wy - 1;
            aw = width;
            ah = by - y;
        } else { // Perpendicular version of the above.
            int tx = x + width, ty = y + height;
            int wx = rnd.nextInt((tx - 1) - x) + x;
            for (int wy = y; wy < ty; ++wy) {
                addWall(wx, wy, Direction.EAST);
            }
            removeWall(wx, rnd.nextInt(ty - y) + y, Direction.EAST);
            bx = wx + 1;
            by = y;
            bw = tx - wx - 1;
            bh = height;
            aw = bx - x;
            ah = height;
        }

        recursiveDivision(x, y, aw, ah);
        recursiveDivision(bx, by, bw, bh);
    }

    /** Chooses wall orientation based on the dimensions of an area. */
    private boolean getOrientation(int width, int height) {
        if (width > 2 * height) {
            return VERTICAL;
        }
        if (height > 2 * width) {
            return HORIZONTAL;
        }
        return rnd.nextBoolean();
    }
}
