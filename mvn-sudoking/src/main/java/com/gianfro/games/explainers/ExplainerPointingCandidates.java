package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.techniques.basic.PointingCandidates;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.Set;

public class ExplainerPointingCandidates {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String boxWelcomingUnit = getBoxWelcomingUnit(changeLog);
        sb.append(String.format(
                "IN BOX %s THE CANDIDATE %s CAN ONLY BE PUT IN %s IN THE CELLS:",
                changeLog.getHouseNumber(),
                changeLog.getUnitExamined().get(0),
                boxWelcomingUnit));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append(String.format(
                "SO I CAN REMOVE %s FROM ALL THE OTHER CELLS OF %s:",
                changeLog.getUnitExamined().get(0),
                boxWelcomingUnit));
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

    private static String getBoxWelcomingUnit(ChangeLog changeLog) {
        SudokuCell c1 = (SudokuCell) changeLog.getUnitMembers().get(0);
        SudokuCell c2 = (SudokuCell) changeLog.getUnitMembers().get(1);
        if (c1.getRow() == c2.getRow()) {
            return "ROW " + Utils.ROWS_LETTERS.get(c1.getRow() - 1);
        } else {
            return "COL " + c1.getCol();
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST POINTING CANDIDATES -----------------------------------------");

        Sudoku sudoku;
        sudoku = Sudoku.fromString(SudokuList.TEST_POINTING_CANDIDATES_ROW);
//        sudoku = Sudoku.fromString(SudokuList.TEST_POINTING_CANDIDATES_COL);
//        sudoku = Sudoku.fromString(SudokuList.TEST_POINTING_CANDIDATES_TRIPLE);

        Utils.megaGrid(sudoku);

        Set<ChangeLog> changeLogs = PointingCandidates.check(sudoku);
        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}