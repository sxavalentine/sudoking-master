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

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
//TODO: Usa solo i campi specificati. NON scrivere equals/hashCode manuali! FONDAMENTALE !!!
@EqualsAndHashCode(of = {"cell", "cellB", "number", "numberB"}, callSuper = false)
@Builder
public class CellGuessed implements CellChange {

    String solvingTechnique;
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    House house;
    SudokuCell cell;
    int number;
    SudokuCell cellB;
    int numberB;

    @Override
    public String toString() {
        return solvingTechnique +
                (house != null ? " " + house : "") +
                ": " + cell.getCoordinates() +
                " --> picked " + number +
                " over " + numberB +
                (cell == cellB ? "" : " in cell " + cellB.getCoordinates());
    }
}
