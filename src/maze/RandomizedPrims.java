package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final long serialVersionUID = -4566819517395897518L;
    
    private static final byte OUT      = 0b0000;
    private static final byte IN       = 0b0001;
    private static final byte FRONTIER = 0b0010;
    
    private Position start;
    
    /**
     * Sets the dimensions. Call {@link #generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RandomizedPrims(int width, int height) {
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
    public RandomizedPrims(int width, int height, int startX, int startY) {
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
    public RandomizedPrims(int width, int height, Position start) {
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
        resetFlags();
        randomizedPrims(start);
    }
    
    private void randomizedPrims(Position start) {
        /* Frontiers are the unvisited nodes adjacent to the visited ones. */
        List<Position> frontiers = new ArrayList<Position>();
        List<Direction> neighbors = new ArrayList<Direction>(4);
        
        /* Marks the starting node as visited and gets its frontiers. */
        setFlags(start, IN);
        getFrontiers(start, frontiers);
        
        while (!frontiers.isEmpty()) {
            /* Picks a random frontier. */
            int random = random(frontiers.size());
            int last = frontiers.size() - 1;
            Collections.swap(frontiers, random, last); // For O(1) removal.
            Position current = frontiers.remove(last);
            
            /* Picks randomly from the visited neighbors of the frontier. */
            getVisitedNeighbors(current, neighbors);
            Direction d = neighbors.get(random(neighbors.size()));
            
            /* 
             * Removes the wall between the frontier and the selected neighbor
             * and then marks the frontier as visited.
             */
            carve(current, d);
            setFlags(current, IN);
            
            /* Looks for new frontiers. */
            getFrontiers(current, frontiers);
        }
    }
    
    /**
     * Finds the unvisited neighbors of a node and adds them to the frontier
     * list if they were not already included.
     */
    private void getFrontiers(Position p, List<Position> frontiers) {
        if (p.y > 0 && getFlags(p.x, p.y - 1) == OUT) {
            markFrontier(p.x, p.y - 1, frontiers);
        }
        if (p.x > 0 && getFlags(p.x - 1, p.y) == OUT) {
            markFrontier(p.x - 1, p.y, frontiers);
        }
        if (p.y < getHeight() - 1 && getFlags(p.x, p.y + 1) == OUT) {
            markFrontier(p.x, p.y + 1, frontiers);
        }
        if (p.x < getWidth() - 1 && getFlags(p.x + 1, p.y) == OUT) {
            markFrontier(p.x + 1, p.y, frontiers);
        }
    }
    
    /** Helper method for getFrontiers. */
    private void markFrontier(int x, int y, List<Position> frontiers) {
        Position p = new Position(x, y);
        setFlags(p, FRONTIER);
        frontiers.add(p);
    }
    
    /** Gets the directions pointing to the visited neighbors of a node. */
    private void getVisitedNeighbors(Position p, List<Direction> neighbors) {
        neighbors.clear();
        if (p.y > 0 && (getFlags(p.x, p.y - 1) & IN) != 0) {
            neighbors.add(Direction.UP);
        }
        if (p.x > 0 && (getFlags(p.x - 1, p.y) & IN) != 0) {
            neighbors.add(Direction.LEFT);
        }
        if (p.y < getHeight() - 1 && (getFlags(p.x, p.y + 1) & IN) != 0) {
            neighbors.add(Direction.DOWN);
        }
        if (p.x < getWidth() - 1 && (getFlags(p.x + 1, p.y) & IN) != 0) {
            neighbors.add(Direction.RIGHT);
        }
    }
}
