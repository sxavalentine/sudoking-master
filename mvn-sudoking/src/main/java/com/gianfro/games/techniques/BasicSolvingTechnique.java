package com.gianfro.games.techniques;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.techniques.basic.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BasicSolvingTechnique {

    NAKED_SINGLE(0),
    HIDDEN_SINGLE(1),
    NAKED_PAIR(2),
    HIDDEN_PAIR(3),
    NAKED_TRIPLE(4),
    HIDDEN_TRIPLE(5),
    NAKED_QUAD(6),
    HIDDEN_QUAD(7),
    POINTING_CANDIDATES(8),
    CLAIMING_CANDIDATES(9);

    int index;

    BasicSolvingTechnique(int index) {
        this.index = index;
    }

    public Set<ChangeLog> check(Sudoku sudoku) {
        return switch (this.index) {
            case 0 -> Naked1.check(sudoku);
            case 1 -> Hidden1.check(sudoku);
            case 2 -> PointingCandidates.check(sudoku);
            case 3 -> ClaimingCandidates.check(sudoku);
            case 4 -> Naked2.check(sudoku);
            case 5 -> Hidden2.check(sudoku);
            case 6 -> Naked3.check(sudoku);
            case 7 -> Hidden3.check(sudoku);
            case 8 -> Naked4.check(sudoku);
            case 9 -> Hidden4.check(sudoku);
            default -> null;
        };
    }
}
