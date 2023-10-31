package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Document(collection = "solutions")
public class SolutionOutput {

    String startingNumbers;
    String solutionNumbers;
    int initialDigits;
    int solutionTime;
    int stepsCount;
    List<SolutionStep> solutionSteps;
    @Id
    @NonFinal
    String id;

    public SolutionOutput(Sudoku sudoku, List<SolutionStep> solutionSteps, int solutionTime) {
        this.startingNumbers = sudoku.getStringNumbers();
        this.solutionNumbers = solutionSteps.get(solutionSteps.size() - 1).getSudokuInstance().getStringNumbers();
        this.initialDigits = 81 - StringUtils.countMatches(startingNumbers, "0");
        this.solutionTime = solutionTime;
        this.stepsCount = solutionSteps.size();
        this.solutionSteps = solutionSteps;
    }
}
