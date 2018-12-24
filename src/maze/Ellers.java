package maze;

import java.io.Serializable;
import java.util.Random;

/** Implements an algorithm which some people refer to as Eller's algorithm. */
public class Ellers extends Maze implements Serializable {
    private static final long serialVersionUID = -4403644618765868512L;

    private final Random rnd;

    /**
     * Sets the dimensions of the maze.
     *
     * @param  width  the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Ellers(int width, int height) {
        super(width, height);
        rnd = new Random();
    }

    @Override
    public void generate() {
        fill();
        ellers();
    }

    private void ellers() {
        /*
         * Circular doubly linked lists are used to store sets of connected maze
         * nodes and these arrays store the left/right links of those lists.
         */
        int[] l = new int[getWidth()];
        int[] r = new int[getWidth()];

        /* Each node in the first row starts out in its own set. */
        for (int x = 0; x < getWidth(); ++x) {
            l[x] = r[x] = x;
        }

        int xl = getWidth() - 1;
        int yl = getHeight() - 1;

        for (int y = 0; y < yl; ++y) {
            for (int x = 0; x < xl; ++x) {
                /* Creates horizontal passages. */
                if (r[x] != x + 1 && rnd.nextInt(5) < 3) {
                    /* Unions the sets by performing a list splice. */
                    l[r[x]] = l[x + 1];
                    r[l[x + 1]] = r[x];
                    l[x + 1] = x;
                    r[x] = x + 1;
                    removeWall(x, y, Direction.EAST);
                }
                /* Creates vertical passages. */
                if (r[x] != x && rnd.nextInt(5) < 3) {
                    /* Removes node from list so it has its own set. */
                    l[r[x]] = l[x];
                    r[l[x]] = r[x];
                    l[x] = r[x] = x;
                } else {
                    removeWall(x, y, Direction.SOUTH);
                }
            }
            /* Creates vertical passages for the last column. */
            if (r[xl] != xl && rnd.nextInt(5) < 3) {
                l[r[xl]] = l[xl];
                r[l[xl]] = r[xl];
                l[xl] = r[xl] = xl;
            } else {
                removeWall(xl, y, Direction.SOUTH);
            }
        }

        /* Creates the last row. */
        for (int x = 0; x < xl; ++x) {
            if (r[x] != x + 1) {
                l[r[x]] = l[x + 1];
                r[l[x + 1]] = r[x];
                l[x + 1] = x;
                r[x] = x + 1;
                removeWall(x, yl, Direction.EAST);
            }
        }
    }
}
