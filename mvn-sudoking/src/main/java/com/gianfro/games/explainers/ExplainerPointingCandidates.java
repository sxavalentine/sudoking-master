package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.PointingCandidates;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerPointingCandidates {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String boxWelcomingUnit = getBoxWelcomingUnit(changeLog);
        sb.append(String.format(
                "IN BOX %s THE CANDIDATE %s CAN ONLY BE PUT IN %s IN THE CELLS:",
                changeLog.getHouseNumber(),
                changeLog.getUnitExamined().get(0),
                boxWelcomingUnit));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append(String.format(
                "SO I CAN REMOVE %s FROM ALL THE OTHER CELLS OF %s:",
                changeLog.getUnitExamined().get(0),
                boxWelcomingUnit));
        sb.append("\n");
        changeLog.getChanges().forEach(c -> {
            Skimming skimming = (Skimming) c;
            sb.append(String.format(
                    "%s --> CANDIDATES REMAINING: %s; CANDIDATES REMOVED: %s",
                    SudokuExplainer.getCell(skimming),
                    skimming.getTab().getNumbers(),
                    skimming.getRemovedCandidates()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
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

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.TEST_POINTING_CANDIDATES_ROW);
//        sudoku = Utils.buildSudoku(SudokuList.TEST_POINTING_CANDIDATES_COL);
        sudoku = Utils.buildSudoku(SudokuList.TEST_POINTING_CANDIDATES_TRIPLE);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);
        Utils.megaGrid(sudoku, tabs);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(PointingCandidates.POINTING_CANDIDATES))
                        .collect(Collectors.toList());

        changeLogs.forEach(changeLog -> System.out.println(explain(changeLog)));
    }
}