SudoKing is an application that not only can solve most of Sudoku grids,
but for every Sudoku solved is able to generate a step by step explanation of the solution process,
telling which solving technique it's been used for that specific step and generating a written explanation.
The results are then stored into a MongoDB collection, and so are the errors of those that couldn't be solved.
To test it, i used a txt file containing 49.151 Sudoku (each line representing a different grid).
They are all MINIMAL SUDOKUS
(grids with only 17 starting digits: according to a study,
17 is the minimum number of digits needed to be solvable AND have only a possible solution).
So far the program can solve 45.231 of them in about 10 minutes (11 milliseconds per grid)
It's still a work in progress (many techniques are hard to implement and test),
but so far it can solve most of them (only 3920 that should be done once the new techniques are implemented).
Here is an example of a SolutionOutput.

---------- STEP 1 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 |     4 | 5 2 3 |
B | 9 5 3 |     2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1   |     7 | 2 4 6 |
E | 7 6   |       | 3   5 |
F |   2   |       | 7   1 |
  +-------+-------+-------+
G |   9 6 |     1 |   3 2 |
H | 2 3   |       |   5 7 |
I |   7   |       |   6 9 |
  +-------+-------+-------+
# NAKED PAIR
IN COL 6 THE ONLY CELLS WITH THE PAIR OF CANDIDATES [8, 9] ARE THE CELLS:
E6, [8, 9]
H6, [8, 9]
SO I CAN REMOVE [8, 9] FROM ALL THE OTHER CELLS OF COL 6:
I6 --> CANDIDATES REMAINING: [3, 5]; CANDIDATES REMOVED: [8]

# NAKED PAIR
IN BOX 5 THE ONLY CELLS WITH THE PAIR OF CANDIDATES [8, 9] ARE THE CELLS:
D5, [8, 9]
E6, [8, 9]
SO I CAN REMOVE [8, 9] FROM ALL THE OTHER CELLS OF BOX 5:
D4 --> CANDIDATES REMAINING: [5]; CANDIDATES REMOVED: [8, 9]
E4 --> CANDIDATES REMAINING: [1, 2]; CANDIDATES REMOVED: [8, 9]
E5 --> CANDIDATES REMAINING: [1, 2]; CANDIDATES REMOVED: [8, 9]
F4 --> CANDIDATES REMAINING: [4, 6]; CANDIDATES REMOVED: [8, 9]
F5 --> CANDIDATES REMAINING: [4, 6]; CANDIDATES REMOVED: [8, 9]
F6 --> CANDIDATES REMAINING: [3, 5]; CANDIDATES REMOVED: [8, 9]

# NAKED PAIR
IN ROW E THE ONLY CELLS WITH THE PAIR OF CANDIDATES [8, 9] ARE THE CELLS:
E6, [8, 9]
E8, [8, 9]
SO I CAN REMOVE [8, 9] FROM ALL THE OTHER CELLS OF ROW E:
E3 --> CANDIDATES REMAINING: [4]; CANDIDATES REMOVED: [8, 9]

# HIDDEN PAIR
IN BOX 5 THE PAIR OF CANDIDATES [1, 2] APPEARS ONLY IN THE CELLS:
E4, [1, 2]
E5, [1, 2]
SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:
E4 --> CANDIDATES REMAINING: [1, 2]; CANDIDATES REMOVED: [4]
E5 --> CANDIDATES REMAINING: [1, 2]; CANDIDATES REMOVED: [4]

# HIDDEN PAIR
IN ROW D THE PAIR OF CANDIDATES [8, 9] APPEARS ONLY IN THE CELLS:
D3, [8, 9]
D5, [8, 9]
SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:
D3 --> CANDIDATES REMAINING: [8, 9]; CANDIDATES REMOVED: [5]

# HIDDEN TRIPLE
IN BOX 4 THE TRIO OF CANDIDATES [5, 8, 9] APPEARS ONLY IN THE CELLS:
D3, [8, 9]
F1, [5, 8]
F3, [5, 8, 9]
SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:
F1 --> CANDIDATES REMAINING: [5, 8]; CANDIDATES REMOVED: [4]
F3 --> CANDIDATES REMAINING: [5, 8, 9]; CANDIDATES REMOVED: [4]

# HIDDEN QUAD
IN BOX 5 THE QUADRUPLE OF CANDIDATES [1, 2, 4, 6] APPEARS ONLY IN THE CELLS:
E4, [1, 2]
E5, [1, 2]
F4, [4, 6]
F5, [4, 6]
SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:
F4 --> CANDIDATES REMAINING: [4, 6]; CANDIDATES REMOVED: [5]
F5 --> CANDIDATES REMAINING: [4, 6]; CANDIDATES REMOVED: [3]

# HIDDEN QUAD
IN COL 3 THE QUADRUPLE OF CANDIDATES [1, 5, 8, 9] APPEARS ONLY IN THE CELLS:
D3, [8, 9]
F3, [5, 8, 9]
H3, [1, 8]
I3, [1, 5, 8]
SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:
H3 --> CANDIDATES REMAINING: [1, 8]; CANDIDATES REMOVED: [4]
I3 --> CANDIDATES REMAINING: [1, 5, 8]; CANDIDATES REMOVED: [4]

