package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.Hidden2;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerHidden2 {

    public static void explain(ChangeLog changeLog) {
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        System.out.println("IN " + welcomingUnit + " THE PAIR OF CANDIDATES " + changeLog.getUnitExamined() + " APPEARS ONLY IN THE CELLS:");
        for (ChangeLogUnitMember tab : changeLog.getUnitMembers()) {
            System.out.println(tab);
        }
        System.out.println("SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:");
        for (Change change : changeLog.getChanges()) {
            Skimming skimming = (Skimming) change;
            System.out.println(SudokuExplainer.getCell(skimming) + " --> CANDIDATES REMAINING " + skimming.getTab().getNumbers() + ", CANDIDATES REMOVED " + skimming.getRemovedCandidates());
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN PAIR -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_HIDDEN_2_BOX;
        Sudoku sudoku = SudokuList.TEST_HIDDEN_2_COL;

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Hidden2.HIDDEN_PAIR))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}
