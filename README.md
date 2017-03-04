This program uses the recursive backtracking method of maze generation to generate an n by m maze. The maze is stored in a BitSet to minimize memory usage.
# To construct a maze:
Create a Maze object of size 'int width' by 'int height' as such - Maze maze = new Maze('width', 'height');

Call Maze.generate(). This takes one step towards generation, and returns true if generation is complete.

You can generate the entirety using the following - while (!maze.generate());

To print an ASCII rendition of the maze, just invoke the Maze objects toString();

To save the maze to a file named 'String name'.png, call Maze.draw('name')

# To solve the maze:
Create a MazeSolver object to solve Maze 'maze' as such - MazeSolver ms = new MazeSolver(maze);

Call MazeSolver.solve(). This takes one step towards solving, and returns true if solving is complete.

You can solve the entirety using the following - while (!ms.solve());

To print the path through the maze, create a new maze object using the BitSet from the MazeSolver,

and print that as such - System.out.println(new Maze(ms.getBitSet()));

