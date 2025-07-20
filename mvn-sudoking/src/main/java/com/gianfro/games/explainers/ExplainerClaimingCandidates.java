package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.SolutionStep;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.basic.ClaimingCandidates;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;

public class ExplainerClaimingCandidates {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String boxNumber = getWelcomingBoxNumber(changeLog);
        sb.append(String.format(
                "IN %s ALL CELLS WITH THE CANDIDATE %s BELONG TO BOX %s:",
                Utils.getWelcomingUnit(changeLog),
                changeLog.getUnitExamined().get(0),
                boxNumber));
        sb.append("\n");
        changeLog.getUnitMembers().forEach(tab -> sb.append(tab).append("\n"));
        sb.append(String.format(
                "SO I CAN REMOVE %s FROM ALL THE OTHER CELLS OF BOX %s:",
                changeLog.getUnitExamined().get(0),
                boxNumber));
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

    private static String getWelcomingBoxNumber(ChangeLog changeLog) {
        return String.valueOf(changeLog.getUnitMembers().get(0).getBox());
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST POINTING CANDIDATES -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Sudoku.fromString(SudokuList.TEST_CLAIMING_CANDIDATES_ROW);
        sudoku = Sudoku.fromString(SudokuList.TEST_CLAIMING_CANDIDATES_COL);

        Utils.megaGrid(sudoku);

        SolutionStep step = SudokuSolver.useBasicSolvingTechniques(sudoku);
        List<ChangeLog> changeLogs =
                step.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(ClaimingCandidates.CLAIMING_CANDIDATES))
                        .toList();

        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
