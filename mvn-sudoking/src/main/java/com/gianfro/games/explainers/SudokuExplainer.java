package com.gianfro.games.explainers;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.*;
import com.gianfro.games.solving.techniques.advanced.XYChain;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.List;

public class SudokuExplainer {

    public static void explain(List<SolutionStep> solutionSteps) {
        for (int i = 0; i < solutionSteps.size(); i++) {
            SolutionStep solutionStep = solutionSteps.get(i);
            System.out.println("---------- STEP " + (i + 1) + " ----------");
            Utils.grid(solutionStep.getSudokuInstance());
            for (ChangeLog changeLog : solutionStep.getChangeLogs()) {
                explainChange(changeLog);
                System.out.println();
            }
        }
    }

    public static void explainChange(ChangeLog changeLog) {
        System.out.println("# " + (changeLog.getSolvingTechniqueVariant() != null ? changeLog.getSolvingTechniqueVariant() : changeLog.getSolvingTechnique()));
        switch (changeLog.getSolvingTechnique()) {
            case Naked1.NAKED_SINGLE:
                ExplainerNaked1.explain(changeLog);
                break;
            case Hidden1.HIDDEN_SINGLE:
                ExplainerHidden1.explain(changeLog);
                break;
            case Naked2.NAKED_PAIR:
                ExplainerNaked2.explain(changeLog);
                break;
            case Hidden2.HIDDEN_PAIR:
                ExplainerHidden2.explain(changeLog);
                break;
            case PointingCandidates.POINTING_CANDIDATES:
                ExplainerPointingCandidates.explain(changeLog);
                break;
            case ClaimingCandidates.CLAIMING_CANDIDATES:
                ExplainerClaimingCandidates.explain(changeLog);
                break;
            case Naked3.NAKED_TRIPLE:
                ExplainerNaked3.explain(changeLog);
                break;
            case Hidden3.HIDDEN_TRIPLE:
                ExplainerHidden3.explain(changeLog);
                break;
            case Naked4.NAKED_QUAD:
                ExplainerNaked4.explain(changeLog);
                break;
            case Hidden4.HIDDEN_QUAD:
                ExplainerHidden4.explain(changeLog);
                break;
//			case XWing.X_WING : 							ExplainerXWing.explain(changeLog); break;
            case XYChain.XY_CHAIN:
                ExplainerXYChain.explain(changeLog);
                break;
            default:
                break;
        }
    }

    public static String getCell(Tab tab) {
        return Utils.ROWS_LETTERS.get(tab.getRow() - 1) + tab.getCol();
    }

    public static String getCell(Change change) {
        return Utils.ROWS_LETTERS.get(change.getRow() - 1) + change.getCol();
    }

    public static void main(String[] args) {
        SolutionOutput out = SudokuSolver.getSolution(SudokuList.EVEREST1);
        List<SolutionStep> steps = out.getSolutionSteps();
        explain(steps);
    }
}
