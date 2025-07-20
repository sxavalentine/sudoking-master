package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

public class Hidden2 {

    /**
     * If there are two cells within a house such as that two candidates appear nowhere outside those cells in that house,
     * those two candidates must be placed in the two cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_PAIR = "HIDDEN PAIR";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_PAIR, sudoku, House.BOX));
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_PAIR, sudoku, House.ROW));
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_PAIR, sudoku, House.COL));
        return changeLogs;
    }
}