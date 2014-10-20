package maze;

import java.io.Serializable;

/** 
 * Implements the binary tree algorithm. The algorithm carves in one of two
 * directions from each node. The result is a binary tree rooted at the corner
 * where the two carving directions converge.
 */
public class BinaryTreeMaze extends Maze implements Serializable {
    private static final long serialVersionUID = -8719576526379394919L;

    /**
     * Sets the dimensions. Call {@link #generate} to generate the maze.
     * 
     * @param width the width of the maze
     * @param height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public BinaryTreeMaze(int width, int height) {
        super(width, height);
    }

    @Override
    public void generate() {
        reset();
        addBorders();
        binaryTreeMaze();
    }
    
    private void binaryTreeMaze() {        
        for (int y = 1; y < getHeight(); ++y) {
            for (int x = 1; x < getWidth(); ++x) {
                addWall(x, y, coinToss() ? Direction.LEFT : Direction.UP);
            }
        }
    }
}
