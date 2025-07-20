package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.techniques.basic.Naked4;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.Set;

public class ExplainerNaked4 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String welcomingUnit = Utils.getWelcomingUnit(changeLog);
        sb.append(String.format(
                "IN %s THE ONLY CELLS WITH THE QUADRUPLE OF CANDIDATES %s ARE THE CELLS:",
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
        System.out.println("------------------------------------- TEST NAKED QUADRUPLE -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Sudoku.fromString(SudokuList.TEST_NAKED_4_BOX);
        sudoku = Sudoku.fromString(SudokuList.TEST_NAKED_4_ROW);

        Utils.megaGrid(sudoku);

        Set<ChangeLog> changeLogs = Naked4.check(sudoku);
        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
