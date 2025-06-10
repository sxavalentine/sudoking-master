package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Link implements ChangeLogUnitMember {

    Tab tab;
    boolean on;
    int number;

    @Override
    public int getBox() {
        return tab.getBox();
    }

    @Override
    public int getRow() {
        return tab.getRow();
    }

    @Override
    public int getCol() {
        return tab.getCol();
    }

    @Override
    public String toString() {
        return tab.getCoordinates() + " (" + (on ? "+" : "-") + number + ")";
    }
}
