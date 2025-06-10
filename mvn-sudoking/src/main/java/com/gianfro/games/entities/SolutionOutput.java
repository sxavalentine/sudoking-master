package com.gianfro.games.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "solutions")
@NoArgsConstructor
public class SolutionOutput {

    @Id
    @JsonProperty("startingNumbers")
    String startingNumbers;
    String solutionNumbers;
    int solutionTime;
    List<SolutionStep> solutionSteps;
    Instant solutionDate;
    int initialDigits;
    int stepsCount;


    public SolutionOutput(Sudoku sudoku, List<SolutionStep> solutionSteps, int solutionTime) {
        this.startingNumbers = sudoku.getStringNumbers();
        this.solutionNumbers = solutionSteps.get(solutionSteps.size() - 1).getSudokuInstance().getStringNumbers();
        this.solutionTime = solutionTime;
        this.solutionSteps = solutionSteps;
        this.solutionDate = Instant.now();
        /*These two properties could be excluded, as they are calculated. I just don't know if I'm going to query often by these two*/
        this.initialDigits = 81 - StringUtils.countMatches(startingNumbers, "0");
        this.stepsCount = solutionSteps.size();
    }
}
