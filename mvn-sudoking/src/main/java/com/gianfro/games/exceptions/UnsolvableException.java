package com.gianfro.games.exceptions;

import com.gianfro.games.entities.SolutionStep;
import com.gianfro.games.entities.Sudoku;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UnsolvableException extends RuntimeException {

    static final long serialVersionUID = 1L;

    Sudoku startingSudoku;
    Sudoku blockedSudoku;
    List<SolutionStep> solutionSteps;
    String message;


}
