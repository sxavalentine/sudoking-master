package com.gianfro.games.techniques;

import com.gianfro.games.entities.SkimmingResult;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.techniques.basic.*;
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
        return switch (this.index) {
            case 0 -> Naked1.check(tabs);
            case 1 -> Hidden1.check(tabs);
            case 2 -> PointingCandidates.check(tabs);
            case 3 -> ClaimingCandidates.check(tabs);
            case 4 -> Naked2.check(tabs);
            case 5 -> Hidden2.check(tabs);
            case 6 -> Naked3.check(tabs);
            case 7 -> Hidden3.check(tabs);
            case 8 -> Naked4.check(tabs);
            case 9 -> Hidden4.check(tabs);
            default -> null;
        };
    }
}
