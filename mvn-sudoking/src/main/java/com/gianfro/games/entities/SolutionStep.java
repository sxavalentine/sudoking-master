package com.gianfro.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gianfro.games.entities.deductions.CellChange;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SolutionStep {

    @JsonIgnore
    @Transient
    Sudoku sudokuInstance;
    String sudokuNumbers;
    @JsonIgnore
    @Transient
    Collection<ChangeLog> changeLogs;
    List<CellChange> changes;

    public SolutionStep(Sudoku sudoku, Collection<ChangeLog> changeLogs) {
        this.sudokuInstance = sudoku;
        this.sudokuNumbers = sudoku.getStringNumbers();
        this.changeLogs = changeLogs;
        this.changes = new ArrayList<>();
        for (ChangeLog cl : changeLogs) {
            if (cl != null) changes.addAll(cl.getChanges());
        }
    }
}
