package maze;

import java.io.Serializable;
import java.util.Arrays;

import maze.coordinates.Point;

/**
 * A two-dimensional maze representation. The walls of a maze node are stored as
 * four bit fields. Each node is given a byte, leaving four unused bits per
 * node. These unused bits are made accessible to subclasses since they can be
 * useful when implementing maze generation algorithms that need to label nodes.
 */
public abstract class Maze implements Serializable {
    private static final long serialVersionUID = 6114059191423368387L;

    private static final int WALL_MASK = 0xf;

    private final byte[] b;
    private final int width;
    private final int height;

    /**
     * Sets the dimensions of the maze.
     *
     * @param  width  the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    protected Maze(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
        b = new byte[width * height];
    }

    /** Generates the maze. */
    public abstract void generate();

    /** Returns the width of the maze. */
    public int getWidth() {
        return width;
    }

    /** Returns the height of the maze. */
    public int getHeight() {
        return height;
    }

    /**
     * Checks for the presence of a wall at a node.
     *
     * @param  x the x-coordinate of the node
     * @param  y the y-coordinate of the node
     * @param  d the direction to check
     * @return true if the wall is present
     * @throws OutOfBoundsException if (x, y) is out of bounds
     * @throws NullPointerException if d is null
     */
    public boolean isWall(int x, int y, Direction d) {
        checkBounds(x, y);
        return (b[y * width + x] & d.mask) != 0;
    }

    /**
     * Checks for the presence of a wall at a node.
     *
     * @param  p the coordinates of the node
     * @param  d the direction to check
     * @return true if the wall is present
     * @throws OutOfBoundsException if p is out of bounds
     * @throws NullPointerException if an argument is null
     */
    public boolean isWall(Point p, Direction d) {
        return isWall(p.getX(), p.getY(), d);
    }

    /**
     * Adds a wall to a node.
     * 
     * @param  x the x-coordinate of the node
     * @param  y the y-coordinate of the node
     * @param  d the direction of the wall
     * @throws OutOfBoundsException if (x, y) is out of bounds
     * @throws NullPointerException if d is null
     */
    public void addWall(int x, int y, Direction d) {
        checkBounds(x, y);
        b[y * width + x] |= d.mask;
        int tx = x + d.dx;
        int ty = y + d.dy;
        if (isInBounds(tx, ty)) {
            b[ty * width + tx] |= d.getReverse().mask;
        }
    }

    /**
     * Adds a wall to a node.
     * 
     * @param  p the coordinates of the node
     * @param  d the direction of the wall
     * @throws OutOfBoundsException if p is out of bounds
     * @throws NullPointerException if an argument is null
     */
    public void addWall(Point p, Direction d) {
        addWall(p.getX(), p.getY(), d);
    }

    /** Puts walls on the border. */
    public void addBorder() {
        for (int y = 0; y < height; ++y) {
            b[y * width] |= Direction.WEST.mask;
            b[(y + 1) * width - 1] |= Direction.EAST.mask;
        }
        for (int x = 0; x < width; ++x) {
            b[x] |= Direction.NORTH.mask;
            b[(height - 1) * width + x] |= Direction.SOUTH.mask;
        }
    }

    /** Puts walls everywhere. */
    public void fill() {
        Arrays.fill(b, (byte) WALL_MASK);
    }

    /**
     * Removes a wall from a node.
     * 
     * @param  x the x-coordinate of the node
     * @param  y the y-coordinate of the node
     * @param  d the direction of the wall
     * @throws OutOfBoundsException if (x, y) is out of bounds
     * @throws NullPointerException if d is null
     */
    public void removeWall(int x, int y, Direction d) {
        checkBounds(x, y);
        b[y * width + x] &= ~d.mask;
        int tx = x + d.dx;
        int ty = y + d.dy;
        if (isInBounds(tx, ty)) {
            b[ty * width + tx] &= ~d.getReverse().mask;
        }
    }

    /**
     * Removes a wall from a node.
     * 
     * @param  p the coordinates of the node
     * @param  d the direction of the wall
     * @throws OutOfBoundsException if p is out of bounds
     * @throws NullPointerException if an argument is null
     */
    public void removeWall(Point p, Direction d) {
        removeWall(p.getX(), p.getY(), d);
    }

    /** Removes all walls. */
    public void clear() {
        Arrays.fill(b, (byte) 0);
    }

    /** Returns the flag bits for a node. */
    protected int getFlags(int x, int y) {
        /* Mask is required b/c of sign extension from widening conversion. */
        return (b[y * width + x] >> 4) & WALL_MASK;
    }

    /** Returns the flag bits for a node. */
    protected int getFlags(Point p) {
        return getFlags(p.getX(), p.getY());
    }

    /** Sets the flag bits for a node. */
    protected void setFlags(int x, int y, int flags) {
        int i = y * width + x;
        b[i] = (byte) ((b[i] & WALL_MASK) | (flags << 4));
    }

    /** Sets the flag bits for a node. */
    protected void setFlags(Point p, int flags) {
        setFlags(p.getX(), p.getY(), flags);
    }

    /** Checks if coordinates are in bounds. */
    protected boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /** Checks if coordinates are in bounds and throws an exception if not. */
    protected void checkBounds(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new OutOfBoundsException("(" + x + ", " + y + ")");
        }
    }

    /** Checks if a node is walled off from all directions. */
    protected boolean isUnvisited(int x, int y) {
        return (b[y * width + x] & WALL_MASK) == WALL_MASK;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for (int x = 0; x < width; ++x) {
            if ((b[x] & Direction.NORTH.mask) != 0) {
                builder.append("---+");
            } else {
                builder.append("   +");
            }
        }
        builder.append(lineSeparator);
        for (int y = 0; y < height; ++y) {
            int yw = y * width;
            if ((b[yw] & Direction.WEST.mask) != 0) {
                builder.append("|");
            } else {
                builder.append(" ");
            }
            for (int x = 0; x < width; ++x) {
                if ((b[yw + x] & Direction.EAST.mask) != 0) {
                    builder.append("   |");
                } else {
                    builder.append("    ");
                }
            }
            builder.append(lineSeparator);
            builder.append("+");
            for (int x = 0; x < width; ++x) {
                if ((b[yw + x] & Direction.SOUTH.mask) != 0) {
                    builder.append("---+");
                } else {
                    builder.append("   +");
                }
            }
            builder.append(lineSeparator);
        }
        return builder.toString();
    }
}
