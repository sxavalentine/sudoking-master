package com.gianfro.games.techniques;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.techniques.advanced.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum AdvancedSolvingTechnique {

    X_WING(0),
    XY_WING(1),
    XYZ_WING(2),
    W_WING(3),
    BASIC_FISH(4),
    SKYSCRAPER(5),
    TWO_STRING_KITE(6);
    int index;

    AdvancedSolvingTechnique(int index) {
        this.index = index;
    }

    public Set<ChangeLog> check(Sudoku sudoku) {
        return switch (this.index) {
            case 0 -> X_Wing.check(sudoku);
            case 1 -> XY_Wing.check(sudoku);
            case 2 -> XYZ_Wing.check(sudoku);
            case 3 -> new HashSet<>();//TODO: implementata male
            case 4 -> BasicFish.check(sudoku);
            case 5 -> Skyscraper.check(sudoku);
            case 6 -> TwoStringKite.check(sudoku);
            default -> null;
        };
    }
}

