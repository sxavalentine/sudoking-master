package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class NoCandidatesLeftException extends GenericSudokuException {

    static final long serialVersionUID = 1L;

    String sudokuNumbers;
    List<SudokuCell> emptyCells;

    public NoCandidatesLeftException(Sudoku sudoku, List<SudokuCell> emptyCells) {
        this.emptyCells = emptyCells;
        sudokuNumbers = sudoku.getStringNumbers();
    }

    @Override
    public String getMessage() {
        String intro = "The sequence " + sudokuNumbers + " after being analyzed resulted in the following empty cells with no candidates left, which is impossibile:\n";
        StringBuilder sb = new StringBuilder();
        sb.append(intro);
        emptyCells.forEach(c -> sb.append(c.getCoordinates()).append("\n"));
        return sb.toString();
    }
}
