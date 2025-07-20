package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class NoFiftyFiftyException extends GenericSudokuException {

    static final long serialVersionUID = 1L;

    Sudoku sudokuAtTheTimeOfException;

    @Override
    public String getMessage() {
        Utils.megaGrid(sudokuAtTheTimeOfException);
        return "No chances of 50/50.";
    }
}
