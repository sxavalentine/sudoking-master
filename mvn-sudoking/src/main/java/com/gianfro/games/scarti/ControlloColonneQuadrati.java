package com.gianfro.games.scarti;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ControlloColonneQuadrati {

    public static List<Change> check(Sudoku sudoku) {
        List<Change> changes = new ArrayList<>();
        for (int boxNumber = 0; boxNumber < 9; boxNumber++) {
            List<Integer> box = sudoku.getBoxes().get(boxNumber);
            List<Integer> boxNumbers = new ArrayList<>(box);
            boxNumbers.removeAll(Arrays.asList(0));
            List<Integer> missingNumbers = new ArrayList<>();
            for (int number : Utils.NUMBERS) {
                if (!boxNumbers.contains(number)) {
                    missingNumbers.add(number);
                }
            }
            List<List<Integer>> rowsIntersecatingBox = sudoku.getRowTrios().get(boxNumber / 3);
            int variable = boxNumber;
            while (variable >= 3) {
                variable -= 3;
            }
            List<List<Integer>> colsIntersecatingBox = sudoku.getColTrios().get(variable);
            List<List<Integer>> boxRows = Utils.getBoxRows(box);
            List<List<Integer>> boxCols = Utils.getBoxColumns(box);
            for (int number : missingNumbers) {
                List<List<Integer>> welcomingBoxCols = new ArrayList<>();
                List<Integer> colsIndexes = new ArrayList<>();
                int boxColIndex = 0;
                for (List<Integer> column : colsIntersecatingBox) {
                    if (Collections.frequency(boxCols.get(boxColIndex), 0) > 0) {
                        if (!column.contains(number)) {
                            welcomingBoxCols.add(boxRows.get(boxColIndex));
                            colsIndexes.add(boxColIndex);
                        }
                    }
                    boxColIndex += 1;
                }
                if (welcomingBoxCols.size() == 1) {
                    int colIndex = colsIndexes.get(0) + 1;
                    List<List<Integer>> welcomingBoxRows = new ArrayList<>();
                    List<Integer> rowsIndexes = new ArrayList<>();
                    int boxRowIndex = 0;
                    for (List<Integer> row : boxRows) {
                        int indexToBeChecked = colsIndexes.get(0);
                        if (row.get(indexToBeChecked) == 0) {
                            if (!rowsIntersecatingBox.get(boxRowIndex).contains(number)) {
                                welcomingBoxRows.add(boxRows.get(boxRowIndex));
                                rowsIndexes.add(boxRowIndex);
                            }
                        }
                        boxRowIndex += 1;
                    }
                    if (welcomingBoxRows.size() == 1) {
                        int rowIndex = rowsIndexes.get(0) + 1;
                        Change change = new Change(
                                "CCQ", House.COL,
                                rowIndex + (3 * (boxNumber / 3)),
                                colIndex + (3 * variable),
                                number);
                        changes.add(change);
                        ///
//						System.out.println(change);
//						Utils.grid(sudoku);
                        ///
                    }
                }
            }
        }
        return changes;
    }

    public static void main(String[] args) {
        Sudoku sudoku = Utils.buildSudoku("860409213940231680213068904689010430304806100501304860436100598158943726792685341");

        Utils.grid(sudoku);
        List<Tab> tabs = Utils.getBasicTabs(sudoku);

        for (Tab tab : tabs) {
            System.out.println(tab);
        }

        List<Change> changes = ControlloColonneQuadrati.check(sudoku);
        for (Change c : changes) {
            System.out.println(c);
        }
    }
}
