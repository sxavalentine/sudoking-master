package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"cell", "numberOn", "numberOff"})
@AllArgsConstructor
public class Link implements ChangeLogUnitMember {

    SudokuCell cell;
    int numberOn;
    int numberOff;

    @Override
    public int getBox() {
        return cell.getBox();
    }

    @Override
    public int getRow() {
        return cell.getRow();
    }

    @Override
    public int getCol() {
        return cell.getCol();
    }

    @Override
    public int getIndex() {
        return cell.getIndex();
    }

    @Override
    public String getCoordinates() {
        return cell.getCoordinates();
    }

    @Override
    public String toString() {
        return cell.getCoordinates() + "(" + (numberOn > 0 ? "+" + numberOn : "") + (numberOff > 0 ? "-" + numberOff : "") + ")";
    }
}
