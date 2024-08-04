package com.gianfro.games.sudoku.solver;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.UnsolvableException;
import com.gianfro.games.techniques.Hidden1;
import com.gianfro.games.techniques.Naked1;
import com.gianfro.games.techniques.StandardSolvingTechnique;
import com.gianfro.games.techniques.advanced.*;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class SudokuSolver {

    public static SolutionOutput getSolution(Sudoku sudoku) {
        System.out.println("START");
        Utils.grid(sudoku);

        long start = System.currentTimeMillis();
        List<SolutionStep> stepRisolutivi = solve(sudoku, true);
        long end = System.currentTimeMillis();
        int tempoImpiegato = (int) (end - start);

        System.out.println("END");
        Utils.grid(stepRisolutivi.get(stepRisolutivi.size() - 1).getSudokuInstance());

        return new SolutionOutput(sudoku, stepRisolutivi, tempoImpiegato);
    }

    private static List<SolutionStep> solve(Sudoku sudoku, boolean isFirstIteration) {

        List<SolutionStep> result = new LinkedList<>();
        List<ChangeLog> changeLogs = new LinkedList<>();
        List<Tab> tabs = Utils.getBasicTabs(sudoku);

        if (Collections.frequency(sudoku.getNumbers(), 0) == 0) {
            result.add(new SolutionStep(sudoku, changeLogs, tabs));
        } else {
            try {
                List<Integer> sequence = new ArrayList<>(sudoku.getNumbers());
                Sudoku step = new Sudoku(sequence);
                SolutionStep solutionStep = useStandardSolvingTechniques(step, tabs);

                changeLogs.addAll(solutionStep.getChangeLogs());
                tabs = solutionStep.getTabs();
                List<Integer> mutation = new ArrayList<>(step.getNumbers());
                int deductionsCount = 0;

                for (ChangeLog changeLog : changeLogs) {
                    for (Change change : changeLog.getChanges()) {
                        if (!(change instanceof Skimming)) {
                            deductionsCount++;
                        }
                    }
                }

                if (deductionsCount == 0) {
                    solutionStep = SudokuSolver.useAdvancedSolvingTechniques(sudoku, tabs);
                    changeLogs.addAll(solutionStep.getChangeLogs());
                    tabs = solutionStep.getTabs();

                    for (ChangeLog changeLog : changeLogs) {
                        for (Change change : changeLog.getChanges()) {
                            if (!(change instanceof Skimming)) {
                                deductionsCount++;
                            }
                        }
                    }
                }
                if (deductionsCount > 0) {
                    for (ChangeLog changeLog : changeLogs) {
                        for (Change change : changeLog.getChanges()) {
                            if (!(change instanceof Skimming)) {
                                Utils.setDeductedNumber(mutation, change);
                            }
                        }
                    }

                    Sudoku nextStep = new Sudoku(mutation);
                    result.add(new SolutionStep(step, changeLogs, tabs));

                    List<SolutionStep> solutionSteps = solve(nextStep, false);
                    result.addAll(solutionSteps);
                } else {
                    throw new UnsolvableException(null, sudoku, result, tabs, "");
//                    System.out.println("------------------------------------------------------------------------------------------------------------------");
//                    System.out.println("I'm about to call method FIFTY-FIFTY, the grid is");
//                    Utils.megaGrid(step, tabs);
//                    SolutionStep firstCallToTryFiftyFifty = FiftyFifty.check(step, tabs, 1);
//                    for (Change change : firstCallToTryFiftyFifty.getChanges()) {
//                        if (!(change instanceof Skimming)) {
//                            change.setSolvingTechnique(FiftyFifty.FIFTY_FIFTY);
//                            ChangeLog log = new ChangeLog(null, null, 0, null, FiftyFifty.FIFTY_FIFTY, null, Collections.singletonList(change));
//                            changeLogs.add(log);
//                            mutation = Utils.setDeductedNumber(mutation, change);
//                        }
//                    }
//                    Sudoku nextStep = new Sudoku(mutation);
//                    result.add(new SolutionStep(step, changeLogs, tabs));
//                    List<SolutionStep> solutionSteps = solve(nextStep, false);
//                    result.addAll(solutionSteps);
                }
            } catch (UnsolvableException ue) {
                String message = isFirstIteration ? "This Sudoku can't be solved by pure logic" : "";
                Sudoku startingSudoku = isFirstIteration ? sudoku : null;
                result.addAll(ue.getSolutionSteps());
                throw new UnsolvableException(startingSudoku, ue.getBlockedSudoku(), result, ue.getTabs(), message);
            }
        }
        return result;
    }


    public static SolutionStep useStandardSolvingTechniques(Sudoku sudoku, List<Tab> tabs) {
        SkimmingResult result;

        result = Naked1.check(tabs);
        Set<ChangeLog> changeLogs = new HashSet<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = Hidden1.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // skimmings
        if (changeLogs.isEmpty()) {
            int solvingTechniqueIndex = 2;
            while (solvingTechniqueIndex < StandardSolvingTechnique.values().length) {
                result = StandardSolvingTechnique.values()[solvingTechniqueIndex].check(tabs);
                changeLogs.addAll(result.getChangeLogs());
                tabs = result.getTabs();
                solvingTechniqueIndex++;
            }

            // further round of NAKED_SINGLE and HIDDEN_SINGLE, after skimmings occurred
            result = Naked1.check(tabs);
            changeLogs.addAll(result.getChangeLogs());
            tabs = result.getTabs();

            result = Hidden1.check(tabs);
            changeLogs.addAll(result.getChangeLogs());
            tabs = result.getTabs();
        }

        List<ChangeLog> changeLogList = new ArrayList<>(changeLogs);
        return new SolutionStep(sudoku, changeLogList, tabs);
    }


    private static SolutionStep useAdvancedSolvingTechniques(Sudoku sudoku, List<Tab> tabs) {
        SkimmingResult result;

        // X WING
        result = XWing.check(tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        // REMOTE PAIRS
        result = RemotePairs.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // DISCINTINUOUS NICE LOOP
        result = DiscontinuousNiceLoop.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // X CHAIN
        result = XChain.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // COLORING
        result = Coloring.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // XY CHAIN
        // TODO (da rivedere in toto, al momento non va)
//        result = XYChain.check(tabs);
//        changeLogs.addAll(result.getChangeLogs());
//        tabs = result.getTabs();

        SolutionStep solutionStep = useStandardSolvingTechniques(sudoku, tabs);
        changeLogs.addAll(solutionStep.getChangeLogs());
        tabs = solutionStep.getTabs();


        return new SolutionStep(sudoku, changeLogs, tabs);
    }


    /**
     * Shows Sudoku solution
     */
    public static List<Integer> showSolution(Sudoku sudoku) {
        List<SolutionStep> allSteps = solve(sudoku, true);
        SolutionStep finalStep = allSteps.get(allSteps.size() - 1);
        Sudoku solvedSudoku = finalStep.getSudokuInstance();
        System.out.println("SUDOKU SOLUTION");
        Utils.grid(solvedSudoku);
        return solvedSudoku.getNumbers();
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
            Utils.grid(solution.get(i).getSudokuInstance());
            if (i != solution.size() - 1) {
                for (Change change : solution.get(i).getChanges()) {
                    System.out.println(change);
                }
            }
            System.out.println();
        }
        System.out.println("SOLVED IN " + solutionOutput.getSolutionTime() + " MILLISECONDS");
    }
}