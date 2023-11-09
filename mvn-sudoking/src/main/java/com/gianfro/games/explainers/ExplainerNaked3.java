package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.Naked3;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerNaked3 {

    public static void explain(ChangeLog changeLog) {
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        System.out.println("IN " + welcomingUnit + " THE ONLY CELLS WITH THE TRIO OF CANDIDATES " + changeLog.getUnitExamined() + " ARE THE CELLS:");
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
        System.out.println("------------------------------------- TEST NAKED TRIPLE -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.TEST_NAKED_3_ROW);
        sudoku = Utils.buildSudoku(SudokuList.TEST_NAKED_3_COL); // <- in teoria sarebbe in col 2, ma evidentemente la scrematura è oscurata da tecniche risolutive precedenti. Trova comunque un caso in row A

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.grid(sudoku);

        SolutionStep step = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(Naked3.NAKED_TRIPLE))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}