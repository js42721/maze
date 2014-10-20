package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * Implements the recursive backtracking algorithm. The algorithm works by
 * exploring unvisited nodes using randomized depth-first search.
 */
public class RecursiveBacktracker extends Maze implements Serializable {
    private static final long serialVersionUID = 2155816886742145694L;
    
    private Node start;
    
    /**
     * Sets the dimensions. Call {@link #generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecursiveBacktracker(int width, int height) {
        super(width, height);
        start = new Node(random(width), random(height));
    }
    
    /**
     * Sets the dimensions and the starting position. Call {@link #generate}
     * to generate the maze.
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
    }
    
    /**
     * Sets the dimensions and the starting position. Call {@link #generate}
     * to generate the maze.
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
     * @throws PositionOutOfBoundsException if start is out of bounds
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
        List<Direction> moves = new ArrayList<Direction>(4);
        int unvisited = getWidth() * getHeight() - 1;
        
        Node current = new Node(start);
        
        while (unvisited > 0) {
            /* Finds adjacent unvisited nodes. */
            getMoves(current, moves);
            
            /* Takes a step back if there are no such nodes. */
            if (moves.isEmpty()) {
                /* Moves in the reverse of the saved direction. */
                Direction rev = directions[getFlags(current)].getReverse();
                current.translate(rev.dx, rev.dy);
                continue;
            }
            
            /* Picks a random adjacent unvisited node and adds it to the maze. */
            Direction d = moves.get(random(moves.size()));
            carve(current, d);
            
            /* Updates the current node to the newly added node. */
            current.translate(d.dx, d.dy);
            
            /* Saves the direction taken to reach the current node. */
            setFlags(current, (byte)d.ordinal());
            
            --unvisited;
        }
    }

    /** Gets the directions which point to adjacent unvisited nodes. */
    private void getMoves(Node n, List<Direction> moves) {
        moves.clear();
        if (n.y > 0 && isClosed(n.x, n.y - 1)) {
            moves.add(Direction.UP);
        }
        if (n.x > 0 && isClosed(n.x - 1, n.y)) {
            moves.add(Direction.LEFT);
        }
        if (n.y < getHeight() - 1 && isClosed(n.x, n.y + 1)) {
            moves.add(Direction.DOWN);
        }
        if (n.x < getWidth() - 1 && isClosed(n.x + 1, n.y)) {
            moves.add(Direction.RIGHT);
        }
    }
}
