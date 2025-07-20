SudokuController calls the method "solveSudoku" in SudokuService;
SudokuService calls the method "getSolution" in SudokuSolver to obtain a SolutionOutput

The Postman collection with API calls to test the application can be found at the following link:
https://winter-zodiac-86017.postman.co/workspace/CLZ~0f499c32-aec5-41be-ab6c-c30b8f49520f/collection/9138070-d3fc34a9-2ae7-4afd-98e8-ced0c687d838?action=share&creator=9138070

--------------------------------------------- Sudoku ---------------------------------------------------------------------

Entity representing a cell of a Sudoku grid.
Contains the following fields:

- final List<SudokuCell> cells --> a list of 81 SudokuCell (ordered top to bottom, left to right) composing the Sudoku grid.

--------------------------------------------- SudokuCell ---------------------------------------------------------------------

Entity representing a cell of a Sudoku.
Contains the following fields:

- final int box             --> number (1 based) of the BOX in which the cell is
- final int row             --> number (1 based) of the ROW in which the cell is
- final int col             --> number (1 based) of the COL in which the cell is
- final int index           --> index (0 based) of the cell in the Sudoku
- final String coordinates  --> alphanumeric coordinates of the SudokuCell (eg: A1, I9)
- int value                 --> value of the cell (0 if it hasn't been solved yet)
- List<Integers> candidates --> numbers that can still be placed in that SudokuCell

------------------------------------------ SolutionOutput ---------------------------------------------------------------------
Entity representing the solution of a Sudoku.
Contains the following fields:
- String startingNumbers            --> initial numbers of a Sudoku
- String solutionNumbers            --> solution numbers of a Sudoku
- int solutionTime                  --> solution time (in milliseconds)
- Instant solutionDate              --> Instant of when the Sudoku has been solved
- List<SolutionStep> solutionSteps  --> list of all the SolutionStep faced during the solution
- int initialDigits                 --> number of initial clues (could be deducted from startingNumbers, but it's a field that can be used to run some queries)
- int stepsCount                    --> number of steps required for the solution (could be deducted from solutionSteps, but it's a field that can be used to run some queries)

The method "getSolution" in SudokuSolver builds the SolutionOutput invoking the private method "solve" of SudokuSolver (which produces a list of SolutionStep).

-------------------------------------------- SolutionStep --------------------------------------------------------------------
Entity representing a solving step of a Sudoku given its current grid state (and the candidates in its cells).
Contains the following fields:

- Sudoku sudokuInstance       --> the Sudoku in that given moment
- String sudokuNumbers        --> the string containing the number sequence representing the Sudoku
- List<ChangeLog> changeLogs  --> list of ChangeLogs (see below)
- List<CellChange> changes    --> list of Changes (see further below)

NOTE:
Only sudokuNumbers and changes are shown in the Json and MongoDB;
the other properties are @JsonIgnore @Transient

--------------------------------------------- ChangeLog ---------------------------------------------------------------------

Entity representing a set of Changes to be applied on the Sudoku after a deduction has been made.
Contains the following fields:

- List<Integer> unitExamined            --> list of numbers examined for the deduction (often will be a list containing a single number)
- House house                           --> the House (BOX, ROW or COL) taken in consideration for the deduction. Can be null (eg: with Naked1)
- int houseNumber                       --> the number of the house taken in consideration for the deduction. 0 when house is null, otherwise always between 1 and 9.
- List<ChangeLogUnitMember>unitMembers  --> list of ChangeLogUnitMember examined for the deduction (explained further below)
- String solvingTechnique               --> the solving technique used for the deduction
- String solvingTechniqueVariant        --> the variant of the SolvingTechnique used for the deduction (often null)
- List<CellChange> changes              --> the list of CellChange to be applied to the Sudoku according to the deduction(s) made.

--------------------------------------------- CellChange ---------------------------------------------------------------------

Interface representing a change to be applied on a SudokuCell of a Sudoku.
Implemented by:
1) CellSolved (a cell of which we can determine with absolute certainty it's value)
2) CellSkimmed (a cell from which we can remove with absolute certainty some candidates)
3) CellGuessed (a cell of which we guessed the value with backtracking solving techniques)

Contains the following methods:
- String getSolvingTechnique() --> returns the SolvingTechnique used for the deduction
- House getHouse()             --> returns the House (BOX, ROW or COL) taken in consideration for the deduction (can be null)
- SudokuCell getCell()         --> the SudokuCell affected by that Change
- int getNumber()              --> the number to be set as value in the affected SudokuCell (0 if the CellChange is an instance of CellSkimmed)

------------------------------------ Solution process ----------------------------------------------------------------

After defining the basic therms, here is how the solution process works.
The algorithm starts with the most basic SolvingTechniques (NakedSingle e HiddenSingle).
These two techniques produce CellSolved, so they actually change the value of some SudokuCell.

If NO CellSolved are produced, it uses the remaining basic solving techniques, who produce CellSkimmed (Hidden/Naked Pair, Triple or Quadruple, Pointing/Claiming Candidates).
If NO CellSkimmed are produced, it uses the set of advanced solving techniques.
If NO CellChange are produced, as a last resource it uses the backtracking method (FIFTY-FIFTY).

If these techniques can produce AT LEAST 1 CellChange, a new instance of Sudoku gets created with the updated list of SudokuCell.

the method solve is then applied recursively from the new Sudoku created, until there are no more SudokuCell with value == 0.


