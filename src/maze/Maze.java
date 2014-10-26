package maze;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/** 
 * A two-dimensional maze representation. The walls of a maze node are stored
 * as four bit fields. Each node is given a byte, leaving four unused bits per
 * node. These unused bits are made accessible to subclasses since they can be
 * useful when implementing maze generation algorithms that need to label
 * nodes.
 */
public abstract class Maze implements Serializable {
    private static final long serialVersionUID = -4078712705879675787L;

    private static final int WALL_MASK = 0b1111;
    
    private final int width;
    private final int height;
    private final byte[] maze;
    
    /**
     * Sets the dimensions.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    protected Maze(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
        maze = new byte[width * height];
    }

    /** Generates the maze. */
    public abstract void generate();
    
    /** Returns the width of the maze. */
    public final int getWidth() {
        return width;
    }

    /** Returns the height of the maze. */
    public final int getHeight() {
        return height;
    }
    
    /**
     * Checks if a certain wall exists at a position.
     * 
     * @param  x the x-coordinate of the position
     * @param  y the y-coordinate of the position
     * @param  d the direction to check
     * @return true if the wall exists
     * @throws PositionOutOfBoundsException if (x, y) is out of bounds
     * @throws NullPointerException if d is null
     */
    public final boolean isWall(int x, int y, Direction d) {
        checkPosition(x, y);
        return (maze[y * width + x] & d.mask) != 0;
    }

