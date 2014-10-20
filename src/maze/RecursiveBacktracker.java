package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * Implements the recursive backtracking algorithm. The algorithm works by
 * exploring unvisited nodes using randomized depth-first search.
 */
public class RecursiveBacktracker extends Maze implements Serializable {
    private static final long serialVersionUID = -5608567941754857805L;
    
    private Position start;
    
    /**
     * Sets the dimensions. Call {@link #generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecursiveBacktracker(int width, int height) {
        this(width, height, randomPosition(width, height));
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
        this(width, height, new Position(startX, startY));
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
        super(width, height);
        setStart(start);
    }
    
    /**
     * Sets the algorithm's starting position.
     * 
     * @throws PositionOutOfBoundsException if start is out of bounds
     * @throws NullPointerException if start is null
     */
    public void setStart(Position start) {
        checkPosition(start);
        this.start = start;
    }
    
    /** Returns the algorithm's starting position. */
    public Position getStart() {
        return start;
    }
    
    @Override
    public void generate() {
        fill();
        recursiveBacktrack(start);
    }
    
    private void recursiveBacktrack(Position start) {
        Direction[] directions = Direction.values();
        List<Direction> moves = new ArrayList<Direction>(4);
        int unvisited = getWidth() * getHeight() - 1;
        
        int x = start.x;
        int y = start.y;
        
        while (unvisited > 0) {
            /* Finds adjacent unvisited nodes. */
            getMoves(x, y, moves);
            
            /* Takes a step back if there are no such nodes. */
            if (moves.isEmpty()) {
                /* Moves in the reverse of the saved direction. */
                Direction rev = directions[getFlags(x, y)].getReverse();
                x += rev.dx;
                y += rev.dy;
                continue;
            }
            
            /* Picks an adjacent unvisited node randomly and adds it to the maze. */
            Direction d = moves.get(random(moves.size()));
            carve(x, y, d);
            
            /* Updates the current node to the newly added node. */
            x += d.dx;
            y += d.dy;
            
            /* Saves the direction taken to reach the current node. */
            setFlags(x, y, (byte)d.ordinal());
            
            --unvisited;
        }
    }

    /** Gets the directions which point to adjacent unvisited nodes. */
    private void getMoves(int x, int y, List<Direction> moves) {
        moves.clear();
        if (y > 0 && isClosed(x, y - 1)) {
            moves.add(Direction.UP);
        }
        if (x > 0 && isClosed(x - 1, y)) {
            moves.add(Direction.LEFT);
        }
        if (y < getHeight() - 1 && isClosed(x, y + 1)) {
            moves.add(Direction.DOWN);
        }
        if (x < getWidth() - 1 && isClosed(x + 1, y)) {
            moves.add(Direction.RIGHT);
        }
    }
}
