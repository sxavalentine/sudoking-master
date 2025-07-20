package com.gianfro.games.entities.errorsDTO;

import com.gianfro.games.entities.SudokuCell;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class NoCandidatesLeftErrorDTO {

    String sudokuNumbers;
    Integer numberSet;
    String cellCoordinates;
    List<SudokuCell> cells;
    List<SudokuCell> emptyCells;
}
