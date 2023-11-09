package com.gianfro.games.service;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.exceptions.UnsolvableException;
import com.gianfro.games.repository.GenericErrorRepository;
import com.gianfro.games.repository.No5050Repository;
import com.gianfro.games.repository.SudokuSolutionsRepository;
import com.gianfro.games.repository.UnsolvableErrorRepository;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.Utils;
import com.mongodb.DuplicateKeyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SudokuServiceImpl implements SudokuService {

    @Autowired
    private SudokuSolutionsRepository sudokuSolutionsRepository;

    @Autowired
    private UnsolvableErrorRepository unsolvableErrorRepository;

    @Autowired
    private GenericErrorRepository genericErrorRepository;

    @Autowired
    private No5050Repository no5050Repository;


    @Override
    public SolutionOutput solveSudoku(String stringNumbers) {

        Sudoku sudoku = Utils.buildSudoku(stringNumbers);
        SolutionOutput solutionOutput = null;

        try {
            solutionOutput = SudokuSolver.getSolution(sudoku);
            try {
                this.sudokuSolutionsRepository.insert(solutionOutput);
            } catch (DuplicateKeyException dke) {
                System.out.println("LOOKS LIKE I ALREADY SOLVED THIS SUDOKU: " + dke.getMessage() + " " + solutionOutput.getSolutionNumbers());
                Utils.grid(Utils.buildSudoku(solutionOutput.getSolutionNumbers()));
            } catch (Exception e) {
                System.out.println("OPS: " + e.getClass() + " " + e.getMessage() + " " + sudoku.getStringNumbers());
            }
        } catch (NoFiftyFiftyException nffe) {
            System.out.println("THERE ARE NO CELLS WITH ONLY 2 OR LESS CANDIDATES");
            No5050Entity no5050Entity = new No5050Entity(sudoku, nffe);
            no5050Repository.insert(no5050Entity);
            throw nffe;
//            Utils.grid(nffe.getSudokuAtTheTimeOfException());
//            for (Tab tab : nffe.getTabs()) {
//                System.out.println(tab);
//            }
        } catch (UnsolvableException ue) {
            UnsolvableError error = new UnsolvableError(
                    sudoku.getStringNumbers(),
                    ue.getBlockedSudoku().getStringNumbers(),
                    81 - StringUtils.countMatches(sudoku.getStringNumbers(), "0"),
                    81 - StringUtils.countMatches(ue.getBlockedSudoku().getStringNumbers(), "0"),
                    ue.getMessage(),
                    ue.getSolutionSteps());
            this.unsolvableErrorRepository.insert(error);
            throw ue;
        } catch (Exception e) {
            GenericError error = new GenericError(sudoku.getStringNumbers(), e.getMessage());
            this.genericErrorRepository.insert(error);
            throw e;
        }
        return solutionOutput;
    }


    /**
     * This method reads from the 50kSudoku.txt file in the resources folder and solves them all.
     * The 50kSudoku.txt file is a file containing actually only 49151 rows, each representing a minimal sudoku.
     * A minimal sudoku is a sudoku containing only 17 starting digits.
     * According to a Boston MIT study, 17 is the minimum number of digits for a sudoku to have a unique possible solution.
     */
    @Override
    public void solve50kSudoku() {
        List<Sudoku> allSudokus = Utils.read50kSudoku();
        List<Sudoku> noFfeSudokus = new LinkedList<>();
        int totalSolutionTime = 0;
        int count = 1;
        int solvedCount = 0;
        int nffeCount = 0;
        int unsolvableCount = 0;
        int geCount = 0;
        for (Sudoku sudoku : allSudokus) {
            try {
                SolutionOutput solutionOutput = this.solveSudoku(sudoku.getStringNumbers());
                totalSolutionTime += solutionOutput.getSolutionTime();
                solvedCount++;
            } catch (NoFiftyFiftyException nffe) {
                System.out.println(count + " " + nffe);
                nffeCount++;
                noFfeSudokus.add(sudoku);
            } catch (UnsolvableException ue) {
                System.out.println(count + " " + ue);
                unsolvableCount++;
            } catch (Exception e) {
                System.out.println(count + " " + e);
                geCount++;
            }
            count++;
        }
        System.out.println("I solved " + solvedCount + " out of " + allSudokus.size() + " sudokus in " + totalSolutionTime + " milliseconds");
        if (nffeCount > 0) System.out.println("NO FIFTY FIFTY EXCEPTION count: " + nffeCount);
        if (unsolvableCount > 0) System.out.println("UNSOLVABLE EXCEPTION count: " + unsolvableCount);
        if (geCount > 0) System.out.println("GENERIC EXCEPTION count: " + geCount);
        System.out.println("These are the NoFiftyFiftyException sudokus: ");
        for (Sudoku s : noFfeSudokus) {
            System.out.println(s.getStringNumbers());
        }
    }

    @Override
    public List<Tab> getSudokuTabs(String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        return Utils.getBasicTabs(s);
    }
}
