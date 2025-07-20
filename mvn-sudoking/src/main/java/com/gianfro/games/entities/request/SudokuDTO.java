package com.gianfro.games.entities.request;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import lombok.Data;

import java.util.List;

@Data
public class SudokuDTO implements DtoInterface<Sudoku> {

    private List<SudokuCell> cells;

    @Override
    public Sudoku toEntity() {
        return Sudoku.fromCells(cells);
    }
}
