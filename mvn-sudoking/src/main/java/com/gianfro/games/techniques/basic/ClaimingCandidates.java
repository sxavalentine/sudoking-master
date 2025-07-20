package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class ClaimingCandidates {

    /**
     * If in a Sudoku Row or Column all cells with the candidate X belong to the same Box
     * then we can remove that candidate X from all the other cells of that Box
     */

    public static final String CLAIMING_CANDIDATES = "CLAIMING CANDIDATES";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(claimingCandidates(sudoku, House.ROW));
        changeLogs.addAll(claimingCandidates(sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> claimingCandidates(Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        for (int houseNumber : Utils.NUMBERS) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
            for (int number : Utils.NUMBERS) {
                List<SudokuCell> welcomingCells = emptyHouseCells.stream().filter(c -> c.getCandidates().contains(number)).toList();
                Set<Integer> welcomingBoxes = welcomingCells.stream().map(c -> c.getBox()).collect(Collectors.toSet());
                if (welcomingBoxes.size() == 1) {
                    int boxIndex = new ArrayList<>(welcomingBoxes).get(0);
                    List<SudokuCell> emptyBoxCells = Utils.getEmptyHouseCells(sudoku, House.BOX, boxIndex);
                    List<SudokuCell> toBeSkimmed = emptyBoxCells.stream().filter(c -> !welcomingCells.contains(c) && c.getCandidates().contains(number)).toList();
                    if (!toBeSkimmed.isEmpty()) {
                        Set<CellSkimmed> deductions = new HashSet<>();
                        List<Integer> removedCandidates = List.of(number);
                        for (SudokuCell cell : toBeSkimmed) {
                            deductions.add(
                                    CellSkimmed.builder()
                                            .solvingTechnique(CLAIMING_CANDIDATES)
                                            .house(house)
                                            .cell(cell)
                                            .removedCandidates(removedCandidates)
                                            .build());
                        }
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(removedCandidates)
                                .house(house)
                                .houseNumber(houseNumber)
                                .unitMembers(new ArrayList<>(welcomingCells))
                                .solvingTechnique(CLAIMING_CANDIDATES)
                                .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                .build();
                        changeLogs.add(changeLog);
                    }
                }
            }
        }
        return changeLogs;
    }

}