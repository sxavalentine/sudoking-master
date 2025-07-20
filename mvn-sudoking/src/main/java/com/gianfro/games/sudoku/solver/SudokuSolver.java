package com.gianfro.games.sudoku.solver;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.SolutionStep;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.deductions.CellChange;
import com.gianfro.games.entities.deductions.CellGuessed;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.exceptions.UnsolvableException;
import com.gianfro.games.techniques.AdvancedSolvingTechnique;
import com.gianfro.games.techniques.BasicSolvingTechnique;
import com.gianfro.games.techniques.basic.Hidden1;
import com.gianfro.games.techniques.basic.Naked1;
import com.gianfro.games.techniques.custom.FiftyFifty;
import com.gianfro.games.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
public class SudokuSolver {

    public static SolutionOutput getSolution(Sudoku sudoku) {
        System.out.println("START");
        Utils.grid(sudoku);

        long start = System.currentTimeMillis();
        List<SolutionStep> stepRisolutivi = solve(sudoku, true, new LinkedList<>());
        long end = System.currentTimeMillis();
        int tempoImpiegato = (int) (end - start);

        System.out.println("END");
        Utils.grid(stepRisolutivi.get(stepRisolutivi.size() - 1).getSudokuInstance());

        return new SolutionOutput(sudoku, stepRisolutivi, tempoImpiegato);
    }

    public static List<SolutionStep> solve(Sudoku sudoku, boolean isFirstIteration, LinkedList<CellGuessed> guesses) {

        if (!guesses.isEmpty()) {
            Utils.checkForBugs(sudoku);
        }

        List<SolutionStep> result = new LinkedList<>();
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();

        if (!sudoku.getStringNumbers().contains("0")) {
            result.add(new SolutionStep(sudoku, changeLogs));
        } else {
            try {
                Sudoku step = Sudoku.fromCells(sudoku.getCells());
                SolutionStep solutionStep = useBasicSolvingTechniques(step);
                if (solutionStep.getChangeLogs().isEmpty()) {
                    solutionStep = useAdvancedSolvingTechniques(step); //TODO scommentare e fixare
                }
                if (solutionStep.getChangeLogs().isEmpty()) {
                    try {
                        List<SolutionStep> solutionSteps = FiftyFifty.check(step, isFirstIteration, guesses);
                        result.addAll(solutionSteps);
                        return result;
                    } catch (NoFiftyFiftyException | UnsolvableException exception) {
                        System.out.println(exception.getMessage());
                        if (exception instanceof UnsolvableException ue) throw ue;
                        if (exception instanceof NoFiftyFiftyException nffe) throw nffe;
                    }
                } else {
                    changeLogs.addAll(solutionStep.getChangeLogs());

                    result.add(new SolutionStep(sudoku, changeLogs));
                    Sudoku nextStep = Utils.applyDeductions(changeLogs, step);
                    List<SolutionStep> solutionSteps = solve(nextStep, false, guesses);
                    result.addAll(solutionSteps);
                }
            } catch (UnsolvableException ue) {
                String message = isFirstIteration ? "This Sudoku can't be solved by pure logic" : "";
                Sudoku startingSudoku = isFirstIteration ? sudoku : null;
                result.addAll(ue.getSolutionSteps());
                throw new UnsolvableException(startingSudoku, ue.getBlockedSudoku(), result, message);
            }
        }
        return result;
    }

    public static SolutionStep useBasicSolvingTechniques(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();

        //Naked1 and Hidden1: these are the only two techniques who produce CellSolved, rather than CellSkimming
        changeLogs.addAll(Naked1.check(sudoku));
        changeLogs.addAll(Hidden1.check(sudoku));

        //All the others basic techniques, only if no cell was solved
        if (changeLogs.isEmpty()) {
            int solvingTechniqueIndex = 2;
            while (solvingTechniqueIndex < BasicSolvingTechnique.values().length) {
                Set<ChangeLog> newDeductions = BasicSolvingTechnique.values()[solvingTechniqueIndex].check(sudoku);
                changeLogs.addAll(newDeductions);
                solvingTechniqueIndex++;
            }
        }
        return new SolutionStep(sudoku, changeLogs);
    }

