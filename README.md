Maze
====

This Java library contains implementations of several maze generation algorithms:

* [Binary tree algorithm](src/maze/BinaryTreeMaze.java)
* [Eller's algorithm](src/maze/Ellers.java)
* [Randomized Kruskal's algorithm](src/maze/RandomizedKruskals.java)
* [Randomized Prim's algorithm](src/maze/RandomizedPrims.java)
* [Recursive backtracking](src/maze/RecursiveBacktracker.java)
* [Recursive division](src/maze/RecursiveDivider.java)
* [Sidewinder](src/maze/Sidewinder.java)
* [Wilson's algorithm](src/maze/Wilsons.java)

It also includes a [tile maze wrapper](src/maze/TileMaze.java) which provides a tile-centric interface.

A maze generated using recursive backtracking:

![Screenshot](recursive_backtracker.png)

A tile maze generated using Wilson's algorithm:

![Screenshot](wilsons.png)
