package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum House {

    BOX("BOX"),
    ROW("ROW"),
    COL("COL");

    String house;

    House(String house) {
        this.house = house;
    }

    @Override
    public String toString() {
        return this.house;
    }
}
