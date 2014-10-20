package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements Wilson's algorithm. The algorithm creates a uniform spanning
 * tree (a spanning tree randomly selected from all possible spanning trees)
 * by performing loop-erased random walks.
 */
public class Wilsons extends Maze implements Serializable {
    private static final long serialVersionUID = -3034136817254278367L;
    
    private static final byte IN = 0b0100;
    
    /**
     * Sets the dimensions. Call {@link #generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Wilsons(int width, int height) {
        super(width, height);
    }
    
    @Override
    public void generate() {
        fill();
        resetFlags();
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
        List<Direction> moves = new ArrayList<Direction>(4);
        int i = getWidth() * getHeight() - 2;
        
        /* Marks the first visited node. */
        setFlags(getWidth() - 1, getHeight() - 1, IN);

        while (i >= 0) {
            int wx, wy, tx, ty;
            ty = wy = i / getWidth();
            tx = wx = i - wy * getWidth();
            
            /* Walks randomly until a visited node is found. */
            while ((getFlags(wx, wy) & IN) == 0) {
                getMoves(wx, wy, moves);
                Direction d = moves.get(random(moves.size()));

                /* Saves the node's exit direction. */
                setFlags(wx, wy, (byte)d.ordinal());

                wx += d.dx;
                wy += d.dy;
            }
            
            /* Traces the path of the walk but avoids any loops. */
            while ((getFlags(tx, ty) & IN) == 0) {
                /* Follows the saved exit direction. */
                Direction d = directions[getFlags(tx, ty)];
                carve(tx, ty, d);
                
                /* Marks the node as visited. */
                setFlags(tx, ty, IN);

                tx += d.dx;
                ty += d.dy;
            }

            /* Finds the next unvisited node. */
            while (i >= 0) {
                int y = i / getWidth();
                int x = i - y * getWidth();
                if ((getFlags(x, y) & IN) == 0) {
                    break;
                }
                --i;
            }
        }
    }
    
    /** Gets the moves that can be made from a position. */
    private void getMoves(int x, int y, List<Direction> moves) {
        moves.clear();
        if (y > 0) {
            moves.add(Direction.UP);
        }
        if (x > 0) {
            moves.add(Direction.LEFT);
        }
        if (y < getHeight() - 1) {
            moves.add(Direction.DOWN);
        }
        if (x < getWidth() - 1) {
            moves.add(Direction.RIGHT);
        }
    }
}
