package com.gianfro.games.scarti;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlloColonneExtreme {

    public static List<Change> check(Sudoku sudoku) {
        List<Change> changes = new ArrayList<>();
        for (int number : Utils.NUMBERS) {
            for (int terzettoColonne : Utils.INDEXES_02) {
                List<Integer> colonneTerzettoOspitali = new ArrayList<>(Utils.INDEXES_02);
                List<Integer> quadratiOspitali = new ArrayList<>(Utils.INDEXES_02);
                List<List<Integer>> quadratiTerzetto = getBoxesTerzettoColonne(sudoku, terzettoColonne);
                for (int quadrato : Utils.INDEXES_02) {
                    List<Integer> quadratoTerzettoEsaminato = sudoku.getBoxes().get(terzettoColonne + (3 * quadrato));
                    List<List<Integer>> colonneQuadrato = Utils.getBoxColumns(quadratoTerzettoEsaminato);
                    List<Integer> colonneQuadratoOspitali = new ArrayList<>(Utils.INDEXES_02);
                    int contoColonne = 0;
                    for (List<Integer> colonna : colonneQuadrato) {
                        if (colonna.contains(number)) {
                            colonneQuadratoOspitali.remove(Integer.valueOf(contoColonne));
                            quadratiOspitali.remove(Integer.valueOf(quadrato));
                            if (colonneTerzettoOspitali.contains(contoColonne)) {
                                colonneTerzettoOspitali.remove(Integer.valueOf(contoColonne));
                            }
                        } else {
                            if (sudoku.getColumns().get((3 * terzettoColonne) + contoColonne).contains(number)) {
                                colonneQuadratoOspitali.remove(Integer.valueOf(contoColonne));
                                if (colonneTerzettoOspitali.contains(contoColonne)) {
                                    colonneTerzettoOspitali.remove(Integer.valueOf(contoColonne));
                                }
                            } else {
                                if (Collections.frequency(colonna, 0) == 0) {
                                    colonneQuadratoOspitali.remove(Integer.valueOf(contoColonne));
                                } else {
                                    List<Integer> elementiOspitali = new ArrayList<>(Utils.INDEXES_02);
                                    int contoElementi = 0;
                                    for (int elemento : colonna) {
                                        if (elemento != 0) {
                                            elementiOspitali.remove(Integer.valueOf(contoElementi));
                                        } else {
                                            if (sudoku.getRows().get((3 * quadrato) + contoElementi).contains(number)) {
                                                elementiOspitali.remove(Integer.valueOf(contoElementi));
                                            }
                                        }
                                        contoElementi++;
                                    }
                                    if (elementiOspitali.isEmpty()) {
                                        colonneQuadratoOspitali.remove(Integer.valueOf(contoColonne));
                                    }
                                }
                            }
                        }
                        contoColonne++;
                    }
                    if (colonneQuadratoOspitali.size() == 1) {
                        if (!quadratoTerzettoEsaminato.contains(number)) {
                            if (colonneTerzettoOspitali.size() > 1) {
                                if (colonneTerzettoOspitali.contains(colonneQuadratoOspitali.get(0))) {
                                    // AGGIUNTA IMPORTANTISSIMA PER EVITARE BUG IN TENTATIVO5050
                                    colonneTerzettoOspitali.remove(colonneQuadratoOspitali.get(0));
                                }
                            }
                            if (quadratiOspitali.contains(quadrato)) {
                                quadratiOspitali.remove(Integer.valueOf(quadrato));
                            }
                        }
                    }
                }
                if (quadratiOspitali.size() == 1) {
                    List<Integer> quadratoLibero = quadratiTerzetto.get(quadratiOspitali.get(0));
                    List<List<Integer>> colonneQuadratoLibero = Utils.getBoxColumns(quadratoLibero);
                    if (Collections.frequency(colonneQuadratoLibero.get(colonneTerzettoOspitali.get(0)), 0) == 1) {
                        int indiceRiga = colonneQuadratoLibero.get(colonneTerzettoOspitali.get(0)).indexOf(0);
                        Change change = Change.builder()
                                .solvingTechnique("CCE")
                                .house(House.COL)
                                .row((3 * quadratiOspitali.get(0)) + (indiceRiga + 1))
                                .col((3 * terzettoColonne) + (colonneTerzettoOspitali.get(0) + 1))
                                .number(number)
                                .build();
                        changes.add(change);
                        ///
//						System.out.println(change);
//						Utils.grid(sudoku);
                        ///
                    } else {
                        List<Integer> righeQuadratoOspitali = new ArrayList<>(Utils.INDEXES_02);
                        int contoRighe = 0;
                        for (int elemento : colonneQuadratoLibero.get(colonneTerzettoOspitali.get(0))) {
                            if (elemento != 0) {
                                righeQuadratoOspitali.remove(Integer.valueOf(contoRighe));
                            } else {
                                if (sudoku.getRows().get((3 * quadratiOspitali.get(0)) + contoRighe).contains(number)) {
                                    righeQuadratoOspitali.remove(Integer.valueOf(contoRighe));
                                }
                            }
                            contoRighe++;
                        }
                        if (righeQuadratoOspitali.size() == 1) {
                            Change change = Change.builder()
                                    .solvingTechnique("CCE")
                                    .house(House.COL)
                                    .row((3 * quadratiOspitali.get(0)) + (righeQuadratoOspitali.get(0) + 1))
                                    .col((3 * terzettoColonne) + (colonneTerzettoOspitali.get(0) + 1))
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

    private static List<List<Integer>> getBoxesTerzettoColonne(Sudoku sudoku, int terzettoColonne) {
        List<List<Integer>> quadratiTerzetto = new ArrayList<>();
        for (int i : Utils.INDEXES_02) {
            quadratiTerzetto.add(sudoku.getBoxes().get(terzettoColonne + (3 * i)));
        }
        return quadratiTerzetto;
    }
}
