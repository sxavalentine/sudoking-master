package com.gianfro.games.techniques.custom;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.*;

public class FiftyFifty {

    public static final String FIFTY_FIFTY = "FIFTY FIFTY";

    public static SolutionStep check(Sudoku sudoku, List<Tab> tabs, int cyclesCount) {

        List<ChangeLog> changeLogs = new LinkedList<>();
        List<Tab> bivalueCells = tabs.stream().filter(tab -> tab.getCandidates().size() == 2).toList();
        Tab bivalueCell;
        try {
            int index = 0; // TODO in futuro si potrebbe fare che prova tutte le possibili caselle 5050 se la prima (come ora, con l'indice fisso a 0) non porta a risultati
            bivalueCell = bivalueCells.get(index);
        } catch (IndexOutOfBoundsException ex) {
            NoFiftyFiftyException nffe = new NoFiftyFiftyException(sudoku, tabs);
            nffe.getMessage();
            throw nffe;
        }
        int speculationRow = bivalueCell.getRow();
        int speculationCol = bivalueCell.getCol();
        int optionA = bivalueCell.getCandidates().get(0);
        int optionB = bivalueCell.getCandidates().get(1);

        List<Integer> hypotheticalSequenceNumbers = new ArrayList<>(sudoku.getNumbers());
        hypotheticalSequenceNumbers.set(((9 * (speculationRow - 1)) + (speculationCol - 1)), optionA);

        AttemptResult attemptResult = attempt5050(optionA, hypotheticalSequenceNumbers, speculationRow, speculationCol);

        if (attemptResult.change != null) {
            Change fiftyFiftyChange = Change.builder()
                    .solvingTechnique(FIFTY_FIFTY)
                    .house(null)
                    .row(speculationRow)
                    .col(speculationCol)
                    .number(optionA)
                    .build();
            ChangeLog log = ChangeLog.builder()
                    .unitExamined(null)
                    .house(null)
                    .houseNumber(0)
                    .unitMembers(null)
                    .solvingTechnique(FIFTY_FIFTY)
                    .solvingTechniqueVariant(null)
                    .changes(List.of(fiftyFiftyChange))
                    .build();
            changeLogs.add(log);
            ///
//			System.out.println("------------------------------------------------------------------------------------------------------------------");
//			System.out.println("SIAMO AL CICLO NUMERO " + cyclesCount);
//			System.out.println("PROCEDENDO CON LA RISOLUZIONE HO FINITO PER FINIRE IL SUDOKU");
//			System.out.println("PERTANTO HO DEDOTTO --> " + fiftyFiftyChange);
//			Utils.grid(sudoku);
            ///
        } else if (attemptResult.bugs != null) {
            if (cyclesCount == 1) {
                Change fiftyFiftyChange = Change.builder()
                        .solvingTechnique(FIFTY_FIFTY)
                        .house(null)
                        .row(speculationRow)
                        .col(speculationCol)
                        .number(optionB)
                        .build();
                ChangeLog log = ChangeLog.builder()
                        .unitExamined(null)
                        .house(null)
                        .houseNumber(0)
                        .unitMembers(null)
                        .solvingTechnique(FIFTY_FIFTY)
                        .solvingTechniqueVariant(null)
                        .changes(List.of(fiftyFiftyChange))
                        .build();
                changeLogs.add(log);
                ///
//				System.out.println("------------------------------------------------------------------------------------------------------------------");
//				System.out.println("SIAMO AL CICLO NUMERO 1");
//				System.out.println("PROCEDENDO CON LA RISOLUZIONE HO TROVATO I SEGUENTI ERRORI:");
//				for (String bug : attemptResult.bugs) {
//					System.out.println(bug);
//				}
//				System.out.println("PERTANTO HO DEDOTTO --> " + fiftyFiftyChange);
//				Utils.grid(sudoku);
                ///

            } else {
                List<Integer> hypotheticalSequenceNumbers2 = new ArrayList<>(sudoku.getNumbers());
                hypotheticalSequenceNumbers2.set(((9 * (speculationRow - 1)) + (speculationCol - 1)), optionB);
                AttemptResult attempt2 = attempt5050(optionB, hypotheticalSequenceNumbers2, speculationRow, speculationCol);

                if (attempt2.change != null) {
                    Change fiftyFiftyChange = Change.builder()
                            .solvingTechnique(FIFTY_FIFTY)
                            .house(null)
                            .row(speculationRow)
                            .col(speculationCol)
                            .number(optionB)
                            .build();
                    ChangeLog log = ChangeLog.builder()
                            .unitExamined(null)
                            .house(null)
                            .houseNumber(0)
                            .unitMembers(null)
                            .solvingTechnique(FIFTY_FIFTY)
                            .solvingTechniqueVariant(null)
                            .changes(List.of(fiftyFiftyChange))
                            .build();
                    changeLogs.add(log);
                    ///
//					System.out.println("------------------------------------------------------------------------------------------------------------------");
//					System.out.println("SIAMO AL CICLO NUMERO " + cyclesCount);
//					System.out.println("PROCEDENDO CON LA RISOLUZIONE HO FINITO PER FINIRE IL SUDOKU");
//					System.out.println("PERTANTO HO DEDOTTO --> " + fiftyFiftyChange);
//					Utils.grid(sudoku);
                    ///
                } else if (attempt2.bugs != null) {
                    changeLogs.add(null);
                    ///
//					System.out.println("------------------------------------------------------------------------------------------------------------------");
//					System.out.println("SIAMO AL CICLO NUMERO " + cyclesCount);
//					System.out.println("PROCEDENDO CON LA RISOLUZIONE CON L'OPZIONE B " + optionB + " HO TROVATO I SEGUENTI ERRORI:");
//					for (String bug : attemptResult.bugs) {
//						System.out.println(bug);
//					}
//					System.out.println("PERTANTO L'ERRORE DEV'ESSERE STATO FATTO CON UNA SUPPOSIZIONE PRECEDENTE");
//					Utils.grid(sudoku);
                    ///
                } else {
                    ///
//					System.out.println("------------------------------------------------------------------------------------------------------------------");
//					System.out.println("SIAMO AL CICLO NUMERO " + cyclesCount);
//					System.out.println("ANCHE CON L'OPZIONE B " + optionB + " NELLA CASELLA " + speculationRow + ", " + speculationCol + " NON HA PORTATO A UNA MAZZA");
//					System.out.println("QUESTA ERA LA GRIGLIA CHE SI ERA VENUTA A FORMARE");
//					Utils.grid(furthestPoint);
//					System.out.println("PERTANTO ORA RI-APPLICO UN FIFTY-FIFTY ALL'INTERNO DEL FIFTY-FIFTY");
                    ///
                    cyclesCount++;
                    Sudoku furthestPoint = attempt2.sudokuDuringAttempt;
                    List<Tab> basicTabs = Utils.getBasicTabs(furthestPoint);
                    List<Tab> tabsFurthestPoint = SudokuSolver.useStandardSolvingTechniques(furthestPoint, basicTabs).getTabs();
                    SolutionStep fiftyFiftyResults = check(furthestPoint, tabsFurthestPoint, cyclesCount);
                    List<ChangeLog> recursiveResults = new ArrayList<>(fiftyFiftyResults.getChangeLogs());

                    if (!recursiveResults.contains(null)) {
                        changeLogs.addAll(recursiveResults);
                        Change fiftyFiftyChange = Change.builder()
                                .solvingTechnique(FIFTY_FIFTY)
                                .house(null)
                                .row(speculationRow)
                                .col(speculationCol)
                                .number(optionB)
                                .build();
                        ChangeLog log = ChangeLog.builder()
                                .unitExamined(null)
                                .house(null)
                                .houseNumber(0)
                                .unitMembers(null)
                                .solvingTechnique(FIFTY_FIFTY)
                                .solvingTechniqueVariant(null)
                                .changes(List.of(fiftyFiftyChange))
                                .build();
                        changeLogs.add(log);
                    } else {
                        changeLogs.add(null);
                    }
                }
            }
        } else {
            Sudoku furthestPoint = attemptResult.sudokuDuringAttempt;
            ///
//			System.out.println("------------------------------------------------------------------------------------------------------------------");
//			System.out.println("SIAMO AL CICLO NUMERO " + cyclesCount);
//			System.out.println("NON HO RISOLTO UNA MAZZA");
//			System.out.println("PER ORA IL MIO TENTATIVO ERA STATO INSERIRE " + optionA + " NELLA CASELLA " + speculationRow + ", " + speculationCol);
//			System.out.println("QUESTA ERA LA GRIGLIA CHE SI ERA VENUTA A FORMARE");
//			Utils.grid(furthestPoint);
//			System.out.println("PERTANTO ORA RI-APPLICO UN FIFTY-FIFTY ALL'INTERNO DEL FIFTY-FIFTY");
            ///
            cyclesCount++;
            List<Tab> basicTabs = Utils.getBasicTabs(furthestPoint);
            List<Tab> tabsFurthestPoint = SudokuSolver.useStandardSolvingTechniques(furthestPoint, basicTabs).getTabs();
            SolutionStep fiftyFiftyResults = check(furthestPoint, tabsFurthestPoint, cyclesCount);

            List<ChangeLog> recursiveResults = new ArrayList<>(fiftyFiftyResults.getChangeLogs());
            if (!recursiveResults.contains(null)) {
                changeLogs.addAll(recursiveResults);
                Change fiftyFiftyChange = Change.builder()
                        .solvingTechnique(FIFTY_FIFTY)
                        .house(null)
                        .row(speculationRow)
                        .col(speculationCol)
                        .number(optionA)
                        .build();
                ChangeLog log = ChangeLog.builder()
                        .unitExamined(null)
                        .house(null)
                        .houseNumber(0)
                        .unitMembers(null)
                        .solvingTechnique(FIFTY_FIFTY)
                        .solvingTechniqueVariant(null)
                        .changes(List.of(fiftyFiftyChange))
                        .build();
                changeLogs.add(log);
            } else {
                if (cyclesCount == 2) {
                    Change fiftyFiftyChange = Change.builder()
                            .solvingTechnique(FIFTY_FIFTY)
                            .house(null)
                            .row(speculationRow)
                            .col(speculationCol)
                            .number(optionB)
                            .build();
                    ChangeLog log = ChangeLog.builder()
                            .unitExamined(null)
                            .house(null)
                            .houseNumber(0)
                            .unitMembers(null)
                            .solvingTechnique(FIFTY_FIFTY)
                            .solvingTechniqueVariant(null)
                            .changes(List.of(fiftyFiftyChange))
                            .build();
                    changeLogs.add(log);
                } else {
                    List<Integer> hypotheticalSequenceNumbers2 = new ArrayList<>(sudoku.getNumbers());
                    Change fiftyFiftyChange = Change.builder()
                            .solvingTechnique(FIFTY_FIFTY)
                            .house(null)
                            .row(speculationRow)
                            .col(speculationCol)
                            .number(optionB)
                            .build();
                    hypotheticalSequenceNumbers2 = Utils.setDeductedNumber(hypotheticalSequenceNumbers2, fiftyFiftyChange);

                    AttemptResult attempt2 = attempt5050(optionB, hypotheticalSequenceNumbers2, speculationRow, speculationCol);

                    if (attempt2.change != null) {
                        Change fiftyFiftyChange2 = Change.builder()
                                .solvingTechnique(FIFTY_FIFTY)
                                .house(null)
                                .row(speculationRow)
                                .col(speculationCol)
                                .number(optionB)
                                .build();
                        ChangeLog log = ChangeLog.builder()
                                .unitExamined(null)
                                .house(null)
                                .houseNumber(0)
                                .unitMembers(null)
                                .solvingTechnique(FIFTY_FIFTY)
                                .solvingTechniqueVariant(null)
                                .changes(List.of(fiftyFiftyChange2))
                                .build();
                        changeLogs.add(log);
                    } else if (attempt2.bugs != null) {
                        changeLogs.add(null);
                    } else {
                        cyclesCount++;
                        Sudoku furthestPoint2 = attempt2.sudokuDuringAttempt;
                        List<Tab> basicTabs2 = Utils.getBasicTabs(furthestPoint);
                        List<Tab> tabsFurthestPoint2 = SudokuSolver.useStandardSolvingTechniques(furthestPoint2, basicTabs2).getTabs();
                        SolutionStep fiftyFiftyResult2 = check(furthestPoint2, tabsFurthestPoint2, cyclesCount);
                        List<ChangeLog> recursiveResults2 = new ArrayList<>(fiftyFiftyResult2.getChangeLogs());

                        if (!recursiveResults2.contains(null)) {
                            changeLogs.addAll(recursiveResults2);
                            Change fiftyFiftyChange2 = Change.builder()
                                    .solvingTechnique(FIFTY_FIFTY)
                                    .house(null)
                                    .row(speculationRow)
                                    .col(speculationCol)
                                    .number(optionB)
                                    .build();
                            ChangeLog log = ChangeLog.builder()
                                    .unitExamined(null)
                                    .house(null)
                                    .houseNumber(0)
                                    .unitMembers(null)
                                    .solvingTechnique(FIFTY_FIFTY)
                                    .solvingTechniqueVariant(null)
                                    .changes(List.of(fiftyFiftyChange2))
                                    .build();
                            changeLogs.add(log);
                        } else {
                            changeLogs.add(null);
                        }
                    }
                }
            }
        }
        return new SolutionStep(sudoku, changeLogs, tabs);
    }

