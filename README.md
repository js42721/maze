Maze Generator
==============

This library contains fast implementations of several classic maze generation
algorithms:

* [Binary tree algorithm](src/maze/BinaryTreeMaze.java)
* [Randomized Prim's algorithm](src/maze/RandomizedPrims.java)
* [Recursive backtracking](src/maze/RecursiveBacktracker.java)
* [Recursive division](src/maze/RecursiveDivider.java)
* [Wilson's algorithm](src/maze/Wilsons.java)

It includes a [tile maze wrapper](src/maze/TileMaze.java) which provides a
tile-centric interface.

A maze generated using recursive backtracking:

![Screenshot](recursive_backtracker.png)

A tile maze generated using Wilson's algorithm:

![Screenshot](wilsons.png)
