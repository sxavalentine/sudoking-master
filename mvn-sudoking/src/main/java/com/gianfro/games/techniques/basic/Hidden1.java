package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSolved;
import com.gianfro.games.utils.Utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Hidden1 {

    /**
     * If in a Sudoku House (BOX, ROW, COL) there is only a cell with the candidate X
     * then the value of that cell is exactly X
     */

    public static final String HIDDEN_SINGLE = "HIDDEN SINGLE";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new HashSet<>();
        changeLogs.addAll(hiddenSingle(sudoku, House.BOX));
        changeLogs.addAll(hiddenSingle(sudoku, House.ROW));
        changeLogs.addAll(hiddenSingle(sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> hiddenSingle(Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                for (int number : Utils.NUMBERS) {
                    List<SudokuCell> welcomingCells = sudoku.getCells().stream().filter(c ->
                            c.getHouseNumber(house) == houseNumber && c.getCandidates().contains(number)).toList();
                    if (welcomingCells.size() == 1) {
                        SudokuCell cell = welcomingCells.get(0);
                        CellSolved change = CellSolved.builder()
                                .solvingTechnique(HIDDEN_SINGLE)
                                .house(house)
                                .cell(cell)
                                .number(number)
                                .build();
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(null)
                                .house(house)
                                .houseNumber(houseNumber)
                                .unitMembers(List.of(cell))
                                .solvingTechnique(HIDDEN_SINGLE)
                                .changes(List.of(change))
                                .build();
                        changeLogs.add(changeLog);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in HIDDEN SINGLE " + house + ": " + e.getMessage());
        }
        return changeLogs;
    }
}
