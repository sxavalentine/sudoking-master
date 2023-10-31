Configuration MongoDB:

1) Download MongoDB Shell
2) Create database named sudoking. Use the default port 27017
3) You don't need to create collections (tables)... If not present, it will create them by itself

- The collection "solutions" is where the solved sudoku get stored. The objects stored are the SolutionOutput of the sudoku solved
- The collection "sudoku_error" is where the sudokus that produced an exception during solution get stored.
  You will find the error message and the sudoku numbers string.

Some queries that could be useful:

{solutionTime: {$gt: 100}}                               --> find all sudokus with solutionTime > 100ms
{"solutionSteps.changes.solvingTechnique": "NAKED QUAD"} --> find all sudokus with at least one SolutionStep with solving technique NAKED QUAD
