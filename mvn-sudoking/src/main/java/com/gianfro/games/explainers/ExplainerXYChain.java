package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Link;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

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
            //TODO 18/06/25: rimpiazzo con + solo se is on
            sb.append(link.isOn() ? link.toString().replace("-", "+") : link);
            sb.append("\n");
//            String linkString = link.toString().replace('-', '+');//TODO perchè rimpiazza il "-" ????
//            sb.append(linkString).append("\n");
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
            CellSkimmed skimming = (CellSkimmed) c;
            sb.append(String.format(
                    "%s --> CANDIDATES REMAINING: %s; CANDIDATES REMOVED: %s",
                    skimming.getCell().getCoordinates(),
                    skimming.getCell().getCandidates(),
                    skimming.getRemovedCandidates()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST XY CHAIN -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Sudoku.fromString(SudokuList.TEST_XY_CHAIN_1);
//        sudoku = Sudoku.fromString(SudokuList.TEST_XY_CHAIN_2);
        sudoku = Sudoku.fromString(SudokuList.TEST_XY_CHAIN_3);

        Utils.megaGrid(sudoku);

//        List<ChangeLog> changeLogs = XYChain.check(sudoku);
//        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
