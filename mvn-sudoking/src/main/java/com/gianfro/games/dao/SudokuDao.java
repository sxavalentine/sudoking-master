package com.gianfro.games.dao;

import java.util.List;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.exceptions.NoFiftyFiftyException;

public interface SudokuDao {

	public boolean salvaNelDB(SolutionOutput s);
	
	public boolean salvaErroreNelDB(Sudoku sudoku, NoFiftyFiftyException nffe);
	
	public List<Sudoku> getSudokuRisolti();
	
	public List<Sudoku> getSudokuBloccati(boolean startOrStuck);
}
