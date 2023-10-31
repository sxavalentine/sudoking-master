package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.PointingCandidates;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerPointingCandidates {

    public static void explain(ChangeLog changeLog) {
        String boxWelcomingUnit = getBoxWelcomingUnit(changeLog);
        System.out.println("IN BOX " + changeLog.getHouseNumber() + " THE CANDIDATE " + changeLog.getUnitExamined().get(0) + " CAN ONLY BE PUT IN " + boxWelcomingUnit + " IN THE CELLS:");
        for (ChangeLogUnitMember tab : changeLog.getUnitMembers()) {
            System.out.println(tab);
        }
        System.out.println("SO I CAN REMOVE " + changeLog.getUnitExamined().get(0) + " FROM ALL THE OTHER CELLS OF " + boxWelcomingUnit + ":");
        for (Change change : changeLog.getChanges()) {
            Skimming skimming = (Skimming) change;
            System.out.println(SudokuExplainer.getCell(skimming) + " --> CANDIDATES REMAINING " + skimming.getTab().getNumbers() + ", CANDIDATES REMOVED " + skimming.getRemovedCandidates());
        }
    }

    private static String getBoxWelcomingUnit(ChangeLog changeLog) {
        Tab tab1 = (Tab) changeLog.getUnitMembers().get(0);
        Tab tab2 = (Tab) changeLog.getUnitMembers().get(1);
        if (tab1.getRow() == tab2.getRow()) {
            return "ROW " + Utils.ROWS_LETTERS.get(tab1.getRow() - 1);
        } else {
            return "COL " + tab1.getCol();
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST POINTING CANDIDATES -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_POINTING_CANDIDATES_ROW;
//		Sudoku sudoku = SudokuList.TEST_POINTING_CANDIDATES_COL;
        Sudoku sudoku = SudokuList.TEST_POINTING_CANDIDATES_TRIPLE;

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(PointingCandidates.POINTING_CANDIDATES))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}