package com.gianfro.games.service;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Tab;

import java.util.List;

public interface SudokuService {

    SolutionOutput solveSudoku(String stringNumbers);

    String solveSudokuWithExplanation(String stringNumbers);

    void solve50kSudoku();

    List<Tab> getSudokuTabs(String stringNumbers);

    SolutionOutput findSolutionByStartingNumbers(String startingNumbers);
}