    /**
     * Checks if a certain wall exists at a position.
     * 
     * @param  p the position
     * @param  d the direction to check
     * @return true if the wall exists
     * @throws PositionOutOfBoundsException if p is out of bounds
     * @throws NullPointerException if an argument is null
     */
    public final boolean isWall(Position p, Direction d) {
        return isWall(p.getX(), p.getY(), d);
    }    
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for (int x = 0; x < width; ++x) {
            builder.append("---+");
        }
        builder.append(System.lineSeparator());
        for (int y = 0; y < height; ++y) {
            int offset = y * width;
            builder.append("|");
            for (int x = 0; x < width; ++x) {
                if ((maze[offset + x] & Direction.RIGHT.mask) != 0) {
                    builder.append("   |");
                } else {
                    builder.append("    ");
                }
            }
            builder.append(System.lineSeparator());
            builder.append("+");
            for (int x = 0; x < width; ++x) {
                if ((maze[offset + x] & Direction.DOWN.mask) != 0) {
                    builder.append("---+");
                } else {
                    builder.append("   +");
                }
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
    
    /** Fills the maze with walls (leaves flags intact). */
    protected final void fillWalls() {
        for (int i = 0; i < maze.length; ++i) {
            maze[i] |= WALL_MASK;
        }
    }
    
    /** Removes all walls (leaves flags intact). */
    protected final void clearWalls() {
        for (int i = 0; i < maze.length; ++i) {
            maze[i] &= ~WALL_MASK;
        }
    }
    
    /** Clears all walls and flags. */
    protected final void reset() {
        Arrays.fill(maze, (byte)0);
    }
    
    /** Fills the maze with walls and clears all flags. */
    protected final void resetFill() {
        Arrays.fill(maze, (byte)WALL_MASK);
    }

    /** Creates the borders. */
    protected final void addBorders() {
        for (int y = 0; y < getHeight(); ++y) {
            maze[y * width] |= Direction.LEFT.mask;
            maze[y * width + width - 1] |= Direction.RIGHT.mask;
        }
        for (int x = 0; x < getWidth(); ++x) {
            maze[x] |= Direction.UP.mask;
            maze[(height - 1) * width + x] |= Direction.DOWN.mask;
        }
    }
    
    /** Places a wall between two positions. */
    protected final void addWall(int x, int y, Direction d) {
        maze[y * width + x] |= d.mask;
        maze[(y + d.dy) * width + x + d.dx] |= d.getReverse().mask;
    }
    
    /** Places a wall between two positions. */
    protected final void addWall(Position p, Direction d) {
        addWall(p.getX(), p.getY(), d);
    }
    
    /** Removes the wall between two positions. */
    protected final void carve(int x, int y, Direction d) {
        maze[y * width + x] &= ~d.mask;
        maze[(y + d.dy) * width + x + d.dx] &= ~d.getReverse().mask;
    }

    /** Removes the wall between two positions. */
    protected final void carve(Position p, Direction d) {
        carve(p.getX(), p.getY(), d);
    }

    /** Adds a wall to the borders. */
    protected final void addBorder(int x, int y, Direction d) {
        if (!isBorder(x, y, d)) {
            throw new IllegalArgumentException("Not a valid border");
        }
        maze[y * width + x] |= d.mask;
    }

    /** Adds a wall to the borders. */
    protected final void addBorder(Position p, Direction d) {
        addBorder(p.getX(), p.getY(), d);
    }

    /** Removes a wall from the borders. */
    protected final void removeBorder(int x, int y, Direction d) {
        if (!isBorder(x, y, d)) {
            throw new IllegalArgumentException("Not a valid border");
        }
        maze[y * width + x] &= ~d.mask;
    }

    /** Removes a wall from the borders. */
    protected final void removeBorder(Position p, Direction d) {
        removeBorder(p.getX(), p.getY(), d);
    }
    
    /** Checks if a position is free of walls on all sides. */
    protected final boolean isClear(int x, int y) {
        return (maze[y * width + x] & WALL_MASK) == 0;
    }

    /** Checks if a position is free of walls on all sides. */
    protected final boolean isClear(Position p) {
        return isClear(p.getX(), p.getY());
    }
    
    /** Checks if a position is blocked by walls on all sides. */
    protected final boolean isClosed(int x, int y) {
        return (maze[y * width + x] & WALL_MASK) == WALL_MASK;
    }

    /** Checks if a position is blocked by walls on all sides. */
    protected final boolean isClosed(Position p) {
        return isClosed(p.getX(), p.getY());
    }

    /** Checks if a position is in maze bounds. */
    protected final boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /** Checks if a wall is a border. */
    protected final boolean isBorder(int x, int y, Direction d) {
        int validBorders = 0;
        if (y == 0) {
            validBorders |= Direction.UP.mask;
        } 
        if (x == 0) {
            validBorders |= Direction.LEFT.mask;
        } 
        if (y == height - 1) {
            validBorders |= Direction.DOWN.mask;
        }
        if (x == width - 1) {
            validBorders |= Direction.RIGHT.mask;
        }
        return (validBorders & d.mask) != 0;
    }

    /** Checks if a position is in bounds and throws an exception if it isn't. */
    protected final void checkPosition(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new PositionOutOfBoundsException("(" + x + ", " + y + ")");
        }
    }

    /** Checks if a position is in bounds and throws an exception if it isn't. */
    protected final void checkPosition(Position p) {        
        checkPosition(p.getX(), p.getY());
    }

    /** Returns the flag bits for the specified position. */
    protected final int getFlags(int x, int y) {
        return (maze[y * width + x] >> 4);
    }

    /** Returns the flag bits for the specified position. */
    protected final int getFlags(Position p) {
        return getFlags(p.getX(), p.getY());
    }

    /** Sets the flag bits for the specified position. */
    protected final void setFlags(int x, int y, int flags) {
        int i = y * width + x;
        maze[i] = (byte)(maze[i] & WALL_MASK | flags << 4);
    }
    
    /** Sets the flag bits for the specified position. */
    protected final void setFlags(Position p, int flags) {
        setFlags(p.getX(), p.getY(), flags);
    }
    
    /** Clears all flags. */
    protected final void resetFlags() {
        for (int i = 0; i < maze.length; ++i) {
            maze[i] &= WALL_MASK;
        }
    }
    
    /** Returns a random boolean. */
    protected static boolean coinToss() {
        return ThreadLocalRandom.current().nextBoolean();
    }
    
    /** Returns a random integer in the range [0, n). */
    protected static int random(int n) {
        return ThreadLocalRandom.current().nextInt(n);
    }
    
    /** Returns a random integer in the range [m, n). */ 
    protected static int random(int m, int n) {
        return ThreadLocalRandom.current().nextInt(m, n);
    }
    
    public enum Direction {
        UP   (0b0001,  0, -1),
        LEFT (0b0010, -1,  0),
        DOWN (0b0100,  0,  1),
        RIGHT(0b1000,  1,  0);
        
        public final int dx;
        public final int dy;

        private final int mask;
        private Direction reverse;
        
        static {
            UP.reverse    = DOWN;
            LEFT.reverse  = RIGHT;
            DOWN.reverse  = UP;
            RIGHT.reverse = LEFT;
        }
        
        Direction(int mask, int dx, int dy) {
            this.mask = mask;
            this.dx = dx;
            this.dy = dy;
        }
        
        public Direction getReverse() {
            return reverse;
        }
    }
}
