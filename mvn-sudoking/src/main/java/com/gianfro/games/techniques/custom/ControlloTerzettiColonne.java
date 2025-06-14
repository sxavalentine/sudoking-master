package com.gianfro.games.techniques.custom;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class ControlloTerzettiColonne {

    public static Set<ChangeLog> check(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        for (int number : Utils.NUMBERS) {
            for (int colsTrio : Utils.INDEXES_02) {
                List<Integer> welcomingCols = new ArrayList<>(Utils.INDEXES_02);
                List<Integer> welcomingBoxes = new ArrayList<>(Utils.INDEXES_02);
                List<List<Integer>> boxesTrio = Utils.getColsTrioBoxes(sudoku, colsTrio);
                for (int box : Utils.INDEXES_02) {
                    List<List<Integer>> boxCols = Utils.getBoxColumns(boxesTrio.get(box));
                    for (int column : Utils.INDEXES_02) {
                        if (boxCols.get(column).contains(number)) {
                            welcomingCols.remove(Integer.valueOf(column));
                            welcomingBoxes.remove(Integer.valueOf(box));
                        }
                    }
                }
                if (welcomingCols.size() == 1) {
                    List<Integer> welcomingBox = boxesTrio.get(welcomingBoxes.get(0));
                    List<List<Integer>> welcomingBoxCols = Utils.getBoxColumns(welcomingBox);
                    if (Collections.frequency(welcomingBoxCols.get(welcomingCols.get(0)), 0) == 1) {
                        int indexBlankSquare = welcomingBoxCols.get(welcomingCols.get(0)).indexOf(0);
                        Change change = Change.builder()
                                .solvingTechnique("CTC")
                                .house(House.COL)
                                .row(((3 * welcomingBoxes.get(0)) + (indexBlankSquare + 1)))
                                .col(((3 * colsTrio) + (welcomingCols.get(0) + 1)))
                                .number(number)
                                .build();
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(null)
                                .house(null)
                                .houseNumber(0)
                                .unitMembers(List.of())
                                .solvingTechnique("CTC")
                                .solvingTechniqueVariant(null)
                                .changes(List.of(change))
                                .build();
                        changeLogs.add(changeLog);
                        ///
//                    	System.out.println(change);
//                    	Utils.grid(sudoku);
                        ///
                    } else {
                        List<Integer> welcomingBoxRows = new ArrayList<>(Utils.INDEXES_02);
                        int rowsCount = 0;
                        for (int element : welcomingBoxCols.get(welcomingCols.get(0))) {
                            if (element != 0) {
                                welcomingBoxRows.remove(Integer.valueOf(rowsCount));
                            } else {
                                if (sudoku.getRows().get((3 * welcomingBoxes.get(0)) + rowsCount).contains(number)) {
                                    welcomingBoxRows.remove(Integer.valueOf(rowsCount));
                                }
                            }
                            rowsCount++;
                        }
                        if (welcomingBoxRows.size() == 1) {
                            Change change = Change.builder()
                                    .solvingTechnique("CTC")
                                    .house(House.COL)
                                    .row(((3 * welcomingBoxes.get(0)) + welcomingBoxRows.get(0) + 1))
                                    .col((3 * colsTrio + welcomingCols.get(0) + 1))
                                    .number(number)
                                    .build();
                            ChangeLog changeLog = ChangeLog.builder()
                                    .unitExamined(null)
                                    .house(null)
                                    .houseNumber(0)
                                    .unitMembers(List.of())
                                    .solvingTechnique("CTC")
                                    .solvingTechniqueVariant(null)
                                    .changes(List.of(change))
                                    .build();
                            changeLogs.add(changeLog);
//	                    	System.out.println(change);
//	                    	Utils.grid(sudoku);
                        }
                    }
                }
            }
        }
        return new HashSet<>(changeLogs);
    }
}
