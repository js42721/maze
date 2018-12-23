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
     * @param  width the width of the maze
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
        /* Links for doubly linked lists (each set is stored as a list). */
        int[] left = new int[getWidth()];
        int[] right = new int[getWidth()];

        /* Each node in the first row starts out in its own set. */
        for (int x = 0; x < getWidth(); ++x) {
            left[x] = right[x] = x;
        }

        int xs = getWidth() - 1;
        int ys = getHeight() - 1;

        int ln, rx;

        for (int y = 0; y < ys; ++y) {
            for (int x = 0; x < xs; ++x) {
                /* Creates horizontal passages. */
                if ((rx = right[x]) != x + 1 && rnd.nextInt(5) < 3) {
                    /* Unions the sets by splicing their lists. */
                    ln = left[rx] = left[x + 1];
                    right[ln] = rx;
                    right[x] = x + 1;
                    left[x + 1] = x;
                    removeWall(x, y, Direction.EAST);
                }
                /* Creates vertical passages. */
                if ((rx = right[x]) != x && rnd.nextInt(5) < 3) {
                    ln = left[rx] = left[x];
                    right[ln] = rx;
                    left[x] = right[x] = x;
                } else {
                    removeWall(x, y, Direction.SOUTH);
                }
            }
            /* Creates vertical passages for the last column. */
            if ((rx = right[xs]) != xs && rnd.nextInt(5) < 3) {
                ln = left[rx] = left[xs];
                right[ln] = rx;
                left[xs] = right[xs] = xs;
            } else {
                removeWall(xs, y, Direction.SOUTH);
            }
        }

        /* Creates the last row. */
        for (int x = 0; x < xs; ++x) {
            if ((rx = right[x]) != x + 1) {
                ln = left[rx] = left[x + 1];
                right[ln] = rx;
                right[x] = x + 1;
                left[x + 1] = x;
                removeWall(x, ys, Direction.EAST);
            }
        }
    }
}
