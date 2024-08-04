package com.gianfro.games.utils;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.InvalidHouseException;
import com.gianfro.games.exceptions.SudokuBuildException;
import org.paukov.combinatorics3.Generator;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static final List<String> ROWS_LETTERS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
    public static final List<Integer> NUMBERS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    public static final List<List<Integer>> SPLITTED_NUMBERS = Arrays.asList(NUMBERS.subList(0, 3), NUMBERS.subList(3, 6), NUMBERS.subList(6, 9));
    public static final List<Integer> INDEXES_02 = Arrays.asList(0, 1, 2);
    private static final List<Character> CHARACTERS = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    /**
     * Given a string, check if it's a valid string to build a Sudoku.
     * If it is, return a Sudoku object, if not throws a SudokuBuildException
     */
    public static Sudoku buildSudoku(String stringNumbers) {
        if (!sudokuStringMatchesPattern(stringNumbers)) {
            throw new SudokuBuildException(stringNumbers);
        }
        List<Integer> numbers = new LinkedList<>();
        for (int i = 0; i < stringNumbers.length(); i++) {
            numbers.add(Integer.parseInt(stringNumbers.substring(i, i + 1)));
        }
        return new Sudoku(numbers);
    }

    /**
     * Given a string, check if it's a valid string to build a sudoku (used by buildSudoku(String stringNumbers))
     */
    private static boolean sudokuStringMatchesPattern(String sudokuString) {
        if (sudokuString.length() != 81) {
            return false;
        }
        for (int i = 0; i < sudokuString.length(); i++) {
            if (!CHARACTERS.contains(sudokuString.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Prints on console an image of the sudoku grid (and returns the String corresponding to that image)
     */
    public static String grid(Sudoku sudoku) {
        StringBuilder sb = new StringBuilder();
        sb.append("    1 2 3   4 5 6   7 8 9").append("\n");
        sb.append("  +-------+-------+-------+").append("\n");
        for (int i = 0; i < 9; i++) {
            List<Integer> row = sudoku.getRows().get(i);
            sb
                    .append(ROWS_LETTERS.get(i)).append(" ").append("| ")
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
     * Prints on console a giant image of the sudoku grid (and returns the String corresponding to that image)
     */
    public static String megaGrid(Sudoku sudoku) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String newLine = "\n";

        String fourSpaces = "    ";
        String fiveSpaces = "     ";
        String elevenSpaces = "          ";
        String elevenHash = "###########";


        StringBuilder numbersRow = new StringBuilder();
        StringBuilder gridString = new StringBuilder();
        for (int number : NUMBERS) {
            numbersRow.append(fiveSpaces).append(ANSI_RED).append(number).append(ANSI_RESET).append(fiveSpaces);
        }

        gridString.append("  ").append(numbersRow).append(newLine);
        gridString.append("  ").append(String.join("", Collections.nCopies(NUMBERS.size(), elevenHash))).append("#").append(newLine);


        for (int i = 0; i < NUMBERS.size() * 4; i++) {
            int sudokuRowIndex = i / 4;
            List<Integer> correspondingSudokuRow = sudoku.getRows().get(sudokuRowIndex);

            if (((i + 1) % 2) == 0 && ((i + 1) % 4) != 0) {
                gridString.append(ANSI_RED).append(ROWS_LETTERS.get(i / 4)).append(ANSI_RESET).append(" #");
                for (int index = 0; index < 9; index++) {
                    if (!correspondingSudokuRow.get(index).equals(0)) {
                        gridString.append(fourSpaces).append(ANSI_GREEN).append(correspondingSudokuRow.get(index)).append(ANSI_RESET).append(fiveSpaces).append((index + 1) % 3 == 0 ? "#" : "|");
                    } else {
                        gridString.append(fourSpaces).append(" ").append(fiveSpaces).append((index + 1) % 3 == 0 ? "#" : "|");
                    }
                }
                gridString.append(newLine);
            } else if (((i + 1) % 4) == 0) {
                if ((i + 1) % 12 == 0) {
                    gridString.append("  ").append(String.join("", Collections.nCopies(NUMBERS.size(), elevenHash))).append("#").append(newLine);
                } else {
                    gridString.append("  ").append(String.join("", Collections.nCopies(NUMBERS.size(), "-----------"))).append("#").append(newLine);
                }
            } else {
                gridString.append("  #");
                for (int index = 0; index < 9; index++) {
                    gridString.append(elevenSpaces).append((index + 1) % 3 == 0 ? "#" : "|");
                }
                gridString.append(newLine);
            }
        }
        System.out.println(gridString);
        return gridString.toString();
    }


    /**
     * Prints a number only if != 0 (used by grid(Sudoku sudoku))
     */
    private static String printIfFilled(int number) {
        return number != 0 ? String.valueOf(number) : " ";
    }


    /**
     * Returns the Tab list (coordinates + list of candidates) of empty cells of Sudoku (based only on present numbers)
     */
    public static List<Tab> getBasicTabs(Sudoku sudoku) {
        List<Tab> tabs = new LinkedList<>();
        for (int row : NUMBERS) {
            for (int column : NUMBERS) {
                if (sudoku.getRows().get(row - 1).get(column - 1) == 0) {
                    List<Integer> missingNumbers = new ArrayList<>(NUMBERS);
                    missingNumbers.removeAll(sudoku.getRows().get(row - 1));
                    missingNumbers.removeAll(sudoku.getColumns().get(column - 1));
                    Tab tab = new Tab(row, column, missingNumbers);
                    List<Integer> boxNumbers = sudoku.getBoxes().get(tab.getBox() - 1);
                    tab.getNumbers().removeAll(boxNumbers);
                    tabs.add(tab);
                }
            }
        }
        return tabs;
    }


    /**
     * Creates a new list of numbers to create a Sudoku object according to the deduction (Change) given in input
     */
    public static List<Integer> setDeductedNumber(List<Integer> numbersList, Change change) {
        numbersList.set(((9 * (change.getRow() - 1)) + (change.getCol() - 1)), change.getNumber());
        return numbersList;
    }


    /**
     * Given a House and a Tab list, returns the list of tabs related to that House
     */
    public static List<Tab> getHouseTabs(House house, int index, List<Tab> tabs) {
        List<Tab> houseTabs = new LinkedList<>();
        for (Tab tab : tabs) {
            int houseValue;
            switch (house) {
                case BOX:
                    houseValue = tab.getBox();
                    break;
                case ROW:
                    houseValue = tab.getRow();
                    break;
                case COL:
                    houseValue = tab.getCol();
                    break;
                default:
                    throw new InvalidHouseException();
            }
            if (houseValue == index) {
                houseTabs.add(tab);
            }
        }
        return houseTabs;
    }


    /**
     * Given a list of candidates and a number N, returns all possible tuples with N elements that can be created with that set
     */
    public static List<List<Integer>> findAllPossibleTuples(List<Integer> candidatesWithAtLeastTwoOccurences, int n) {
        return Generator
                .combination(candidatesWithAtLeastTwoOccurences)
                .simple(n)
                .stream()
                .collect(Collectors.<List<Integer>>toList());
    }


    // data una lista di candidati di un tab e una tupla di N candidati, stabilisce se la tupla ne contiene almeno quanti il numero passato come parametro
    public static boolean containsAtLeastXCandidates(List<Integer> candidates, List<Integer> tuple, int minimumCandidates) {
        int count = 0;
        for (Integer number : candidates) {
            if (tuple.contains(number)) {
                count++;
            }
        }
        return count >= minimumCandidates;
    }


    // dato un tab e una tupla di candidati, stabilisce se i candidati del tab sono uguali alla tupla o un suo sottoinsieme
    public static boolean candidatesAreSameOrSubset(Tab tab, List<Integer> tuple) {
        for (Integer numero : tab.getNumbers()) {
            if (!tuple.contains(numero)) {
                return false;
            }
        }
        return true;
    }

    // restituisce l'array delle tre righe da 3 elementi del quadrato (TODO da scartare in futuro)
    public static List<List<Integer>> getBoxRows(List<Integer> box) {
        List<List<Integer>> boxRows = new LinkedList<>();
        for (int i : INDEXES_02) {
            boxRows.add(new LinkedList<>(box.subList(3 * i, (3 * i) + 3)));
        }
        return boxRows;
    }


    // restituisce la lista delle tre colonne da 3 elementi del quadrato (TODO da scartare in futuro)
    public static List<List<Integer>> getBoxColumns(List<Integer> box) {
        List<List<Integer>> boxColumns = new LinkedList<>();
        for (int i : INDEXES_02) {
            List<Integer> thirdPartOfColumn = new ArrayList<>();
            for (List<Integer> row : getBoxRows(box)) {
                thirdPartOfColumn.add(row.get(i));
            }
            boxColumns.add(thirdPartOfColumn);
        }
        return boxColumns;
    }


    // restituisce l'array dei quadrati del terzetto righe (indice terzetto tra 0 e 2) (TODO da scartare in futuro)
    public static List<List<Integer>> getRowsTrioBoxes(Sudoku sudoku, int indexRowsTrio) {
        return sudoku.getBoxes().subList(indexRowsTrio * 3, 3 + (indexRowsTrio * 3));
    }


    // restituisce la lista dei quadrati del terzetto colonne (indice terzetto tra 0 e 2) (TODO da scartare in futuro)
    public static List<List<Integer>> getColsTrioBoxes(Sudoku sudoku, int indexRowsTrio) {
        List<List<Integer>> boxesTrio = new LinkedList<>();
        for (int i : INDEXES_02) {
            boxesTrio.add(sudoku.getBoxes().get(indexRowsTrio + (3 * i)));
        }
        return boxesTrio;
    }


    // controlla per ogni quadrato, riga e colonna se presenta numeriRipetuti (bug che incorre col metodo FIFTY-FIFTY)
    // TODO rendere un metodo privato della classe ?
    public static Set<String> checkForBugs(Sudoku attempt) {
        Set<String> bugs = new HashSet<>();
        for (List<Integer> box : attempt.getBoxes()) {
            for (int number : box) {
                if (number != 0) {
                    int occurrences = Collections.frequency(box, number);
                    if (occurrences > 1) {
                        bugs.add("Box " + box + " has " + occurrences + " times the number " + number);
                    }
                }
            }
        }
        for (List<Integer> row : attempt.getRows()) {
            for (int number : row) {
                if (number != 0) {
                    int occurrences = Collections.frequency(row, number);
                    if (occurrences > 1) {
                        bugs.add("Row " + row + " has " + occurrences + " times the number " + number);
                    }
                }
            }
        }
        for (List<Integer> column : attempt.getColumns()) {
            for (int number : column) {
                if (number != 0) {
                    int occurrences = Collections.frequency(column, number);
                    if (occurrences > 1) {
                        bugs.add("Column " + column + " has " + occurrences + " times the number " + number);
                    }
                }
            }
        }
        return bugs;
    }


    // controlla se ci sono caselle che non hanno candidati pur non essendo valorizzate
    // (bug che incorre col metodo FIFTY-FIFTY) TODO rendere un metodo privato della classe ?
    public static Set<String> checkForEmptySquaresWithNoCandidates(Sudoku sudoku, List<Tab> tabs) {
        Set<String> bugs = new HashSet<>();
        for (Tab tab : tabs) {
            int sudokuSquare = ((9 * (tab.getRow() - 1))) + (tab.getCol() - 1);
            if (tab.getNumbers().isEmpty() && sudoku.getNumbers().get(sudokuSquare) == 0) {
                bugs.add("Cell (" + ROWS_LETTERS.get(tab.getRow() - 1) + tab.getCol() + ") is empty but has no candidates");
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
            for (ChangeLogUnitMember tab : changeLog.getUnitMembers()) {
                System.out.println(tab);
            }
            System.out.println("I then deducted:");
            for (Change change : changeLog.getChanges()) {
                if (change instanceof Skimming) {
                    Skimming skimming = (Skimming) change;
                    System.out.println(skimming);
                } else {
                    System.out.println(change);
                }
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
        switch (Objects.requireNonNull(changeLog.getHouse())) {
            case BOX:
                return "BOX " + changeLog.getUnitMembers().get(0).getBox();
            case ROW:
                return "ROW " + Utils.ROWS_LETTERS.get(changeLog.getUnitMembers().get(0).getRow() - 1);
            case COL:
                return "COL " + changeLog.getUnitMembers().get(0).getCol();
            default:
                throw new InvalidHouseException();
        }
    }

    /**
     * Per debuggare: stampa una griglia del sudoku dove ci sono i tabs di ogni casella (se risolta, tab con un solo candidato)
     */
    public static void printSkimmedTabs(Sudoku sudoku, List<Tab> skimmedTabs) {
        List<Tab> allTabs = new ArrayList<>();
        for (int row : NUMBERS) {
            for (int col : NUMBERS) {
                int number = sudoku.getRows().get(row - 1).get(col - 1);
                Tab tab = null;
                if (number == 0) {
                    for (Tab skimmedTab : skimmedTabs) {
                        if (skimmedTab.getRow() == row && skimmedTab.getCol() == col) {
                            tab = new Tab(row, col, skimmedTab.getNumbers());
                        }
                    }
                } else {
                    tab = new Tab(row, col, Collections.singletonList(number));
                }
                allTabs.add(tab);
            }
        }
        int rowCount = 0;
        System.out.print("       " + "    COL 1    " + "    COL 2    " + "    COL 3    ");
        System.out.print("       " + "    COL 4    " + "    COL 5    " + "    COL 6    ");
        System.out.print("       " + "    COL 7    " + "    COL 8    " + "    COL 9    ");
        for (int i = 0; i < 81; i += 9) {
            List<Tab> tabsRow = allTabs.subList(i, i + 9);
            System.out.println();
            System.out.print("ROW " + (Utils.ROWS_LETTERS.get(rowCount)) + "  ");
            for (int t = 0; t < 9; t++) {
                Tab tab = tabsRow.get(t);
                System.out.print(" " + printCandidates(tab) + " ");
                if (((t + 1) % 3) == 0 && t < 8) {
                    System.out.print("       ");
                }
            }
            if (((rowCount + 1) % 3) == 0) {
                System.out.println();
            }
            rowCount++;
        }
    }


    /**
     * Per debuggare: identico al metodo di sopra, ma sono tabs semplici, senza scremature *
     */
    public static void printTabs(Sudoku sudoku) {
        List<Tab> allTabs = new ArrayList<>();
        for (int row : NUMBERS) {
            for (int col : NUMBERS) {
                int number = sudoku.getRows().get(row - 1).get(col - 1);
                Tab tab;
                if (number == 0) {
                    List<Integer> missingNumbers = new ArrayList<>(Utils.NUMBERS);
                    missingNumbers.removeAll(sudoku.getRows().get(row - 1));
                    missingNumbers.removeAll(sudoku.getColumns().get(col - 1));
                    tab = new Tab(row, col, missingNumbers);
                    List<Integer> boxNumbers = sudoku.getBoxes().get(tab.getBox() - 1);
                    tab.getNumbers().removeAll(boxNumbers);
                } else {
                    tab = new Tab(row, col, Collections.singletonList(number));
                }
                allTabs.add(tab);
            }
        }
        int rowCount = 0;
        System.out.print("       " + "    COL A    " + "    COL B    " + "    COL C    ");
        System.out.print("       " + "    COL D    " + "    COL E    " + "    COL F    ");
        System.out.print("       " + "    COL G    " + "    COL H    " + "    COL I    ");
        for (int i = 0; i < 81; i += 9) {
            List<Tab> tabsRow = allTabs.subList(i, i + 9);
            System.out.println();
            System.out.print("ROW " + (rowCount + 1) + "  ");
            for (int t = 0; t < 9; t++) {
                Tab tab = tabsRow.get(t);
                System.out.print(" " + printCandidates(tab) + " ");
                if (((t + 1) % 3) == 0 && t < 8) {
                    System.out.print("       ");
                }
            }
            if (((rowCount + 1) % 3) == 0) {
                System.out.println();
            }
            rowCount++;
        }
    }


    // usato dai due metodi printTabs e printSkimmedTabs
    private static String printCandidates(Tab tab) {
        StringBuilder candidates = new StringBuilder();
        for (int candidate : tab.getNumbers()) {
            candidates.append(candidate);
        }
        while (candidates.length() < 9) {
            candidates.append(" ");
        }
        return "[" + candidates + "]";
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
                Sudoku s = Utils.buildSudoku(data);
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

    // Data una lista di Tab, restituisce il numero con meno celle possibili dove può essere inserito.
    // Per ora non usato, ma può tornare utile
    public static Integer getNumberWithLessPossibleCells(List<Tab> tabs) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer number : NUMBERS) {
            int cellCount = 0;
            for (Tab t : tabs) {
                if (t.getNumbers().contains(number)) cellCount++;
            }
            map.put(number, cellCount);
        }
        return map.get(Collections.min(map.values()));
    }

    /**
     * Given a Sudoku and a Tab List, prints a giant grid, where each determined cell has its number set in green.
     * The remaining cells have show list of candidates still available.
     * TODO FOR SURE IT CAN BE WRITTEN WAY BETTER
     */
    public static String megaGrid(Sudoku sudoku, List<Tab> tabs) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RED = "\u001B[31m";
        String NEW_LINE = "\n";

        String fourSpaces = "    ";
        String nineSpaces = "         ";
        String tenHash = "##########";
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
            List<Integer> correspondingSudokuRow = sudoku.getRows().get(sudokuRowIndex);
            int candidatesRowIndex = i;
            while (candidatesRowIndex >= 4) {
                candidatesRowIndex -= 4;
            }

            if (((i + 1) % 2) == 0 && ((i + 1) % 4) != 0) {
                gridString.append(ROWS_LETTERS.get(i / 4)).append(ANSI_RED).append(" #").append(ANSI_RESET);
                for (int index = 0; index < 9; index++) {
                    String endLineBox = (index + 1) % 3 == 0 ? ANSI_RED + "#" + ANSI_RESET : "|";
                    if (!correspondingSudokuRow.get(index).equals(0)) {
                        gridString.append(fourSpaces).append(ANSI_GREEN).append(correspondingSudokuRow.get(index)).append(ANSI_RESET).append(fourSpaces).append(endLineBox);
                    } else {
                        int cellColumn = index;
                        List<Tab> rowTabs = tabs.stream().filter(t -> t.getRow() == sudokuRowIndex + 1).collect(Collectors.toList());
                        Tab currentCellTab = rowTabs.stream().filter(t -> t.getCol() == cellColumn + 1).findFirst().orElse(null);
                        if (currentCellTab != null) {
                            for (Integer number : SPLITTED_NUMBERS.get(candidatesRowIndex)) {
                                gridString.append(" ").append(currentCellTab.getNumbers().contains(number) ? number : " ").append(" ");
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
                    if (correspondingSudokuRow.get(index).equals(0)) {
                        int cellColumn = index;
                        List<Tab> rowTabs = tabs.stream().filter(t -> t.getRow() == sudokuRowIndex + 1).collect(Collectors.toList());
                        Tab currentCellTab = rowTabs.stream().filter(t -> t.getCol() == cellColumn + 1).findFirst().orElse(null);
                        if (currentCellTab != null) {
                            for (Integer number : SPLITTED_NUMBERS.get(candidatesRowIndex)) {
                                gridString.append(" ").append(currentCellTab.getNumbers().contains(number) ? number : " ").append(" ");
                            }
                            gridString.append(endLineBox);
                        }
                    } else {
                        gridString.append(nineSpaces).append(endLineBox);
                    }
                }
                gridString.append(NEW_LINE);
            }
        }
        System.out.println(gridString);
        return gridString.toString();
    }

    public static void main(String[] args) {
        Sudoku sudoku = buildSudoku("000000206000080109900700000000030090056000000029000000000106500400000030000203000");
        List<Tab> tabs = getBasicTabs(sudoku);

        grid(sudoku);
        megaGrid(sudoku, tabs);
        printTabs(sudoku);
    }
}
