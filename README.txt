SudoKing is an application that can solve practically every Sudoku grids
(tho its techniques pool are still limited, hardest sudokus can only be solved with backtracking).
On top of that for every Sudoku solved is able to generate a step by step explanation of the solution process,
telling which solving technique it's been used for that specific step and generating a written explanation.
The results are then stored into a MongoDB collection, and so are the errors of those that couldn't be solved.
To test it, i used a txt file containing 49.151 Sudoku (each line representing a different grid).
They are all MINIMAL SUDOKUS
(grids with only 17 starting digits: according to a study,
17 is the minimum number of digits needed to be solvable AND have only a possible solution).
So far the program can solve all of them in about 16 minutes (3624 with backtracking).
Here is an example of a SolutionOutput, from a super easy sudoku
(stringNumbers = 074120000206400070198700000951840000300971008000065291000007854020004607000083910)

---------- STEP 1 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 |       |       |
B |     2 |   3   |     4 |
C |       | 5     | 6   7 |
  +-------+-------+-------+
D | 5     | 1 4   |       |
E |   7   |       |   2   |
F |       |   7 8 |     9 |
  +-------+-------+-------+
G | 8   7 |     9 |       |
H | 4     |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# HIDDEN SINGLE
CELL H3 IN COL 3 IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL F8 IN ROW F IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL D7 IN COL 7 IS THE ONLY CELL WITH THE CANDIDATE 7

---------- STEP 2 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 |       |       |
B |     2 |   3   |     4 |
C |       | 5     | 6   7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7     |
E |   7   |       |   2   |
F |       |   7 8 |   5 9 |
  +-------+-------+-------+
G | 8   7 |     9 |       |
H | 4   5 |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# HIDDEN SINGLE
CELL G5 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL B2 IN ROW B IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL E6 IN COL 6 IS THE ONLY CELL WITH THE CANDIDATE 5

# HIDDEN SINGLE
CELL A9 IN BOX 3 IS THE ONLY CELL WITH THE CANDIDATE 5

---------- STEP 3 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 |       |     5 |
B |   5 2 |   3   |     4 |
C |       | 5     | 6   7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7     |
E |   7   |     5 |   2   |
F |       |   7 8 |   5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 |       |
H | 4   5 |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL E5 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL A7 IN BOX 3 IS THE ONLY CELL WITH THE CANDIDATE 2

---------- STEP 4 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 |       | 2   5 |
B |   5 2 |   3   |     4 |
C |       | 5     | 6   7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7     |
E |   7   |   9 5 |   2   |
F |       |   7 8 |   5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 |       |
H | 4   5 |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL A5 NUMBER 8 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL B7 IN COL 7 IS THE ONLY CELL WITH THE CANDIDATE 9

---------- STEP 5 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 |   8   | 2   5 |
B |   5 2 |   3   | 9   4 |
C |       | 5     | 6   7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7     |
E |   7   |   9 5 |   2   |
F |       |   7 8 |   5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 |       |
H | 4   5 |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL A8 NUMBER 3 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL A4 IN BOX 2 IS THE ONLY CELL WITH THE CANDIDATE 9

# HIDDEN SINGLE
CELL E7 IN COL 7 IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL B8 IN ROW B IS THE ONLY CELL WITH THE CANDIDATE 8

---------- STEP 6 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 | 9 8   | 2 3 5 |
B |   5 2 |   3   | 9 8 4 |
C |       | 5     | 6   7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7     |
E |   7   |   9 5 | 8 2   |
F |       |   7 8 |   5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 |       |
H | 4   5 |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL C8 NUMBER 1 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL D8 NUMBER 6 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL E3 IN ROW E IS THE ONLY CELL WITH THE CANDIDATE 4

# HIDDEN SINGLE
CELL F7 IN BOX 6 IS THE ONLY CELL WITH THE CANDIDATE 4

# HIDDEN SINGLE
CELL B6 IN ROW B IS THE ONLY CELL WITH THE CANDIDATE 1

---------- STEP 7 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 | 9 8   | 2 3 5 |
B |   5 2 |   3 1 | 9 8 4 |
C |       | 5     | 6 1 7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7 6   |
E |   7 4 |   9 5 | 8 2   |
F |       |   7 8 | 4 5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 |       |
H | 4   5 |   6   | 3     |
I |       |       | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL C5 NUMBER 2 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL D9 NUMBER 3 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL G7 NUMBER 1 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL G8 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL I5 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 1

# HIDDEN SINGLE
CELL A6 IN COL 6 IS THE ONLY CELL WITH THE CANDIDATE 6

# HIDDEN SINGLE
CELL E9 IN BOX 6 IS THE ONLY CELL WITH THE CANDIDATE 1

---------- STEP 8 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A |     1 | 9 8 6 | 2 3 5 |
B |   5 2 |   3 1 | 9 8 4 |
C |       | 5 2   | 6 1 7 |
  +-------+-------+-------+
D | 5     | 1 4   | 7 6 3 |
E |   7 4 |   9 5 | 8 2 1 |
F |       |   7 8 | 4 5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 | 1 4   |
H | 4   5 |   6   | 3     |
I |       |   1   | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL A1 NUMBER 7 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL A2 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL B4 NUMBER 7 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL C6 NUMBER 4 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL D6 NUMBER 2 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL H2 IN BOX 7 IS THE ONLY CELL WITH THE CANDIDATE 1

