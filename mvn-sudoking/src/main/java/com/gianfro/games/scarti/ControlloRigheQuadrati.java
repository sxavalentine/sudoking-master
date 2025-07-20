package com.gianfro.games.scarti;

public class ControlloRigheQuadrati {

//    public static List<CellSolved> check(Sudoku sudoku) {
//        List<CellSolved> changes = new LinkedList<>();
//        for (int boxNumber = 0; boxNumber < 9; boxNumber++) {
//            List<Integer> box = sudoku.getBoxes().get(boxNumber);
//            List<Integer> boxNumbers = new ArrayList<>(box);
//            boxNumbers.removeAll(List.of(0));
//            List<Integer> missingNumbers = new LinkedList<>();
//            for (int number : Utils.NUMBERS) {
//                if (!boxNumbers.contains(number)) {
//                    missingNumbers.add(number);
//                }
//            }
//            List<List<Integer>> rowsIntersecatingBox = sudoku.getRowTrios().get(boxNumber / 3);
//            int variable = boxNumber;
//            while (variable >= 3) {
//                variable -= 3;
//            }
//            List<List<Integer>> colsIntersecatingBox = sudoku.getColTrios().get(variable);
//            List<List<Integer>> boxRows = Utils.getBoxRows(box);
//            List<List<Integer>> boxCols = Utils.getBoxColumns(box);
//            for (int number : missingNumbers) {
//                List<List<Integer>> welcomingBoxRows = new LinkedList<>();
//                List<Integer> rowsIndexes = new LinkedList<>();
//                int boxRowIndex = 0;
//                for (List<Integer> row : rowsIntersecatingBox) {
//                    if (Collections.frequency(boxRows.get(boxRowIndex), 0) > 0) {
//                        if (!row.contains(number)) {
//                            welcomingBoxRows.add(boxRows.get(boxRowIndex));
//                            rowsIndexes.add(boxRowIndex);
//                        }
//                    }
//                    boxRowIndex += 1;
//                }
//                if (welcomingBoxRows.size() == 1) {
//                    int rowIndex = rowsIndexes.get(0) + 1;
//                    List<List<Integer>> welcomingBoxColumns = new LinkedList<>();
//                    List<Integer> columnsIndexes = new LinkedList<>();
//                    int boxColIndex = 0;
//                    for (List<Integer> column : boxCols) {
//                        int indexToBeChecked = rowsIndexes.get(0);
//                        if (column.get(indexToBeChecked) == 0) {
//                            if (!colsIntersecatingBox.get(boxColIndex).contains(number)) {
//                                welcomingBoxColumns.add(boxCols.get(boxColIndex));
//                                columnsIndexes.add(boxColIndex);
//                            }
//                        }
//                        boxColIndex += 1;
//                    }
//                    if (welcomingBoxColumns.size() == 1) {
//                        int columnIndex = columnsIndexes.get(0) + 1;
//                        CellSolved change = CellSolved.builder()
//                                .solvingTechnique("CRQ")
//                                .house(House.ROW)
//                                .row(rowIndex + (3 * (boxNumber / 3)))
//                                .col(columnIndex + (3 * variable))
//                                .number(number)
//                                .build();
//                        changes.add(change);
//                    }
//                }
//            }
//        }
//        return changes;
//    }
}
