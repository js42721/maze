package maze;

import java.io.Serializable;

import fastrandom.FastRandom;
import fastrandom.LFib4;

/**
 * Implements Wilson's algorithm. The algorithm creates a uniform spanning
 * tree (a spanning tree randomly selected from all possible spanning trees)
 * by performing loop-erased random walks.
 */
public class Wilsons extends Maze implements Serializable {
    private static final long serialVersionUID = 5105826907978290069L;

    /* The first two bits are reserved for directions. */
    private static final int IN = 1 << 2;
    
    private final FastRandom rnd;
    
    /**
     * Sets the dimensions. Call {@code generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Wilsons(int width, int height) {
        super(width, height);
        rnd = new LFib4();
    }
    
    @Override
    public void generate() {
        resetFill();
        wilson();
    }
    
    /*
              WWW       WWW
              WWWW     WWWW
        WWW   WWWWW    WWWW    
        WWWW  WWWWW   WWWWW   WWW
        WWWWW WWWWWW WWWWWW  WWWW
         WWWWWWWWWWWWWWWWWWWWWWWW
         WWWWWWWWWWWWWWWWWWWWWWWW
         WWWWWWWWWWWWWWWWWWWWWWW
         WWWWWWWWWWWWWWWWWWWWWWW
        WWWWWW  WWWWWWWWW   WWWWW
        WWWW      WWWWW      WWWWW
       WWWWW  WW  WWWWW  WW  WWWWW
       WWWWWW    WWWWWWW    WWWWWWW
       WWWWWWWWWWWWWWWWWWWWWWWWWWWW
       WWWWWWWWWWWWW  WWWWWWWWWWWWW
       WWWWWWWWWWWWWWWWWWWWWWWWWWW
        WWWWWW              WWWWWW
        WWWWWWWW          WWWWWWW
         WWWWWWWWWWWWWWWWWWWWWWW
           WWWWWWWWWWWWWWWWWWWW
               WWWWWWWWWWW
     */
    private void wilson() {
        Direction[] directions = Direction.values();
        Direction[] moves = new Direction[4];
        Node walk = new Node();
        Node trace = new Node();
        int i = getWidth() * getHeight() - 2;
        
        /* Marks a node as visited. */
        setFlags(getWidth() - 1, getHeight() - 1, IN);
        
        /* Picks an unvisited node. */
        Node current = new Node(i % getWidth(), i / getWidth());

        while (i >= 0) {
            walk.set(current);
            
            /* Walks randomly until a visited node is found. */
            while (getFlags(walk) != IN) {
                int moveCount = getMoves(walk, moves);
                Direction d = moves[rnd.nextInt(moveCount)];

                /* Saves the node's exit direction. */
                setFlags(walk, d.ordinal());

                walk.translate(d.dx, d.dy);
            }
            
            trace.set(current);
            int flags = getFlags(trace);
            
            /* Traces the path of the walk but avoids any loops. */
            while (flags != IN) {
                /* Carves along the saved exit direction. */
                Direction d = directions[flags];
                carve(trace, d);
                
                /* Marks the node as visited. */
                setFlags(trace, IN);

                trace.translate(d.dx, d.dy);
                flags = getFlags(trace);
            }

            /* Finds the next unvisited node. */
            while (i >= 0) {
                current.set(i % getWidth(), i / getWidth());
                if (getFlags(current) != IN) {
                    break;
                }
                --i;
            }
        }
    }
    
    /** Gets the moves that can be made from a node. */
    private int getMoves(Node n, Direction[] moves) {
        int count = 0;
        if (n.y > 0) {
            moves[count++] = Direction.UP;
        }
        if (n.x > 0) {
            moves[count++] = Direction.LEFT;
        }
        if (n.y < getHeight() - 1) {
            moves[count++] = Direction.DOWN;
        }
        if (n.x < getWidth() - 1) {
            moves[count++] = Direction.RIGHT;
        }
        return count;
    }
}
