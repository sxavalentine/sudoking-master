package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.Hidden1;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerHidden1 {

    public static void explain(ChangeLog changeLog) {
        for (Change change : changeLog.getChanges()) {
            System.out.println("IN " + Utils.getWelcomingUnit(changeLog) + " THE CELL " + SudokuExplainer.getCell(change) + " IS THE ONLY CELL WITH THE CANDIDATE " + change.getNumber());
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN SINGLE -----------------------------------------");

        Sudoku sudoku = SudokuList.TEST_HIDDEN_1_BOX;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Hidden1.HIDDEN_SINGLE))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}