# HIDDEN SINGLE
CELL I5 IN COL 5 IS THE ONLY CELL WITH THE CANDIDATE 3

# NAKED SINGLE
IN CELL D4 NUMBER 5 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL E3 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL F6 IN BOX 5 IS THE ONLY CELL WITH THE CANDIDATE 3

---------- STEP 2 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 |     4 | 5 2 3 |
B | 9 5 3 |     2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1   | 5   7 | 2 4 6 |
E | 7 6 4 |       | 3   5 |
F |   2   |     3 | 7   1 |
  +-------+-------+-------+
G |   9 6 |     1 |   3 2 |
H | 2 3   |       |   5 7 |
I |   7   |   3   |   6 9 |
  +-------+-------+-------+
# HIDDEN SINGLE
CELL I4 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 2

# HIDDEN SINGLE
CELL I6 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL G1 IN ROW G IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL E5 IN COL 5 IS THE ONLY CELL WITH THE CANDIDATE 2

---------- STEP 3 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 |     4 | 5 2 3 |
B | 9 5 3 |     2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1   | 5   7 | 2 4 6 |
E | 7 6 4 |   2   | 3   5 |
F |   2   |     3 | 7   1 |
  +-------+-------+-------+
G | 5 9 6 |     1 |   3 2 |
H | 2 3   |       |   5 7 |
I |   7   | 2 3 5 |   6 9 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL F1 NUMBER 8 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL F3 IN BOX 4 IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL E4 IN BOX 5 IS THE ONLY CELL WITH THE CANDIDATE 1

# HIDDEN SINGLE
CELL I1 IN BOX 7 IS THE ONLY CELL WITH THE CANDIDATE 4

# HIDDEN SINGLE
CELL A5 IN COL 5 IS THE ONLY CELL WITH THE CANDIDATE 1

---------- STEP 4 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 |   1 4 | 5 2 3 |
B | 9 5 3 |     2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1   | 5   7 | 2 4 6 |
E | 7 6 4 | 1 2   | 3   5 |
F | 8 2 5 |     3 | 7   1 |
  +-------+-------+-------+
G | 5 9 6 |     1 |   3 2 |
H | 2 3   |       |   5 7 |
I | 4 7   | 2 3 5 |   6 9 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL A4 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL D3 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL F8 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL E8 IN BOX 6 IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL D5 IN ROW D IS THE ONLY CELL WITH THE CANDIDATE 8

---------- STEP 5 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 | 9 1 4 | 5 2 3 |
B | 9 5 3 |     2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1 9 | 5 8 7 | 2 4 6 |
E | 7 6 4 | 1 2   | 3 8 5 |
F | 8 2 5 |     3 | 7 9 1 |
  +-------+-------+-------+
G | 5 9 6 |     1 |   3 2 |
H | 2 3   |       |   5 7 |
I | 4 7   | 2 3 5 |   6 9 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL B5 NUMBER 7 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL E6 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL B4 IN BOX 2 IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL H5 IN COL 5 IS THE ONLY CELL WITH THE CANDIDATE 9

# HIDDEN SINGLE
CELL H6 IN COL 6 IS THE ONLY CELL WITH THE CANDIDATE 8

---------- STEP 6 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 | 9 1 4 | 5 2 3 |
B | 9 5 3 | 8 7 2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1 9 | 5 8 7 | 2 4 6 |
E | 7 6 4 | 1 2 9 | 3 8 5 |
F | 8 2 5 |     3 | 7 9 1 |
  +-------+-------+-------+
G | 5 9 6 |     1 |   3 2 |
H | 2 3   |   9 8 |   5 7 |
I | 4 7   | 2 3 5 |   6 9 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL G5 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL H3 NUMBER 1 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL I3 IN BOX 7 IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL H4 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 6

# HIDDEN SINGLE
CELL G4 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 7

# HIDDEN SINGLE
CELL G7 IN ROW G IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL F5 IN COL 5 IS THE ONLY CELL WITH THE CANDIDATE 6

---------- STEP 7 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 | 9 1 4 | 5 2 3 |
B | 9 5 3 | 8 7 2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1 9 | 5 8 7 | 2 4 6 |
E | 7 6 4 | 1 2 9 | 3 8 5 |
F | 8 2 5 |   6 3 | 7 9 1 |
  +-------+-------+-------+
G | 5 9 6 | 7 4 1 | 8 3 2 |
H | 2 3 1 | 6 9 8 |   5 7 |
I | 4 7 8 | 2 3 5 |   6 9 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL F4 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL H7 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL I7 NUMBER 1 IS THE ONLY CANDIDATE LEFT

---------- STEP 8 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 6 8 7 | 9 1 4 | 5 2 3 |
B | 9 5 3 | 8 7 2 | 6 1 4 |
C | 1 4 2 | 3 5 6 | 9 7 8 |
  +-------+-------+-------+
D | 3 1 9 | 5 8 7 | 2 4 6 |
E | 7 6 4 | 1 2 9 | 3 8 5 |
F | 8 2 5 | 4 6 3 | 7 9 1 |
  +-------+-------+-------+
G | 5 9 6 | 7 4 1 | 8 3 2 |
H | 2 3 1 | 6 9 8 | 4 5 7 |
I | 4 7 8 | 2 3 5 | 1 6 9 |
  +-------+-------+-------+
