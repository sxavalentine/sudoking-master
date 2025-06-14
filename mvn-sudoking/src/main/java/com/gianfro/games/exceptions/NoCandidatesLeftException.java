package com.gianfro.games.exceptions;

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
public class NoCandidatesLeftException extends RuntimeException {

    static final long serialVersionUID = 1L;

    String sudokuNumbers;
    List<Tab> tabs;
    List<Tab> emptyTabs;

    @Override
    public String getMessage() {
        String intro = "The sequence " + sudokuNumbers + " after being analyzed resulted in the following empty cells with no candidates left, which is impossibile:\n";
        StringBuilder sb = new StringBuilder();
        sb.append(intro);
        emptyTabs.forEach(t -> sb.append(t.getCoordinates()).append("\n"));
        return sb.toString();
    }
}
