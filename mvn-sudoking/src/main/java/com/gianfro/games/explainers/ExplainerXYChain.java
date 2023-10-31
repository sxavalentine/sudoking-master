package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.advanced.XYChain;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ExplainerXYChain {

    public static void explain(ChangeLog changeLog) {

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

        System.out.println("All cells forming the following chain are bi-value cells strongly linked with each other:");
        for (ChangeLogUnitMember unitMember : changeLog.getUnitMembers()) {
            Link link = (Link) unitMember;
            String linkString = link.toString().replace('-', '+');
            System.out.println(linkString);
        }
        System.out.println("This ensures that at least one end of the chain contains the candidate " + changeLog.getUnitExamined().get(0) + ".");
        System.out.println("The following cells see both ends of the chain, so they can't contain " + changeLog.getUnitExamined().get(0) + ".");
        for (Change change : changeLog.getChanges()) {
            Skimming skimming = (Skimming) change;
            System.out.println(SudokuExplainer.getCell(skimming) + " --> CANDIDATES REMAINING " + skimming.getTab().getNumbers() + ", CANDIDATES REMOVED " + skimming.getRemovedCandidates());
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST XY CHAIN -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_XY_CHAIN_1;
//		Sudoku sudoku = SudokuList.TEST_XY_CHAIN_2;
        Sudoku sudoku = SudokuList.TEST_XY_CHAIN_3;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();
        List<ChangeLog> changeLogs =
                XYChain.check(tabs)
                        .getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(XYChain.XY_CHAIN))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            explain(changeLog);
            System.out.println();
        }
    }
}
