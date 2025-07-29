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

public class HiddenTuple {

    /**
     * If there are N cells within a house such as that N candidates appear nowhere outside those cells in that house,
     * those N candidates must be placed in those N cells. All other candidates can therefore be eliminated.
     * NOTE:
     */

    public static final String HIDDEN_PAIR = "HIDDEN PAIR";
    public static final String HIDDEN_TRIPLE = "HIDDEN TRIPLE";
    public static final String HIDDEN_QUAD = "HIDDEN QUAD";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();

        changeLogs.addAll(checkHidden(HIDDEN_PAIR, sudoku, House.BOX));
        changeLogs.addAll(checkHidden(HIDDEN_PAIR, sudoku, House.ROW));
        changeLogs.addAll(checkHidden(HIDDEN_PAIR, sudoku, House.COL));

        changeLogs.addAll(checkHidden(HIDDEN_TRIPLE, sudoku, House.BOX));
        changeLogs.addAll(checkHidden(HIDDEN_TRIPLE, sudoku, House.ROW));
        changeLogs.addAll(checkHidden(HIDDEN_TRIPLE, sudoku, House.COL));

        changeLogs.addAll(checkHidden(HIDDEN_QUAD, sudoku, House.BOX));
        changeLogs.addAll(checkHidden(HIDDEN_QUAD, sudoku, House.ROW));
        changeLogs.addAll(checkHidden(HIDDEN_QUAD, sudoku, House.COL));

        return changeLogs;
    }

    private static List<ChangeLog> checkHidden(String technique, Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        int size = getSize(technique);
        for (int houseNumber = 1; houseNumber < 10; houseNumber++) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
            if (emptyHouseCells.size() >= size) {
                Set<Integer> emptyHouseCandidates = emptyHouseCells.stream().flatMap(c -> c.getCandidates().stream()).collect(Collectors.toSet());
                List<List<Integer>> candidateTuples = Generator.combination(emptyHouseCandidates).simple(size).stream().toList();
                for (List<Integer> tuple : candidateTuples) {
                    List<SudokuCell> welcomingCells = emptyHouseCells.stream().filter(c -> cellHasSomeCandidatesInCommon(c, tuple)).toList();
                    if (welcomingCells.size() == size) {
                        Set<CellSkimmed> deductions = new HashSet<>();
                        for (SudokuCell cell : welcomingCells) {
                            List<Integer> removedCandidates = cell.getCandidates().stream().filter(c -> !tuple.contains(c)).toList();
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
                                    .unitExamined(tuple.stream().sorted().toList())
                                    .house(house)
                                    .houseNumber(houseNumber)
                                    .unitMembers(new ArrayList<>(welcomingCells))
                                    .solvingTechnique(technique)
                                    .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                    .build();
                            changeLogs.add(changeLog);
                        }
                    }
                }
            }
        }
        return changeLogs;
    }

    private static int getSize(String technique) {
        return switch (technique) {
            case HIDDEN_PAIR -> 2;
            case HIDDEN_TRIPLE -> 3;
            case HIDDEN_QUAD -> 4;
            default -> throw new IllegalArgumentException("Invalid technique name: " + technique);
        };
    }

    private static boolean cellHasSomeCandidatesInCommon(SudokuCell cell, List<Integer> tuple) {
        for (Integer candidate : tuple) {
            if (cell.getCandidates().contains(candidate)) {
                return true;
            }
        }
        return false;
    }
}
