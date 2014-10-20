package maze;

import java.io.Serializable;

/** An immutable pair of coordinates. */
public final class ImmutablePosition extends Position implements Serializable {
    private static final long serialVersionUID = 3157903951803175800L;
    
    public final int x;
    public final int y;
    
    /**
     * Creates a position with the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public ImmutablePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a position from another position.
     * 
     * @param  other the other position
     * @throws NullPointerException if other is null
     */
    public ImmutablePosition(ImmutablePosition other) {
        this(other.x, other.y);
    }
    
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
