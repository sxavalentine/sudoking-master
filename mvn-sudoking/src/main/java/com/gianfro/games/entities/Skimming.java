package com.gianfro.games.entities;

import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Skimming extends Change {

    Tab tab;
    List<Integer> removedCandidates;

    @Override
    public String toString() {
        return
                this.getSolvingTechnique() + " " +
                        (this.getHouse() != null ? " " + this.getHouse() : "") + ": " +
                        Utils.ROWS_LETTERS.get(this.getRow() - 1) + this.getCol() +
                        ", candidates: " + this.tab.getCandidates() +
                        ", candidates removed: " + this.getRemovedCandidates();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Skimming s) {
            return s.getRemovedCandidates().equals(this.removedCandidates) && s.getRow() == this.getRow() && s.getCol() == this.getCol();
        }
        return false;
    }

}