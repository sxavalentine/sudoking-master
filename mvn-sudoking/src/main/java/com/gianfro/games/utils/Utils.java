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
    public static final List<Integer> INDEXES_02 = Arrays.asList(0, 1, 2);
    private static final List<Character> CHARACTERS = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    // data una stringa, costruisce un Sudoku. Se questa non rispetta il pattern lancia una RuntimeException SudokuBuildException
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


    // data una stringa, stabilisce se è una stringa valida per costruire un Sudoku (usato da buildSudoku(String stringNumbers))
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


    // stampa in console un'immagine della grid del Sudoku
    public static void grid(Sudoku sudoku) {
        System.out.println("    1 2 3   4 5 6   7 8 9");
        System.out.println("  +-------+-------+-------+");
        for (int i = 0; i < 9; i++) {
            List<Integer> row = sudoku.getRows().get(i);
            System.out.println(
                    "" + (ROWS_LETTERS.get(i)) + " " +
                            "| " + printIfFilled(row.get(0)) + " " + printIfFilled(row.get(1)) + " " + printIfFilled(row.get(2)) +
                            " | " + printIfFilled(row.get(3)) + " " + printIfFilled(row.get(4)) + " " + printIfFilled(row.get(5)) +
                            " | " + printIfFilled(row.get(6)) + " " + printIfFilled(row.get(7)) + " " + printIfFilled(row.get(8)) + " |");
            if (((i + 1) % 3) == 0) {
                System.out.println("  +-------+-------+-------+");
            }
        }
    }


    // stampa un numero solo se diverso da 0 (usato da gid(Sudoku sudoku))
    private static String printIfFilled(int number) {
        return number != 0 ? "" + number : " ";
    }


    // restituisce i tabs (coordinate + lista di candidati) delle caselle vuote del Sudoku (si basa solo sui numeri presenti)
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


    // crea una nuova lista di numeri con cui costruire un sudoku in base alla deduzione (Change) in input
    public static List<Integer> setDeductedNumber(List<Integer> numbersList, Change change) {
        numbersList.set(((9 * (change.getRow() - 1)) + (change.getCol() - 1)), change.getNumber());
        return numbersList;
    }


    // data una House e una lista di tabs restituisce la lista dei tabs appartenenti a quella casa
    public static List<Tab> getHouseTabs(House house, int index, List<Tab> tabs) {
        List<Tab> houseTabs = new ArrayList<>();
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


    // data una lista di candidati e un numero N, restituisce tutte le possibili tuple di N elementi che si possono ottenere con quel set
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


    // NON USATO
//	public static boolean seesCell(tab tab, tab toBeSeen, List<Integer> candidates) {
//		if (containsAtLeastOneCandidate(tab, candidates) && tab != toBeSeen) {
//			return 
//					tab.getBox() == toBeSeen.getBox() ||
//					tab.getRow() == toBeSeen.getRow() ||
//					tab.getCol() == toBeSeen.getCol();
//		}
//		return false;
//	}

//	private static boolean containsAtLeastOneCandidate(tab tab, List<Integer> candidates) {
//		for (Integer i : candidates) {
//			if (tab.getNumbers().contains(i)) {
//				return true;
//			}
//		}
//		return false;
//	}


    // restituisce l'array delle tre righe da 3 elementi del quadrato (da scartare in futuro)
    public static List<List<Integer>> getBoxRows(List<Integer> box) {
        List<List<Integer>> boxRows = new ArrayList<>();
        for (int i : INDEXES_02) {
            boxRows.add(new ArrayList<>(box.subList(3 * i, (3 * i) + 3)));
        }
        return boxRows;
    }


    // restituisce l'array delle tre colonne da 3 elementi del quadrato (da scartare in futuro)
    public static List<List<Integer>> getBoxColumns(List<Integer> box) {
        List<List<Integer>> boxColumns = new ArrayList<>();
        for (int i : INDEXES_02) {
            List<Integer> thirdPartOfColumn = new ArrayList<>();
            for (List<Integer> row : getBoxRows(box)) {
                thirdPartOfColumn.add(row.get(i));
            }
            boxColumns.add(thirdPartOfColumn);
        }
        return boxColumns;
    }


    // restituisce l'array dei quadrati del terzetto righe (indice terzetto tra 0 e 2) (da scartare in futuro)
    public static List<List<Integer>> getRowsTrioBoxes(Sudoku sudoku, int indexRowsTrio) {
        return sudoku.getBoxes().subList(indexRowsTrio * 3, 3 + (indexRowsTrio * 3));
    }


    // restituisce l'array dei quadrati del terzetto colonne (indice terzetto tra 0 e 2) (da scartare in futuro)
    public static List<List<Integer>> getColsTrioBoxes(Sudoku sudoku, int indexRowsTrio) {
        List<List<Integer>> boxesTrio = new ArrayList<>();
        for (int i : INDEXES_02) {
            boxesTrio.add(sudoku.getBoxes().get(indexRowsTrio + (3 * i)));
        }
        return boxesTrio;
    }


    // controlla per ogni quadrato, riga e colonna se presenta numeriRipetuti
    //(bug che incorre col metodo FIFTY FIFTY) rendere un metodo privato della classe ?
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
    //(bug che incorre col metodo FIFTY FIFTY) rendere un metodo privato della classe ?
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


    // per ogni changeLog in uno skimming result, ne stampa una versione semplificata
    public static void printChangeLogs(SkimmingResult result) {
        for (ChangeLog changeLog : result.getChangeLogs()) {
            System.out.println(changeLog.getSolvingTechnique() + ": " + changeLog.getUnitExamined());
            if (changeLog.getHouse() != null) {
                System.out.println("IT'S IN " + getWelcomingUnit(changeLog));
            }
            System.out.println("ITS TABS ARE:");
            for (ChangeLogUnitMember tab : changeLog.getUnitMembers()) {
                System.out.println(tab);
            }
            System.out.println("I THEN DEDUCTED:");
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


    // Dato un ChangeLog, restituisce la stringa corrispondente (usato dagli explainers)
    public static String getWelcomingUnit(ChangeLog changeLog) {
        switch (changeLog.getHouse()) {
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


    // per debuggare // NON USATO
//	public static void printHouseTabs(House house, List<tab> tabs) {
//		List<List<tab>> houseTabs = new ArrayList<>();
//		for (int i = 0; i < 9; i++) {
//			houseTabs.add(new ArrayList<tab>());
//		}
//		for (tab tab : tabs) {
//			switch (house) {
//				case BOX : houseTabs.get(tab.getBox() - 1).add(tab); break;
//				case ROW : houseTabs.get(tab.getRow() - 1).add(tab); break;
//				case COL : houseTabs.get(tab.getCol() - 1).add(tab); break;
//				default : throw new InvalidHouseException();
//			}
//		}
//		for (int i : NUMBERS) {
//			System.out.println("-- TABS " + house + " " + i);
//			for (tab tab : houseTabs.get(i - 1)) {
//				System.out.println(tab);
//			}
//		}
//	}


    // per debuggare: stempa una griglia del sudoku dove ci sono i tabs di ogni casella (se risolta, tab con un solo candidato)
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
                    tab = new Tab(row, col, Arrays.asList(number));
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


    // per debuggare: identico al metodo di sopra, ma sono tabs semplici, senza scremature
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
                    tab = new Tab(row, col, Arrays.asList(number));
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
        String candidates = "";
        for (int candidate : tab.getNumbers()) {
            candidates += candidate;
        }
        while (candidates.length() < 9) {
            candidates += " ";
        }
        return "[" + candidates + "]";
    }

    public static List<Sudoku> read50kSudoku() {
        List<Sudoku> allSudokus = new ArrayList<>();
        try {
            ClassLoader classLoader = Utils.class.getClassLoader();
            URL resource = classLoader.getResource("50kSudoku.txt");
            File sudokuList = new File(resource.toURI());
            Scanner myReader = new Scanner(sudokuList);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Sudoku s = Utils.buildSudoku(data);
                allSudokus.add(s);
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        System.out.println("READ " + allSudokus.size() + " SUDOKUS");
        return allSudokus;
    }
}
