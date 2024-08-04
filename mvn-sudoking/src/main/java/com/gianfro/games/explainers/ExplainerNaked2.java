package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.Naked2;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerNaked2 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        sb.append(String.format(
                "IN %s THE ONLY CELLS WITH THE PAIR OF CANDIDATES %s ARE THE CELLS:",
                welcomingUnit,
                changeLog.getUnitExamined()));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append(String.format(
                "SO I CAN REMOVE %s FROM ALL THE OTHER CELLS OF %s:",
                changeLog.getUnitExamined(),
                welcomingUnit));
        sb.append("\n");
        changeLog.getChanges().forEach(c -> {
            Skimming skimming = (Skimming) c;
            sb.append(String.format("%s --> CANDIDATES REMAINING: %s; CANDIDATES REMOVED: %s",
                    SudokuExplainer.getCell(skimming),
                    skimming.getTab().getNumbers(),
                    skimming.getRemovedCandidates()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST NAKED PAIR -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.TEST_NAKED_2_ALL);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Naked2.NAKED_PAIR))
                        .collect(Collectors.toList());

        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
