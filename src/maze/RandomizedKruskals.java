package maze;

import java.io.Serializable;

import datastructures.DisjointSetForest;
import fastrandom.FastRandom;
import fastrandom.Taus88;

/** 
 * Implements a randomized version of Kruskal's algorithm. It is essentially
 * Kruskal's algorithm with random edge weights so it results in a minimum
 * spanning tree for an equally weighted graph.
 */
public class RandomizedKruskals extends Maze implements Serializable {
    private static final long serialVersionUID = 612546716632291472L;
    
    private final FastRandom rnd;

    /**
     * Sets the dimensions. Call {@code generate} to generate the maze.
     * 
     * @param  width the width of the maze
     * @param  height the height of the maze
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RandomizedKruskals(int width, int height) {
        super(width, height);
        rnd = new Taus88();
    }

    @Override
    public void generate() {
        resetFill();
        randomizedKruskals();
    }

    private void randomizedKruskals() {
        int[] edges = getEdges();
        
        /* Allows us to iterate over the edge list in random order. */
        shuffle(edges);

        /* Creates a disjoint set forest containing a set for each node. */
        DisjointSetForest dsf = new DisjointSetForest(getWidth() * getHeight());

        /* 
         * Processes each edge (in random order) by merging its nodes if they
         * belong to different sets. 
         */        
        for (int e : edges) {
            Direction d = (e < 0) ? Direction.RIGHT : Direction.DOWN;
            int u = e & 0x7fffffff;
            int v = u + d.dy * getWidth() + d.dx;
            if (dsf.union(u, v)) {
                carve(u % getWidth(), u / getWidth(), d);
            }
        }
    }
    
    /** Returns a list of all the edges in the maze.*/ 
    private int[] getEdges() {
        int nodes = getWidth() * getHeight();
        int[] edges = new int[2 * nodes - getWidth() - getHeight()];
        int index = 0;
        
        /* 
         * Adds edges to the edge list. The sign bit is used to distinguish
         * the horizontal edges from the vertical ones.
         */
        for (int y = 0; y < getHeight() - 1; ++y) {
            for (int x = 0; x < getWidth() - 1; ++x) {
                int mazeIndex = y * getWidth() + x;
                edges[index++] = mazeIndex;
                edges[index++] = mazeIndex | 0x80000000;
            }
        }
        
        /* Adds the remaining vertical edges. */
        for (int y = 0; y < getHeight() - 1; ++y) {
            edges[index++] = y * getWidth() + getWidth() - 1;
        }

        /* Adds the remaining horizontal edges. */
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
}
