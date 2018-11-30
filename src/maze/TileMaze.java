package maze;

import java.io.Serializable;

import maze.coordinates.Point;

/** Tile maze wrapper for {@link Maze}. */
public class TileMaze implements Serializable {
    private static final long serialVersionUID = -5050221133107186563L;

    private Maze maze;
    private int width;
    private int height;

    /**
     * Creates a tile maze wrapper around the specified {@link Maze} object.
     *
     * @param  maze the {@link Maze} object to be wrapped
     * @throws NullPointerException if maze is null
     */
    public TileMaze(Maze maze) {
        setMaze(maze);
    }

    /**
     * Wraps this around the specified {@link Maze} object.
     *
     * @param  maze the {@link Maze} object to be wrapped
     * @throws NullPointerException if maze is null
     */
    public void setMaze(Maze maze) {
        if (maze == null) {
            throw new NullPointerException();
        }
        this.maze = maze;
        width = 2 * maze.getWidth() + 1;
        height = 2 * maze.getHeight() + 1;
    }

    /** Returns the internal {@link Maze} object. */
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
     * Checks if the tile at the specified coordinates is a wall.
     *
     * @param  x the x-coordinate of the tile
     * @param  y the y-coordinate of the tile
     * @return true if the tile at (x, y) is a wall
     * @throws OutOfBoundsException if (x, y) is out of bounds
     */
    public boolean isWall(int x, int y) {
        checkBounds(x, y);
        boolean xIsEven = (x % 2 == 0);
        boolean yIsEven = (y % 2 == 0);
        if (xIsEven && yIsEven) {
            return true;
        }
        if (x == 0) {
            return maze.isWall(0, (y - 1) / 2, Direction.WEST);
        }
        if (y == 0) {
            return maze.isWall((x - 1) / 2, 0, Direction.NORTH);
        }
        if (xIsEven) {
            return maze.isWall((x - 1) / 2, (y - 1) / 2, Direction.EAST);
        }
        if (yIsEven) {
            return maze.isWall((x - 1) / 2, (y - 1) / 2, Direction.SOUTH);
        }
        return false;
    }

    /**
     * Checks if the tile at the specified coordinates is a wall.
     *
     * @param  p the coordinates of the tile to check
     * @return true if the tile at p is a wall
     * @throws OutOfBoundsException if p is out of bounds
     * @throws NullPointerException if p is null
     */
    public boolean isWall(Point p) {
        return isWall(p.getX(), p.getY());
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (isWall(x, y)) {
                    builder.append("# ");
                } else {
                    builder.append("  ");
                }
            }
            builder.append(lineSeparator);
        }
        return builder.toString();
    }

    private void checkBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new OutOfBoundsException("(" + x + ", " + y + ")");
        }
    }
}
