package maze;

import java.io.Serializable;

import maze.Maze.Direction;

/** Tile maze wrapper for {@link Maze}. */
public class TileMaze implements Serializable {
    private static final long serialVersionUID = -5050221133107186563L;
    
    private Maze maze;
    private int width;
    private int height;
    
    /**
     * Creates a tile maze wrapper around the specified maze, allowing
     * tile-centric interaction.
     * 
     * @param  maze the maze to wrap
     * @throws NullPointerException if the maze is null
     */
    public TileMaze(Maze maze) {
        setMaze(maze);
    }
    
    /**
     * Wraps this around the specified maze.
     * 
     * @param  maze the maze to wrap
     * @throws NullPointerException if the maze is null
     */
    public void setMaze(Maze maze) {
        if (maze == null) {
            throw new NullPointerException();
        }
        this.maze = maze;
        width = maze.getWidth() * 2 + 1;
        height = maze.getHeight() * 2 + 1;
    }
    
    /** Returns the internal maze object. */
    public Maze getMaze() {
        return maze;
    }
    
    /** Generates the maze. */
    public void generate() {
        maze.generate();
    }
    
    /** Returns the width of the maze in tiles. */
    public int getWidth() {
        return width;
    }

    /** Returns the height of the maze in tiles. */
    public int getHeight() {
        return height;
    }
    
    /**
     * Checks if the tile at the specified position is a wall.
     * 
     * @param  x the x-coordinate of the tile to check
     * @param  y the y-coordinate of the tile to check
     * @return true if the tile is a wall
     * @throws PositionOutOfBoundsException if (x, y) is out of bounds
     */
    public boolean isWall(int x, int y) {
        checkBounds(x, y);
        boolean xEven = (x % 2 == 0);
        boolean yEven = (y % 2 == 0);
        if (xEven && yEven) { // Even-even means wall.
            return true;
        }
        if (x == 0) { // Tile is on the left border.
            return maze.isWall((x - 1) / 2, (y - 1) / 2, Direction.LEFT);
        }
        if (y == 0) { // Tile is on the upper border.
            return maze.isWall((x - 1) / 2, (y - 1) / 2, Direction.UP);
        }
        if (xEven) { // Tile is on the right of the position's transform.
            return maze.isWall((x - 1) / 2, (y - 1) / 2, Direction.RIGHT);
        }
        if (yEven) { // Tile is below the position's transform.
            return maze.isWall((x - 1) / 2, (y - 1) / 2, Direction.DOWN);
        }
        return false; // Odd-odd (the rest) means no wall.
    }
    
    /**
     * Checks if the tile at the specified position is a wall.
     * 
     * @param  p the position of the tile to check
     * @return true if the tile is a wall
     * @throws PositionOutOfBoundsException if p is out of bounds
     * @throws NullPointerException if p is null
     */
    public boolean isWall(Position p) {
        return isWall(p.getX(), p.getY());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (isWall(x, y)) {
                    builder.append("# ");
                } else {
                    builder.append("  ");
                }
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
    
    private void checkBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new PositionOutOfBoundsException("(" + x + ", " + y + ")");
        }
    }
}
