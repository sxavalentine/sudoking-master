package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class ChainUtils {

    /**
     * Given a SudokuCell, a Sudoku (of which the cell is part of), and a candidate, returns the list of SudokuCell that have a strong link
     * with the input cell
     */
    public static List<SudokuCell> findStrongLinksOnCandidate(SudokuCell cell, Sudoku sudoku, int candidate) {
        List<SudokuCell> strongLinks = new LinkedList<>();
        for (House house : House.values()) {
            List<SudokuCell> houseCells = Utils.getHouseCells(sudoku, house, cell.getHouseNumber(house));
            List<SudokuCell> welcomingCells = houseCells.stream().filter(c ->
                            c.getCandidates().contains(candidate) &&
                                    c != cell)
                    .toList();
            if (welcomingCells.size() == 1) {
                strongLinks.addAll(welcomingCells);
            }
        }
        return strongLinks;
    }
}
