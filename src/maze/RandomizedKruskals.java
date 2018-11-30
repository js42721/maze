package maze;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Implements a randomized version of Kruskal's algorithm. It is essentially
 * Kruskal's algorithm with random edge weights so its result is a minimum
 * spanning tree for an equally weighted graph.
 */
public class RandomizedKruskals extends Maze implements Serializable {
    private static final long serialVersionUID = 612546716632291472L;

    private final Random rnd;

    /**
     * Sets the dimensions of the maze.
     *
     * @param width  the width of the maze
     * @param height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RandomizedKruskals(int width, int height) {
        super(width, height);
        rnd = new Random();
    }

    @Override
    public void generate() {
        fill();
        randomizedKruskals();
    }

    private void randomizedKruskals() {
        /* Creates a list of all edges. */
        int[] edges = getEdges();

        /* Randomizes the order of the edge list. */
        shuffle(edges);

        /* Creates a disjoint set forest with a set for each node. */
        DisjointSetForest dsf = new DisjointSetForest(getWidth() * getHeight());

        /* Merges the nodes if they belong to different sets. */
        for (int e : edges) {
            Direction d = (e < 0) ? Direction.EAST : Direction.SOUTH;
            int u = e & 0x7fffffff;
            int v = u + d.dy * getWidth() + d.dx;
            if (dsf.union(u, v)) {
                removeWall(u % getWidth(), u / getWidth(), d);
            }
        }
    }

    /**
     * Returns a list of all the edges in the maze. The sign bit is used to
     * distinguish the horizontal edges from the vertical ones.
     */
    private int[] getEdges() {
        int nodes = getWidth() * getHeight();
        int[] edges = new int[2 * nodes - getWidth() - getHeight()];
        int index = 0;

        for (int y = 0; y < getHeight() - 1; ++y) {
            for (int x = 0; x < getWidth() - 1; ++x) {
                int mazeIndex = y * getWidth() + x;
                edges[index++] = mazeIndex;
                edges[index++] = mazeIndex | 0x80000000;
            }
        }

        for (int y = 1; y < getHeight(); ++y) {
            edges[index++] = y * getWidth() - 1;
        }

        for (int x = nodes - getWidth(); x < nodes - 1; ++x) {
            edges[index++] = x | 0x80000000;
        }

        return edges;
    }

    /** Randomly permutes the elements in an array. */
    private void shuffle(int[] array) {
        for (int i = array.length - 1; i >= 1; --i) {
            int j = rnd.nextInt(i + 1);
            int tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
    }

    /** Allows for efficient union/find operations. */
    private static class DisjointSetForest {
        int[] a;

        DisjointSetForest(int n) {
            a = new int[n];
            Arrays.fill(a, -1);
        }

        int find(int x) {
            int root = x;
            int current = a[x];
            while (current >= 0) {
                root = current;
                current = a[current];
            }
            current = x;
            while (current != root) {
                int old = current;
                current = a[current];
                a[old] = root;
            }
            return root;
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return false;
            }
            int rankX = a[rootX];
            int rankY = a[rootY];
            if (rankX > rankY) {
                a[rootX] = rootY;
            } else {
                if (rankX == rankY) {
                    --a[rootX];
                }
                a[rootY] = rootX;
            }
            return true;
        }
    }
}
