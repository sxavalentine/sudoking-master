package com.gianfro.games.solving.techniques.custom;

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
                            welcomingCols.remove(new Integer(column));
                            welcomingBoxes.remove(new Integer(box));
                        }
                    }
                }
                if (welcomingCols.size() == 1) {
                    List<Integer> welcomingBox = boxesTrio.get(welcomingBoxes.get(0));
                    List<List<Integer>> welcomingBoxCols = Utils.getBoxColumns(welcomingBox);
                    if (Collections.frequency(welcomingBoxCols.get(welcomingCols.get(0)), 0) == 1) {
                        int indexBlankSquare = welcomingBoxCols.get(welcomingCols.get(0)).indexOf(0);
                        Change change = new Change(
                                "CTC",
                                House.COL,
                                ((3 * welcomingBoxes.get(0)) + (indexBlankSquare + 1)),
                                ((3 * colsTrio) + (welcomingCols.get(0) + 1)),
                                number);
                        changeLogs.add(new ChangeLog(null, null, 0, Arrays.asList(), "CTC", null, Arrays.asList(change)));
                        ///
//                    	System.out.println(change);
//                    	Utils.grid(sudoku);
                        ///
                    } else {
                        List<Integer> welcomingBoxRows = new ArrayList<>(Utils.INDEXES_02);
                        int rowsCount = 0;
                        for (int element : welcomingBoxCols.get(welcomingCols.get(0))) {
                            if (element != 0) {
                                welcomingBoxRows.remove(new Integer(rowsCount));
                            } else {
                                if (sudoku.getRows().get((3 * welcomingBoxes.get(0)) + rowsCount).contains(number)) {
                                    welcomingBoxRows.remove(new Integer(rowsCount));
                                }
                            }
                            rowsCount++;
                        }
                        if (welcomingBoxRows.size() == 1) {
                            Change change = new Change(
                                    "CTC", House.COL,
                                    ((3 * welcomingBoxes.get(0)) + welcomingBoxRows.get(0) + 1),
                                    (3 * colsTrio + welcomingCols.get(0) + 1),
                                    number);
                            changeLogs.add(new ChangeLog(null, null, 0, Arrays.asList(), "CTC", null, Arrays.asList(change)));
                            ///
//	                    	System.out.println(change);
//	                    	Utils.grid(sudoku);
                            ///
                        }
                    }
                }
            }
        }
        return new HashSet<>(changeLogs);
    }
}
