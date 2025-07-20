package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.techniques.basic.Hidden2;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.Set;

public class ExplainerHidden2 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        sb.append(String.format(
                "IN %s THE PAIR OF CANDIDATES %s APPEARS ONLY IN THE CELLS:",
                welcomingUnit,
                changeLog.getUnitExamined()));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append("SO I CAN REMOVE ALL THE OTHER CANDIDATES FROM THOSE CELLS:").append("\n");
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
        System.out.println("------------------------------------- TEST HIDDEN PAIR -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Sudoku.fromString(SudokuList.TEST_HIDDEN_2_BOX);
        sudoku = Sudoku.fromString(SudokuList.TEST_HIDDEN_2_COL);

        Utils.megaGrid(sudoku);

        Set<ChangeLog> changeLogs = Hidden2.check(sudoku);
        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
