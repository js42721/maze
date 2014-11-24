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
        int width = getWidth();
        int height = getHeight();
        int nodes = width * height;
        
        int[] edgeList = new int[2 * nodes - width - height];
        int edgeIndex = 0;
        
        /* 
         * Adds edges to the edge list. The sign bit is used to distinguish
         * the horizontal edges from the vertical ones.
         */
        for (int y = 0; y < height - 1; ++y) {
            for (int x = 0; x < width - 1; ++x) {
                int mazeIndex = y * width + x;
                edgeList[edgeIndex++] = mazeIndex;
                edgeList[edgeIndex++] = mazeIndex | 0x80000000;
            }
        }
        
        /* Adds the remaining vertical edges. */
        for (int y = 0; y < height - 1; ++y) {
            edgeList[edgeIndex++] = y * width + width - 1;
        }

        /* Adds the remaining horizontal edges. */
        for (int x = nodes - width; x < nodes - 1; ++x) {
            edgeList[edgeIndex++] = x | 0x80000000;
        }

        /* Allows us to iterate over the edge list in random order. */
        shuffle(edgeList);

        /* Creates a disjoint set forest containing a set for each node. */
        DisjointSetForest dsf = new DisjointSetForest(nodes);

        /* 
         * Processes each edge (in random order) by merging its nodes if they
         * belong to different sets. 
         */        
        for (int e : edgeList) {
            Direction d = (e < 0) ? Direction.RIGHT : Direction.DOWN;
            int u = e & 0x7fffffff;
            int v = u + d.dy * width + d.dx;
            if (dsf.union(u, v)) {
                carve(u % width, u / width, d);
            }
        }
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
