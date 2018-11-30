package maze.coordinates;

import java.io.Serializable;

/** Represents a maze node. */
public class Node extends Point implements Serializable {
    private static final long serialVersionUID = -243974042045188243L;

    public int x;
    public int y;

    /** Creates a node with the coordinates (0, 0). */
    public Node() {
    }

    /**
     * Creates a node with the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a node from another node.
     *
     * @param  other the other node
     * @throws NullPointerException if the other node is null
     */
    public Node(Node other) {
        this(other.x, other.y);
    }

    /**
     * Sets the coordinates of this node.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the coordinates of this node to those of another.
     *
     * @param  other the other node
     * @throws NullPointerException if the other node is null
     */
    public void set(Node other) {
        set(other.x, other.y);
    }

    /**
     * Translates this node by dx along the x-axis and dy along the y-axis.
     *
     * @param dx the distance along the x-axis
     * @param dy the distance along the y-axis
     */
    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
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
