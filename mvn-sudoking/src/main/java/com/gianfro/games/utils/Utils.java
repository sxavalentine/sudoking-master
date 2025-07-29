package com.gianfro.games.utils;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellChange;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.exceptions.SudokuBugException;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Utils {

    public static final List<Integer> NUMBERS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    public static final List<String> ROWS_LETTERS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
    public static final List<List<Integer>> SPLITTED_NUMBERS = Arrays.asList(NUMBERS.subList(0, 3), NUMBERS.subList(3, 6), NUMBERS.subList(6, 9));

    /**
     * Prints on console an image of the sudoku grid (and returns the String corresponding to that image)
     */
    public static String grid(Sudoku sudoku) {
        List<SudokuCell> cells = sudoku.getCells();
        StringBuilder sb = new StringBuilder();
        sb.append("    1 2 3   4 5 6   7 8 9").append("\n");
        sb.append("  +-------+-------+-------+").append("\n");

        for (int i = 0; i < 9; i++) {
            int finalI = i;
            List<Integer> row = cells.stream().filter(c -> c.getRow() - 1 == finalI).map(SudokuCell::getValue).toList();
            sb.append(ROWS_LETTERS.get(i)).append(" ").append("| ")
                    .append(printIfFilled(row.get(0))).append(" ")
                    .append(printIfFilled(row.get(1))).append(" ")
                    .append(printIfFilled(row.get(2))).append(" | ")
                    .append(printIfFilled(row.get(3))).append(" ")
                    .append(printIfFilled(row.get(4))).append(" ")
                    .append(printIfFilled(row.get(5))).append(" | ")
                    .append(printIfFilled(row.get(6))).append(" ")
                    .append(printIfFilled(row.get(7))).append(" ")
                    .append(printIfFilled(row.get(8))).append(" |").append("\n");
            if (((i + 1) % 3) == 0) {
                sb.append("  +-------+-------+-------+").append("\n");
            }
        }
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * Prints a number only if != 0 (used by grid(Sudoku sudoku))
     */
    private static String printIfFilled(int number) {
        return number != 0 ? String.valueOf(number) : " ";
    }

    /**
     * Returns a new Sudoku object after applying the Changes found through deductions
     * IMPORTANT: the changes are always applied to the COPY of the sudoku
     * after which the Sudoku for nextStep is built from
     */
    public static Sudoku applyDeductions(Collection<ChangeLog> changeLogs, Sudoku sudoku) {

        List<CellChange> changes = changeLogs.stream()
                .flatMap(x -> x.getChanges().stream()).distinct().toList();

        for (CellChange change : changes) {
            SudokuCell impactedCell = sudoku.getCells().get(change.getCell().getIndex());
            if (change instanceof CellSkimmed skimming) {
                impactedCell.getCandidates().removeAll(skimming.getRemovedCandidates());
            } else {
                impactedCell.setValue(change.getNumber());
            }
        }
        return Sudoku.fromCells(sudoku.getCells());
    }

    /**
     * Given a Sudoku, returns the list of all empty cells in it
     */
    public static List<SudokuCell> getEmptyCells(Sudoku sudoku) {
        List<SudokuCell> emptyCells = new ArrayList<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (cell.isEmpty()) {
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    /**
     * Given a Sudoku, a House and its index, returns the list of all cells in that House
     */
    public static List<SudokuCell> getHouseCells(Sudoku sudoku, House house, int index) {
        List<SudokuCell> houseCells = new ArrayList<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (cell.getHouseNumber(house) == index) {
                houseCells.add(cell);
            }
        }
        return houseCells;
//        return sudoku.getCells().stream().filter(c -> c.getHouseNumber(house) == index).toList();
    }

    /**
     * Given a Sudoku, a House and its index, returns the list of all EMPTY cells in that House
     */
    public static List<SudokuCell> getEmptyHouseCells(Sudoku sudoku, House house, int index) {
        List<SudokuCell> emptyHouseCells = new ArrayList<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (cell.isEmpty() && cell.getHouseNumber(house) == index) {
                emptyHouseCells.add(cell);
            }
        }
        return emptyHouseCells;
//        return sudoku.getCells().stream().filter(c -> c.isEmpty() && c.getHouseNumber(house) == index).toList();
    }

    /**
     * Given a Sudoku, a House and its index, returns the list of all SOLVED cells in that House
     */
    public static List<SudokuCell> getSolvedHouseCells(Sudoku sudoku, House house, int index) {
        List<SudokuCell> solvedHouseCells = new ArrayList<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (!cell.isEmpty() && cell.getHouseNumber(house) == index) {
                solvedHouseCells.add(cell);
            }
        }
        return solvedHouseCells;
//        return sudoku.getCells().stream().filter(c -> c.getValue() != 0 && c.getHouseNumber(house) == index).toList();
    }

    /**
     * Given two SudokuCell and an integer quantity, returns true if they share a number of candidates >= quantity
     */
    public static boolean cellsShareNCandidates(SudokuCell cellA, SudokuCell cellB, int quantity) {
        int shared = (int) cellA.getCandidates().stream().filter(c -> cellB.getCandidates().contains(c)).count();
        return shared >= quantity;
    }

    /**
     * Returns the list of SudokuCell in a list that are seen by the SudokuCell in input
     *
     * @param cell    the SudokuCell of which we want to know all seen cells
     * @param cellSet the pool we want to check from (can be a whole Sudoku or a restricted pool of cells)
     */
    public static List<SudokuCell> getSeenCells(SudokuCell cell, List<SudokuCell> cellSet) {
        List<SudokuCell> seenCells = new ArrayList<>();
        for (SudokuCell otherCell : cellSet) {
            if (cell.canSeeOther(otherCell)) {
                seenCells.add(otherCell);
            }
        }
        return seenCells;
//        return cellSet.stream().filter(c -> c.canSeeOther(cell)).toList();
    }

    /**
     * Returns the list of SudokuCell in a Sudoku that are seen by the SudokuCell in input
     *
     * @param cell      the SudokuCell of which we want to know all seen cells
     * @param sudoku    the Sudoku we want to check from
     * @param emptyOnly if true, filters the output returning only the empty cells
     */
    public static List<SudokuCell> getSeenCells(SudokuCell cell, Sudoku sudoku, boolean emptyOnly) {
        List<SudokuCell> seenCells = new ArrayList<>();
        for (SudokuCell otherCell : sudoku.getCells()) {
            if (cell.canSeeOther(otherCell) && (emptyOnly ? otherCell.isEmpty() : true)) {
                seenCells.add(otherCell);
            }
        }
        return seenCells;
    }

    /**
     * Given a ChangeLogUnitMember(SudokuCell or Link) and a Sudoku, returns the list of all the other empty cells seen by the one passed as parameter
     */
    public static List<SudokuCell> getSeenEmptyCells(ChangeLogUnitMember cell, Sudoku sudoku) {
        return sudoku.getCells().stream().filter(c -> c.canSeeOther(cell) && c.isEmpty()).toList();
    }

    /**
     * Perform some basic operations to check if the Sudoku has some logic impossibilities
     * (which may happen when "guessing" cell values with FiftyFifty method)
     */
    public static void checkForBugs(Sudoku sudoku) {
        Set<String> bugs = new HashSet<>();
        bugs.addAll(checkForEmptyCellsWithNoCandidates(sudoku));
        bugs.addAll(checkForHouseDuplicates(sudoku));
        bugs.addAll(checkForHousesWithCellsHavingSameLastCandidate(sudoku));
        if (!bugs.isEmpty()) {
            System.out.println("Found the following bugs during solution:");
            for (String bug : bugs) {
                System.out.println(bug);
            }
            throw new SudokuBugException(sudoku, bugs);
        }
    }

    /**
     * Check if a Sudoku has empty cells with no candidates left (which is an error).
     */
    public static Set<String> checkForEmptyCellsWithNoCandidates(Sudoku sudoku) {
        Set<String> bugs = new HashSet<>();
        List<SudokuCell> emptyWithNoCandidates = sudoku.getCells().stream().filter(c -> c.isEmpty() && c.getCandidates().isEmpty()).toList();
        if (!emptyWithNoCandidates.isEmpty()) {
            bugs.add("The following cells are empty and have no candidates left: " + emptyWithNoCandidates);
        }
        return bugs;
    }

    /**
     * Check if there are houses with multiple cells with the same value (which is an error).
     */
    public static Set<String> checkForHouseDuplicates(Sudoku sudoku) {
        Set<String> bugs = new HashSet<>();
        for (House house : House.values()) {
            for (int houseNumber : NUMBERS) {
                List<SudokuCell> solvedHouseCells = getSolvedHouseCells(sudoku, house, houseNumber);
                Map<Integer, List<SudokuCell>> valueMap = new HashMap<>();
                for (SudokuCell cell : solvedHouseCells) {
                    int cellValue = cell.getValue();
                    valueMap.computeIfAbsent(cellValue, k -> new ArrayList<>()).add(cell);
                }
                valueMap.forEach((value, cells) -> {
                    if (cells.size() > 1) {
                        bugs.add(house.name() + " " + houseNumber + " has " + cells.size() + " cells with value " + value + ": " + cells.stream().map(SudokuCell::getCoordinates).toList());
                    }
                });
            }
        }
        return bugs;
    }

    /**
     * Check if there are houses with multiple cells that share the same last candidate left (which is an error).
     */
    public static Set<String> checkForHousesWithCellsHavingSameLastCandidate(Sudoku sudoku) {
        Set<String> bugs = new HashSet<>();
        for (House house : House.values()) {
            for (Integer houseNumber : NUMBERS) {
                List<SudokuCell> emptyCells = getEmptyHouseCells(sudoku, house, houseNumber);
                for (int candidate : NUMBERS) {
                    List<SudokuCell> welcomingCells = emptyCells.stream().filter(
                            c -> c.getCandidates().size() == 1 && c.getCandidates().contains(candidate)).toList();
                    if (welcomingCells.size() > 1) {
                        bugs.add(house.name() + " " + houseNumber + " has multiple cells having " + candidate + " as last candidate left: " + welcomingCells);
                    }
                }
            }
        }
        return bugs;
    }


    /**
     * For every ChangeLog in a SkimmingResult, prints a simplified version of it
     */
    public static void printChangeLogs(SkimmingResult result) {
        for (ChangeLog changeLog : result.getChangeLogs()) {
            System.out.println(changeLog.getSolvingTechniqueVariant() != null ? changeLog.getSolvingTechniqueVariant() : changeLog.getSolvingTechnique() + ": " + changeLog.getUnitExamined());
            if (changeLog.getHouse() != null) {
                System.out.println("It's in " + getWelcomingUnit(changeLog));
            }
            System.out.println("It's tabs are:");
            for (ChangeLogUnitMember unitMember : changeLog.getUnitMembers()) {
                System.out.println(unitMember);
            }
            System.out.println("I then deducted:");
            for (CellChange change : changeLog.getChanges()) {
                System.out.println(change);
            }
            System.out.println();
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println();
        }
    }


    /**
     * Given a ChangeLog, returns the corresponding string (used by explainers)
     */
    public static String getWelcomingUnit(ChangeLog changeLog) {
        return switch (Objects.requireNonNull(changeLog.getHouse())) {
            case BOX -> "BOX " + changeLog.getUnitMembers().get(0).getBox();
            case ROW -> "ROW " + Utils.ROWS_LETTERS.get(changeLog.getUnitMembers().get(0).getRow() - 1);
            case COL -> "COL " + changeLog.getUnitMembers().get(0).getCol();
        };
    }

    public static List<Sudoku> read50kSudoku() {
        List<Sudoku> allSudoku = new ArrayList<>();
        try {
            ClassLoader classLoader = Utils.class.getClassLoader();
            URL resource = classLoader.getResource("50kSudoku.txt");
            assert resource != null;
            File sudokuList = new File(resource.toURI());
            Scanner myReader = new Scanner(sudokuList);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Sudoku s = Sudoku.fromString(data);
                allSudoku.add(s);
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        System.out.println("READ " + allSudoku.size() + " SUDOKUS");
        return allSudoku;
    }

    // Dato un sudoku, restituisce il numero con meno celle possibili dove può essere inserito.
    // Per ora non usato, ma può tornare utile
    public static Integer getNumberWithLessPossibleCells(Sudoku sudoku) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer number : NUMBERS) {
            map.put(number, (int) sudoku.getCells().stream().filter(c -> c.getCandidates().contains(number)).count());
        }
        return map.get(Collections.min(map.values()));
    }

    /**
     * Prints on console a giant image of the sudoku grid (and returns the String corresponding to that image)
     */
    public static String megaGridNoCandidates(Sudoku sudoku) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String NEW_LINE = "\n";

        String fourSpaces = "    ";
        String fiveSpaces = "     ";
        String elevenSpaces = "          ";
        String elevenHash = "###########";


        StringBuilder numbersRow = new StringBuilder();
        StringBuilder gridString = new StringBuilder();
        for (int number : NUMBERS) {
            numbersRow.append(fiveSpaces).append(ANSI_RED).append(number).append(ANSI_RESET).append(fiveSpaces);
        }

        gridString.append("  ").append(numbersRow).append(NEW_LINE);
        gridString.append("  ").append(String.join("", Collections.nCopies(NUMBERS.size(), elevenHash))).append("#").append(NEW_LINE);


        for (int i = 0; i < NUMBERS.size() * 4; i++) {
            int sudokuRowIndex = i / 4;
            List<SudokuCell> rowCells = getHouseCells(sudoku, House.ROW, sudokuRowIndex + 1);
            List<Integer> rowNumbers = rowCells.stream().map(SudokuCell::getValue).toList();

            if (((i + 1) % 2) == 0 && ((i + 1) % 4) != 0) {
                gridString.append(ANSI_RED).append(ROWS_LETTERS.get(i / 4)).append(ANSI_RESET).append(" #");
                for (int index = 0; index < 9; index++) {
                    if (!rowNumbers.get(index).equals(0)) {
                        gridString.append(fourSpaces).append(ANSI_GREEN).append(rowNumbers.get(index)).append(ANSI_RESET).append(fiveSpaces).append((index + 1) % 3 == 0 ? "#" : "|");
                    } else {
                        gridString.append(fourSpaces).append(" ").append(fiveSpaces).append((index + 1) % 3 == 0 ? "#" : "|");
                    }
                }
                gridString.append(NEW_LINE);
            } else if (((i + 1) % 4) == 0) {
                if ((i + 1) % 12 == 0) {
                    gridString.append("  ").append(String.join("", Collections.nCopies(NUMBERS.size(), elevenHash))).append("#").append(NEW_LINE);
                } else {
                    gridString.append("  ").append(String.join("", Collections.nCopies(NUMBERS.size(), "-----------"))).append("#").append(NEW_LINE);
                }
            } else {
                gridString.append("  #");
                for (int index = 0; index < 9; index++) {
                    gridString.append(elevenSpaces).append((index + 1) % 3 == 0 ? "#" : "|");
                }
                gridString.append(NEW_LINE);
            }
        }
        System.out.println(gridString);
        return gridString.toString();
    }

    /**
     * Given a Sudoku, prints a giant grid, where each determined cell has its number set in green.
     * The remaining cells have show list of candidates still available.
     * TODO FOR SURE IT CAN BE WRITTEN WAY BETTER
     */
    public static String megaGrid(Sudoku sudoku) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String NEW_LINE = "\n";

        String fourSpaces = "    ";
        String fiveSpaces = "     ";
        String nineSpaces = "         ";
        String elevenSpaces = "          ";
        String tenHash = "##########";
        String elevenHash = "###########";

        String midColumnSeparator = "---------#---------#---------" + ANSI_RED + "#" + ANSI_RESET;

        StringBuilder numbersRow = new StringBuilder();
        StringBuilder gridString = new StringBuilder();
        for (int number : NUMBERS) {
            numbersRow.append("     ").append(number).append(fourSpaces);
        }

        gridString.append("  ").append(numbersRow).append(NEW_LINE);
        gridString.append("  ").append(ANSI_RED).append(String.join("", Collections.nCopies(NUMBERS.size(), tenHash))).append("#").append(ANSI_RESET).append(NEW_LINE);

        for (int i = 0; i < NUMBERS.size() * 4; i++) {
            int sudokuRowIndex = i / 4;
            List<SudokuCell> rowCells = getHouseCells(sudoku, House.ROW, sudokuRowIndex + 1);
            List<Integer> rowNumbers = rowCells.stream().map(SudokuCell::getValue).toList();
            int candidatesRowIndex = i;
            while (candidatesRowIndex >= 4) {
                candidatesRowIndex -= 4;
            }

            if (((i + 1) % 2) == 0 && ((i + 1) % 4) != 0) {
                gridString.append(ROWS_LETTERS.get(i / 4)).append(ANSI_RED).append(" #").append(ANSI_RESET);
                for (int index = 0; index < 9; index++) {
                    String endLineBox = (index + 1) % 3 == 0 ? ANSI_RED + "#" + ANSI_RESET : "|";
                    if (!rowNumbers.get(index).equals(0)) {
                        gridString.append(fourSpaces).append(ANSI_GREEN).append(rowNumbers.get(index)).append(ANSI_RESET).append(fourSpaces).append(endLineBox);
                    } else {
                        SudokuCell currentCell = rowCells.get(index);
                        if (currentCell != null) {
                            for (Integer number : SPLITTED_NUMBERS.get(candidatesRowIndex)) {
                                gridString.append(" ").append(currentCell.getCandidates().contains(number) ? number : " ").append(" ");
                            }
                            gridString.append(endLineBox);
                        }
                    }
                }
                gridString.append(NEW_LINE);
            } else if (((i + 1) % 4) == 0) {
                if ((i + 1) % 12 == 0) {
                    gridString.append("  ").append(ANSI_RED).append(String.join("", Collections.nCopies(NUMBERS.size(), tenHash))).append("#").append(ANSI_RESET).append(NEW_LINE);
                } else {
                    gridString.append("  ").append(ANSI_RED).append("#").append(ANSI_RESET).append(String.join("", Collections.nCopies(3, midColumnSeparator))).append(NEW_LINE);
                }
            } else {
                gridString.append(ANSI_RED).append("  #").append(ANSI_RESET);
                for (int index = 0; index < 9; index++) {
                    String endLineBox = (index + 1) % 3 == 0 ? ANSI_RED + "#" + ANSI_RESET : "|";
                    if (rowNumbers.get(index).equals(0)) {
                        SudokuCell currentCell = rowCells.get(index);
                        for (Integer number : SPLITTED_NUMBERS.get(candidatesRowIndex)) {
                            gridString.append(" ").append(currentCell.getCandidates().contains(number) ? number : " ").append(" ");
                        }
                        gridString.append(endLineBox);
                    } else {
                        gridString.append(nineSpaces).append(endLineBox);
                    }
                }
                gridString.append(NEW_LINE);
            }
        }
        System.out.println(gridString);

        return gridString.toString().replace(ANSI_GREEN, "").replace(ANSI_RED, "").replace(ANSI_RESET, "");
    }

    public static void main(String[] args) {
        Sudoku sudoku = Sudoku.fromString("000000206000080109900700000000030090056000000029000000000106500400000030000203000");
        grid(sudoku);
        String s = megaGrid(sudoku);
        System.out.println(s);
    }
}
