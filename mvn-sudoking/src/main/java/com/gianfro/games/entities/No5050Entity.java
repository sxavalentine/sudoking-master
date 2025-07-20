package com.gianfro.games.entities;

import com.gianfro.games.exceptions.NoFiftyFiftyException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Document(collection = "no_5050")
public class No5050Entity {

    int startingDigits;
    String startingNumbers;
    int impasseDigits;
    String impasseNumbers;
    List<SudokuCell> cells;

    public No5050Entity(Sudoku sudoku, NoFiftyFiftyException nffe) {
        this.startingDigits = 81 - StringUtils.countMatches(sudoku.getStringNumbers(), "0");
        this.startingNumbers = sudoku.getStringNumbers();
        this.impasseDigits = 81 - StringUtils.countMatches(nffe.getSudokuAtTheTimeOfException().getStringNumbers(), "0");
        this.impasseNumbers = nffe.getSudokuAtTheTimeOfException().getStringNumbers();
        this.cells = sudoku.getCells();
    }
}
