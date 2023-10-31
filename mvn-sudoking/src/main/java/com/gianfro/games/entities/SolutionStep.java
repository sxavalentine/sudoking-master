package com.gianfro.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SolutionStep {

    @JsonIgnore
    @Transient
    Sudoku sudokuInstance;
    String sudokuNumbers;
    @JsonIgnore
    @Transient
    List<ChangeLog> changeLogs;
    List<Change> changes;
    @JsonIgnore
    @Transient
    List<Tab> tabs;

    public SolutionStep(Sudoku sudoku, List<ChangeLog> changeLogs, List<Tab> tabs) {
        this.sudokuInstance = sudoku;
        this.sudokuNumbers = sudoku.getStringNumbers();
        this.changeLogs = changeLogs;
        this.changes = new ArrayList<>();
        for (ChangeLog cl : changeLogs) {
            if (cl != null) changes.addAll(cl.getChanges());
        }
        this.tabs = tabs;
    }
}
