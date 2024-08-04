package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.advanced.XYChain;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerXYChain {

    public static String explain(ChangeLog changeLog) {

//		String chain = "[";
//		for (ChangeLogUnitMember unitMember : changeLog.getUnitMembers()) {
//			Link link = (Link)unitMember;
//			String linkString = link.toString().replace('-', '+');
//			chain += linkString;
//			if (!(unitMember == changeLog.getUnitMembers().get(changeLog.getUnitMembers().size() - 1))) {
//				chain += " - ";
//			} else {
//				chain += "]";
//			}
//		}
//		System.out.println("All cells forming the chain " + chain + " are bi-value cells strongly linked with each other");

        StringBuilder sb = new StringBuilder("All cells forming the following chain are bi-value cells strongly linked with each other:\n");
        changeLog.getUnitMembers().forEach(c -> {
            Link link = (Link) c;
            String linkString = link.toString().replace('-', '+');
            sb.append(linkString).append("\n");
        });
        sb.append(String.format(
                "This ensures that at least one end of the chain contains the candidate %s.",
                changeLog.getUnitExamined().get(0)));
        sb.append("\n");
        sb.append(String.format(
                "The following cells see both ends of the chain, so they can't contain %s.",
                changeLog.getUnitExamined().get(0)));
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

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST XY CHAIN -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.TEST_XY_CHAIN_1);
//        sudoku = Utils.buildSudoku(SudokuList.TEST_XY_CHAIN_2);
        sudoku = Utils.buildSudoku(SudokuList.TEST_XY_CHAIN_3);
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();
        List<ChangeLog> changeLogs =
                XYChain.check(tabs)
                        .getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(XYChain.XY_CHAIN))
                        .collect(Collectors.toList());

        changeLogs.forEach(changeLog -> System.out.println(explain(changeLog)));
    }
}
