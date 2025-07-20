package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class SudokuBugException extends GenericSudokuException {

    Sudoku sudokuAtTheTimeOfException;
    Set<String> bugs;

    @Override
    public String getMessage() {
        Utils.megaGrid(sudokuAtTheTimeOfException);
        return buildMessage();
    }

    private String buildMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("FOUND THE FOLLOWING BUGS:");
        sb.append("\n");
        for (String bug : bugs) {
            sb.append(bug);
            sb.append("\n");
        }
        return sb.toString();
    }
}
