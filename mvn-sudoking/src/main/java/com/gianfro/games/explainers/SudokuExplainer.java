package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.*;
import com.gianfro.games.techniques.advanced.XYChain;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;

public class SudokuExplainer {

    public static String explain(List<SolutionStep> solutionSteps) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < solutionSteps.size(); i++) {
            SolutionStep solutionStep = solutionSteps.get(i);
            sb.append("---------- STEP " + (i + 1) + " ----------").append("\n");
            sb.append(Utils.grid(solutionStep.getSudokuInstance()));
            for (ChangeLog changeLog : solutionStep.getChangeLogs()) {
                sb.append(explainChange(changeLog)).append("\n");
            }
        }
        return sb.toString();
    }

    public static String explainChange(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        String solvingTechniqueName = "# " + (changeLog.getSolvingTechniqueVariant() != null ?
                changeLog.getSolvingTechniqueVariant() : changeLog.getSolvingTechnique());
        sb.append(solvingTechniqueName).append("\n");
        System.out.println(solvingTechniqueName);
        switch (changeLog.getSolvingTechnique()) {
            case Naked1.NAKED_SINGLE:
                sb.append(ExplainerNaked1.explain(changeLog));
                break;
            case Hidden1.HIDDEN_SINGLE:
                sb.append(ExplainerHidden1.explain(changeLog));
                break;
            case Naked2.NAKED_PAIR:
                sb.append(ExplainerNaked2.explain(changeLog));
                break;
            case Hidden2.HIDDEN_PAIR:
                sb.append(ExplainerHidden2.explain(changeLog));
                break;
            case PointingCandidates.POINTING_CANDIDATES:
                sb.append(ExplainerPointingCandidates.explain(changeLog));
                break;
            case ClaimingCandidates.CLAIMING_CANDIDATES:
                sb.append(ExplainerClaimingCandidates.explain(changeLog));
                break;
            case Naked3.NAKED_TRIPLE:
                sb.append(ExplainerNaked3.explain(changeLog));
                break;
            case Hidden3.HIDDEN_TRIPLE:
                sb.append(ExplainerHidden3.explain(changeLog));
                break;
            case Naked4.NAKED_QUAD:
                sb.append(ExplainerNaked4.explain(changeLog));
                break;
            case Hidden4.HIDDEN_QUAD:
                sb.append(ExplainerHidden4.explain(changeLog));
                break;
//			TODO rimetterci mano e fargli restituire una String come gli altri explainers
//			 case XWing.X_WING :
//                 ExplainerXWing.explain(changeLog);
//                 break;
            case XYChain.XY_CHAIN:
                sb.append(ExplainerXYChain.explain(changeLog));
                break;
            default:
                break;
        }
        return sb.toString();
    }

    public static String getCell(Tab tab) {
        return Utils.ROWS_LETTERS.get(tab.getRow() - 1) + tab.getCol();
    }

    public static String getCell(Change change) {
        return Utils.ROWS_LETTERS.get(change.getRow() - 1) + change.getCol();
    }

    public static void main(String[] args) {
        SolutionOutput out = SudokuSolver.getSolution(Utils.buildSudoku(SudokuList.EVEREST1));
        List<SolutionStep> steps = out.getSolutionSteps();
        explain(steps);
    }
}
