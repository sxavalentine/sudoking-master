package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.ClaimingCandidates;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerClaimingCandidates {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String boxNumber = getWelcomingBoxNumber(changeLog);
        sb.append(String.format(
                "IN %s ALL CELLS WITH THE CANDIDATE %s BELONG TO BOX %s:",
                Utils.getWelcomingUnit(changeLog),
                changeLog.getUnitExamined().get(0),
                boxNumber));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append(String.format(
                "SO I CAN REMOVE %s FROM ALL THE OTHER CELLS OF BOX %s:",
                changeLog.getUnitExamined().get(0),
                boxNumber));
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

    private static String getWelcomingBoxNumber(ChangeLog changeLog) {
        return String.valueOf(changeLog.getUnitMembers().get(0).getBox());
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST POINTING CANDIDATES -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.TEST_CLAIMING_CANDIDATES_ROW);
        sudoku = Utils.buildSudoku(SudokuList.TEST_CLAIMING_CANDIDATES_COL);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(ClaimingCandidates.CLAIMING_CANDIDATES))
                        .collect(Collectors.toList());

        changeLogs.forEach(changeLog -> System.out.println(explain(changeLog)));
    }
}
