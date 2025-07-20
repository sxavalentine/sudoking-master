package com.gianfro.games.techniques.custom;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellGuessed;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.exceptions.SudokuBugException;
import com.gianfro.games.exceptions.UnsolvableException;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

public class FiftyFifty {

    public static final String FIFTY_FIFTY = "FIFTY FIFTY";

    public static List<SolutionStep> check(Sudoku sudoku, boolean isFirstIteration, LinkedList<CellGuessed> guesses) {

        List<ChangeLog> changeLogs = new LinkedList<>();
        Bivalue bivalue = getBivalueOptions(sudoku);

        Sudoku attempt1 = Sudoku.fromCells(sudoku.getCells());
        attempt1.getCells().get(bivalue.cellA.getIndex()).setValue(bivalue.optionA);
        attempt1.updateBasicCandidates();

        String solvingTechniqueVariant = bivalue.cellA.equals(bivalue.cellB) ? "FIFTY FIFTY BI-VALUE" : "FIFTY FIFTY STRONG LINK";

        CellGuessed guessA = CellGuessed.builder()
                .solvingTechnique(FIFTY_FIFTY)
                .house(bivalue.house)
                .cell(bivalue.cellA)
                .number(bivalue.optionA)
                .cellB(bivalue.cellB)
                .numberB(bivalue.optionB)
                .build();
        guesses.add(guessA);
        printGuesses(guesses);

        List<SolutionStep> solutionSteps;
        try {
            solutionSteps = SudokuSolver.solve(attempt1, isFirstIteration, guesses);
            if (!solutionSteps.get(solutionSteps.size() - 1).getSudokuNumbers().contains("0")) {
                ChangeLog log = ChangeLog.builder()
                        .unitExamined(List.of(bivalue.optionA))
                        .house(bivalue.house)
                        .houseNumber(bivalue.houseNumber)
                        .unitMembers(List.of(bivalue.cellA, bivalue.cellB))
                        .solvingTechnique(FIFTY_FIFTY)
                        .solvingTechniqueVariant(solvingTechniqueVariant)
                        .changes(List.of(guessA))
                        .build();
                changeLogs.add(log);
            }
        } catch (SudokuBugException sbe1) {
            System.out.println(sbe1.getMessage());//TODO REMOVE

            Sudoku attempt2 = Sudoku.fromCells(sudoku.getCells());
            attempt2.getCells().get(bivalue.cellB.getIndex()).setValue(bivalue.optionB);
            attempt2.updateBasicCandidates();

            CellGuessed guessB = CellGuessed.builder()
                    .solvingTechnique(FIFTY_FIFTY)
                    .house(bivalue.house)
                    .cell(bivalue.cellB)
                    .number(bivalue.optionB)
                    .cellB(bivalue.cellA)
                    .numberB(bivalue.optionA)
                    .build();
            guesses.pop();
            guesses.add(guessB);
            printGuesses(guesses);

            try {
                solutionSteps = SudokuSolver.solve(attempt2, isFirstIteration, guesses);
                if (!solutionSteps.get(solutionSteps.size() - 1).getSudokuNumbers().contains("0")) {
                    ChangeLog log = ChangeLog.builder()
                            .unitExamined(List.of(bivalue.optionB))
                            .house(bivalue.house)
                            .houseNumber(bivalue.houseNumber)
                            .unitMembers(List.of(bivalue.cellA, bivalue.cellB))
                            .solvingTechnique(FIFTY_FIFTY)
                            .solvingTechniqueVariant(solvingTechniqueVariant)
                            .changes(List.of(guessB))
                            .build();
                    changeLogs.add(log);
                }
            } catch (SudokuBugException sbe2) {
                System.out.println(sbe2.getMessage());//TODO REMOVE

                if (guesses.size() > 1) {
                    guesses.pop();
                    throw sbe2;
                } else {
                    System.out.println("IMPOSSIBLE TO SOLVE: WRONG INPUT ???");
                    Utils.megaGrid(attempt2);
                    throw new UnsolvableException(null, sudoku, null, null);
                }
            }
        }
        SolutionStep fiftyFiftyStep = new SolutionStep(sudoku, changeLogs);
        solutionSteps.add(0, fiftyFiftyStep);
        return solutionSteps;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    private static class Bivalue {
        SudokuCell cellA;
        SudokuCell cellB;
        int optionA;
        int optionB;
        //These two fields have value only when it's a FIFTY-FIFTY STRONG LINK
        House house;
        int houseNumber;
    }

    /**
     * Finds a Bivalue (object used solely by this class) to find a cell with 50% chance to insert the correct value.
     * First tries to find a cell with only 2 candidates left
     * (if found, cellB will be the same as cellA, and optionA/optionB will be its 2 candidates ).
     * If none are found, looks for houses that, for a given candidate, have only 2 cells with that candidate
     * (if found optionA and optionB will be the same, but for different SudokuCells).
     * If none are found, throws a NoFiftyFiftyException
     */
    private static Bivalue getBivalueOptions(Sudoku sudoku) {
        SudokuCell firstBivalueCell = sudoku.getCells().stream().filter(c -> c.getCandidates().size() == 2).findFirst().orElse(null);
        if (firstBivalueCell != null) {
            return Bivalue.builder()
                    .cellA(firstBivalueCell)
                    .cellB(firstBivalueCell)
                    .optionA(firstBivalueCell.getCandidates().get(0))
                    .optionB(firstBivalueCell.getCandidates().get(1))
                    .build();
        } else {
            for (Integer num : Utils.NUMBERS) {
                for (House house : House.values()) {
                    for (int houseNumber : Utils.NUMBERS) {
                        List<SudokuCell> emptyCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
                        List<SudokuCell> welcomingCells = emptyCells.stream().filter(c -> c.getCandidates().contains(num)).toList();
                        if (welcomingCells.size() == 2) {
                            return Bivalue.builder()
                                    .cellA(welcomingCells.get(0))
                                    .cellB(welcomingCells.get(1))
                                    .optionA(num)
                                    .optionB(num)
                                    .house(house)
                                    .houseNumber(houseNumber)
                                    .build();
                        }
                    }
                }
            }
        }
        throw new NoFiftyFiftyException(sudoku);
    }

    //TODO: REMOVE (DEBUG ONLY)
    private static void printGuesses(List<CellGuessed> guesses) {
        System.out.println("GUESSES:");
        for (CellGuessed cg : guesses) {
            System.out.println(cg);
        }
    }
}