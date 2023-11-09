package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.Naked4;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerNaked4 {

    public static void explain(ChangeLog changeLog) {
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        System.out.println("IN " + welcomingUnit + " THE ONLY CELLS WITH THE QUADRUPLE OF CANDIDATES " + changeLog.getUnitExamined() + " ARE THE CELLS:");
        for (ChangeLogUnitMember tab : changeLog.getUnitMembers()) {
            System.out.println(tab);
        }
        System.out.println("SO I CAN REMOVE " + changeLog.getUnitExamined() + " FROM ALL THE OTHER CELLS OF " + welcomingUnit + ":");
        for (Change change : changeLog.getChanges()) {
            Skimming skimming = (Skimming) change;
            System.out.println(SudokuExplainer.getCell(skimming) + " --> CANDIDATES REMAINING " + skimming.getTab().getNumbers() + ", CANDIDATES REMOVED " + skimming.getRemovedCandidates());
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST NAKED QUADRUPLE -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.TEST_NAKED_4_BOX);
        sudoku = Utils.buildSudoku(SudokuList.TEST_NAKED_4_ROW);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Naked4.NAKED_QUAD))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}
