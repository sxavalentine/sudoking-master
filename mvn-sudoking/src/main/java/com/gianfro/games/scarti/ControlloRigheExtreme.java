package com.gianfro.games.scarti;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlloRigheExtreme {

    public static List<Change> check(Sudoku sudoku) {
        List<Change> changes = new ArrayList<>();
        for (int number : Utils.NUMBERS) {
            for (int indexRowsTrio : Utils.INDEXES_02) {
                List<Integer> welcomingRows = new ArrayList<>(Utils.INDEXES_02);
                List<Integer> welcomingBoxes = new ArrayList<>(Utils.INDEXES_02);
                List<List<Integer>> boxesTrio = Utils.getRowsTrioBoxes(sudoku, indexRowsTrio);
                for (int boxNumber : Utils.INDEXES_02) {
                    List<Integer> box = sudoku.getBoxes().get(boxNumber + (3 * indexRowsTrio));
                    List<List<Integer>> boxRows = Utils.getBoxRows(box);
                    List<Integer> welcomingBoxRows = new ArrayList<>(Utils.INDEXES_02);
                    int rowsCount = 0;
                    for (List<Integer> row : boxRows) {
                        if (row.contains(number)) {
                            welcomingBoxRows.remove(Integer.valueOf(rowsCount));
                            welcomingBoxes.remove(Integer.valueOf(boxNumber));
                            if (welcomingRows.contains(rowsCount)) {
                                welcomingRows.remove(Integer.valueOf(rowsCount));
                            }
                        } else {
                            if (sudoku.getRows().get((3 * indexRowsTrio) + rowsCount).contains(number)) {
                                welcomingBoxRows.remove(Integer.valueOf(rowsCount));
                                if (welcomingRows.contains(rowsCount)) {
                                    welcomingRows.remove(Integer.valueOf(rowsCount));
                                }
                            } else {
                                if (Collections.frequency(row, 0) == 0) {
                                    welcomingBoxRows.remove(Integer.valueOf(rowsCount));
                                } else {
                                    List<Integer> welcomingHouses = new ArrayList<>(Utils.INDEXES_02);
                                    int housesCount = 0;
                                    for (int square : row) {
                                        if (square != 0) {
                                            welcomingHouses.remove(Integer.valueOf(housesCount));
                                        } else {
                                            if (sudoku.getColumns().get((3 * boxNumber) + housesCount).contains(number)) {
                                                welcomingHouses.remove(Integer.valueOf(housesCount));
                                            }
                                        }
                                        housesCount++;
                                    }
                                    if (welcomingHouses.isEmpty()) {
                                        welcomingBoxRows.remove(Integer.valueOf(rowsCount));
                                    }
                                }
                            }
                        }
                        rowsCount++;
                    }
                    if (welcomingBoxRows.size() == 1) {
                        if (!box.contains(number)) {
                            if (welcomingRows.size() > 1) {
                                if (welcomingRows.contains(welcomingBoxRows.get(0))) {
                                    // AGGIUNTA IMPORTANTISSIMA PER EVITARE BUG IN TENTATIVO5050
                                    welcomingRows.remove(welcomingBoxRows.get(0));
                                }
                            }
                            if (welcomingBoxes.contains(boxNumber)) {
                                welcomingBoxes.remove(Integer.valueOf(boxNumber));
                            }
                        }
                    }
                }
                if (welcomingBoxes.size() == 1) {
                    List<Integer> quadratoLibero = boxesTrio.get(welcomingBoxes.get(0));
                    List<List<Integer>> righeQuadratoLibero = Utils.getBoxRows(quadratoLibero);
                    if (Collections.frequency(righeQuadratoLibero.get(welcomingRows.get(0)), 0) == 1) {
                        int indiceColonna = righeQuadratoLibero.get(welcomingRows.get(0)).indexOf(0);
                        Change change = Change.builder()
                                .solvingTechnique("CRE")
                                .house(House.ROW)
                                .row((3 * indexRowsTrio) + (welcomingRows.get(0) + 1))
                                .col((3 * welcomingBoxes.get(0)) + (indiceColonna + 1))
                                .number(number)
                                .build();
                        changes.add(change);
                        ///
//						System.out.println(change);
//						Utils.grid(sudoku);
                        ///
                    } else {
                        List<Integer> colonneQuadratoOspitali = new ArrayList<>(Utils.INDEXES_02);
                        int contoColonne = 0;
                        for (int elemento : righeQuadratoLibero.get(welcomingRows.get(0))) {
                            if (elemento != 0) {
                                colonneQuadratoOspitali.remove(Integer.valueOf(contoColonne));
                            } else {
                                if (sudoku.getColumns().get(contoColonne + (3 * welcomingBoxes.get(0))).contains(number)) {
                                    colonneQuadratoOspitali.remove(Integer.valueOf(contoColonne));
                                }
                            }
                            contoColonne++;
                        }
                        if (colonneQuadratoOspitali.size() == 1) {
                            Change change = Change.builder()
                                    .solvingTechnique("CRE")
                                    .house(House.ROW)
                                    .row((welcomingRows.get(0) + 1) + (3 * indexRowsTrio))
                                    .col((colonneQuadratoOspitali.get(0) + 1) + (3 * welcomingBoxes.get(0)))
                                    .number(number)
                                    .build();
                            changes.add(change);
                            ///
//							System.out.println(change);
//							Utils.grid(sudoku);
                            ///
                        }
                    }
                }
            }
        }
        return changes;
    }
}
