package maze;

import java.io.Serializable;

/** An immutable pair of maze coordinates. */
public final class Position implements Serializable {
    private static final long serialVersionUID = -5356304224760074820L;
    
    public final int x;
    public final int y;
    
    /**
     * Creates a pair of maze coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Creates a pair of coordinates from another.
     * 
     * @param  other the other position
     * @throws NullPointerException if the other position is null
     */
    public Position(Position other) {
        this(other.x, other.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position)obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }
}