    private static SolutionStep useAdvancedSolvingTechniques(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        int solvingTechniqueIndex = 0;
        while (solvingTechniqueIndex < AdvancedSolvingTechnique.values().length) {
            Set<ChangeLog> newDeductions = AdvancedSolvingTechnique.values()[solvingTechniqueIndex].check(sudoku);
            changeLogs.addAll(newDeductions);
            solvingTechniqueIndex++;
        }
        return new SolutionStep(sudoku, changeLogs);
//        SkimmingResult result;
//
//        // X WING
//        result = XWing.check(sudoku);
//        Set<ChangeLog> changeLogs = new LinkedHashSet<>(result.getChangeLogs());
////        tabs = result.getTabs();
//
//        // REMOTE PAIRS
//        result = RemotePairs.check(sudoku);
//        changeLogs.addAll(result.getChangeLogs());
////        tabs = result.getTabs();
//
//        // DISCINTINUOUS NICE LOOP
//        result = DiscontinuousNiceLoop.check(sudoku);
//        changeLogs.addAll(result.getChangeLogs());
////        tabs = result.getTabs();
//
//        // X CHAIN
//        result = XChain.check(sudoku);
//        changeLogs.addAll(result.getChangeLogs());
////        tabs = result.getTabs();
//
//        // COLORING
//        result = Coloring.check(sudoku);
//        changeLogs.addAll(result.getChangeLogs());
////        tabs = result.getTabs();
//
//        // XY CHAIN
//        // TODO (da rivedere in toto, al momento non va)
////        result = XYChain.check(tabs);
////        changeLogs.addAll(result.getChangeLogs());
////        tabs = result.getTabs();
//
//        SolutionStep solutionStep = useStandardSolvingTechniques(sudoku);
//        changeLogs.addAll(solutionStep.getChangeLogs());
////        tabs = solutionStep.getTabs();
//
//
//        return new SolutionStep(sudoku, changeLogs);
    }


    /**
     * Shows Sudoku solution, prints the final grid and returns the solved Sudoku entity
     */
    public static Sudoku showSolution(Sudoku sudoku) {
        List<SolutionStep> allSteps = solve(sudoku, true, new LinkedList<>());
        SolutionStep finalStep = allSteps.get(allSteps.size() - 1);
        Sudoku solvedSudoku = finalStep.getSudokuInstance();
        System.out.println("SUDOKU SOLUTION");
        Utils.grid(solvedSudoku);
        return solvedSudoku;
    }


    /**
     * Shows solution steps and the solving techniques used
     */
    public static void showStepsAndTechniques(Sudoku sudoku) {
        SolutionOutput solutionOutput = getSolution(sudoku);
        List<SolutionStep> solution = solutionOutput.getSolutionSteps();
        for (int i = 0; i < solution.size(); i++) {
            if (i == 0) {
                System.out.println("STARTING GRID");
            } else if (i < solution.size() - 1) {
                System.out.println("STEP " + i);
            } else {
                System.out.println("STEP " + i + " (AND FINAL SOLUTION)");
            }
            Utils.megaGrid(solution.get(i).getSudokuInstance());
            if (i != solution.size() - 1) {
                for (CellChange change : solution.get(i).getChanges()) {
                    System.out.println(change);
                }
            }
            System.out.println();
        }
        System.out.println("SOLVED IN " + solutionOutput.getSolutionTime() + " MILLISECONDS");
    }

    public static void main(String[] args) {
        Sudoku sudoku = Sudoku.fromString("000750024003610008700000500600000050140000086080000007001000009400068300350097000");
        getSolution(sudoku);
    }
}