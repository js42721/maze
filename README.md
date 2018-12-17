Maze
====

A small Java library for generating 2D orthogonal "perfect" mazes. It contains implementations of the following maze generation algorithms:

* [Binary tree algorithm](src/maze/BinaryTreeMaze.java)
* [Eller's algorithm](src/maze/Ellers.java)
* [Randomized Kruskal's algorithm](src/maze/RandomizedKruskals.java)
* [Randomized Prim's algorithm](src/maze/RandomizedPrims.java)
* [Recursive backtracking](src/maze/RecursiveBacktracker.java)
* [Recursive division](src/maze/RecursiveDivider.java)
* [Sidewinder](src/maze/Sidewinder.java)
* [Wilson's algorithm](src/maze/Wilsons.java)

It also includes a [wrapper class](src/maze/TileMaze.java) that provides a tile-centric interface, in case you would rather deal with a maze as a grid of tiles than a collection of lines.

A maze generated using recursive backtracking:

![Screenshot](recursive_backtracker.png)

A tile maze generated using Wilson's algorithm:

![Screenshot](wilsons.png)
