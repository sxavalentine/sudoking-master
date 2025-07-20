package com.gianfro.games.entities.deductions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.SudokuCell;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.util.List;

@Data
//TODO: Usa solo i campi specificati. NON scrivere equals/hashCode manuali! FONDAMENTALE !!!
@EqualsAndHashCode(of = {"cell", "removedCandidates"}, callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CellSkimmed implements CellChange {

    String solvingTechnique;
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    House house;
    SudokuCell cell;
    int number;
    List<Integer> removedCandidates;

    @Override
    public String toString() {
        return this.getSolvingTechnique() +
                " " +
                (house != null ? " " + house : "") +
                ": " + cell.getCoordinates() +
                "--> candidates: " + cell.getCandidates() +
                ", candidates removed: " + removedCandidates;
    }
}