    private static AttemptResult attempt5050(int option, List<Integer> hypotheticalSequenceNumbers, int speculationRow, int speculationCol) {

        Sudoku attempt = new Sudoku(hypotheticalSequenceNumbers);
        List<Tab> basicTabs = Utils.getBasicTabs(attempt);
        SolutionStep standardTechniquesOnly = SudokuSolver.useStandardSolvingTechniques(attempt, basicTabs);
        List<Tab> tabs = standardTechniquesOnly.getTabs();

        AttemptResult attemptResult = new AttemptResult(attempt);

        Set<String> bugs = Utils.checkForBugs(attempt);
        bugs.addAll(Utils.checkForEmptySquaresWithNoCandidates(attempt, tabs));

        if (!bugs.isEmpty()) {
            attemptResult.setBugs(bugs);
//			System.out.println("I FOUND THE FOLLOWING ERRORS");
//			Utils.grid(attempt);
//			for (String bug : bugs) {
//				System.out.println(bug);
//			}
        } else {
            if (Collections.frequency(attempt.getNumbers(), 0) == 0) {
                Change fiftyFiftyChange = Change.builder()
                        .solvingTechnique(FIFTY_FIFTY)
                        .house(null)
                        .row(speculationRow)
                        .col(speculationCol)
                        .number(option)
                        .build();
                attemptResult.setChange(fiftyFiftyChange);
            } else {

                List<ChangeLog> changeLogs = new LinkedList<>(standardTechniquesOnly.getChangeLogs());

                int deductionsCount = 0;
                for (ChangeLog changeLog : changeLogs) {
                    for (Change change : changeLog.getChanges()) {
                        if (!(change instanceof Skimming)) {
                            deductionsCount++;
                        }
                    }
                }

                if (deductionsCount == 0) {
                    deductionsCount += changeLogs.addAll(ControlloTerzettiRighe.check(attempt)) ? 1 : 0;
                    deductionsCount += changeLogs.addAll(ControlloTerzettiColonne.check(attempt)) ? 1 : 0;
//					Utils.grid(attempt);
//					Utils.printSkimmedTabs(attempt, tabs);
                }

                if (deductionsCount > 0) {
                    List<Integer> mutation = new ArrayList<>(attempt.getNumbers());
                    for (ChangeLog changeLog : changeLogs) {
                        for (Change change : changeLog.getChanges()) {
                            if (!(change instanceof Skimming)) {
                                mutation = Utils.setDeductedNumber(mutation, change);
                            }
                        }
                    }

                    Sudoku changedSudoku = new Sudoku(mutation);
                    basicTabs = Utils.getBasicTabs(changedSudoku);
                    List<Tab> tabsPostChange = SudokuSolver.useStandardSolvingTechniques(changedSudoku, basicTabs).getTabs();
                    Set<String> emptySquareWithNoCandidates = Utils.checkForEmptySquaresWithNoCandidates(changedSudoku, tabsPostChange);

                    if (emptySquareWithNoCandidates.isEmpty()) {
                        return attempt5050(option, mutation, speculationRow, speculationCol);
                    } else {
                        bugs.addAll(emptySquareWithNoCandidates);
                        attemptResult = new AttemptResult(changedSudoku);
                        attemptResult.setBugs(bugs);
                    }
                }
            }
        }
        return attemptResult;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class AttemptResult {

        final Sudoku sudokuDuringAttempt;
        Change change;
        Set<String> bugs;

        private AttemptResult(Sudoku sudokuDuringAttempt) {
            this.sudokuDuringAttempt = sudokuDuringAttempt;
        }
    }
}