package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.utils.Utils;
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
public class NoCandidatesLeftException extends RuntimeException {

    Sudoku sudokuAtTheTimeOfException;
    Integer numberSet;
    String cellCoordinates;
    List<Tab> tabs;
    List<Tab> emptyTabs;

    @Override
    public String getMessage() {
        Utils.megaGrid(sudokuAtTheTimeOfException, tabs);//TODO can be removed, for debug only
        StringBuilder sb = new StringBuilder();
        String intro = numberSet != null ?
                ("After setting value " + numberSet + " in cell " + cellCoordinates) :
                ("After examining the sudoku " + sudokuAtTheTimeOfException.getStringNumbers());
        sb.append(intro);
        sb.append(" the following cells would be left with no candidates:");
        emptyTabs.forEach(
                t -> sb.append("\n")
                        .append(t)
                        .append("\n"));
        return sb.toString();
    }
}
