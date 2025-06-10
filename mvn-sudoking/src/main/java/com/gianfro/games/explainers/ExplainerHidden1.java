package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.SolutionStep;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.basic.Hidden1;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;

public class ExplainerHidden1 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        changeLog.getChanges().forEach(c -> {
            sb.append(String.format(
                    "CELL %s IN %s IS THE ONLY CELL WITH THE CANDIDATE %s",
                    SudokuExplainer.getCell(c),
                    Utils.getWelcomingUnit(changeLog),
                    c.getNumber()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN SINGLE -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.TEST_HIDDEN_1_BOX);
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Hidden1.HIDDEN_SINGLE))
                        .toList();

        changeLogs.forEach(changeLog -> System.out.println(explain(changeLog)));
    }
}
