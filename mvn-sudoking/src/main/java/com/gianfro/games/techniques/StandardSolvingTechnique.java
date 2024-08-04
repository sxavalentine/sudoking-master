package com.gianfro.games.techniques;

import com.gianfro.games.entities.SkimmingResult;
import com.gianfro.games.entities.Tab;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum StandardSolvingTechnique {

    NAKED_SINGLE(0),
    HIDDEN_SINGLE(1),
    POINTING_CANDIDATES(2),
    CLAIMING_CANDIDATES(3),
    NAKED_PAIR(4),
    HIDDEN_PAIR(5),
    NAKED_TRIPLE(6),
    HIDDEN_TRIPLE(7),
    NAKED_QUAD(8),
    HIDDEN_QUAD(9);

    int index;

    StandardSolvingTechnique(int index) {
        this.index = index;
    }

    public SkimmingResult check(List<Tab> tabs) {
        switch (this.index) {
            case 0:
                return Naked1.check(tabs);
            case 1:
                return Hidden1.check(tabs);
            case 2:
                return PointingCandidates.check(tabs);
            case 3:
                return ClaimingCandidates.check(tabs);
            case 4:
                return Naked2.check(tabs);
            case 5:
                return Hidden2.check(tabs);
            case 6:
                return Naked3.check(tabs);
            case 7:
                return Hidden3.check(tabs);
            case 8:
                return Naked4.check(tabs);
            case 9:
                return Hidden4.check(tabs);
        }
        return null;
    }
}
