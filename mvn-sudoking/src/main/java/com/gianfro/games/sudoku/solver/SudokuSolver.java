package com.gianfro.games.sudoku.solver;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.solving.techniques.Hidden1;
import com.gianfro.games.solving.techniques.Naked1;
import com.gianfro.games.solving.techniques.StandardSolvingTechnique;
import com.gianfro.games.solving.techniques.advanced.Chain;
import com.gianfro.games.solving.techniques.advanced.RemotePairs;
import com.gianfro.games.solving.techniques.advanced.XWing;
import com.gianfro.games.solving.techniques.custom.FiftyFifty;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class SudokuSolver {

    public static SolutionOutput getSolution(Sudoku sudoku) {
        System.out.println("START");
        Utils.grid(sudoku);

        long start = System.currentTimeMillis();
        List<SolutionStep> stepRisolutivi = solve(sudoku);
        long end = System.currentTimeMillis();
        int tempoImpiegato = (int) (end - start);

        System.out.println("END");
        Utils.grid(stepRisolutivi.get(stepRisolutivi.size() - 1).getSudokuInstance());

        return new SolutionOutput(sudoku, stepRisolutivi, tempoImpiegato);
    }

    private static List<SolutionStep> solve(Sudoku sudoku) {

        List<SolutionStep> result = new LinkedList<>();
        List<ChangeLog> changeLogs = new LinkedList<>();
        List<Tab> tabs = Utils.getBasicTabs(sudoku);

        if (Collections.frequency(sudoku.getNumbers(), 0) == 0) {
            result.add(new SolutionStep(sudoku, changeLogs, tabs));
        } else {
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
                solutionStep = SudokuSolver.useAdvancedTechniques(sudoku, tabs);
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

//            if (deductionsCount == 0) {
//                deductionsCount += changeLogs.addAll(ControlloTerzettiRighe.check(step)) ? 1 : 0;
//                deductionsCount += changeLogs.addAll(ControlloTerzettiColonne.check(step)) ? 1 : 0;
//            }

            if (deductionsCount > 0) {
                for (ChangeLog changeLog : changeLogs) {
                    for (Change change : changeLog.getChanges()) {
                        if (!(change instanceof Skimming)) {
                            mutation = Utils.setDeductedNumber(mutation, change);
                        }
                    }
                }

                Sudoku nextStep = new Sudoku(mutation);
                result.add(new SolutionStep(step, changeLogs, tabs));
                ///
//				System.out.println("PER EFFETTO DELLE DEDUZIONI DI SOPRA, LA NUOVA GRIGLIA E'");
//				Utils.grid(nextStep);
//				if (nextStep.getNumbers().contains(0)) System.out.println("ORA APPLICHERO' RICORSIVAMENTE IL METODO SOLVE");
//				System.out.println("------------------------------------------------------------------------------------------------------------------");
                ///
                List<SolutionStep> solutionSteps = solve(nextStep);
                result.addAll(solutionSteps);
            } else {
                ///
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("I'm about to call method FIFTY FIFTY, the grid is");
                Utils.grid(step);
                ///
                SolutionStep firstCallToTryFiftyFifty = FiftyFifty.check(step, tabs, 1);
                for (Change change : firstCallToTryFiftyFifty.getChanges()) {
                    if (!(change instanceof Skimming)) {
                        change.setSolvingTechnique(FiftyFifty.FIFTY_FIFTY);
                        ChangeLog log = new ChangeLog(null, null, 0, null, FiftyFifty.FIFTY_FIFTY, null, Arrays.asList(change));
                        changeLogs.add(log);
                        mutation = Utils.setDeductedNumber(mutation, change);
                    }
                }
                Sudoku nextStep = new Sudoku(mutation);
                result.add(new SolutionStep(step, changeLogs, tabs));
                ///
//				System.out.println("PER EFFETTO DELLE DEDUZIONI DI SOPRA, LA NUOVA GRIGLIA E'");
//				Utils.grid(nextStep);
//				if (nextStep.getNumbers().contains(0)) System.out.println("ORA APPLICHERO' RICORSIVAMENTE IL METODO SOLVE");
//				System.out.println("------------------------------------------------------------------------------------------------------------------");
                ///
                List<SolutionStep> solutionSteps = solve(nextStep);
                result.addAll(solutionSteps);
            }
        }
        return result;
    }


    public static SolutionStep useStandardSolvingTechniques(Sudoku sudoku, List<Tab> tabs) {
        SkimmingResult result;
        List<ChangeLog> changeLogs = new LinkedList<>();

        result = Naked1.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
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

        return new SolutionStep(sudoku, changeLogs, tabs);
    }


    private static SolutionStep useAdvancedTechniques(Sudoku sudoku, List<Tab> tabs) {
        SkimmingResult result;
        List<ChangeLog> changeLogs = new LinkedList<>();

        // X WING
        result = XWing.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // REMOTE PAIRS
        result = RemotePairs.check(tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        // CHAINS
        result = Chain.check(tabs);
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


    // shows Sudoku solution
    public static List<Integer> showSolution(Sudoku sudoku) {
        List<SolutionStep> allSteps = solve(sudoku);
        SolutionStep finalStep = allSteps.get(allSteps.size() - 1);
        Sudoku solvedSudoku = finalStep.getSudokuInstance();
        System.out.println("SUDOKU SOLUTION");
        Utils.grid(solvedSudoku);
        return solvedSudoku.getNumbers();
    }


    // shows solution steps and the solving techniques used
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

    public static void solve50kSudokus() {
        List<Sudoku> allSudokus = Utils.read50kSudoku();
        List<Sudoku> noFfeSudokus = new LinkedList<>();
        int totalSolutionTime = 0;
        int count = 1;
        int solvedCount = 0;
        int nffeCount = 0;
        int geCount = 0;
        for (Sudoku sudoku : allSudokus) {
            try {
                SolutionOutput solutionOutput = getSolution(sudoku);
                totalSolutionTime += solutionOutput.getSolutionTime();
                solvedCount++;
            } catch (NoFiftyFiftyException nffe) {
                System.out.println(count + " " + nffe);
                nffeCount++;
                noFfeSudokus.add(sudoku);
            } catch (Exception e) {
                System.out.println(count + " " + e);
                geCount++;
            }
            count++;
        }
        System.out.println("I solved " + solvedCount + " out of " + allSudokus.size() + " sudokus in " + totalSolutionTime + " milliseconds");
        if (nffeCount > 0) System.out.println("NO FIFTY FIFTY EXCEPTION count: " + nffeCount);
        if (geCount > 0) System.out.println("GENERIC EXCEPTION count: " + geCount);
        System.out.println("These are the NoFiftyFiftyException sudokus: ");
        for (Sudoku s : noFfeSudokus) {
            System.out.println(s.getStringNumbers());
        }

    }


    public static void main(String[] args) {

        SudokuSolver solver = new SudokuSolver();
//        solve50000sudokusMongoDB(allSudokus);         // SOLVE 49151 AND SAVES THEM ON MONGODB


//		Sudoku sudoku = allSudokus.get(2428);
//        Sudoku sudoku = SudokuList.EVEREST1;
//        showStepsAndTechniques(sudoku);

//		// SAFELY SOLVE A SUDOKU
//		Sudoku sudoku = SudokuList.BLOCKED.get(1);
//		try {
//			SolutionOutput so = getSolution(sudoku);
//			int stepCount = 1;
//			for (SolutionStep step : so.getSolutionSteps()) {
//				System.out.println("STEP " + stepCount);
//				Utils.grid(step.getSudokuInstance());
//				for (Change change : step.getChanges()) {
//					System.out.println(change);
//				}
//				stepCount++;
//			}
//			System.out.println("SOLVED IN " + so.getSolutionTime() + " MILLISECONDS");
//		} catch (NoFiftyFiftyException nffe) {
//			Sudoku blockedSudoku = nffe.getSudokuAtTheTimeOfException();
//			Utils.grid(blockedSudoku);
//			int startingNumbers = 81 - Collections.frequency(sudoku.getNumbers(), 0);
//			int impasseNumbers = 81 - Collections.frequency(blockedSudoku.getNumbers(), 0);
//			System.out.println("I COULDN'T SOLVE IT. I MANAGED TO DEDUCT ONLY " + (impasseNumbers - startingNumbers) + " NUMBERS");
//			System.out.println("THESE ARE THE TABS");
//			System.out.println();
//			Utils.printSkimmedTabs(blockedSudoku, nffe.getTabs());
//		}


        // CHECK SE I SUDOKU RISOLTI PRESENTANO BUG DI SORTA
//		SudokuDao dao = new SudokuDaoImpl();				
//		List<Sudoku> sudokuRisolti = dao.getSudokuRisolti();
//		int count = 1;
//		int countFintiRisolti = 0;
//		for (Sudoku s : sudokuRisolti) {
//			if (!Utils.checkForBugs(s).isEmpty() && !Utils.checkForEmptySquaresWithNoCandidates(s, s.getTabs()).isEmpty()) {
//				System.out.println("Sudoku " + count + " non e' realmente risolto");
//				countFintiRisolti++;
//			}
//			count++;
//		}
//		System.out.println("CI SONO " + countFintiRisolti + " SUDOKU FINTAMENTE RISOLTI");


        //MOSTRA I PASSAGGI DEL PRIMO BLOCCATO (ORA RISOLTO)
//		Sudoku sudoku = SudokuList.EVEREST1;
//		Sudoku sudoku = SudokuList.DISCOUNTINUOUS_NICE_LOOP;
//		Sudoku sudoku = SudokuList.BLOCKED.get(0);
//		showStepsAndTechniques(sudoku);


        // SOLVE 49151 SUDOKU AND SAVES THEM ON MYSQL
//		List<Sudoku> allSudokus = read50000Sudoku();
//		solve50000sudokus(allSudokus);


        // RISOLVE E SALVA SU MONGODB EVEREST 1 DI ARTO INKALA
//		Sudoku sudoku = SudokuList.EVEREST1;
//		SudokuService service = new SudokuServiceImpl();
//		service.solveSudoku(sudoku.getStringNumbers());


        // RIPROVA A RISOLVERE I SUDOKU BLOCCATI
//		List<Sudoku> sudokuBloccati = SudokuList.BLOCKED;
//		int count = 0;
//		int indice = 1;
//		for (Sudoku s : sudokuBloccati) {
//			try {
//				showSolution(s);
//				System.out.println("HO RISOLTO IL NUMERO " + indice);
//				count++;
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}
//			indice++;
//		}
//		System.out.println(count);


        // STAMPA I tabs AL MOMENTO DEL BLOCCO DEL PRIMO SUDOKU BLOCCATO
//		SudokuDao dao = new SudokuDaoImpl();
//		List<Sudoku> list = dao.getSudokuBloccati(false);
//		Sudoku sudoku = list.get(0);
//		List<tab> tabs = useStandardSolvingTechniques(sudoku).getTabs();
//		Utils.printSkimmedTabs(sudoku, tabs);


//		SudokuDao dao = new SudokuDaoImpl();
//		List<Sudoku> list = dao.getSudokuBloccati(false);
//		int count = 1;
//		for (Sudoku sudoku : list) {
//			System.out.println("SUDOKU " + count);
//			Utils.grid(sudoku);
//			Utils.printTabs(sudoku);
//			count++;
//		}

    }
}