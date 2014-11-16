package maze;

/** A pair of coordinates. */
public abstract class Position {
    /** Returns the x-coordinate. */
    public abstract int getX();

    /** Returns the y-coordinate. */
    public abstract int getY();
    
    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + getX();
        result = result * 31 + getY();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position p = (Position)obj;
            return getX() == p.getX() && getY() == p.getY();
        }
        return false;
    }
}
