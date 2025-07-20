package com.gianfro.games.service;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Sudoku;

import java.util.List;

public interface SudokuService {

    SolutionOutput solveSudoku(String stringNumbers);

    SolutionOutput solveSudoku(Sudoku sudoku);

    String solveSudokuWithExplanation(String stringNumbers);

    void solve50kSudoku();

    SolutionOutput findSolutionByStartingNumbers(String startingNumbers);

    List<SolutionOutput> solveUnsolvableSudokus();

    String getRandomSudoku();
}
