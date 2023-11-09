package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.ClaimingCandidates;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerClaimingCandidates {

    public static void explain(ChangeLog changeLog) {
        String boxNumber = getWelcomingBoxNumber(changeLog);
        System.out.println("IN " + Utils.getWelcomingUnit(changeLog) + " ALL CELLS WITH THE CANDIDATE " + changeLog.getUnitExamined().get(0) + " BELONG TO BOX " + boxNumber + ":");
        for (ChangeLogUnitMember tab : changeLog.getUnitMembers()) {
            System.out.println(tab);
        }
        System.out.println("SO I CAN REMOVE " + changeLog.getUnitExamined().get(0) + " FROM ALL THE OTHER CELLS OF BOX " + boxNumber + ":");
        for (Change change : changeLog.getChanges()) {
            Skimming skimming = (Skimming) change;
            System.out.println(SudokuExplainer.getCell(skimming) + " --> CANDIDATES REMAINING " + skimming.getTab().getNumbers() + ", CANDIDATES REMOVED " + skimming.getRemovedCandidates());
        }
    }

    private static String getWelcomingBoxNumber(ChangeLog changeLog) {
        return "" + ((Tab) changeLog.getUnitMembers().get(0)).getBox();
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST POINTING CANDIDATES -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.TEST_CLAIMING_CANDIDATES_ROW);
        sudoku = Utils.buildSudoku(SudokuList.TEST_CLAIMING_CANDIDATES_COL);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(ClaimingCandidates.CLAIMING_CANDIDATES))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}
