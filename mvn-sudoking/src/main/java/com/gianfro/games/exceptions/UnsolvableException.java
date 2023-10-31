package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UnsolvableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    Sudoku startingSudoku;
    Sudoku blockedSudoku;
    List<Tab> tabs;


}
