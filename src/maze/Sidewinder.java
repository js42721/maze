package maze;

import java.io.Serializable;
import java.util.Random;

import maze.Direction;
import maze.Maze;

/** Implements the Sidewinder algorithm. */
public class Sidewinder extends Maze implements Serializable {
    private static final long serialVersionUID = -9051529889756722075L;

    private final Random rnd;

    /**
     * Sets the dimensions of the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Sidewinder(int width, int height) {
        super(width, height);
        rnd = new Random();
    }

    @Override
    public void generate() {
        fill();
        sidewinder();
    }

    private void sidewinder() {
        for (int x = 0; x < getWidth() - 1; ++x) {
            removeWall(x, 0, Direction.EAST);
        }
        for (int y = 1; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                int z = 1;
                while (x < getWidth() - 1 && rnd.nextBoolean()) {
                    removeWall(x, y, Direction.EAST);
                    ++x;
                    ++z;
                }
                removeWall(x - rnd.nextInt(z), y, Direction.NORTH);
            }
        }
    }
}
