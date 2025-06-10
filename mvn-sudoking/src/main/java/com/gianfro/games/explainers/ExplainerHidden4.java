package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.basic.Hidden4;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;

public class ExplainerHidden4 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        sb.append(String.format(
                "IN %s THE QUADRUPLE OF CANDIDATES %s APPEARS ONLY IN THE CELLS:",
                welcomingUnit,
                changeLog.getUnitExamined()));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append("SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:").append("\n");
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

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN QUADRUPLE -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.TEST_HIDDEN_4_BOX);
//        sudoku = Utils.buildSudoku(SudokuList.TEST_HIDDEN_4_ROW);
        sudoku = Utils.buildSudoku(SudokuList.TEST_HIDDEN_4_COL);
//        sudoku = Utils.buildSudoku("650087024000649050040025000570438061000501000310902085000890010000213000130750098"); // TEST COL 7 STRONZO ENG RICHIEDE XY-WING

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Hidden4.HIDDEN_QUAD))
                        .toList();

        changeLogs.forEach(changeLog -> System.out.println(explain(changeLog)));
    }
}
