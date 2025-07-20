package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Link implements ChangeLogUnitMember {

    SudokuCell cell;
    boolean on;
    int number;

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
    public String getCoordinates() {
        return cell.getCoordinates();
    }

    @Override
    public String toString() {
        return cell.getCoordinates() + " (" + (on ? "+" : "-") + number + ")";
    }
}
