package maze;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

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
        recursiveDivision();
    }
    
    private void recursiveDivision() {
        Deque<Rectangle> stack = new ArrayDeque<Rectangle>();
        stack.push(new Rectangle(0, 0, getWidth(), getHeight()));
        
        while (!stack.isEmpty()) {
            Rectangle r = stack.pop();
            
            if (r.width < 2 || r.height < 2) {
                continue;
            }

            int endX = r.x + r.width;
            int endY = r.y + r.height;
            
            /* The two subsections. */
            Rectangle a = new Rectangle(r.x, r.y, 0, 0);
            Rectangle b = new Rectangle();

            if (getOrientation(r.width, r.height) == HORIZONTAL) {
                int y = random(r.y, endY - 1); // Picks a random location.
                for (int x = r.x; x < endX; ++x) { // Places the wall.
                    addWall(x, y, Direction.DOWN);
                }
                carve(random(r.x, endX), y, Direction.DOWN); // Makes an opening.
                b.x = r.x;
                b.y = y + 1;
                b.width = r.width;
                b.height = endY - y - 1;
                a.width = r.width;
                a.height = b.y - r.y;
            } else { // Perpendicular version of the above.
                int x = random(r.x, endX - 1);
                for (int y = r.y; y < endY; ++y) {
                    addWall(x, y, Direction.RIGHT);
                }
                carve(x, random(r.y, endY), Direction.RIGHT);
                b.x = x + 1;
                b.y = r.y;
                b.width = endX - x - 1;
                b.height = r.height;
                a.width = b.x - r.x;
                a.height = r.height;
            }
            
            stack.push(b);
            stack.push(a);
        }
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