# HIDDEN SINGLE
CELL F1 IN COL 1 IS THE ONLY CELL WITH THE CANDIDATE 1

# HIDDEN SINGLE
CELL I4 IN COL 4 IS THE ONLY CELL WITH THE CANDIDATE 4

# HIDDEN SINGLE
CELL I6 IN COL 6 IS THE ONLY CELL WITH THE CANDIDATE 3

# HIDDEN SINGLE
CELL B1 IN BOX 1 IS THE ONLY CELL WITH THE CANDIDATE 6

---------- STEP 9 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 7 4 1 | 9 8 6 | 2 3 5 |
B | 6 5 2 | 7 3 1 | 9 8 4 |
C |       | 5 2 4 | 6 1 7 |
  +-------+-------+-------+
D | 5     | 1 4 2 | 7 6 3 |
E |   7 4 |   9 5 | 8 2 1 |
F | 1     |   7 8 | 4 5 9 |
  +-------+-------+-------+
G | 8   7 |   5 9 | 1 4   |
H | 4 1 5 |   6   | 3     |
I |       | 4 1 3 | 5     |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL E1 NUMBER 3 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL G4 NUMBER 2 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL H6 NUMBER 7 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL F2 IN BOX 4 IS THE ONLY CELL WITH THE CANDIDATE 2

# HIDDEN SINGLE
CELL H8 IN ROW H IS THE ONLY CELL WITH THE CANDIDATE 9

# HIDDEN SINGLE
CELL H4 IN BOX 8 IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL I9 IN ROW I IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL G2 IN BOX 7 IS THE ONLY CELL WITH THE CANDIDATE 3

# HIDDEN SINGLE
CELL I8 IN ROW I IS THE ONLY CELL WITH THE CANDIDATE 7

# HIDDEN SINGLE
CELL I1 IN COL 1 IS THE ONLY CELL WITH THE CANDIDATE 2

# HIDDEN SINGLE
CELL E4 IN ROW E IS THE ONLY CELL WITH THE CANDIDATE 6

---------- STEP 10 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 7 4 1 | 9 8 6 | 2 3 5 |
B | 6 5 2 | 7 3 1 | 9 8 4 |
C |       | 5 2 4 | 6 1 7 |
  +-------+-------+-------+
D | 5     | 1 4 2 | 7 6 3 |
E | 3 7 4 | 6 9 5 | 8 2 1 |
F | 1 2   |   7 8 | 4 5 9 |
  +-------+-------+-------+
G | 8 3 7 | 2 5 9 | 1 4   |
H | 4 1 5 | 8 6 7 | 3 9   |
I | 2     | 4 1 3 | 5 7 8 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL C1 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL F3 NUMBER 6 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL F4 NUMBER 3 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL G9 NUMBER 6 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL H9 NUMBER 2 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL I2 IN COL 2 IS THE ONLY CELL WITH THE CANDIDATE 6

# HIDDEN SINGLE
CELL C3 IN BOX 1 IS THE ONLY CELL WITH THE CANDIDATE 3

---------- STEP 11 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 7 4 1 | 9 8 6 | 2 3 5 |
B | 6 5 2 | 7 3 1 | 9 8 4 |
C | 9   3 | 5 2 4 | 6 1 7 |
  +-------+-------+-------+
D | 5     | 1 4 2 | 7 6 3 |
E | 3 7 4 | 6 9 5 | 8 2 1 |
F | 1 2 6 | 3 7 8 | 4 5 9 |
  +-------+-------+-------+
G | 8 3 7 | 2 5 9 | 1 4 6 |
H | 4 1 5 | 8 6 7 | 3 9 2 |
I | 2 6   | 4 1 3 | 5 7 8 |
  +-------+-------+-------+
# NAKED SINGLE
IN CELL C2 NUMBER 8 IS THE ONLY CANDIDATE LEFT

# NAKED SINGLE
IN CELL I3 NUMBER 9 IS THE ONLY CANDIDATE LEFT

# HIDDEN SINGLE
CELL D3 IN COL 3 IS THE ONLY CELL WITH THE CANDIDATE 8

# HIDDEN SINGLE
CELL D2 IN COL 2 IS THE ONLY CELL WITH THE CANDIDATE 9

---------- STEP 12 ----------
    1 2 3   4 5 6   7 8 9
  +-------+-------+-------+
A | 7 4 1 | 9 8 6 | 2 3 5 |
B | 6 5 2 | 7 3 1 | 9 8 4 |
C | 9 8 3 | 5 2 4 | 6 1 7 |
  +-------+-------+-------+
D | 5 9 8 | 1 4 2 | 7 6 3 |
E | 3 7 4 | 6 9 5 | 8 2 1 |
F | 1 2 6 | 3 7 8 | 4 5 9 |
  +-------+-------+-------+
G | 8 3 7 | 2 5 9 | 1 4 6 |
H | 4 1 5 | 8 6 7 | 3 9 2 |
I | 2 6 9 | 4 1 3 | 5 7 8 |
  +-------+-------+-------+
