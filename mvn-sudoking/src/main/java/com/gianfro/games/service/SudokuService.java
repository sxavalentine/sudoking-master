package com.gianfro.games.service;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Tab;

import java.util.List;

public interface SudokuService {

    public SolutionOutput solveSudoku(String stringNumbers);

    public void solve50kSudoku();

    public List<Tab> getSudokuTabs(String stringNumbers);
}
