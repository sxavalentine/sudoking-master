package com.gianfro.games.entities;

import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Skimming extends Change {

    Tab tab;
    List<Integer> removedCandidates;

    public Skimming(String solvinqTechnique, House house, Tab tab, List<Integer> removedCandidates) {
        super(solvinqTechnique, house, tab.getRow(), tab.getCol(), 0);
        this.tab = tab;
        this.removedCandidates = removedCandidates;
    }

    @Override
    public String toString() {
        return
                this.getSolvingTechnique() + " " +
                        (this.getHouse() != null ? " " + this.getHouse() : "") + ": " +
                        Utils.ROWS_LETTERS.get(this.getRow() - 1) + this.getCol()
                        + ", candidates: " + this.tab.getNumbers()
                        + ", candidates removed: " + this.getRemovedCandidates();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Skimming) {
            Skimming s = (Skimming) obj;
            return s.getRemovedCandidates().equals(this.removedCandidates) && s.getRow() == this.getRow() && s.getCol() == this.getCol();
        }
        return false;
    }

}