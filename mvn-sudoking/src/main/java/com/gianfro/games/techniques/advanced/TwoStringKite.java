package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class TwoStringKite {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String TWO_STRING_KITE = "TWO STRING KITE";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>(findKite(sudoku));
        return changeLogs;
    }

    public static List<ChangeLog> findKite(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        for (int candidate : Utils.NUMBERS) {
            List<List<SudokuCell>> rowsWithTwoWelcomingHouses = new ArrayList<>();
            List<List<SudokuCell>> colsWithTwoWelcomingHouses = new ArrayList<>();
            for (int houseNumber : Utils.NUMBERS) {
                List<SudokuCell> welcomingRowCells = Utils.getEmptyHouseCells(sudoku, House.ROW, houseNumber)
                        .stream().filter(c -> c.getCandidates().contains(candidate)).toList();
                if (welcomingRowCells.size() == 2) {
                    rowsWithTwoWelcomingHouses.add(welcomingRowCells);
                }
                List<SudokuCell> welcomingColCells = Utils.getEmptyHouseCells(sudoku, House.COL, houseNumber)
                        .stream().filter(c -> c.getCandidates().contains(candidate)).toList();
                if (welcomingColCells.size() == 2) {
                    colsWithTwoWelcomingHouses.add(welcomingColCells);
                }
            }
            for (List<SudokuCell> rowCells : rowsWithTwoWelcomingHouses) {
                for (List<SudokuCell> colCells : colsWithTwoWelcomingHouses) {

                    List<SudokuCell> kiteCells;
                    List<SudokuCell> baseCells;

                    if (rowCells.get(0).canSeeOther(colCells.get(0))) {
                        kiteCells = List.of(rowCells.get(0), colCells.get(0));
                        baseCells = List.of(rowCells.get(1), colCells.get(1));
                    } else if (rowCells.get(0).canSeeOther(colCells.get(1))) {
                        kiteCells = List.of(rowCells.get(0), colCells.get(1));
                        baseCells = List.of(rowCells.get(1), colCells.get(0));
                    } else if (rowCells.get(1).canSeeOther(colCells.get(0))) {
                        kiteCells = List.of(rowCells.get(1), colCells.get(0));
                        baseCells = List.of(rowCells.get(0), colCells.get(1));
                    } else if (rowCells.get(1).canSeeOther(colCells.get(1))) {
                        kiteCells = List.of(rowCells.get(1), colCells.get(1));
                        baseCells = List.of(rowCells.get(0), colCells.get(0));
                    } else {
                        kiteCells = null;
                        baseCells = null;
                    }

                    if (kiteCells != null) {
                        List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                        c -> c.getCandidates().contains(candidate) &&
                                                c.canSeeOther(baseCells.get(0)) &&
                                                c.canSeeOther(baseCells.get(1)) &&
                                                !kiteCells.contains(c))
                                .toList();

                        if (!toBeSkimmed.isEmpty()) {
                            Set<CellSkimmed> deductions = new HashSet<>();
                            List<Integer> removedCandidates = List.of(candidate);
                            for (SudokuCell cell : toBeSkimmed) {
                                deductions.add(
                                        CellSkimmed.builder()
                                                .solvingTechnique(TWO_STRING_KITE)
                                                .cell(cell)
                                                .removedCandidates(removedCandidates)
                                                .build());
                            }
                            // The unitMembers are ordered as such: ROW.BASE1, COL.BASE 1, ROW.KITE1, ROW.KITE2
                            List<ChangeLogUnitMember> unitMembers = List.of(baseCells.get(0), baseCells.get(1), kiteCells.get(0), kiteCells.get(1));
                            ChangeLog changeLog = ChangeLog.builder()
                                    .unitExamined(removedCandidates)
                                    .unitMembers(unitMembers)
                                    .solvingTechnique(TWO_STRING_KITE)
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
}
