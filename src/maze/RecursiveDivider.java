package maze;

import java.io.Serializable;

/**
 * Implements the recursive division algorithm. The algorithm starts with an
 * empty maze and recursively divides it into two with a randomly placed
 * horizontal or vertical wall.
 */
public class RecursiveDivider extends Maze implements Serializable {
    private static final long serialVersionUID = -758182684038944859L;
    
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL   = false;
    
    /**
     * Sets the dimensions. Call {@link #generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecursiveDivider(int width, int height) {
        super(width, height);
    }
    
    @Override
    public void generate() {
        reset();
        addBorders();
        recursiveDivision(0, 0, getWidth(), getHeight());
    }
    
    private void recursiveDivision(int x, int y, int width, int height) {
        if (width < 2 || height < 2) {
            return;
        }
        
        int aw, ah;
        int bx, by, bw, bh;

        if (getOrientation(width, height) == HORIZONTAL) {
            int tx = x + width, ty = y + height;
            int wy = random(y, ty - 1); // Picks a random location.
            for (int wx = x; wx < tx; ++wx) { // Places the wall.
                addWall(wx, wy, Direction.DOWN);
            }
            carve(random(x, tx), wy, Direction.DOWN); // Makes an opening.
            bx = x;
            by = wy + 1;
            bw = width;
            bh = ty - wy - 1;
            aw = width;
            ah = by - y;
        } else { // Perpendicular version of the above.
            int tx = x + width, ty = y + height;
            int wx = random(x, tx - 1);
            for (int wy = y; wy < ty; ++wy) {
                addWall(wx, wy, Direction.RIGHT);
            }
            carve(wx, random(y, ty), Direction.RIGHT);
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
        if (width > height * 2) {
            return VERTICAL;
        }
        if (height > width * 2) {
            return HORIZONTAL;
        }
        return coinToss();
    }
}
