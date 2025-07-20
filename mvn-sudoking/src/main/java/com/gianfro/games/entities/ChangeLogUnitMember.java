package com.gianfro.games.entities;

public interface ChangeLogUnitMember {

    int getBox();

    int getRow();

    int getCol();

    String getCoordinates();

    default int getHouseNumber(House house) {
        return switch (house) {
            case BOX -> getBox();
            case ROW -> getRow();
            case COL -> getCol();
        };
    }
}
