package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSolved;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Naked1 {

    /**
     * If in a Sudoku there is a cell with only a candidate left
     * then that candidate is the value of the cell
     */

    public static final String NAKED_SINGLE = "NAKED SINGLE";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        try {
            for (SudokuCell cell : sudoku.getCells()) {
                if (cell.getCandidates().size() == 1) {
                    CellSolved change = CellSolved.builder()
                            .solvingTechnique(NAKED_SINGLE)
                            .house(null)
                            .cell(cell)
                            .number(cell.getCandidates().get(0))
                            .build();
                    ChangeLog changeLog = ChangeLog.builder()
                            .unitExamined(List.of(cell.getCandidates().get(0)))
                            .house(null)
                            .houseNumber(0)
                            .unitMembers(List.of(cell))
                            .solvingTechnique(NAKED_SINGLE)
                            .changes(List.of(change))
                            .build();
                    changeLogs.add(changeLog);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in NAKED SINGLE: " + e.getMessage());
        }
        return changeLogs;
    }
}
