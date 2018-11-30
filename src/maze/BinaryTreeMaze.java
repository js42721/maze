package maze;

import java.io.Serializable;
import java.util.Random;

/**
 * Implements the binary tree algorithm. The algorithm carves in one of two
 * directions from each node. The result is a binary tree rooted at the corner
 * where the two carving directions converge.
 */
public class BinaryTreeMaze extends Maze implements Serializable {
    private static final long serialVersionUID = -7079713075260480096L;

    private final Random rnd;

    /**
     * Sets the dimensions of the maze.
     *
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public BinaryTreeMaze(int width, int height) {
        super(width, height);
        rnd = new Random();
    }

    @Override
    public void generate() {
        clear();
        addBorder();
        binaryTreeMaze();
    }

    private void binaryTreeMaze() {
        for (int y = 1; y < getHeight(); ++y) {
            for (int x = 1; x < getWidth(); ++x) {
                addWall(x, y, rnd.nextBoolean() ? Direction.WEST : Direction.NORTH);
            }
        }
    }
}
