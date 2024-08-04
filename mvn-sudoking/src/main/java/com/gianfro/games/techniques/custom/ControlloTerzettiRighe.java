package com.gianfro.games.techniques.custom;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class ControlloTerzettiRighe {

    public static Set<ChangeLog> check(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int number : Utils.NUMBERS) {
                for (int rowsTrio : Utils.INDEXES_02) {
                    List<Integer> welcomingRows = new ArrayList<>(Utils.INDEXES_02);
                    List<Integer> welcomingBoxes = new ArrayList<>(Utils.INDEXES_02);
                    List<List<Integer>> boxesTrio = Utils.getRowsTrioBoxes(sudoku, rowsTrio);
                    for (int box : Utils.INDEXES_02) {
                        List<List<Integer>> boxRows = Utils.getBoxRows(boxesTrio.get(box));
                        for (int row : Utils.INDEXES_02) {
                            if (boxRows.get(row).contains(number)) {
                                welcomingRows.remove(new Integer(row));
                                welcomingBoxes.remove(new Integer(box));
                            }
                        }
                    }
                    if (welcomingRows.size() == 1) {
                        List<Integer> welcomingBox = boxesTrio.get(welcomingBoxes.get(0));
                        List<List<Integer>> welcomingBoxRows = Utils.getBoxRows(welcomingBox);
                        if (Collections.frequency(welcomingBoxRows.get(welcomingRows.get(0)), 0) == 1) {
                            int indexBlankSquare = welcomingBoxRows.get(welcomingRows.get(0)).indexOf(0);
                            Change change = new Change(
                                    "CTR",
                                    House.ROW,
                                    ((3 * rowsTrio) + welcomingRows.get(0) + 1),
                                    ((3 * welcomingBoxes.get(0) + (indexBlankSquare + 1))),
                                    number);
                            changeLogs.add(new ChangeLog(null, null, 0, Arrays.asList(), "CTR", null, Arrays.asList(change)));
                        } else {
                            List<Integer> welcomingBoxCols = new ArrayList<>(Utils.INDEXES_02);
                            int columnCount = 0;
                            for (int element : welcomingBoxRows.get(welcomingRows.get(0))) {
                                if (element != 0) {
                                    welcomingBoxCols.remove(new Integer(columnCount));
                                } else {
                                    if (sudoku.getColumns().get((3 * welcomingBoxes.get(0)) + columnCount).contains(number)) {
                                        welcomingBoxCols.remove(new Integer(columnCount));
                                    }
                                }
                                columnCount++;
                            }
                            if (welcomingBoxCols.size() == 1) {
                                Change change = new Change(
                                        "CTR",
                                        House.ROW,
                                        ((3 * rowsTrio) + welcomingRows.get(0) + 1),
                                        (3 * welcomingBoxes.get(0) + welcomingBoxCols.get(0) + 1),
                                        number);
                                changeLogs.add(new ChangeLog(null, null, 0, Arrays.asList(), "CTR", null, Arrays.asList(change)));
                                ///
                                //                        	System.out.println(change);
                                //                        	Utils.grid(sudoku);
                                ///
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new HashSet<>(changeLogs);
    }

    public static void main(String[] args) {
        Sudoku sudoku = Utils.buildSudoku(SudokuList.EVEREST1);

        Utils.grid(sudoku);
        List<Tab> tabs = Utils.getBasicTabs(sudoku);

        for (Tab tab : tabs) {
            System.out.println(tab);
        }

        Set<ChangeLog> changeLogs = ControlloTerzettiRighe.check(sudoku);
        for (ChangeLog changeLog : changeLogs) {
            for (Change change : changeLog.getChanges()) {
                if (!(change instanceof Skimming)) {
                    System.out.println(change);
                }
            }
        }
    }
}
