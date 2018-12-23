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

        for (int y = 0; y < ys; ++y) {
            for (int x = 0; x < xs; ++x) {
                /* Creates horizontal passages. */
                if (right[x] != x + 1 && rnd.nextInt(5) < 3) {
                    /* Unions the adjacent sets. */
                    left[right[x]] = left[x + 1];
                    right[left[x + 1]] = right[x];
                    right[x] = x + 1;
                    left[x + 1] = x;
                    removeWall(x, y, Direction.EAST);
                }
                /* Creates vertical passages. */
                if (right[x] != x && rnd.nextInt(5) < 3) {
                    left[right[x]] = left[x];
                    right[left[x]] = right[x];
                    left[x] = right[x] = x;
                } else {
                    removeWall(x, y, Direction.SOUTH);
                }
            }
            /* Creates vertical passages for the last column. */
            if (right[xs] != xs && rnd.nextInt(5) < 3) {
                left[right[xs]] = left[xs];
                right[left[xs]] = right[xs];
                left[xs] = right[xs] = xs;
            } else {
                removeWall(xs, y, Direction.SOUTH);
            }
        }

        /* Creates the last row. */
        for (int x = 0; x < xs; ++x) {
            if (right[x] != x + 1) {
                left[right[x]] = left[x + 1];
                right[left[x + 1]] = right[x];
                right[x] = x + 1;
                left[x + 1] = x;
                removeWall(x, ys, Direction.EAST);
            }
        }
    }
}
