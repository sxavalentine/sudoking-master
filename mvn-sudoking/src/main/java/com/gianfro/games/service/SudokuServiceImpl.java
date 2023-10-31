package com.gianfro.games.service;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuError;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.repository.ErrorsRepository;
import com.gianfro.games.repository.SudokuSolutionsRepository;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.Utils;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SudokuServiceImpl implements SudokuService {

    @Autowired
    private SudokuSolutionsRepository sudokuSolutionsRepository;

    @Autowired
    private ErrorsRepository errorsRepository;


    @Override
    public SolutionOutput solveSudoku(String stringNumbers) {

        Sudoku sudoku = Utils.buildSudoku(stringNumbers);
        SolutionOutput solutionOutput = null;

        try {
            solutionOutput = SudokuSolver.getSolution(sudoku);
        } catch (NoFiftyFiftyException nffe) {
            System.out.println("THERE ARE NO CELLS WITH ONLY 2 OR LESS CANDIDATES");
            Utils.grid(nffe.getSudokuAtTheTimeOfException());
            for (Tab tab : nffe.getTabs()) {
                System.out.println(tab);
            }
        } catch (Exception e) {
            SudokuError error = new SudokuError(sudoku.getStringNumbers(), e.getMessage());
            this.errorsRepository.insert(error);
        }
        try {
            this.sudokuSolutionsRepository.insert(solutionOutput);
        } catch (DuplicateKeyException dke) {
            System.out.println("LOOKS LIKE I ALREADY SOLVED THIS SUDOKU: " + dke.getMessage() + " " + solutionOutput.getSolutionNumbers());
            Utils.grid(Utils.buildSudoku(solutionOutput.getSolutionNumbers()));
        } catch (Exception e) {
            System.out.println("OPS: " + e.getClass() + " " + e.getMessage() + " " + sudoku.getStringNumbers());
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
        SudokuSolver.solve50kSudokus();
    }

    @Override
    public List<Tab> getSudokuTabs(String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        return Utils.getBasicTabs(s);
    }
}
