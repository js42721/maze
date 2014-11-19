package maze;

import java.io.Serializable;

import fastrandom.FastRandom;
import fastrandom.Taus88;

/** 
 * Implements the recursive backtracking algorithm. The algorithm works by
 * exploring unvisited nodes using randomized depth-first search.
 */
public class RecursiveBacktracker extends Maze implements Serializable {
    private static final long serialVersionUID = -5689416515127359434L;
    
    private final FastRandom rnd;
    private final Node start;
    
    /**
     * Sets the dimensions. Call {@code generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecursiveBacktracker(int width, int height) {
        super(width, height);
        rnd = new Taus88();
        start = new Node(rnd.nextInt(width), rnd.nextInt(height));
    }
    
    /**
     * Sets the dimensions and the starting position. Call {@code generate} to
     * generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @param  startX the x-coordinate of the algorithm's starting position
     * @param  startY the y-coordinate of the algorithm's starting position
     * @throws IllegalArgumentException if width or height is not positive
     * @throws PositionOutOfBoundsException if (x, y) is out of bounds
     */
    public RecursiveBacktracker(int width, int height, int startX, int startY) {
        super(width, height);
        checkPosition(startX, startY);
        start = new Node(startX, startY);
        rnd = new Taus88();
    }
    
    /**
     * Sets the dimensions and the starting position. Call {@code generate} to
     * generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @param  start the algorithm's starting position
     * @throws IllegalArgumentException if width or height is not positive
     * @throws PositionOutOfBoundsException if start is out of bounds
     * @throws NullPointerException if start is null
     */
    public RecursiveBacktracker(int width, int height, Position start) {
        this(width, height, start.getX(), start.getY());
    }

    /**
     * Sets the algorithm's starting position.
     * 
     * @param  x the x-coordinate of the starting position
     * @param  y the y-coordinate of the starting position
     * @throws PositionOutOfBoundsException if (x, y) is out of bounds
     */
    public void setStart(int x, int y) {
        checkPosition(x, y);
        start.set(x, y);
    }
    
    /**
     * Sets the algorithm's starting position.
     * 
     * @param  start the starting position
     * @throws PositionOutOfBoundsException if start is out of bounds
     * @throws NullPointerException if start is null
     */
    public void setStart(Position start) {
        setStart(start.getX(), start.getY());
    }
    
    /** Returns the algorithm's starting position. */
    public Position getStart() {
        return new Node(start);
    }
    
    @Override
    public void generate() {
        resetFill();
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
            carve(current, d);
            
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
        if (n.y > 0 && isClosed(n.x, n.y - 1)) {
            moves[count++] = Direction.UP;
        }
        if (n.x > 0 && isClosed(n.x - 1, n.y)) {
            moves[count++] = Direction.LEFT;
        }
        if (n.y < getHeight() - 1 && isClosed(n.x, n.y + 1)) {
            moves[count++] = Direction.DOWN;
        }
        if (n.x < getWidth() - 1 && isClosed(n.x + 1, n.y)) {
            moves[count++] = Direction.RIGHT;
        }
        return count;
    }
}
