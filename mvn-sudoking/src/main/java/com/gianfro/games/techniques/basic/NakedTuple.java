package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.stream.Collectors;

public class NakedTuple {

    /**
     * If in a Sudoku House (BOX, ROW, COL) there is a group of exactly N cells containing only a certain tuple of N candidates
     * then we can remove those candidates from all other cells of that House.
     * NOTE: the N cells forming the tuple don't necessarily need to include ALL the N candidates
     */

    public static final String NAKED_PAIR = "NAKED PAIR";
    public static final String NAKED_TRIPLE = "NAKED TRIPLE";
    public static final String NAKED_QUAD = "NAKED QUAD";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();

        changeLogs.addAll(checkNaked(NAKED_PAIR, sudoku, House.BOX));
        changeLogs.addAll(checkNaked(NAKED_PAIR, sudoku, House.ROW));
        changeLogs.addAll(checkNaked(NAKED_PAIR, sudoku, House.COL));

        changeLogs.addAll(checkNaked(NAKED_TRIPLE, sudoku, House.BOX));
        changeLogs.addAll(checkNaked(NAKED_TRIPLE, sudoku, House.ROW));
        changeLogs.addAll(checkNaked(NAKED_TRIPLE, sudoku, House.COL));

        changeLogs.addAll(checkNaked(NAKED_QUAD, sudoku, House.BOX));
        changeLogs.addAll(checkNaked(NAKED_QUAD, sudoku, House.ROW));
        changeLogs.addAll(checkNaked(NAKED_QUAD, sudoku, House.COL));

        return changeLogs;
    }

    private static List<ChangeLog> checkNaked(String technique, Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        int size = getSize(technique);
        for (int houseNumber = 1; houseNumber < 10; houseNumber++) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
            if (emptyHouseCells.size() >= size) {
                List<List<SudokuCell>> tuples = findAllCellTuples(emptyHouseCells, size);
                for (List<SudokuCell> tuple : tuples) {
                    Set<Integer> tupleCandidates = tuple.stream().flatMap(t -> t.getCandidates().stream()).collect(Collectors.toSet());
                    List<SudokuCell> cellsToSkim = emptyHouseCells.stream().filter(c -> !tuple.contains(c)).toList();
                    Set<CellSkimmed> deductions = new HashSet<>();
                    for (SudokuCell cell : cellsToSkim) {
                        List<Integer> removedCandidates = cell.getCandidates().stream().filter(x -> tupleCandidates.contains(x)).toList();
                        if (!removedCandidates.isEmpty()) {
                            deductions.add(
                                    CellSkimmed.builder()
                                            .solvingTechnique(technique)
                                            .house(house)
                                            .cell(cell)
                                            .removedCandidates(removedCandidates)
                                            .build());
                        }
                    }
                    if (!deductions.isEmpty()) {
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(tupleCandidates.stream().sorted().toList())
                                .house(house)
                                .houseNumber(houseNumber)
                                .unitMembers(new ArrayList<>(tuple))
                                .solvingTechnique(technique)
                                .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                .build();
                        changeLogs.add(changeLog);
                    }
                }
            }
        }
        return changeLogs;
    }

    private static int getSize(String technique) {
        return switch (technique) {
            case NAKED_PAIR -> 2;
            case NAKED_TRIPLE -> 3;
            case NAKED_QUAD -> 4;
            default -> throw new IllegalArgumentException("Invalid technique name: " + technique);
        };
    }

    /**
     * @param cells: the list of SudokuCell (with value 0) from a given House
     * @param size:  the size the tuples must have
     * @return : The list of tuples with N cells
     */
    private static List<List<SudokuCell>> findAllCellTuples(List<SudokuCell> cells, int size) {
        List<List<SudokuCell>> tuples = new ArrayList<>();
        List<SudokuCell> cellsWithAtMostNCandidates = cells.stream().filter(c -> c.getCandidates().size() <= size).toList();
        if (cellsWithAtMostNCandidates.size() >= size) {
            // calculates all the tuples of size N that can be made with cellsWithAtMostNCandidates
            List<List<SudokuCell>> tuplesToCheck = Generator.combination(cellsWithAtMostNCandidates).simple(size).stream().toList();
            for (List<SudokuCell> t : tuplesToCheck) {
                Set<Integer> tupleCandidates = t.stream().flatMap(x -> x.getCandidates().stream()).collect(Collectors.toSet());
                if (tupleCandidates.size() == size) {
                    // sorts the elements of the tuple according to their index
                    tuples.add(t.stream().sorted(Comparator.comparingInt(SudokuCell::getIndex)).toList());
                }
            }
        }
        return tuples;
    }
}
