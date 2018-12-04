package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import maze.coordinates.Node;
import maze.coordinates.Point;

/**
 * Implements a randomized version of Prim's algorithm. This algorithm is not
 * to be confused with regular Prim's algorithm with random weights. While
 * both algorithms work by expanding from a single node, the randomized
 * version simply chooses edges randomly rather than assigning random edge
 * weights and choosing edges by weight. The randomized version spreads from
 * the starting point more or less evenly, creating a different kind of
 * spanning tree.
 */
public class RandomizedPrims extends Maze implements Serializable {
    private static final long serialVersionUID = 1761553893140412668L;

    private static final int OUT      = 0;
    private static final int IN       = 1 << 0;
    private static final int FRONTIER = 1 << 1;

    private final Random rnd;
    private final Node start;

    /**
     * Sets the dimensions of the maze.
     *
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RandomizedPrims(int width, int height) {
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
    public RandomizedPrims(int width, int height, int startX, int startY) {
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
    public RandomizedPrims(int width, int height, Point start) {
        this(width, height, start.getX(), start.getY());
    }

    /**
     * Sets the starting point of the maze generation algorithm.
     *
     * @param  x the x-coordinate of the algorithm's starting point
     * @param  y the y-coordinate of the algorithm's starting point
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
        randomizedPrims(start);
    }

    private void randomizedPrims(Node start) {
        /* Frontiers are the unvisited nodes adjacent to the visited ones. */
        List<Node> frontiers = new ArrayList<Node>();
        Direction[] neighbors = new Direction[4];

        /* Marks the starting node as visited and gets its frontiers. */
        setFlags(start, IN);
        getFrontiers(start, frontiers);

        while (!frontiers.isEmpty()) {
            /* Picks a random frontier. */
            int random = rnd.nextInt(frontiers.size());
            int last = frontiers.size() - 1;
            Collections.swap(frontiers, random, last); // For O(1) removal.
            Node current = frontiers.remove(last);

            /* Picks a random visited neighbor of the frontier. */
            int neighborCount = getVisitedNeighbors(current, neighbors);
            Direction d = neighbors[rnd.nextInt(neighborCount)];

            /*
             * Removes the wall between the frontier and the selected neighbor
             * and then marks the frontier as visited.
             */
            removeWall(current, d);
            setFlags(current, IN);

            /* Looks for new frontiers. */
            getFrontiers(current, frontiers);
        }
    }

    /**
     * Finds the unvisited neighbors of a node and adds them to the frontier
     * list if they were not already included.
     */
    private void getFrontiers(Node n, List<Node> frontiers) {
        if (n.y > 0 && getFlags(n.x, n.y - 1) == OUT) {
            markFrontier(n.x, n.y - 1, frontiers);
        }
        if (n.x < getWidth() - 1 && getFlags(n.x + 1, n.y) == OUT) {
            markFrontier(n.x + 1, n.y, frontiers);
        }
        if (n.y < getHeight() - 1 && getFlags(n.x, n.y + 1) == OUT) {
            markFrontier(n.x, n.y + 1, frontiers);
        }
        if (n.x > 0 && getFlags(n.x - 1, n.y) == OUT) {
            markFrontier(n.x - 1, n.y, frontiers);
        }
    }

    /** Helper method for getFrontiers. */
    private void markFrontier(int x, int y, List<Node> frontiers) {
        Node n = new Node(x, y);
        setFlags(n, FRONTIER);
        frontiers.add(n);
    }

    /** Gets the directions pointing to the visited neighbors of a node. */
    private int getVisitedNeighbors(Node n, Direction[] neighbors) {
        int count = 0;
        if (n.y > 0 && getFlags(n.x, n.y - 1) == IN) {
            neighbors[count++] = Direction.NORTH;
        }
        if (n.x < getWidth() - 1 && getFlags(n.x + 1, n.y) == IN) {
            neighbors[count++] = Direction.EAST;
        }
        if (n.y < getHeight() - 1 && getFlags(n.x, n.y + 1) == IN) {
            neighbors[count++] = Direction.SOUTH;
        }
        if (n.x > 0 && getFlags(n.x - 1, n.y) == IN) {
            neighbors[count++] = Direction.WEST;
        }
        return count;
    }
}
