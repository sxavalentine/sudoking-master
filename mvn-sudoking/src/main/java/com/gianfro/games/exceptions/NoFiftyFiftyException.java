package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class NoFiftyFiftyException extends RuntimeException {

    static final long serialVersionUID = 1L;

    Sudoku sudokuAtTheTimeOfException;
    List<Tab> tabs;

    @Override
    public String getMessage() {
        return "NO CHANCE OF 50/50. CELL CANDIDATES ARE: " + tabs;
    }
}
