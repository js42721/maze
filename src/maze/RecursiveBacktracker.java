package maze;

import java.io.Serializable;
import java.util.Random;

import maze.coordinates.Node;
import maze.coordinates.Point;

/**
 * Implements the recursive backtracking algorithm. The algorithm works by
 * exploring unvisited nodes using randomized depth-first search.
 */
public class RecursiveBacktracker extends Maze implements Serializable {
    private static final long serialVersionUID = -5689416515127359434L;

    private final Random rnd;
    private final Node start;

    /**
     * Sets the dimensions of the maze.
     *
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecursiveBacktracker(int width, int height) {
        super(width, height);
        rnd = new Random();
        start = new Node(rnd.nextInt(width), rnd.nextInt(height));
    }

    /**
     * Sets the dimensions of the maze and the starting point of the maze
     * generation algorithm.
     *
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @param  startX the x-coordinate of the algorithm's starting point
     * @param  startY the y-coordinate of the algorithm's starting point
     * @throws IllegalArgumentException if width or height is not positive
     * @throws OutOfBoundsException if (x, y) is out of bounds
     */
    public RecursiveBacktracker(int width, int height, int startX, int startY) {
        super(width, height);
        checkBounds(startX, startY);
        start = new Node(startX, startY);
        rnd = new Random();
    }

    /**
     * Sets the dimensions of the maze and the starting point of the maze
     * generation algorithm.
     *
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @param  start the algorithm's starting point
     * @throws IllegalArgumentException if width or height is not positive
     * @throws OutOfBoundsException if start is out of bounds
     * @throws NullPointerException if start is null
     */
    public RecursiveBacktracker(int width, int height, Point start) {
        this(width, height, start.getX(), start.getY());
    }

    /**
     * Sets the starting point of the maze generation algorithm.
     *
     * @param  startX the x-coordinate of the algorithm's starting point
     * @param  startY the y-coordinate of the algorithm's starting point
     * @throws OutOfBoundsException if (x, y) is out of bounds
     */
    public void setStart(int x, int y) {
        checkBounds(x, y);
        start.set(x, y);
    }

    /**
     * Sets the starting point of the maze generation algorithm.
     *
     * @param  start the algorithm's starting point
     * @throws OutOfBoundsException if start is out of bounds
     * @throws NullPointerException if start is null
     */
    public void setStart(Point start) {
        setStart(start.getX(), start.getY());
    }

    /** Returns the starting point of the maze generation algorithm. */
    public Point getStart() {
        return new Node(start);
    }

    @Override
    public void generate() {
        fill();
        recursiveBacktrack(start);
    }

    private void recursiveBacktrack(Node start) {
        Direction[] directions = Direction.values();
        Direction[] moves = new Direction[4];
        int unvisited = getWidth() * getHeight() - 1;

        Node current = new Node(start);

        while (unvisited > 0) {
            /* Finds adjacent unvisited nodes. */
            int moveCount = getMoves(current, moves);

            /* Takes a step back if there are no such nodes. */
            if (moveCount == 0) {
                /* Moves in the reverse of the saved direction. */
                Direction rev = directions[getFlags(current)].getReverse();
                current.translate(rev.dx, rev.dy);
                continue;
            }

            /* Picks a random adjacent unvisited node and adds it to the maze. */
            Direction d = moves[rnd.nextInt(moveCount)];
            removeWall(current, d);

            /* Updates the current node to the newly added node. */
            current.translate(d.dx, d.dy);

            /* Saves the direction taken to reach the current node. */
            setFlags(current, d.ordinal());

            --unvisited;
        }
    }

    /** Gets the directions which point to adjacent unvisited nodes. */
    private int getMoves(Node n, Direction[] moves) {
        int count = 0;
        if (n.y > 0 && isUnvisited(n.x, n.y - 1)) {
            moves[count++] = Direction.NORTH;
        }
        if (n.x < getWidth() - 1 && isUnvisited(n.x + 1, n.y)) {
            moves[count++] = Direction.EAST;
        }
        if (n.y < getHeight() - 1 && isUnvisited(n.x, n.y + 1)) {
            moves[count++] = Direction.SOUTH;
        }
        if (n.x > 0 && isUnvisited(n.x - 1, n.y)) {
            moves[count++] = Direction.WEST;
        }
        return count;
    }
}
