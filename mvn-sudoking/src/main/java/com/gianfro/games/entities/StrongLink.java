package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Builder
public class StrongLink {

    SudokuCell linkA;
    SudokuCell linkB;
    int sharedCandidate;
    House sharedHouse;
    int houseNumber;

    public StrongLink(SudokuCell linkA, SudokuCell linkB, int candidate, House sharedHouse, int houseNumber) {
        sharedCandidate = candidate;
        if (linkA.getIndex() < linkB.getIndex()) {
            this.linkA = linkA;
            this.linkB = linkB;
        } else {
            this.linkA = linkB;
            this.linkB = linkA;
        }
        this.sharedHouse = sharedHouse;
        this.houseNumber = houseNumber;
    }
}
