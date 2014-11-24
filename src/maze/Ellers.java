package maze;

import java.io.Serializable;

import fastrandom.FastRandom;
import fastrandom.Taus88;

/** Implements an algorithm which some people refer to as Eller's algorithm. */
public class Ellers extends Maze implements Serializable {
    private static final long serialVersionUID = -4403644618765868512L;
    
    private final FastRandom rnd;

    /**
     * Sets the dimensions. Call {@code generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Ellers(int width, int height) {
        super(width, height);
        rnd = new Taus88();
    }

    @Override
    public void generate() {
        resetFill();
        ellers();
    }
    
    private void ellers() {
        int width = getWidth();
        int height = getHeight();
        int xEnd = width - 1;
        int yEnd = height - 1;
        
        /* Left/right pointers of a doubly-linked list (the set container). */
        int[] left = new int[width];
        int[] right = new int[width];
        
        /* Initializes the sets. */
        for (int x = 0; x < width; ++x) {
            left[x] = right[x] = x;
        }
        
        for (int y = 0; y < yEnd; ++y) {
            for (int x = 0; x < xEnd; ++x) {
                /* Creates horizontal passages. */
                if (right[x] != x + 1 && rnd.nextInt(5) < 3) {
                    /* Unions the two sets. */
                    left[right[x]] = left[x + 1];
                    right[left[x + 1]] = right[x];
                    right[x] = x + 1;
                    left[x + 1] = x;
                    carve(x, y, Direction.RIGHT);
                }
                /* Creates vertical passages. */
                if (right[x] != x && rnd.nextInt(5) < 3) {
                    left[right[x]] = left[x];
                    right[left[x]] = right[x];
                    left[x] = right[x] = x;
                } else {
                    carve(x, y, Direction.DOWN);
                }
            }

            /* Handles the rightmost position. */
            if (right[xEnd] != xEnd && rnd.nextInt(5) < 3) {
                left[right[xEnd]] = left[xEnd];
                right[left[xEnd]] = right[xEnd];
                left[xEnd] = right[xEnd] = xEnd;
            } else {
                carve(xEnd, y, Direction.DOWN);
            }
        }
        
        /* Creates the last row. */
        for (int x = 0; x < xEnd; ++x) {
            if (right[x] != x + 1) {
                left[right[x]] = left[x + 1];
                right[left[x + 1]] = right[x];
                right[x] = x + 1;
                left[x + 1] = x;
                carve(x, height - 1, Direction.RIGHT);
            }
        }
    }
}